package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DwcaRepositoryTest extends BaseRepositoryIT {

  private final Field<String> idField = DSL.field("dwcaid", String.class);
  private final Field<JSONB> dataField = DSL.field("data", JSONB.class);
  @Mock
  BatchInserter batchInserter;
  private DwcaRepository repository;

  private static JsonNode givenRecord(String corruptedValue) {
    var objectNode = MAPPER.createObjectNode();
    objectNode.put("someRandomInformation", "someRandomInformation");
    objectNode.put("someCorruptedInformation", corruptedValue);
    return objectNode;
  }

  @BeforeEach
  void setup() {
    repository = new DwcaRepository(MAPPER, context, batchInserter);
  }

  @Test
  void getCoreRecords() {
    // Given
    var tableName = "temp_XXX-XXX-XXX_Core";
    var records = givenCoreRecords();
    repository.createTable(tableName);
    postRecords(tableName, records);

    // When
    var results = repository.getCoreRecords(records.stream().map(Pair::getLeft).toList(),
        tableName);
    repository.deleteTable(tableName);

    // Then
    assertThat(results).isEqualTo(
        records.stream().collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));
  }

  @Test
  void getCorruptCoreRecords() {
    // Given
    var tableName = "temp_XXX-XXX-XXX_Core";
    var records = List.of(
        Pair.of(UUID.randomUUID().toString(), givenRecord("\u0000 someCorruptedInformation")));
    repository.createTable(tableName);
    postRecords(tableName, records);

    // When
    var results = repository.getCoreRecords(records.stream().map(Pair::getLeft).toList(),
        tableName);
    repository.deleteTable(tableName);

    // Then
    assertThat(results).isEqualTo(
        Map.of(records.get(0).getLeft(), givenRecord(" someCorruptedInformation")));
  }

  @Test
  void getCoreExtensionRecords() {
    // Given
    var tableName = "temp_XXX-XXX-XXX_Extension";
    var records = givenExtensionRecord();
    repository.createTable(tableName);
    postRecords(tableName, records);

    // When
    var results = repository.getRecords(records.stream().map(Pair::getLeft).toList(), tableName);
    repository.deleteTable(tableName);

    // Then
    assertThat(results).isEqualTo(
        records.stream().collect(groupingBy(Pair::getLeft, mapping(Pair::getRight, toList()))));
  }

  @Test
  void testPostRecords() throws Exception {
    // Given
    var tableName = "temp_XXX-XXX-XXX_Extension";
    var records = givenExtensionRecord();

    // When
    repository.postRecords(tableName, records);

    // Then
    then(batchInserter).should().batchCopy(tableName, records);
  }

  private ArrayList<Pair<String, JsonNode>> givenExtensionRecord() {
    var records = new ArrayList<Pair<String, JsonNode>>();
    for (int i = 0; i < 10; i++) {
      var id = UUID.randomUUID().toString();
      for (int j = 0; j < 3; j++) {
        var pair = Pair.of(
            id,
            (JsonNode) MAPPER.createObjectNode()
        );
        records.add(pair);
      }
    }
    return records;
  }

  private List<Pair<String, JsonNode>> givenCoreRecords() {
    var records = new ArrayList<Pair<String, JsonNode>>();
    for (int i = 0; i < 10; i++) {
      var pair = Pair.of(
          UUID.randomUUID().toString(),
          (JsonNode) MAPPER.createObjectNode()
      );
      records.add(pair);
    }
    return records;
  }

  private void postRecords(String tableName, List<Pair<String, JsonNode>> dbRecords) {
    var queries = dbRecords.stream().map(dbRecord -> recordToQuery(tableName, dbRecord)).toList();
    context.batch(queries).execute();
  }

  private Query recordToQuery(String tableName, Pair<String, JsonNode> dbRecord) {
    try {
      return context.insertInto(getTable(tableName)).set(idField, dbRecord.getLeft())
          .set(dataField,
              JSONB.jsonb(MAPPER.writeValueAsString(dbRecord.getRight()).replace("\\u0000", "")));
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  private Table<Record> getTable(String tableName) {
    return DSL.table("\"" + tableName + "\"");
  }

}
