package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.service.DwcaService.AC_MULTIMEDIA;
import static eu.dissco.core.translator.service.DwcaService.EXTENSIONS;
import static eu.dissco.core.translator.service.DwcaService.GBIF_MULTIMEDIA;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class IsKnownToContainMedia extends Term {

  public static final String TERM = ODS_PREFIX + "isKnownToContainMedia";
  private final List<String> dwcaTerms = List.of("dwc:associatedMedia");
  private final List<String> abcdTerms = List.of(
      "abcd:multiMediaObjects/multiMediaObject/0/fileURI");

  private static String toBoolean(String result) {
    return String.valueOf(result != null);
  }

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    var result = super.searchJsonForTerm(unit, dwcaTerms);
    if (result != null) {
      return String.valueOf(true);
    } else if (unit.get(EXTENSIONS) != null) {
      var extensions = unit.get(EXTENSIONS);
      var gbifExtension = extensions.get(GBIF_MULTIMEDIA);
      if (gbifExtension != null && gbifExtension.size() > 0) {
        return String.valueOf(true);
      }
      var acExtension = extensions.get(AC_MULTIMEDIA);
      if (acExtension != null && acExtension.size() > 0) {
        return String.valueOf(true);
      }
    }
    return String.valueOf(false);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var result = super.searchJsonForTerm(unit, abcdTerms);
    return toBoolean(result);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
