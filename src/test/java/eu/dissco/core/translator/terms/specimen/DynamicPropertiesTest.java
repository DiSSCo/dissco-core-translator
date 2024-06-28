package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DynamicPropertiesTest {

  private static final String DYNAMIC_PROPERTIES_STRING = "{\"relativeHumidity\": 28,\"airTemperatureInCelsius\": 22}";
  private final DynamicProperties dynamicProperties = new DynamicProperties();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:dynamicProperties", DYNAMIC_PROPERTIES_STRING);

    // When
    var result = dynamicProperties.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(DYNAMIC_PROPERTIES_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = dynamicProperties.getTerm();

    // Then
    assertThat(result).isEqualTo(DynamicProperties.TERM);
  }
}
