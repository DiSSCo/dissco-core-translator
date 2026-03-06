package eu.dissco.core.translator.terms;

import eu.dissco.core.translator.Profiles;
import eu.dissco.core.translator.component.OrganisationNameComponent;
import eu.dissco.core.translator.component.SourceSystemComponent;
import eu.dissco.core.translator.properties.FdoProperties;
import eu.dissco.core.translator.schema.ChronometricAge;
import eu.dissco.core.translator.schema.Citation;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.Identification;
import eu.dissco.core.translator.schema.TaxonIdentification;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
@Component
@Profile(Profiles.DWCA)
public class DwcaDigitalObjectDirector extends BaseDigitalObjectDirector {

  private static final String EXTENSION = "extensions";
  private static final Citation EMTPY_CITATION = new Citation().withType("ods:Citation");
  private static final Identification EMPTY_IDENTIFICATION = new Identification().withType(
      "ods:Identification").withOdsHasTaxonIdentifications(
      List.of(new TaxonIdentification().withType("ods:TaxonIdentification")));

  public DwcaDigitalObjectDirector(JsonMapper mapper, TermMapper termMapper,
      OrganisationNameComponent rorComponent, SourceSystemComponent sourceSystemComponent,
      FdoProperties fdoProperties) {
    super(mapper, termMapper, rorComponent, sourceSystemComponent, fdoProperties,
        identifierTerms());
  }

  private static List<String> identifierTerms() {
    var list = new ArrayList<String>();
    list.add("dwc:occurrenceID");
    list.add("dwca:ID");
    list.add("dwc:catalogNumber");
    list.add("dwc:otherCatalogNumbers");
    list.add("dcterms:identifier");
    list.add("dwc:materialEntityID");
    list.add("http://rs.tdwg.org/dwc/terms/materialEntityID");
    return list;
  }

  @Override
  protected void addStandardSpecificLogic(DigitalSpecimen ds, JsonNode data) {
    log.debug("DWCA does not need to add any specific logic");
  }

  @Override
  protected List<Citation> assembleSpecimenCitations(JsonNode data, boolean dwc) {
    var citations = new ArrayList<Citation>();
    if (data.get(EXTENSION) != null
        && data.get(EXTENSION).get("gbif:Reference") != null) {
      var references = data.get(EXTENSION).get("gbif:Reference");
      for (int i = 0; i < references.size(); i++) {
        var citationJson = references.get(i);
        if (citationJson.properties().size() <= 1) {
          log.debug("Skipping citation with only one property: {}", citationJson);
        } else {
          citations.add(createCitation(citationJson, dwc));
        }
      }
    } else {
      var citation = createCitation(data, dwc);
      if (!Objects.equals(citation, EMTPY_CITATION)) {
        citations.add(citation);
      }
    }
    return citations;
  }

  @Override
  protected List<Citation> assembleIdentificationCitations(JsonNode data, boolean dwc) {
    log.debug("DWCA does not have identification citations");
    return List.of();
  }

  @Override
  protected List<Identification> assembleIdentifications(JsonNode data, boolean dwc) {
    var mappedIdentifications = new ArrayList<Identification>();
    if (data.get(EXTENSION) != null
        && data.get(EXTENSION).get("dwc:Identification") != null) {
      var identifications = data.get(EXTENSION).get("dwc:Identification");
      for (int i = 0; i < identifications.size(); i++) {
        var identification = identifications.get(i);
        mappedIdentifications.add(createIdentification(identification, dwc));
      }
    }
    var occurrenceIdentification = createIdentification(data, dwc);
    if (!Objects.equals(occurrenceIdentification, EMPTY_IDENTIFICATION)) {
      mappedIdentifications.add(occurrenceIdentification);
    }

    if (mappedIdentifications.size() == 1
        && mappedIdentifications.getFirst().getOdsIsVerifiedIdentification() == null) {
      //If there is only one identification, and it doesn't have a verification status, set it to true
      mappedIdentifications.getFirst().setOdsIsVerifiedIdentification(Boolean.TRUE);
    }
    return mappedIdentifications;
  }

  @Override
  protected List<ChronometricAge> assembleChronometricAges(JsonNode data, boolean dwc) {
    var mappedChrono = new ArrayList<ChronometricAge>();
    if (data.get(EXTENSION) != null
        && data.get(EXTENSION).get("http://rs.tdwg.org/chrono/terms/ChronometricAge") != null) {
      var chronometricAges = data.get(EXTENSION)
          .get("http://rs.tdwg.org/chrono/terms/ChronometricAge");
      for (int i = 0; i < chronometricAges.size(); i++) {
        var chronometricAge = chronometricAges.get(i);
        mappedChrono.add(createChronometricAge(chronometricAge, dwc));
      }
    }
    return mappedChrono;
  }
}


