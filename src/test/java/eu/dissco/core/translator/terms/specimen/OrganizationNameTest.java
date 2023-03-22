package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganizationNameTest {

  private final OrganizationName organizationName = new OrganizationName();

  @Test
  void testGetTerm(){
    // When
    var result = organizationName.getTerm();

    // Then
    assertThat(result).isEqualTo(OrganizationName.TERM);

  }
}
