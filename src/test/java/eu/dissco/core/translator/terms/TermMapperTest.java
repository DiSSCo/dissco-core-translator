package eu.dissco.core.translator.terms;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.component.DataMappingComponent;
import eu.dissco.core.translator.terms.specimen.CollectionID;
import eu.dissco.core.translator.terms.specimen.OrganisationID;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenID;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TermMapperTest {

  @Mock
  private DataMappingComponent dataMappingComponent;

  private TermMapper termMapper;

  @BeforeEach
  void setup() {
    termMapper = new TermMapper(dataMappingComponent);
  }

  @Test
  void testRetrieveFromDWCADefault() {
    // Given
    given(dataMappingComponent.getDefaults()).willReturn(
        Map.of(OrganisationID.TERM, "https://ror.org/02y22ws83"));

    // When
    var result = termMapper.retrieveTerm(new OrganisationID(), mock(JsonNode.class), true);

    // Then
    assertThat(result).isEqualTo("https://ror.org/02y22ws83");
  }

  @Test
  void testRetrieveFromDWCAFieldMapping() {
    // Given
    var physicalSpecimenCollection = new CollectionID();
    var dwcaTerm = "dwc:collectionID";
    given(dataMappingComponent.getFieldMappings()).willReturn(
        Map.of(CollectionID.TERM, dwcaTerm));
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:collectionID", "TestCollection");

    // When
    var result = termMapper.retrieveTerm(physicalSpecimenCollection, unit, true);

    // Then
    assertThat(result).isEqualTo("TestCollection");
  }

  @Test
  void testRetrieveFromDWCANoMapping() {
    // Given
    var collectionId = mock(CollectionID.class);
    given(collectionId.getTerm()).willReturn(CollectionID.TERM);
    var attributes = mock(JsonNode.class);

    // When
    termMapper.retrieveTerm(collectionId, attributes, true);

    // Then
    then(collectionId).should().retrieveFromDWCA(attributes);
  }

  @Test
  void testRetrieveFromABCDDefault() {
    // Given
    var organisationId = new OrganisationID();
    given(dataMappingComponent.getDefaults()).willReturn(
        Map.of(OrganisationID.TERM, "https://ror.org/02y22ws83"));

    // When
    var result = termMapper.retrieveTerm(organisationId, mock(JsonNode.class), false);

    // Then
    assertThat(result).isEqualTo("https://ror.org/02y22ws83");
  }

  @Test
  void testRetrieveFromABCDFieldMapping() {
    // Given
    var abcdTerm = "abcd:unitID";
    var attributes = MAPPER.createObjectNode();
    attributes.put(abcdTerm, "123456");
    given(dataMappingComponent.getFieldMappings()).willReturn(
        Map.of(PhysicalSpecimenID.TERM, abcdTerm));

    // When
    var result = termMapper.retrieveTerm(new PhysicalSpecimenID(), attributes, false);

    // Then
    assertThat(result).isEqualTo("123456");
  }

  @Test
  void testRetrieveFromABCDNoMapping() {
    // Given
    var physicalSpecimenCollection = mock(CollectionID.class);
    given(physicalSpecimenCollection.getTerm()).willReturn(CollectionID.TERM);
    var attributes = mock(JsonNode.class);

    // When
    termMapper.retrieveTerm(physicalSpecimenCollection, attributes, false);

    // Then
    then(physicalSpecimenCollection).should().retrieveFromABCD(attributes);
  }

}
