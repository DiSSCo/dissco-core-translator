package eu.dissco.core.translator.terms.utils;

import static eu.dissco.core.translator.schema.Identifier.DctermsType.*;
import static eu.dissco.core.translator.schema.Identifier.OdsGupriLevel.GLOBALLY_UNIQUE_STABLE;
import static eu.dissco.core.translator.schema.Identifier.OdsGupriLevel.GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE;
import static eu.dissco.core.translator.schema.Identifier.OdsGupriLevel.GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE_FDO_COMPLIANT;
import static java.util.regex.Pattern.compile;

import eu.dissco.core.translator.schema.Identifier;
import eu.dissco.core.translator.schema.Identifier.DctermsType;
import eu.dissco.core.translator.schema.Identifier.OdsGupriLevel;
import eu.dissco.core.translator.schema.Identifier.OdsIdentifierStatus;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;

@Slf4j
public class IdentifierUtils {

  private static final Map<List<Pattern>, Triple<DctermsType, String, OdsGupriLevel>> PATTERN_MAP = patternMap();

  private IdentifierUtils() {
    // This is a Utility class
  }

  private static Map<List<Pattern>, Triple<DctermsType, String, OdsGupriLevel>> patternMap() {
    var linkedMap = new LinkedHashMap<List<Pattern>, Triple<DctermsType, String, OdsGupriLevel>>();
    linkedMap.put(List.of(compile("^https?://doi.org")),
        Triple.of(DOI, "DOI", GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE_FDO_COMPLIANT));
    linkedMap.put(List.of(compile("^https?://hdl.handle.net")),
        Triple.of(HANDLE, "Handle", GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE_FDO_COMPLIANT));
    linkedMap.put(List.of(compile("^https?://www.wikidata.org")),
        Triple.of(URL, "Wikidata", GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE));
    linkedMap.put(List.of(compile("^https?://orcid.org")),
        Triple.of(URL, "ORCID", GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE));
    linkedMap.put(List.of(compile("^https?://\\w+.\\w+/ark:/\\w+/\\w+")),
        Triple.of(ARK, "ARK", GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE));
    linkedMap.put(List.of(compile("https?://purl.org")),
        Triple.of(PURL, "PURL", GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE));
    linkedMap.put(List.of(compile("^https?")),
        Triple.of(URL, "URL", GLOBALLY_UNIQUE_STABLE_PERSISTENT_RESOLVABLE));
    linkedMap.put(List.of(compile(
            "(uuid:)*[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")),
        Triple.of(UUID, "UUID", GLOBALLY_UNIQUE_STABLE));
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
    for (var entry : PATTERN_MAP.entrySet()) {
      for (var prefix : entry.getKey()) {
        if (prefix.matcher(identifierString).find()) {
          identifier.setDctermsType(entry.getValue().getLeft());
          identifier.setDctermsTitle(getDcTermsTitle(identifierName, entry.getValue().getMiddle()));
          identifier.setOdsGupriLevel(entry.getValue().getRight());
          return identifier;
        }
      }
    }
    log.debug(
        "Unable to recognise the type of identifier: {}. Assuming locally unique identifier",
        identifierString);
    identifier.setDctermsType(DctermsType.LOCALLY_UNIQUE_IDENTIFIER);
    identifier.setDctermsTitle(identifierName);
    identifier.setOdsGupriLevel(OdsGupriLevel.LOCALLY_UNIQUE_STABLE);
    return identifier;
  }

  private static String getDcTermsTitle(String identifierName, String defaultValue) {
    if (identifierName != null) {
      return identifierName;
    } else {
      return defaultValue;
    }
  }
}
