package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.dissco.core.translator.component.DataMappingComponent;
import eu.dissco.core.translator.exception.IllegalDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.jooq.exception.IntegrityConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DwcaRepositoryTest extends BaseRepositoryIT {

  @Mock
  private DataMappingComponent mappingComponent;
  private DwcaRepository repository;

  private static JsonNode givenRecord(String corruptedValue, String id) {
    var objectNode = MAPPER.createObjectNode();
    objectNode.put("someRandomInformation", "someRandomInformation");
    objectNode.put("dwc:occurrenceID", id);
    objectNode.put("someCorruptedInformation", corruptedValue);
    return objectNode;
  }

  private static ObjectNode getExtensionNode(String accessURI) {
    return MAPPER.createObjectNode().put("ac:accessURI", accessURI);
  }

  private static ObjectNode getCoreNode(String uuid) {
    return MAPPER.createObjectNode().put("dwc:occurrenceID", uuid);
  }

  @BeforeEach
  void setup() {
    repository = new DwcaRepository(MAPPER, context, mappingComponent);
  }

  @Test
  void getCoreRecords() {
    // Given
    var tableName = "temp_XXX-XXX-XXX_Core";
    var records = givenCoreRecords();
    repository.createTable(tableName, DwcTerm.Occurrence);
    repository.postRecords(tableName, DwcTerm.Occurrence, records);

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
    String id = UUID.randomUUID().toString();
    var records = List.of(
        Pair.of(id, givenRecord("\u0000 someCorruptedInformation", id)));
    repository.createTable(tableName, DwcTerm.Occurrence);
    repository.postRecords(tableName, DwcTerm.Occurrence, records);

    // When
    var results = repository.getCoreRecords(records.stream().map(Pair::getLeft).toList(),
        tableName);
    repository.deleteTable(tableName);

    // Then
    assertThat(results).isEqualTo(
        Map.of(records.get(0).getLeft(), givenRecord(" someCorruptedInformation", id)));
  }

  @Test
  void exceptionDuplicatePhysicalSpecimenID() {
    // Given
    var tableName = "temp_XXX-XXX-XXX_Core";
    String id = UUID.randomUUID().toString();
    var records = List.of(
        Pair.of(id, (JsonNode) getCoreNode(id)),
        Pair.of(id, (JsonNode) getCoreNode(id)));
    repository.createTable(tableName, DwcTerm.Occurrence);

    // When / Then
    assertThrows(IntegrityConstraintViolationException.class,
        () -> repository.postRecords(tableName, DwcTerm.Occurrence, records));
    repository.deleteTable(tableName);
  }

  @Test
  void exceptionNoPhysicalSpecimenID() {
    // Given
    var tableName = "temp_XXX-XXX-XXX_Core";
    String id = UUID.randomUUID().toString();
    var records = List.of(
        Pair.of(id, (JsonNode) getCoreNode(null)));
    repository.createTable(tableName, DwcTerm.Occurrence);

    // When / Then
    assertThrows(IllegalDataException.class,
        () -> repository.postRecords(tableName, DwcTerm.Occurrence, records));
    repository.deleteTable(tableName);
  }

  @Test
  void getCoreExtensionRecords() {
    // Given
    var tableName = "temp_XXX-XXX-XXX_Extension";
    var records = givenExtensionRecord();
    repository.createTable(tableName, GbifTerm.Multimedia);
    repository.postRecords(tableName, GbifTerm.Multimedia, records);

    // When
    var results = repository.getRecords(records.stream().map(Pair::getLeft).toList(), tableName);
    repository.deleteTable(tableName);

    // Then
    assertThat(results)
        .hasSize(10)
        .isEqualTo(
            records.stream().distinct()
                .collect(groupingBy(Pair::getLeft, mapping(Pair::getRight, toList()))));
  }

  @Test
  void exceptionNoAccessURI() {
    // Given
    var tableName = "temp_XXX-XXX-XXX_Core";
    String id = UUID.randomUUID().toString();
    var records = List.of(
        Pair.of(id, (JsonNode) getExtensionNode(null)));
    repository.createTable(tableName, GbifTerm.Multimedia);

    // When / Then
    assertThrows(IllegalDataException.class,
        () -> repository.postRecords(tableName, GbifTerm.Multimedia, records));
    repository.deleteTable(tableName);
  }

  private ArrayList<Pair<String, JsonNode>> givenExtensionRecord() {
    var records = new ArrayList<Pair<String, JsonNode>>();
    for (int i = 0; i < 10; i++) {
      var id = UUID.randomUUID().toString();
      for (int j = 0; j < 3; j++) {
        var pair = Pair.of(
            id,
            (JsonNode) getExtensionNode("http://example.com/" + j)
        );
        records.add(pair);
      }
    }
    return records;
  }

  private List<Pair<String, JsonNode>> givenCoreRecords() {
    var records = new ArrayList<Pair<String, JsonNode>>();
    for (int i = 0; i < 10; i++) {
      var uuid = UUID.randomUUID().toString();
      var pair = Pair.of(
          uuid,
          (JsonNode) getCoreNode(uuid)
      );
      records.add(pair);
    }
    return records;
  }

}
