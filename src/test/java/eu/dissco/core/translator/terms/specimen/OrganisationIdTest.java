package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganisationIdTest {

  private final OrganisationId organisationId = new OrganisationId();

  @Test
  void testGetTerm(){
    // When
    var result = organisationId.getTerm();

    // Then
    assertThat(result).isEqualTo(OrganisationId.TERM);

  }
}
