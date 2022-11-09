package eu.dissco.core.translator.component;

import static eu.dissco.core.translator.TestUtils.DWC_DEFAULTS;
import static eu.dissco.core.translator.TestUtils.DWC_FIELD_MAPPING;
import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.MAPPING_JSON;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.repository.MappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MappingComponentTest {

  @Mock
  private WebClientProperties properties;
  @Mock
  private MappingRepository repository;

  private MappingComponent mappingComponent;

  @BeforeEach
  void setup() {
    mappingComponent = new MappingComponent(properties, repository);
  }

  @Test
  void testSetup() throws Exception {
    // Given
    given(properties.getSourceSystemId()).willReturn(SOURCE_SYSTEM_ID);
    given(repository.retrieveMapping(SOURCE_SYSTEM_ID)).willReturn(MAPPER.readTree(MAPPING_JSON));

    // When
    mappingComponent.setup();

    // Then
    assertThat(mappingComponent.getFieldMappings()).isEqualTo(DWC_FIELD_MAPPING);
    assertThat(mappingComponent.getDefaultMappings()).isEqualTo(DWC_DEFAULTS);
  }

}
