package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.service.DwcaService.AC_MULTIMEDIA;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.dissco.core.translator.component.DataMappingComponent;
import eu.dissco.core.translator.exception.IllegalDataException;
import eu.dissco.core.translator.terms.media.AccessURI;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.gbif.dwc.terms.AcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.Term;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DwcaRepository {

  private final Field<String> dwcaIDField = DSL.field("dwcaid", String.class);
  private final Field<String> uniqueIDField = DSL.field("id", String.class);
  private final Field<JSONB> dataField = DSL.field("data", JSONB.class);

  private final ObjectMapper mapper;
  private final DSLContext context;
  private final DataMappingComponent dataMappingComponent;

  private static String getUniqueIDValue(Pair<String, JsonNode> dbRecord,
      List<String> fieldsToCheck, boolean isOccurrence) {
    for (var fieldName : fieldsToCheck) {
      if (dbRecord.getRight().has(fieldName) && !dbRecord.getRight().get(fieldName).isNull()) {
        return dbRecord.getRight().get(fieldName).asText();
      }
    }
    if (isOccurrence) {
      log.warn("No physicalSpecimenID found for record: {}", dbRecord.getLeft());
      throw new IllegalDataException(
          "No physicalSpecimenID found for record: " + dbRecord.getRight());
    } else {
      log.warn("No accessURI found for record: {}", dbRecord.getLeft());
      throw new IllegalDataException("No accessURI found for record: " + dbRecord.getRight());
    }
  }

  public void createTable(String tableName, Term fileType) {
    context.createTable(tableName)
        .column(dwcaIDField, SQLDataType.VARCHAR)
        .column(uniqueIDField, SQLDataType.VARCHAR)
        .column(dataField, SQLDataType.JSONB)
        .execute();
    if (fileType.equals(DwcTerm.Occurrence)) {
      context.createUniqueIndex().on(tableName, uniqueIDField.getName()).execute();
    } else if (fileType.prefixedName().equals(AC_MULTIMEDIA)
        || fileType.equals(GbifTerm.Multimedia)) {
      context.createUniqueIndex().on(tableName, dwcaIDField.getName(), uniqueIDField.getName())
          .execute();
    }
    context.createIndex().on(tableName, dwcaIDField.getName()).execute();
  }

  private Table<Record> getTable(String tableName) {
    return DSL.table("\"" + tableName + "\"");
  }

  public void postRecords(String tableName, Term fileType, List<Pair<String, JsonNode>> dbRecords) {
    var queries = dbRecords.stream().map(dbRecord -> recordToQuery(tableName, fileType, dbRecord))
        .filter(
            Objects::nonNull).toList();
    context.batch(queries).execute();
  }

  private Query recordToQuery(String tableName, Term fileType, Pair<String, JsonNode> dbRecord) {
    try {
      var query = context.insertInto(getTable(tableName)).set(dwcaIDField, dbRecord.getLeft())
          .set(dataField,
              JSONB.jsonb(mapper.writeValueAsString(dbRecord.getRight()).replace("\\u0000", "")));
      if (fileType.equals(DwcTerm.Occurrence)) {
        var uniqueField = dataMappingComponent.getFieldMappings()
            .getOrDefault("ods:physicalSpecimenID", "dwc:occurrenceID");
        var fieldsToCheck = new ArrayList<>(PhysicalSpecimenID.DWCA_TERMS);
        fieldsToCheck.add(0, uniqueField);
        return query.set(uniqueIDField,
            getUniqueIDValue(dbRecord, fieldsToCheck, true));
      }
      if (fileType.equals(AcTerm.Multimedia) || fileType.equals(GbifTerm.Multimedia)) {
        var uniqueField = dataMappingComponent.getFieldMappings()
            .getOrDefault("ac:accessURI", "ac:accessURI");
        var fieldsToCheck = new ArrayList<>(AccessURI.DWCA_TERMS);
        fieldsToCheck.add(0, uniqueField);
        return query.set(uniqueIDField, getUniqueIDValue(dbRecord, fieldsToCheck, false))
            .onConflict(dwcaIDField, uniqueIDField)
            .doUpdate().set(dataField,
                JSONB.jsonb(mapper.writeValueAsString(dbRecord.getRight()).replace("\\u0000", "")));
      } else {
        return query;
      }
    } catch (JsonProcessingException e) {
      log.error("Unable to map JSON to JSONB, ignoring record: {}", dbRecord.getLeft(), e);
      return null;
    }
  }

  public Map<String, ObjectNode> getCoreRecords(List<String> batch, String tableName) {
    return context.selectFrom(getTable(tableName)).where(dwcaIDField.in(batch)).fetchMap(
        dbRecord -> dbRecord.get(dwcaIDField), this::getNode);
  }

  private ObjectNode getNode(Record dbRecord) {
    try {
      return mapper.readValue(dbRecord.get(dataField).data(), ObjectNode.class);
    } catch (JsonProcessingException e) {
      log.error("Unable to map JSONB to JSON, ignoring record: {}", dbRecord.get(dwcaIDField), e);
      return null;
    }
  }

  public Map<String, List<ObjectNode>> getRecords(List<String> batch, String tableName) {
    return context.selectFrom(getTable(tableName)).where(dwcaIDField.in(batch))
        .stream()
        .collect(
            groupingBy(dbRecord -> dbRecord.get(dwcaIDField), mapping(this::getNode, toList())));
  }

  public void deleteTable(String tableName) {
    context.dropTableIfExists(tableName).execute();
  }

}

