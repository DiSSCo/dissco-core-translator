package eu.dissco.core.translator.terms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.Profiles;
import eu.dissco.core.translator.component.RorComponent;
import eu.dissco.core.translator.properties.FdoProperties;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.schema.Citations;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.Identifications;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile(Profiles.DWCA)
public class DwcaDigitalObjectDirector extends BaseDigitalObjectDirector {

  private static final String EXTENSION = "extensions";

  public DwcaDigitalObjectDirector(ObjectMapper mapper, TermMapper termMapper,
      RorComponent rorComponent, WebClientProperties webClientProperties,
      FdoProperties fdoProperties) {
    super(mapper, termMapper, rorComponent, webClientProperties, fdoProperties, identifierTerms());
  }

  private static List<String> identifierTerms() {
    var list = new ArrayList<String>();
    list.add("dwc:occurrenceID");
    list.add("dwca:ID");
    list.add("dwc:catalogNumber");
    list.add("dwc:otherCatalogNumbers");
    list.add("dcterms:identifier");
    return list;
  }

  @Override
  protected void addStandardSpecificLogic(DigitalSpecimen ds, JsonNode data) {
    log.debug("DWCA does not need to add any specific logic");
  }

  @Override
  protected List<Citations> assembleSpecimenCitations(JsonNode data, boolean dwc) {
    var citations = new ArrayList<Citations>();
    if (data.get(EXTENSION) != null
        && data.get(EXTENSION).get("gbif:Reference") != null) {
      var references = data.get(EXTENSION).get("gbif:Reference");
      for (int i = 0; i < references.size(); i++) {
        var identification = references.get(i);
        citations.add(createCitation(identification, dwc));
      }
    } else {
      citations.add(createCitation(data, dwc));
    }
    return citations;
  }

  @Override
  protected List<Citations> assembleIdentificationCitations(JsonNode data, boolean dwc) {
    log.debug("DWCA does not have identification citations");
    return List.of();
  }

  @Override
  protected List<Identifications> assembleIdentifications(JsonNode data, boolean dwc) {
    var mappedIdentifications = new ArrayList<Identifications>();
    if (data.get(EXTENSION) != null
        && data.get(EXTENSION).get("dwc:Identification") != null) {
      var identifications = data.get(EXTENSION).get("dwc:Identification");
      for (int i = 0; i < identifications.size(); i++) {
        var identification = identifications.get(i);
        mappedIdentifications.add(createIdentification(identification, dwc));
      }
    } else {
      mappedIdentifications.add(createIdentification(data, dwc));
    }
    return mappedIdentifications;
  }
}


