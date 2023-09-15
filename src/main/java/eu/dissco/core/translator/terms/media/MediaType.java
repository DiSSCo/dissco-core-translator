package eu.dissco.core.translator.terms.media;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MediaType extends Term {

  public static final String TERM = "dcterms:type";

  private final List<String> dwcaTerms = List.of(TERM, "dc:type");
  private final List<String> imageFormats = List.of("IMAGE/JPG", "JPG", "IMAGE/JPEG",
      "JPEG", "IMAGE/PNG", "PNG", "IMAGE/TIF", "TIF");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    var recoveredType = super.searchJsonForTerm(unit, dwcaTerms);
    if (recoveredType == null) {
      var format = new Format().retrieveFromDWCA(unit);
      return checkFormat(format);
    } else {
      return parseToOdsType(recoveredType);
    }
  }

  private String parseToOdsType(String recoveredType) {
    switch (recoveredType) {
      case "StillImage", "Image" -> {
        return "StillImage";
      }
      case "Sound" -> {
        return "Sound";
      }
      case "MovingImage" -> {
        return "MovingImage";
      }
      default -> {
        return null;
      }
    }
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var format = new Format().retrieveFromABCD(unit);
    if (format != null) {
      format = format.toUpperCase();
      if (imageFormats.contains(format)) {
        return "StillImage";
      } else {
        log.warn("Unable to determine media type of digital media object");
        return null;
      }
    }
    log.info("No format available to base type on");
    return null;
  }

  private String checkFormat(String format) {
    if (format != null) {
      if (imageFormats.contains(format) || format.startsWith("image")) {
        return "StillImage";
      }
      if (format.startsWith("audio")) {
        return "Sound";
      }
      if (format.startsWith("video")) {
        return "MovingImage";
      }
    }
    log.info("Unable to determine type based on format");
    return null;
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
