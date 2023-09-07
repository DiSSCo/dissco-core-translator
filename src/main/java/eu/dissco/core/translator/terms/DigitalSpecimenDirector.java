package eu.dissco.core.translator.terms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.component.RorComponent;
import eu.dissco.core.translator.exception.OrganisationNotRorId;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsTopicDiscipline;
import eu.dissco.core.translator.schema.Georeference;
import eu.dissco.core.translator.schema.Identifications;
import eu.dissco.core.translator.schema.Identifiers;
import eu.dissco.core.translator.schema.Occurrences.DwcOccurrenceStatus;
import eu.dissco.core.translator.schema.TaxonIdentification;
import eu.dissco.core.translator.terms.specimen.AccessRights;
import eu.dissco.core.translator.terms.specimen.BasisOfRecord;
import eu.dissco.core.translator.terms.specimen.DatasetName;
import eu.dissco.core.translator.terms.specimen.DcTermsType;
import eu.dissco.core.translator.terms.specimen.Disposition;
import eu.dissco.core.translator.terms.specimen.LivingOrPreserved;
import eu.dissco.core.translator.terms.specimen.Modified;
import eu.dissco.core.translator.terms.specimen.ObjectType;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenCollection;
import eu.dissco.core.translator.terms.specimen.RecordedBy;
import eu.dissco.core.translator.terms.specimen.RightsHolder;
import eu.dissco.core.translator.terms.specimen.TopicDiscipline;
import eu.dissco.core.translator.terms.specimen.citation.BibliographicCitation;
import eu.dissco.core.translator.terms.specimen.location.Continent;
import eu.dissco.core.translator.terms.specimen.location.Country;
import eu.dissco.core.translator.terms.specimen.location.CountryCode;
import eu.dissco.core.translator.terms.specimen.location.County;
import eu.dissco.core.translator.terms.specimen.location.HigherGeography;
import eu.dissco.core.translator.terms.specimen.location.Island;
import eu.dissco.core.translator.terms.specimen.location.IslandGroup;
import eu.dissco.core.translator.terms.specimen.location.Locality;
import eu.dissco.core.translator.terms.specimen.location.StateProvince;
import eu.dissco.core.translator.terms.specimen.location.WaterBody;
import eu.dissco.core.translator.terms.specimen.location.georeference.CoordinatePrecision;
import eu.dissco.core.translator.terms.specimen.location.georeference.CoordinateUncertaintyInMeters;
import eu.dissco.core.translator.terms.specimen.location.georeference.DecimalLatitude;
import eu.dissco.core.translator.terms.specimen.location.georeference.DecimalLongitude;
import eu.dissco.core.translator.terms.specimen.location.georeference.GeodeticDatum;
import eu.dissco.core.translator.terms.specimen.location.georeference.GeoreferenceProtocol;
import eu.dissco.core.translator.terms.specimen.occurence.CollectingNumber;
import eu.dissco.core.translator.terms.specimen.occurence.DateCollected;
import eu.dissco.core.translator.terms.specimen.occurence.EventRemark;
import eu.dissco.core.translator.terms.specimen.occurence.FieldNotes;
import eu.dissco.core.translator.terms.specimen.occurence.LifeStage;
import eu.dissco.core.translator.terms.specimen.occurence.OccurrenceAssertions;
import eu.dissco.core.translator.terms.specimen.occurence.OccurrenceStatus;
import eu.dissco.core.translator.terms.specimen.occurence.OrganismQuantity;
import eu.dissco.core.translator.terms.specimen.occurence.OrganismQuantityType;
import eu.dissco.core.translator.terms.specimen.occurence.SamplingProtocol;
import eu.dissco.core.translator.terms.specimen.occurence.Sex;
import eu.dissco.core.translator.terms.specimen.stratigraphy.biostratigraphic.HighestBiostratigraphicZone;
import eu.dissco.core.translator.terms.specimen.stratigraphy.biostratigraphic.LowestBiostratigraphicZone;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.EarliestAgeOrLowestStage;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.EarliestEpochOrLowestSeries;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.EarliestEraOrLowestErathem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.EarliestPeriodOrLowestSystem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.LatestAgeOrHighestStage;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.LatestEpochOrHighestSeries;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.LatestEraOrHighestErathem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.LatestPeriodOrHighestSystem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Bed;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Formation;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Group;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Member;
import eu.dissco.core.translator.terms.specimen.taxonomy.Class;
import eu.dissco.core.translator.terms.specimen.taxonomy.Family;
import eu.dissco.core.translator.terms.specimen.taxonomy.Genus;
import eu.dissco.core.translator.terms.specimen.taxonomy.IdentificationVerificationStatus;
import eu.dissco.core.translator.terms.specimen.taxonomy.Kingdom;
import eu.dissco.core.translator.terms.specimen.taxonomy.NomenclaturalCode;
import eu.dissco.core.translator.terms.specimen.taxonomy.Order;
import eu.dissco.core.translator.terms.specimen.taxonomy.Phylum;
import eu.dissco.core.translator.terms.specimen.taxonomy.ScientificNameAuthorship;
import eu.dissco.core.translator.terms.specimen.taxonomy.SpecificEpithet;
import eu.dissco.core.translator.terms.specimen.taxonomy.SpecimenName;
import eu.dissco.core.translator.terms.specimen.taxonomy.TaxonRank;
import eu.dissco.core.translator.terms.specimen.taxonomy.TaxonomicStatus;
import eu.dissco.core.translator.terms.specimen.taxonomy.TypeStatus;
import eu.dissco.core.translator.terms.specimen.taxonomy.VernacularName;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DigitalSpecimenDirector {

  private final ObjectMapper mapper;
  private final TermMapper termMapper;
  private final RorComponent rorComponent;

  public static List<String> identifierTerms() {
    var list = new ArrayList<String>();
    list.add("dwc:occurrenceID");
    list.add("dwca:ID");
    list.add("dwc:catalogNumber");
    list.add("dwc:otherCatalogNumbers");
    list.add("abcd:unitID");
    list.add("abcd:unitIDNumeric");
    list.add("abcd:unitGUID");
    list.add("abcd:recordURI");
    return list;
  }

  public DigitalSpecimen constructDigitalSpecimen(DigitalSpecimen ds, boolean dwc, JsonNode data)
      throws OrganisationNotRorId {
    assembleDigitalSpecimenTerms(data, ds, dwc);
    assembleOccurrenceTerms(data, ds, dwc);
    assembleIdentifications(data, ds, dwc);
    assembleIdentifiers(data, ds);
    assembleEntityRelationships(data, ds);
    assembleCitations(data, ds, dwc);
    return ds;
  }

  private DigitalSpecimen assembleCitations(JsonNode data,
      eu.dissco.core.translator.schema.DigitalSpecimen ds, boolean dwc) {
    var citation = new eu.dissco.core.translator.schema.Citations()
        .withBibliographicCitation(termMapper.retrieveTerm(new BibliographicCitation(), data, dwc));
    ds.withCitations(List.of(citation));
    return ds;
  }

  private DigitalSpecimen assembleDigitalSpecimenTerms(JsonNode data, DigitalSpecimen ds,
      boolean dwc)
      throws OrganisationNotRorId {
    ds.withOdsTopicDiscipline(
            OdsTopicDiscipline.fromValue(termMapper.retrieveTerm(new TopicDiscipline(), data, dwc)))
        .withDctermsLicense(termMapper.retrieveTerm(new License(), data, dwc))
        .withOdsLivingOrPreserved(termMapper.retrieveTerm(new LivingOrPreserved(), data, dwc))
        .withDwcPreparations(termMapper.retrieveTerm(new ObjectType(), data, dwc))
        .withDwcCollectionId(termMapper.retrieveTerm(new PhysicalSpecimenCollection(), data, dwc))
        .withDctermsModified(termMapper.retrieveTerm(new Modified(), data, dwc))
        .withDwcInstitutionName(
            rorComponent.getRoRId(minifyOrganisationId(ds.getDwcInstitutionId())))
        .withDctermsType(termMapper.retrieveTerm(new DcTermsType(), data, dwc))
        .withDwcRecordedBy(termMapper.retrieveTerm(new RecordedBy(), data, dwc))
        .withDwcBasisOfRecord(termMapper.retrieveTerm(new BasisOfRecord(), data, dwc))
        .withDctermsAccessRights(termMapper.retrieveTerm(new AccessRights(), data, dwc))
        .withDctermsRightsHolder(termMapper.retrieveTerm(new RightsHolder(), data, dwc))
        .withDwcDatasetName(termMapper.retrieveTerm(new DatasetName(), data, dwc))
        .withDwcDisposition(termMapper.retrieveTerm(new Disposition(), data, dwc));
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

  private DigitalSpecimen assembleIdentifications(JsonNode data, DigitalSpecimen ds, boolean dwc) {
    List<Identifications> identifications = null;
    if (dwc) {
      identifications = gatherDwcaIdentifications(data, dwc);
    } else {
      identifications = gatherAbcdIdentifications(data, dwc);
    }
    return ds.withDwcIdentification(identifications);
  }

  private List<eu.dissco.core.translator.schema.Identifications> gatherAbcdIdentifications(
      JsonNode data, boolean dwc) {
    var identifications = new ArrayList<Identifications>();
    var iterateOverElements = true;
    var count = 0;
    while (iterateOverElements) {
      var identificationNode = getIdentificationJson(data, count);
      if (!identificationNode.isEmpty()) {
        identifications.add(createIdentification(identificationNode, dwc));
        count++;
      } else {
        iterateOverElements = false;
      }
    }
    return identifications;
  }

  private JsonNode getIdentificationJson(JsonNode data, int count) {
    var identificationNode = mapper.createObjectNode();
    data.fields().forEachRemaining(field -> {
      if (field.getKey().startsWith("abcd:identifications/identification/" + count)) {
        identificationNode.put(
            field.getKey().replace("abcd:identifications/identification/" + count + "/", ""),
            field.getValue());
      }
    });
    return identificationNode;
  }

  private List<Identifications> gatherDwcaIdentifications(
      JsonNode data, boolean dwc) {
    var mappedIdentifications = new ArrayList<Identifications>();
    if (data.get("extensions") != null
        && data.get("extensions").get("dwc:Identification") != null) {
      var identifications = data.get("extensions").get("dwc:Identification");
      for (int i = 0; i < identifications.size(); i++) {
        var identification = identifications.get(i);
        mappedIdentifications.add(createIdentification(identification, dwc));
      }
    } else {
      mappedIdentifications.add(createIdentification(data, dwc));
    }
    return mappedIdentifications;
  }

  private Identifications createIdentification(JsonNode data, boolean dwc) {
    var mappedTaxonIdentification = new TaxonIdentification()
        .withDwcKingdom(termMapper.retrieveTerm(new Kingdom(), data, dwc))
        .withDwcTaxonRank(termMapper.retrieveTerm(new TaxonRank(), data, dwc))
        .withDwcGenus(termMapper.retrieveTerm(new Genus(), data, dwc))
        .withDwcOrder(termMapper.retrieveTerm(new Order(), data, dwc))
        .withDwcScientificName(termMapper.retrieveTerm(new SpecimenName(), data, dwc))
        .withDwcScientificNameAuthorship(
            termMapper.retrieveTerm(new ScientificNameAuthorship(), data, dwc))
        .withDwcClass(termMapper.retrieveTerm(new Class(), data, dwc))
        .withDwcFamily(termMapper.retrieveTerm(new Family(), data, dwc))
        .withDwcPhylum(termMapper.retrieveTerm(new Phylum(), data, dwc))
        .withDwcSpecificEpithet(termMapper.retrieveTerm(new SpecificEpithet(), data, dwc))
        .withDwcTaxonomicStatus(termMapper.retrieveTerm(new TaxonomicStatus(), data, dwc))
        .withDwcNomenclaturalCode(termMapper.retrieveTerm(new NomenclaturalCode(), data, dwc))
        .withDwcVernacularName(termMapper.retrieveTerm(new VernacularName(), data, dwc));
    return new Identifications()
        .withDwcIdentificationVerificationStatus(Boolean.valueOf(
            termMapper.retrieveTerm(new IdentificationVerificationStatus(), data, dwc)))
        .withDwcTypeStatus(termMapper.retrieveTerm(new TypeStatus(), data, dwc))
        .withTaxonIdentifications(List.of(mappedTaxonIdentification));
  }

  private eu.dissco.core.translator.schema.DigitalSpecimen assembleOccurrenceTerms(JsonNode data,
      eu.dissco.core.translator.schema.DigitalSpecimen ds, boolean dwc) {
    var georeference = new Georeference()
        .withDwcDecimalLatitude(
            termMapper.retrieveTerm(new DecimalLatitude(), data, dwc) != null ? Double.valueOf(
                termMapper.retrieveTerm(new DecimalLatitude(), data, dwc)) : null)
        .withDwcDecimalLongitude(
            termMapper.retrieveTerm(new DecimalLongitude(), data, dwc) != null ? Double.valueOf(
                termMapper.retrieveTerm(new DecimalLongitude(), data, dwc)) : null)
        .withDwcGeodeticDatum(termMapper.retrieveTerm(new GeodeticDatum(), data, dwc))
        .withDwcGeoreferenceProtocol(termMapper.retrieveTerm(new GeoreferenceProtocol(), data, dwc))
        .withDwcCoordinatePrecision(
            termMapper.retrieveTerm(new CoordinatePrecision(), data, dwc) != null ? Double.valueOf(
                termMapper.retrieveTerm(new CoordinatePrecision(), data, dwc)) : null)
        .withDwcCoordinateUncertaintyInMeters(
            termMapper.retrieveTerm(new CoordinateUncertaintyInMeters(), data, dwc) != null
                ? Double.valueOf(
                termMapper.retrieveTerm(new CoordinateUncertaintyInMeters(), data, dwc)) : null);
    var geologicalContext = new eu.dissco.core.translator.schema.GeologicalContext()
        .withDwcLowestBiostratigraphicZone(
            termMapper.retrieveTerm(new LowestBiostratigraphicZone(), data, dwc))
        .withDwcHighestBiostratigraphicZone(
            termMapper.retrieveTerm(new HighestBiostratigraphicZone(), data, dwc))
        .withDwcEarliestAgeOrLowestStage(
            termMapper.retrieveTerm(new EarliestAgeOrLowestStage(), data, dwc))
        .withDwcLatestAgeOrHighestStage(
            termMapper.retrieveTerm(new LatestAgeOrHighestStage(), data, dwc))
        .withDwcEarliestEpochOrLowestSeries(
            termMapper.retrieveTerm(new EarliestEpochOrLowestSeries(), data, dwc))
        .withDwcLatestEpochOrHighestSeries(
            termMapper.retrieveTerm(new LatestEpochOrHighestSeries(), data, dwc))
        .withDwcEarliestEraOrLowestErathem(
            termMapper.retrieveTerm(new EarliestEraOrLowestErathem(), data, dwc))
        .withDwcLatestEraOrHighestErathem(
            termMapper.retrieveTerm(new LatestEraOrHighestErathem(), data, dwc))
        .withDwcEarliestPeriodOrLowestSystem(
            termMapper.retrieveTerm(new EarliestPeriodOrLowestSystem(), data, dwc))
        .withDwcLatestPeriodOrHighestSystem(
            termMapper.retrieveTerm(new LatestPeriodOrHighestSystem(), data, dwc))
        .withDwcBed(termMapper.retrieveTerm(new Bed(), data, dwc))
        .withDwcFormation(termMapper.retrieveTerm(new Formation(), data, dwc))
        .withDwcGroup(termMapper.retrieveTerm(new Group(), data, dwc))
        .withDwcMember(termMapper.retrieveTerm(new Member(), data, dwc));
    var location = new eu.dissco.core.translator.schema.Location()
        .withDwcContinent(termMapper.retrieveTerm(new Continent(), data, dwc))
        .withDwcCountry(termMapper.retrieveTerm(new Country(), data, dwc))
        .withDwcCountryCode(termMapper.retrieveTerm(new CountryCode(), data, dwc))
        .withDwcCounty(termMapper.retrieveTerm(new County(), data, dwc))
        .withDwcIsland(termMapper.retrieveTerm(new Island(), data, dwc))
        .withDwcIslandGroup(termMapper.retrieveTerm(new IslandGroup(), data, dwc))
        .withDwcLocality(termMapper.retrieveTerm(new Locality(), data, dwc))
        .withDwcStateProvince(termMapper.retrieveTerm(new StateProvince(), data, dwc))
        .withDwcWaterBody(termMapper.retrieveTerm(new WaterBody(), data, dwc))
        .withDwcHigherGeography(termMapper.retrieveTerm(new HigherGeography(), data, dwc))
        .withGeoreference(georeference)
        .withGeologicalContext(geologicalContext);
    var assertions = new OccurrenceAssertions().gatherOccurrenceAssertions(data, dwc);
    var occurrence = new eu.dissco.core.translator.schema.Occurrences()
        .withDwcFieldNumber(termMapper.retrieveTerm(new CollectingNumber(), data, dwc))
        .withDwcEventDate(termMapper.retrieveTerm(new DateCollected(), data, dwc))
        .withDwcSex(termMapper.retrieveTerm(new Sex(), data, dwc))
        .withDwcOrganismQuantity(termMapper.retrieveTerm(new OrganismQuantity(), data, dwc))
        .withDwcOrganismQuantityType(termMapper.retrieveTerm(new OrganismQuantityType(), data, dwc))
        .withDwcSamplingProtocol(termMapper.retrieveTerm(new SamplingProtocol(), data, dwc))
        .withDwcLifeStage(termMapper.retrieveTerm(new LifeStage(), data, dwc))
        .withDwcEventRemarks(termMapper.retrieveTerm(new EventRemark(), data, dwc))
        .withDwcFieldNumber(termMapper.retrieveTerm(new CollectingNumber(), data, dwc))
        .withDwcFieldNotes(termMapper.retrieveTerm(new FieldNotes(), data, dwc))
        .withDwcOccurrenceStatus(termMapper.retrieveTerm(new OccurrenceStatus(), data, dwc) != null
            ? DwcOccurrenceStatus.fromValue(
            termMapper.retrieveTerm(new OccurrenceStatus(), data, dwc).toLowerCase()) : null)
        .withLocation(location)
        .withAssertions(assertions);

    return ds.withOccurrences(List.of(occurrence));
  }

  private String minifyOrganisationId(String organisationId) throws OrganisationNotRorId {
    if (organisationId.startsWith("https://ror.org")) {
      return organisationId.replace("https://ror.org/", "");
    } else {
      throw new OrganisationNotRorId(organisationId + " is not a valid ror");
    }
  }
}
