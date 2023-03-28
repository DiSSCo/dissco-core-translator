package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganisationNameTest {

  private final OrganisationName organisationName = new OrganisationName();

  @Test
  void testGetTerm(){
    // When
    var result = organisationName.getTerm();

    // Then
    assertThat(result).isEqualTo(OrganisationName.TERM);

  }
}
