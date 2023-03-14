package eu.dissco.core.translator.repository;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DwcaRepository {

  private final Field<String> idField = DSL.field("dwcaid", String.class);
  private final Field<JSONB> dataField = DSL.field("data", JSONB.class);

  private final ObjectMapper mapper;
  private final DSLContext context;

  public void createTable(String tableName) {
    context.createTable(tableName)
        .column(idField, SQLDataType.VARCHAR)
        .column(dataField, SQLDataType.JSONB)
        .execute();
    context.createIndex().on(tableName, idField.getName()).execute();
  }


  private Table<Record> getTable(String tableName) {
    return DSL.table("\"" + tableName + "\"");
  }

  public void postRecords(String tableName, List<Pair<String, JsonNode>> dbRecords) {
    var queries = dbRecords.stream().map(dbRecord -> recordToQuery(tableName, dbRecord)).toList();
    context.batch(queries).execute();
  }

  private Query recordToQuery(String tableName, Pair<String, JsonNode> dbRecord) {
    try {
      return context.insertInto(getTable(tableName)).set(idField, dbRecord.getLeft())
          .set(dataField, JSONB.jsonb(mapper.writeValueAsString(dbRecord.getRight())));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteTable(String tableName) {
    context.dropTableIfExists(tableName).execute();
  }

  public Map<String, ObjectNode> getCoreRecords(List<String> batch, String tableName) {
    return context.selectFrom(getTable(tableName)).where(idField.in(batch)).fetchMap(
        dbRecord -> dbRecord.get(idField), this::getNode);
  }

  private ObjectNode getNode(Record dbRecord) {
    try {
      return mapper.readValue(dbRecord.get(dataField).data(), ObjectNode.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public Map<String, List<ObjectNode>> getRecords(List<String> batch, String tableName) {
    return context.selectFrom(getTable(tableName)).where(idField.in(batch))
        .stream().collect(groupingBy(dbRecord -> dbRecord.get(idField), mapping(this::getNode, toList())));
  }

}

