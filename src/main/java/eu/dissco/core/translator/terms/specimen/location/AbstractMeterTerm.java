package eu.dissco.core.translator.terms.specimen.location;

import eu.dissco.core.translator.terms.Term;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMeterTerm extends Term {

  private static final Pattern M_PATTERN = Pattern.compile("((-\\s?)?\\d+([.,]\\d+)?)\\s*m\\.?(eter)?(tr)?(\\sm.?)?");

  protected String sanitizeInput(String input) {
    input = input.trim().toLowerCase();
    Matcher matcher = M_PATTERN.matcher(input);
    if (matcher.matches()) {
      log.debug("Parsed Number: {}", matcher.group(1));
      return matcher.group(1);
    } else {
      log.debug("Input string does not match the expected format: {}", input);
      return input;
    }
  }

}
