package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class HasMedia extends Term {

  public static final String TERM = ODS_PREFIX + "hasMedia";
  private final List<String> dwcaTerms = List.of("dwc:associatedMedia");
  private final List<String> abcdTerms = List.of(
      "abcd:multiMediaObjects/multiMediaObject/0/fileURI");

  private static String toBoolean(String result) {
    return String.valueOf(result != null);
  }

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    var result = super.searchJsonForStringTerm(unit, dwcaTerms);
    if (result != null) {
      return String.valueOf(true);
    } else if (unit.get("extensions") != null) {
      var extensions = unit.get("extensions");
      var gbifExtension = extensions.get("gbif:Multimedia");
      if (gbifExtension != null && gbifExtension.size() > 0) {
        return String.valueOf(true);
      }
      var acExtension = extensions.get("http://rs.tdwg.org/ac/terms/Multimedia");
      if (acExtension != null && acExtension.size() > 0) {
        return String.valueOf(true);
      }
    }
    return String.valueOf(false);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var result = super.searchJsonForStringTerm(unit, abcdTerms);
    return toBoolean(result);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
