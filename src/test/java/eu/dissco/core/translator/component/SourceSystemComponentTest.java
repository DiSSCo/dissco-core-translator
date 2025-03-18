package eu.dissco.core.translator.component;

import static eu.dissco.core.translator.TestUtils.ENDPOINT;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_ID;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_NAME;
import static eu.dissco.core.translator.TestUtils.givenSourceSystemInformation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import eu.dissco.core.translator.domain.SourceSystemInformation;
import eu.dissco.core.translator.properties.ApplicationProperties;
import eu.dissco.core.translator.repository.SourceSystemRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SourceSystemComponentTest {

  @Mock
  private ApplicationProperties properties;
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
    assertThat(component.getSourceSystemFilters()).isEmpty();
  }

  @Test
  void testGetSourceSystemWithFilter() {
    // Given
    var filterStr = "<filter>foo</filter><filter>bar</filter>";
    given(properties.getSourceSystemId()).willReturn(SOURCE_SYSTEM_ID);
    given(repository.getSourceSystem(SOURCE_SYSTEM_ID)).willReturn(
        new SourceSystemInformation(SOURCE_SYSTEM_NAME, ENDPOINT, List.of("<filter>foo</filter>", "<filter>bar</filter>")));

    // When
    var component = new SourceSystemComponent(properties, repository);

    // Then
    assertThat(component.getSourceSystemName()).isEqualTo(SOURCE_SYSTEM_NAME);
    assertThat(component.getSourceSystemID()).isEqualTo(SOURCE_SYSTEM_ID);
    assertThat(component.getSourceSystemEndpoint()).isEqualTo(ENDPOINT);
    assertThat(component.getSourceSystemFilters()).isEqualTo(filterStr);
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
