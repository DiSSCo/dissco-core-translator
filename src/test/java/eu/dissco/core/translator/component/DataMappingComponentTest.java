package eu.dissco.core.translator.component;

import static eu.dissco.core.translator.TestUtils.DEFAULT_MAPPING;
import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.MAPPING_JSON;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_ID;
import static eu.dissco.core.translator.TestUtils.TERM_MAPPING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import eu.dissco.core.translator.properties.ApplicationProperties;
import eu.dissco.core.translator.repository.DataMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataMappingComponentTest {

  @Mock
  private ApplicationProperties properties;
  @Mock
  private DataMappingRepository repository;

  private DataMappingComponent dataMappingComponent;

  @BeforeEach
  void setup() {
    dataMappingComponent = new DataMappingComponent(properties, repository);
  }

  @Test
  void testSetup() throws Exception {
    // Given
    given(properties.getSourceSystemId()).willReturn(SOURCE_SYSTEM_ID);
    given(repository.retrieveMapping(SOURCE_SYSTEM_ID)).willReturn(MAPPER.readTree(MAPPING_JSON));

    // When
    dataMappingComponent.setup();

    // Then
    assertThat(dataMappingComponent.getFieldMappings()).isEqualTo(TERM_MAPPING);
    assertThat(dataMappingComponent.getDefaults()).isEqualTo(DEFAULT_MAPPING);
  }

}
