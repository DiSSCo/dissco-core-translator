package eu.dissco.core.translator.component;

import static eu.dissco.core.translator.TestUtils.ENDPOINT;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_ID;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_NAME;
import static eu.dissco.core.translator.TestUtils.givenSourceSystemInformation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.repository.SourceSystemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SourceSystemComponentTest {

  @Mock
  private WebClientProperties properties;
  @Mock
  private SourceSystemRepository repository;

  @Test
  void testGetSourceSystem() {
    // Given
    given(properties.getSourceSystemId()).willReturn(SOURCE_SYSTEM_ID);
    given(repository.getSourceSystem(SOURCE_SYSTEM_ID)).willReturn(givenSourceSystemInformation());

    // When
    var component = new SourceSystemComponent(properties, repository);

    // Then
    assertThat(component.getSourceSystemName()).isEqualTo(SOURCE_SYSTEM_NAME);
    assertThat(component.getSourceSystemID()).isEqualTo(SOURCE_SYSTEM_ID);
    assertThat(component.getSourceSystemEndpoint()).isEqualTo(ENDPOINT);
  }

  @Test
  void testFailedGetSourceSystem() {
    // Given
    given(properties.getSourceSystemId()).willReturn(SOURCE_SYSTEM_ID);
    given(repository.getSourceSystem(SOURCE_SYSTEM_ID)).willReturn(null);

    // When / Then
    assertThrows(IllegalArgumentException.class,
        () -> new SourceSystemComponent(properties, repository));
  }
}
