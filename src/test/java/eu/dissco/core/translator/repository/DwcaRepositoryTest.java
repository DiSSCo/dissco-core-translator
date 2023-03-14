package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DwcaRepositoryTest extends BaseRepositoryIT {

  private DwcaRepository repository;

  @BeforeEach
  void setup() {
    repository = new DwcaRepository(MAPPER, context);
  }

  @Test
  void getCoreRecords() {
    // Given
    var tableName = "XXX-XXX-XXX_Core";
    var records = givenCoreRecords();
    repository.createTable(tableName);
    repository.postRecords(tableName, records);

    // When
    var results = repository.getCoreRecords(records.stream().map(Pair::getLeft).toList(),
        tableName);
    repository.deleteTable(tableName);

    // Then
    assertThat(results).isEqualTo(
        records.stream().collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));
  }

  @Test
  void getCoreExtensionRecords() {
    // Given
    var tableName = "XXX-XXX-XXX_Extension";
    var records = givenExtensionRecord();
    repository.createTable(tableName);
    repository.postRecords(tableName, records);

    // When
    var results = repository.getRecords(records.stream().map(Pair::getLeft).toList(), tableName);
    repository.deleteTable(tableName);

    // Then
    assertThat(results).isEqualTo(
        records.stream().collect(groupingBy(Pair::getLeft, mapping(Pair::getRight, toList()))));
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

}
