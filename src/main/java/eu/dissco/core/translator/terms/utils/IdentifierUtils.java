package eu.dissco.core.translator.terms.utils;

import static eu.dissco.core.translator.schema.Identifier.DctermsType.DOI;
import static eu.dissco.core.translator.schema.Identifier.DctermsType.HANDLE;
import static eu.dissco.core.translator.schema.Identifier.DctermsType.URL;
import static eu.dissco.core.translator.schema.Identifier.OdsGupriLevel.GLOBALLY_UNIQUE_STABLE;
import static eu.dissco.core.translator.schema.Identifier.OdsGupriLevel.GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE;
import static eu.dissco.core.translator.schema.Identifier.OdsGupriLevel.GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE_FDO_COMPLIANT;

import eu.dissco.core.translator.schema.Identifier;
import eu.dissco.core.translator.schema.Identifier.DctermsType;
import eu.dissco.core.translator.schema.Identifier.OdsGupriLevel;
import eu.dissco.core.translator.schema.Identifier.OdsIdentifierStatus;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;

@Slf4j
public class IdentifierUtils {

  private static final Map<List<String>, Triple<DctermsType, String, OdsGupriLevel>> map = getPrefixMap();

  private IdentifierUtils() {
    // This is a Utility class
  }

  private static Map<List<String>, Triple<DctermsType, String, OdsGupriLevel>> getPrefixMap() {
    var linkedMap = new LinkedHashMap<List<String>, Triple<DctermsType, String, OdsGupriLevel>>();
    linkedMap.put(List.of("https://doi.org"),
        Triple.of(DOI, "DOI", GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE_FDO_COMPLIANT));
    linkedMap.put(List.of("https://hdl.handle.net"),
        Triple.of(HANDLE, "Handle", GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE_FDO_COMPLIANT));
    linkedMap.put(List.of("http://www.wikidata.org", "https://www.wikidata.org"),
        Triple.of(URL, "Wikidata", GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE));
    linkedMap.put(List.of("http://orcid.org", "https://orcid.org"),
        Triple.of(URL, "ORCID", GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE));
    linkedMap.put(List.of("http", "https"),
        Triple.of(URL, "URL", GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE));
    linkedMap.put(List.of("urn:uuid"),
        Triple.of(DctermsType.UUID, "UUID", GLOBALLY_UNIQUE_STABLE));
    return linkedMap;
  }

  public static Identifier addIdentifier(String identifierString) {
    return addIdentifier(identifierString, null, null);
  }

  public static Identifier addIdentifier(String identifierString, String identifierName) {
    return addIdentifier(identifierString, identifierName, null);
  }

  public static Identifier addIdentifier(String identifierString, String identifierName,
      OdsIdentifierStatus identifierStatus) {
    if (identifierString == null) {
      return null;
    }
    var identifier = new Identifier()
        .withId(identifierString)
        .withType("ods:Identifier")
        .withDctermsIdentifier(identifierString)
        .withOdsIdentifierStatus(identifierStatus);
    for (var entry : map.entrySet()) {
      for (var prefix : entry.getKey()) {
        if (identifierString.startsWith(prefix)) {
          identifier.setDctermsType(entry.getValue().getLeft());
          identifier.setDctermsTitle(getDcTermsTitle(identifierName, entry.getValue().getMiddle()));
          identifier.setOdsGupriLevel(entry.getValue().getRight());
          return identifier;
        }
      }
    }
    if (isValidUUID(identifierString)) {
      identifier.setDctermsType(DctermsType.UUID);
      identifier.setDctermsTitle(getDcTermsTitle(identifierName, "UUID"));
      identifier.setOdsGupriLevel(GLOBALLY_UNIQUE_STABLE);
      return identifier;
    } else {
      log.debug(
          "Unable to recognise the type of identifier: {}. Assuming locally unique identifier",
          identifierString);
      identifier.setDctermsType(DctermsType.LOCALLY_UNIQUE_IDENTIFIER);
      identifier.setDctermsTitle(identifierName);
      identifier.setOdsGupriLevel(OdsGupriLevel.LOCALLY_UNIQUE_STABLE);
      return identifier;
    }
  }

  private static String getDcTermsTitle(String identifierName, String defaultValue) {
    if (identifierName != null) {
      return identifierName;
    } else {
      return defaultValue;
    }
  }

  private static boolean isValidUUID(String identifierString) {
    try {
      UUID.fromString(identifierString);
      return true;
    } catch (IllegalArgumentException e) {
      log.debug("Identifier {} is not a valid UUID", identifierString);
      return false;
    }
  }
}
