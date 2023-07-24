package eu.dissco.core.translator.terms;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.component.MappingComponent;
import eu.dissco.core.translator.component.RorComponent;
import eu.dissco.core.translator.exception.OrganisationNotRorId;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.Georeference;
import eu.dissco.core.translator.schema.Identifications;
import eu.dissco.core.translator.schema.Identifiers;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsTopicDiscipline;
import eu.dissco.core.translator.schema.TaxonIdentification;
import eu.dissco.core.translator.service.IngestionUtility;
import eu.dissco.core.translator.terms.specimen.CollectingNumber;
import eu.dissco.core.translator.terms.specimen.Collector;
import eu.dissco.core.translator.terms.specimen.DateCollected;
import eu.dissco.core.translator.terms.specimen.LivingOrPreserved;
import eu.dissco.core.translator.terms.specimen.ObjectType;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenCollection;
import eu.dissco.core.translator.terms.specimen.TopicDiscipline;
import eu.dissco.core.translator.terms.specimen.location.Continent;
import eu.dissco.core.translator.terms.specimen.location.Country;
import eu.dissco.core.translator.terms.specimen.location.CountryCode;
import eu.dissco.core.translator.terms.specimen.location.County;
import eu.dissco.core.translator.terms.specimen.location.DecimalLatitude;
import eu.dissco.core.translator.terms.specimen.location.DecimalLongitude;
import eu.dissco.core.translator.terms.specimen.location.GeodeticDatum;
import eu.dissco.core.translator.terms.specimen.location.Island;
import eu.dissco.core.translator.terms.specimen.location.IslandGroup;
import eu.dissco.core.translator.terms.specimen.location.Locality;
import eu.dissco.core.translator.terms.specimen.location.StateProvince;
import eu.dissco.core.translator.terms.specimen.location.WaterBody;
import eu.dissco.core.translator.terms.specimen.taxonomy.Class;
import eu.dissco.core.translator.terms.specimen.taxonomy.Family;
import eu.dissco.core.translator.terms.specimen.taxonomy.Genus;
import eu.dissco.core.translator.terms.specimen.taxonomy.Kingdom;
import eu.dissco.core.translator.terms.specimen.taxonomy.Order;
import eu.dissco.core.translator.terms.specimen.taxonomy.ScientificNameAuthorship;
import eu.dissco.core.translator.terms.specimen.taxonomy.SpecimenName;
import eu.dissco.core.translator.terms.specimen.taxonomy.TaxonRank;
import eu.dissco.core.translator.terms.specimen.taxonomy.TypeStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DigitalSpecimenDirector {

  private final MappingComponent mapping;
  private final RorComponent rorComponent;

  public static List<String> identifierTerms() {
    var list = new ArrayList<String>();
    list.add("dwc:occurrenceID");
    list.add("dwca:ID");
    list.add("dwc:otherCatalogNumbers");
    return list;
  }

  public DigitalSpecimen constructDigitalSpecimen(DigitalSpecimen ds, boolean dwc, JsonNode data)
      throws OrganisationNotRorId {
    assembleDigitalSpecimenTerms(data, ds, dwc);
    assembleOccurenceTerms(data, ds);
    assembleIdentifications(data, ds);
    assembleIdentifiers(data, ds);
    assembleEntityRelationships(data, ds);
    return ds;
  }

  private DigitalSpecimen assembleDigitalSpecimenTerms(JsonNode data, DigitalSpecimen ds, boolean dwc)
      throws OrganisationNotRorId {
    ds.withOdsTopicDiscipline(
            OdsTopicDiscipline.fromValue(new TopicDiscipline().retrieveFromABCD(data)))
        .withDctermsLicense(new License().retrieveFromABCD(data))
        .withOdsLivingOrPreserved(new LivingOrPreserved().retrieveFromDWCA(data))
        .withDwcPreparations(new ObjectType().retrieveFromDWCA(data))
        .withDwcCollectionId(new PhysicalSpecimenCollection().retrieveFromABCD(data))
        .withDwcInstitutionName(
            rorComponent.getRoRId(IngestionUtility.minifyOrganisationId(ds.getDwcInstitutionId())));
    return ds;
  }


  private DigitalSpecimen assembleEntityRelationships(
      JsonNode data, DigitalSpecimen ds) {
    return ds;
  }

  private DigitalSpecimen assembleIdentifiers(JsonNode data,
      DigitalSpecimen ds) {
    var identifiers = new ArrayList<Identifiers>();
    for (String identifierTerm : identifierTerms()) {
      if (data.get(identifierTerm) != null) {
        var identifier = new Identifiers()
            .withIdentifierType(identifierTerm)
            .withIdentifierValue(data.get(identifierTerm).asText());
        identifiers.add(identifier);
      }
    }
    return ds.withIdentifiers(identifiers);
  }

  private DigitalSpecimen assembleIdentifications(JsonNode data, DigitalSpecimen ds) {
    var identifications = gatherDwcaIdentifications(data);
    return ds.withDwcIdentification(identifications);
  }

  private List<Identifications> gatherDwcaIdentifications(
      JsonNode data) {
    var mappedIdentifications = new ArrayList<Identifications>();
    if (data.get("extensions") != null
        && data.get("extensions").get("dwc:Identification") != null) {
      var identifications = data.get("extensions").get("dwc:Identification");
      for (int i = 0; i < identifications.size(); i++) {
        var identification = identifications.get(i);
        var mappedTaxonIdentification = new TaxonIdentification()
            .withDwcClass(new Class().retrieveFromDWCA(identification))
            .withDwcScientificName(new SpecimenName().retrieveFromDWCA(identification));
        var mappedIdentification = new eu.dissco.core.translator.schema.Identifications()
            .withDwcTypeStatus(new TypeStatus().retrieveFromDWCA(identification))
            .withTaxonIdentifications(List.of(mappedTaxonIdentification));
        mappedIdentifications.add(mappedIdentification);
      }
    } else {
      var mappedTaxonIdentification = new eu.dissco.core.translator.schema.TaxonIdentification()
          .withDwcKingdom(new Kingdom().retrieveFromDWCA(data))
          .withDwcTaxonRank(new TaxonRank().retrieveFromDWCA(data))
          .withDwcGenus(new Genus().retrieveFromDWCA(data))
          .withDwcOrder(new Order().retrieveFromDWCA(data))
          .withDwcScientificName(new SpecimenName().retrieveFromDWCA(data))
          .withDwcScientificNameAuthorship(new ScientificNameAuthorship().retrieveFromDWCA(data))
          .withDwcClass(new Class().retrieveFromDWCA(data))
          .withDwcFamily(new Family().retrieveFromDWCA(data));
      var mappedIdentification = new eu.dissco.core.translator.schema.Identifications()
          .withDwcIdentificationVerificationStatus(true)
          .withDwcTypeStatus(new TypeStatus().retrieveFromDWCA(data))
          .withTaxonIdentifications(List.of(mappedTaxonIdentification));
      mappedIdentifications.add(mappedIdentification);
    }
    return mappedIdentifications;
  }

  private eu.dissco.core.translator.schema.DigitalSpecimen assembleOccurenceTerms(JsonNode data,
      eu.dissco.core.translator.schema.DigitalSpecimen ds) {
    var georeference = new Georeference()
        .withDwcDecimalLatitude(
            new DecimalLatitude().retrieveFromDWCA(data) != null ? Double.valueOf(
                new DecimalLatitude().retrieveFromDWCA(data)) : null)
        .withDwcDecimalLongitude(
            new DecimalLongitude().retrieveFromDWCA(data) != null ? Double.valueOf(
                new DecimalLongitude().retrieveFromDWCA(data)) : null)
        .withDwcGeodeticDatum(new GeodeticDatum().retrieveFromDWCA(data));
    var location = new eu.dissco.core.translator.schema.Location()
        .withDwcContinent(new Continent().retrieveFromDWCA(data))
        .withDwcCountry(new Country().retrieveFromDWCA(data))
        .withDwcCountryCode(new CountryCode().retrieveFromDWCA(data))
        .withDwcCounty(new County().retrieveFromDWCA(data))
        .withDwcIsland(new Island().retrieveFromDWCA(data))
        .withDwcIslandGroup(new IslandGroup().retrieveFromDWCA(data))
        .withDwcLocality(new Locality().retrieveFromDWCA(data))
        .withDwcStateProvince(new StateProvince().retrieveFromDWCA(data))
        .withDwcWaterBody(new WaterBody().retrieveFromDWCA(data))
        .withGeoreference(georeference);
    var occurrence = new eu.dissco.core.translator.schema.Occurrences()
        .withCollectorId(new Collector().retrieveFromABCD(data))
        .withDwcFieldNumber(new CollectingNumber().retrieveFromDWCA(data))
        .withDwcEventDate(new DateCollected().retrieveFromDWCA(data))
        .withLocation(location);

    return ds.withOccurrences(List.of(occurrence));
  }
}
