package eu.dissco.core.translator.terms;

import eu.dissco.core.translator.terms.utils.LicenseUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;

@Slf4j
public class License extends Term {

  public static final String TERM = DCTERMS_PREFIX + "license";

  // Fall back to dc terms rights if no license is present
  private final List<String> dwcaTerms = List.of(TERM, "dc:license",
      "dcterms:rights", "dc:rights", "eml:license");
  private final List<String> abcdUnitTerms = List.of(
      "abcd:iprstatements/licenses/license/0/uri",
      "abcd:iprstatements/licenses/license/0/text",
      "abcd:ipr/licenses/license/0/uri",
      "abcd:ipr/licenses/license/0/text");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    var license = super.searchJsonForTerm(unit, dwcaTerms);
    var harmonisedLicense = LicenseUtils.harmoniseLicense(license);
    if (harmonisedLicense == null) {
        return null;
    } else {
        return harmonisedLicense.getUrl();
    }
  }

  @Override
  public String getTerm() {
    return TERM;
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var license = searchJsonForTerm(unit, abcdUnitTerms);
    var harmonisedLicense = LicenseUtils.harmoniseLicense(license);
    if (harmonisedLicense == null) {
      return null;
    } else {
        return harmonisedLicense.getUrl();
    }
  }
}
