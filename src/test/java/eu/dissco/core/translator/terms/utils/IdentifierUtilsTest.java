package eu.dissco.core.translator.terms.utils;

import static eu.dissco.core.translator.schema.Identifier.DctermsType.ARK;
import static eu.dissco.core.translator.schema.Identifier.DctermsType.DOI;
import static eu.dissco.core.translator.schema.Identifier.DctermsType.HANDLE;
import static eu.dissco.core.translator.schema.Identifier.DctermsType.LOCALLY_UNIQUE_IDENTIFIER;
import static eu.dissco.core.translator.schema.Identifier.DctermsType.PURL;
import static eu.dissco.core.translator.schema.Identifier.DctermsType.URL;
import static eu.dissco.core.translator.schema.Identifier.DctermsType.UUID;
import static eu.dissco.core.translator.schema.Identifier.OdsGupriLevel.GLOBALLY_UNIQUE_STABLE;
import static eu.dissco.core.translator.schema.Identifier.OdsGupriLevel.GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE;
import static eu.dissco.core.translator.schema.Identifier.OdsGupriLevel.GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE_FDO_COMPLIANT;
import static eu.dissco.core.translator.schema.Identifier.OdsGupriLevel.LOCALLY_UNIQUE_STABLE;
import static eu.dissco.core.translator.schema.Identifier.OdsIdentifierStatus.PREFERRED;
import static eu.dissco.core.translator.terms.utils.IdentifierUtils.addIdentifier;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.schema.Identifier;
import eu.dissco.core.translator.schema.Identifier.DctermsType;
import eu.dissco.core.translator.schema.Identifier.OdsGupriLevel;
import eu.dissco.core.translator.schema.Identifier.OdsIdentifierStatus;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdentifierUtilsTest {


  public static Stream<Arguments> identifierProvider() {
    return Stream.of(
        Arguments.of("https://doi.org/10.3535/M42-Z4P-DRD", null, PREFERRED,
            createIdentifier("https://doi.org/10.3535/M42-Z4P-DRD", DOI, "DOI",
                GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE_FDO_COMPLIANT, PREFERRED)),
        Arguments.of("https://doi.org/21.T11148/894b1e6cad57e921764e", null, PREFERRED,
            createIdentifier("https://doi.org/21.T11148/894b1e6cad57e921764e", HANDLE, "Handle",
                GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE_FDO_COMPLIANT, PREFERRED)),
        Arguments.of("https://www.wikidata.org/wiki/Q66581882", null, null,
            createIdentifier("https://www.wikidata.org/wiki/Q66581882", URL, "Wikidata",
                GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE, null)),
        Arguments.of("https://hdl.handle.net/XXX-XXX-XXX", null, PREFERRED,
            createIdentifier("https://hdl.handle.net/XXX-XXX-XXX", HANDLE, "Handle",
                GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE_FDO_COMPLIANT, PREFERRED)),
        Arguments.of("88e320d5-c47a-4288-b265-fa3c93c57440", "dwc:catalogueNumber", null,
            createIdentifier("88e320d5-c47a-4288-b265-fa3c93c57440", UUID, "dwc:catalogueNumber",
                GLOBALLY_UNIQUE_STABLE, null)),
        Arguments.of("urn:uuid:541fd754-17e8-43c8-ba4e-b413a1bf3a2f", "dwca:ID", null,
            createIdentifier("urn:uuid:541fd754-17e8-43c8-ba4e-b413a1bf3a2f", UUID, "dwca:ID",
                GLOBALLY_UNIQUE_STABLE, null)),
        Arguments.of("https://geocollections.info/specimen/126758", "abcd:unitGUID", null,
            createIdentifier("https://geocollections.info/specimen/126758", URL, "abcd:unitGUID",
                GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE, null)),
        Arguments.of("http://n2t.net/ark:/65665/3173bef93-f5c6-4534-bd31-42289606938b",
            "dwc:catalogueNumber", null,
            createIdentifier("http://n2t.net/ark:/65665/3173bef93-f5c6-4534-bd31-42289606938b", ARK,
                "dwc:catalogueNumber",
                GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE, null)),
        Arguments.of("http://purl.org/dc/terms/accessRights", null, null,
            createIdentifier("http://purl.org/dc/terms/accessRights", PURL, "PURL",
                GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE, null)),
        Arguments.of("AVES-071259", "dwc:occurrenceID", null,
            createIdentifier("AVES-071259", LOCALLY_UNIQUE_IDENTIFIER, "dwc:occurrenceID",
                LOCALLY_UNIQUE_STABLE, null)),
        Arguments.of(null, null, null, null)
    );
  }

  private static Identifier createIdentifier(String id, DctermsType type, String title,
      OdsGupriLevel gupriLevel, OdsIdentifierStatus status) {
    return new Identifier()
        .withId(id)
        .withType("ods:Identifier")
        .withDctermsIdentifier(id)
        .withDctermsTitle(title)
        .withDctermsType(type)
        .withOdsGupriLevel(gupriLevel)
        .withOdsIdentifierStatus(status);
  }

  @ParameterizedTest
  @MethodSource("identifierProvider")
  void testAddIdentifier(String identifier, String identifierName, OdsIdentifierStatus status,
      Identifier expected) {
    // When
    var result = addIdentifier(identifier, identifierName, status);

    // Then
    assertThat(result).isEqualTo(expected);
  }
}
