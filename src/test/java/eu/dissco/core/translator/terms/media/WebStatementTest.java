package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WebStatementTest {

  private final WebStatement webStatement = new WebStatement();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var webStatementString = "https://creativecommons.org/licenses/by/4.0/";
    var unit = MAPPER.createObjectNode();
    unit.put("xmpRights:WebStatement", webStatementString);

    // When
    var result = webStatement.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(webStatementString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = webStatement.getTerm();

    // Then
    assertThat(result).isEqualTo(WebStatement.TERM);
  }

}
