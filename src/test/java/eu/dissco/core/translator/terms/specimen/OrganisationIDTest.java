package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganisationIDTest {

  private final OrganisationID organisationId = new OrganisationID();

  @Test
  void testGetTerm() {
    // When
    var result = organisationId.getTerm();

    // Then
    assertThat(result).isEqualTo(OrganisationID.TERM);

  }
}
