package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

class BatchInserterTest extends BaseRepositoryIT {

  private static final String TABLE_NAME = "temp_xxx_xxx_xxx_core";
  private static final Field<JSONB> DATA_FIELD = DSL.field("data", JSONB.class);
  private static final String RECORD_ID = "11a8a4c6-3188-4305-9688-d68942f4038e";
  private static final String RECORD_ID_ALT = "32546f7b-f62a-4368-8c60-922f1cba4ab8";
  private final Field<String> ID_FIELD = DSL.field("dwcaid", String.class);
  private BatchInserter batchInserter;

  private static Stream<Arguments> badStrings() {
    return Stream.of(
        Arguments.of("bad \b string"),
        Arguments.of("bad \f string"),
        Arguments.of("bad \n string"),
        Arguments.of("bad \r string"),
        Arguments.of("bad \t string"),
        Arguments.of("bad, string"),
        Arguments.of("bad \\N string")
    );
  }

  @BeforeEach
  void setup() throws SQLException {
    var connection = DriverManager.getConnection(dataSource.getJdbcUrl(), dataSource.getUsername(),
        dataSource.getPassword());
    var copyManager = new CopyManager((BaseConnection) connection);
    batchInserter = new BatchInserter(copyManager);
    context.createTable(TABLE_NAME)
        .column(ID_FIELD, SQLDataType.VARCHAR)
        .column(DATA_FIELD, SQLDataType.JSONB)
        .execute();
    context.createIndex().on(TABLE_NAME, ID_FIELD.getName()).execute();
  }

  @AfterEach
  void destroy() {
    context.dropTableIfExists(getTable(TABLE_NAME)).execute();
  }

  @Test
  void testBatchInsert() throws Exception {
    // Given
    var records = givenCoreRecords();
    var idField = context.meta().getTables(TABLE_NAME).get(0).field(ID_FIELD);

    // When
    batchInserter.batchCopy(TABLE_NAME, records);
    var result = context.select(getTable(TABLE_NAME).asterisk())
        .from(getTable(TABLE_NAME))
        .where(idField.eq(RECORD_ID))
        .fetchOne();

    // Then
    assertThat(MAPPER.readTree(result.get(DATA_FIELD).data())).isEqualTo(givenJsonNode());
  }

  @ParameterizedTest
  @MethodSource("badStrings")
  void testBadCharacters(String badString) throws Exception {
    // Given
    var node = MAPPER.createObjectNode();
    node.put("field", badString);
    var pair = List.of(Pair.of(RECORD_ID, (JsonNode) node));
    var idField = context.meta().getTables(TABLE_NAME).get(0).field(ID_FIELD);

    // When
    batchInserter.batchCopy(TABLE_NAME, pair);
    var result = context.select(getTable(TABLE_NAME).asterisk())
        .from(getTable(TABLE_NAME))
        .where(idField.eq(RECORD_ID))
        .fetchOne();

    // Then
    assertThat(MAPPER.readTree(result.get(DATA_FIELD).data())).isEqualTo(node);
  }

  @Test
  void testBadCharacters() throws Exception {
    // Given
    var node = MAPPER.createObjectNode();
    node.put("field", "\u0000");
    var pair = List.of(Pair.of(RECORD_ID, (JsonNode) node));
    var expected = MAPPER.readTree("""
        {
          "field":""
        }
        """);
    var idField = context.meta().getTables(TABLE_NAME).get(0).field(ID_FIELD);

    // When
    batchInserter.batchCopy(TABLE_NAME, pair);
    var result = context.select(getTable(TABLE_NAME).asterisk())
        .from(getTable(TABLE_NAME))
        .where(idField.eq(RECORD_ID))
        .fetchOne();

    // Then
    assertThat(MAPPER.readTree(result.get(DATA_FIELD).data())).isEqualTo(expected);
  }

  private List<Pair<String, JsonNode>> givenCoreRecords() {
    var records = new ArrayList<Pair<String, JsonNode>>();
    records.add(Pair.of(RECORD_ID, givenJsonNode()));
    records.add(Pair.of(RECORD_ID_ALT, MAPPER.createObjectNode()));
    return records;
  }

  private JsonNode givenJsonNode() {
    var node = MAPPER.createObjectNode();
    node.put("test", "test");
    node.put("data", "value");
    return node;
  }

  private Table<Record> getTable(String tableName) {
    return DSL.table("\"" + tableName + "\"");
  }
}
