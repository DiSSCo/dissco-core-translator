package eu.dissco.core.translator.terms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.Profiles;
import eu.dissco.core.translator.component.OrganisationNameComponent;
import eu.dissco.core.translator.component.SourceSystemComponent;
import eu.dissco.core.translator.properties.FdoProperties;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.schema.Citation;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.Identification;
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
      OrganisationNameComponent rorComponent, SourceSystemComponent sourceSystemComponent,
      FdoProperties fdoProperties) {
    super(mapper, termMapper, rorComponent, sourceSystemComponent, fdoProperties, identifierTerms());
  }

  private static List<String> identifierTerms() {
    var list = new ArrayList<String>();
    list.add("abcd:id");
    list.add("abcd:unitID");
    list.add("abcd:unitIDNumeric");
    list.add("abcd:unitGUID");
    list.add("abcd:recordURI");
    // For now just look at the two first accession numbers as a shortcut
    list.add(
        "abcd:specimenUnit/accessions/accessionDateAndAccessionCatalogueAndAccessionNumber/0/value");
    list.add(
        "abcd:specimenUnit/accessions/accessionDateAndAccessionCatalogueAndAccessionNumber/1/value");
    return list;
  }

  @Override
  protected void addStandardSpecificLogic(DigitalSpecimen ds, JsonNode data) {
    new AbcdTypeInformation().addTypeInformation(ds, data, mapper);
  }

  @Override
  protected List<Citation> assembleSpecimenCitations(JsonNode data, boolean dwc) {
    return getCitations(data, dwc, "abcd:unitReferences/unitReference/");
  }

  @Override
  protected List<Citation> assembleIdentificationCitations(JsonNode data, boolean dwc) {
    return getCitations(data, dwc, "references/reference/");
  }

  @Override
  protected List<Identification> assembleIdentifications(JsonNode data, boolean dwc) {
    var identifications = new ArrayList<Identification>();
    var iterateOverElements = true;
    var count = 0;
    while (iterateOverElements) {
      var identificationNode = getSubJsonAbcd(data, count,
          List.of("abcd:identifications/identification/",
              "abcd-efg:identifications/identification/"));
      if (!identificationNode.isEmpty()) {
        identifications.add(createIdentification(identificationNode, dwc));
        count++;
      } else {
        iterateOverElements = false;
      }
    }
    if (identifications.size() == 1
        && identifications.get(0).getOdsIsVerifiedIdentification() == null) {
      //If there is only one identification, and it doesn't have a verification status, set it to true
      identifications.get(0).setOdsIsVerifiedIdentification(Boolean.TRUE);
    }
    return identifications;
  }

  private ArrayList<Citation> getCitations(JsonNode data, boolean dwc, String subPath) {
    var citations = new ArrayList<Citation>();
    var iterateOverElements = true;
    var count = 0;
    while (iterateOverElements) {
      var citationNode = getSubJsonAbcd(data, count, List.of(subPath));
      if (!citationNode.isEmpty()) {
        citations.add(super.createCitation(citationNode, dwc));
        count++;
      } else {
        iterateOverElements = false;
      }
    }
    return citations;
  }

  private JsonNode getSubJsonAbcd(JsonNode data, int count, List<String> paths) {
    var subNode = mapper.createObjectNode();
    paths.forEach(path -> data.fields().forEachRemaining(field -> {
      if (field.getKey().startsWith(path + count)) {
        subNode.set(
            field.getKey().replace(path + count + "/", ""),
            field.getValue());
      }
    }));
    return subNode;
  }
}


