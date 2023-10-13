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
import eu.dissco.core.translator.terms.specimen.identification.AbcdTypeInformation;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile(Profiles.BIOCASE)
public class BiocaseDigitalObjectDirector extends BaseDigitalObjectDirector {

  public BiocaseDigitalObjectDirector(ObjectMapper mapper, TermMapper termMapper,
      RorComponent rorComponent, WebClientProperties webClientProperties,
      FdoProperties fdoProperties) {
    super(mapper, termMapper, rorComponent, webClientProperties, fdoProperties, identifierTerms());
  }

  private static List<String> identifierTerms() {
    var list = new ArrayList<String>();
    list.add("abcd:id");
    list.add("abcd:unitID");
    list.add("abcd:unitIDNumeric");
    list.add("abcd:unitGUID");
    list.add("abcd:recordURI");
    return list;
  }

  @Override
  protected void addStandardSpecificLogic(DigitalSpecimen ds, JsonNode data, boolean dwc) {
    new AbcdTypeInformation().addTypeInformation(ds, data, mapper);
  }

  @Override
  protected List<Citations> gatherCitations(JsonNode data, boolean dwc) {
    return getCitations(data, dwc, "abcd:unitReferences/unitReference/");
  }

  @Override
  protected List<Citations> gatherIdentificationCitations(JsonNode data, boolean dwc) {
    return getCitations(data, dwc, "references/reference/");
  }

  @Override
  protected List<Identifications> gatherIdentifications(JsonNode data, boolean dwc) {
    var identifications = new ArrayList<Identifications>();
    var iterateOverElements = true;
    var count = 0;
    while (iterateOverElements) {
      var identificationNode = getSubJsonAbcd(data, count, "abcd:identifications/identification/");
      if (!identificationNode.isEmpty()) {
        identifications.add(createIdentification(identificationNode, dwc));
        count++;
      } else {
        iterateOverElements = false;
      }
    }
    return identifications;
  }

  private ArrayList<Citations> getCitations(JsonNode data, boolean dwc, String subPath) {
    var citations = new ArrayList<Citations>();
    var iterateOverElements = true;
    var count = 0;
    while (iterateOverElements) {
      var citationNode = getSubJsonAbcd(data, count, subPath);
      if (!citationNode.isEmpty()) {
        citations.add(super.createCitation(citationNode, dwc));
        count++;
      } else {
        iterateOverElements = false;
      }
    }
    return citations;
  }

  private JsonNode getSubJsonAbcd(JsonNode data, int count, String path) {
    var identificationNode = mapper.createObjectNode();
    data.fields().forEachRemaining(field -> {
      if (field.getKey().startsWith(path + count)) {
        identificationNode.set(
            field.getKey().replace(path + count + "/", ""),
            field.getValue());
      }
    });
    return identificationNode;
  }
}


