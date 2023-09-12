package eu.dissco.core.translator.terms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.component.RorComponent;
import eu.dissco.core.translator.exception.OrganisationNotRorId;
import eu.dissco.core.translator.schema.Citations;
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
import eu.dissco.core.translator.terms.specimen.citation.CitationRemarks;
import eu.dissco.core.translator.terms.specimen.citation.ReferenceIri;
import eu.dissco.core.translator.terms.specimen.identification.DateIdentified;
import eu.dissco.core.translator.terms.specimen.identification.IdentificationRemarks;
import eu.dissco.core.translator.terms.specimen.identification.IdentificationVerificationStatus;
import eu.dissco.core.translator.terms.specimen.identification.IdentifiedBy;
import eu.dissco.core.translator.terms.specimen.identification.TypeStatus;
import eu.dissco.core.translator.terms.specimen.identification.VerbatimIdentification;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Class;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Family;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Genus;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Kingdom;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.NomenclaturalCode;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Order;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Phylum;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.ScientificNameAuthorship;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.SpecificEpithet;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.SpecimenName;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.TaxonRank;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.TaxonomicStatus;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.VernacularName;
import eu.dissco.core.translator.terms.specimen.location.Continent;
import eu.dissco.core.translator.terms.specimen.location.Country;
import eu.dissco.core.translator.terms.specimen.location.CountryCode;
import eu.dissco.core.translator.terms.specimen.location.County;
import eu.dissco.core.translator.terms.specimen.location.HigherGeography;
import eu.dissco.core.translator.terms.specimen.location.Island;
import eu.dissco.core.translator.terms.specimen.location.IslandGroup;
import eu.dissco.core.translator.terms.specimen.location.Locality;
import eu.dissco.core.translator.terms.specimen.location.LocationAccordingTo;
import eu.dissco.core.translator.terms.specimen.location.LocationRemarks;
import eu.dissco.core.translator.terms.specimen.location.MaximumDepthInMeters;
import eu.dissco.core.translator.terms.specimen.location.MaximumDistanceAboveSurfaceInMeters;
import eu.dissco.core.translator.terms.specimen.location.MaximumElevationInMeters;
import eu.dissco.core.translator.terms.specimen.location.MinimumDepthInMeters;
import eu.dissco.core.translator.terms.specimen.location.MinimumDistanceAboveSurfaceInMeters;
import eu.dissco.core.translator.terms.specimen.location.MinimumElevationInMeters;
import eu.dissco.core.translator.terms.specimen.location.StateProvince;
import eu.dissco.core.translator.terms.specimen.location.VerticalDatum;
import eu.dissco.core.translator.terms.specimen.location.WaterBody;
import eu.dissco.core.translator.terms.specimen.location.georeference.CoordinatePrecision;
import eu.dissco.core.translator.terms.specimen.location.georeference.CoordinateUncertaintyInMeters;
import eu.dissco.core.translator.terms.specimen.location.georeference.DecimalLatitude;
import eu.dissco.core.translator.terms.specimen.location.georeference.DecimalLongitude;
import eu.dissco.core.translator.terms.specimen.location.georeference.FootprintSpatialFit;
import eu.dissco.core.translator.terms.specimen.location.georeference.FootprintWkt;
import eu.dissco.core.translator.terms.specimen.location.georeference.GeodeticDatum;
import eu.dissco.core.translator.terms.specimen.location.georeference.GeoreferenceProtocol;
import eu.dissco.core.translator.terms.specimen.location.georeference.GeoreferenceRemarks;
import eu.dissco.core.translator.terms.specimen.location.georeference.GeoreferenceSources;
import eu.dissco.core.translator.terms.specimen.location.georeference.GeoreferencedDate;
import eu.dissco.core.translator.terms.specimen.location.georeference.PointRadiusSpatialFit;
import eu.dissco.core.translator.terms.specimen.occurence.Behavior;
import eu.dissco.core.translator.terms.specimen.occurence.DataGeneralizations;
import eu.dissco.core.translator.terms.specimen.occurence.DateCollected;
import eu.dissco.core.translator.terms.specimen.occurence.DegreeOfEstablishment;
import eu.dissco.core.translator.terms.specimen.occurence.EstablishmentMeans;
import eu.dissco.core.translator.terms.specimen.occurence.EventRemark;
import eu.dissco.core.translator.terms.specimen.occurence.FieldNotes;
import eu.dissco.core.translator.terms.specimen.occurence.FieldNumber;
import eu.dissco.core.translator.terms.specimen.occurence.GeoreferenceVerificationStatus;
import eu.dissco.core.translator.terms.specimen.occurence.Habitat;
import eu.dissco.core.translator.terms.specimen.occurence.InformationWithheld;
import eu.dissco.core.translator.terms.specimen.occurence.LifeStage;
import eu.dissco.core.translator.terms.specimen.occurence.OccurrenceAssertions;
import eu.dissco.core.translator.terms.specimen.occurence.OccurrenceRemarks;
import eu.dissco.core.translator.terms.specimen.occurence.OccurrenceStatus;
import eu.dissco.core.translator.terms.specimen.occurence.OrganismQuantity;
import eu.dissco.core.translator.terms.specimen.occurence.OrganismQuantityType;
import eu.dissco.core.translator.terms.specimen.occurence.Pathway;
import eu.dissco.core.translator.terms.specimen.occurence.ReproductiveCondition;
import eu.dissco.core.translator.terms.specimen.occurence.SampleSizeUnit;
import eu.dissco.core.translator.terms.specimen.occurence.SampleSizeValue;
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
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DigitalSpecimenDirector {

  private static final String EXTENSION = "extensions";

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
    ds.withOccurrences(assembleOccurrenceTerms(data, dwc));
    ds.withDwcIdentification(assembleIdentifications(data, dwc));
    ds.withIdentifiers(assembleIdentifiers(data));
    ds.withEntityRelationships(assembleEntityRelationships(data));
    ds.withCitations(assembleSpecimenCitations(data, dwc));
    return ds;
  }

  private List<Citations> assembleSpecimenCitations(JsonNode data, boolean dwc) {
    List<Citations> citations;
    if (dwc) {
      citations = gatherDwcaCitations(data, dwc);
    } else {
      citations = gatherAbcdCitations(data, dwc, "abcd:unitReferences/unitReference/");
    }
    return citations;
  }

  private List<Citations> assembleIdentificationCitations(JsonNode data, boolean dwc) {
    List<Citations> citations = List.of();
    if (dwc) {
      log.debug("Reference extension has been added to the occurence");
    } else {
      citations = gatherAbcdCitations(data, dwc, "references/reference/");
    }
    return citations;
  }

  private List<Citations> gatherAbcdCitations(JsonNode data,
      boolean dwc, String subpath) {
    var citations = new ArrayList<Citations>();
    var iterateOverElements = true;
    var count = 0;
    while (iterateOverElements) {
      var citationNode = getSubJsonAbcd(data, count, subpath);
      if (!citationNode.isEmpty()) {
        citations.add(createCitation(citationNode, dwc));
        count++;
      } else {
        iterateOverElements = false;
      }
    }
    return citations;
  }

  private Citations createCitation(JsonNode data,
      boolean dwc) {
    return new eu.dissco.core.translator.schema.Citations()
        .withDctermsBibliographicCitation(
            termMapper.retrieveTerm(new BibliographicCitation(), data, dwc))
        .withCitationRemarks(termMapper.retrieveTerm(new CitationRemarks(), data, dwc))
        .withReferenceIri(termMapper.retrieveTerm(new ReferenceIri(), data, dwc));
  }

  private List<eu.dissco.core.translator.schema.Citations> gatherDwcaCitations(JsonNode data,
      boolean dwc) {
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


  private List<eu.dissco.core.translator.schema.EntityRelationships> assembleEntityRelationships(
      JsonNode data) {
    return List.of();
  }

  private List<Identifiers> assembleIdentifiers(JsonNode data) {
    var identifiers = new ArrayList<Identifiers>();
    for (String identifierTerm : identifierTerms()) {
      if (data.get(identifierTerm) != null) {
        var identifier = new Identifiers()
            .withIdentifierType(identifierTerm)
            .withIdentifierValue(data.get(identifierTerm).asText());
        identifiers.add(identifier);
      }
    }
    return identifiers;
  }

  private List<Identifications> assembleIdentifications(JsonNode data, boolean dwc) {
    List<Identifications> identifications = null;
    if (dwc) {
      identifications = gatherDwcaIdentifications(data, dwc);
    } else {
      identifications = gatherAbcdIdentifications(data, dwc);
    }
    return identifications;
  }

  private List<eu.dissco.core.translator.schema.Identifications> gatherAbcdIdentifications(
      JsonNode data, boolean dwc) {
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

  private JsonNode getSubJsonAbcd(JsonNode data, int count, String path) {
    var identificationNode = mapper.createObjectNode();
    data.fields().forEachRemaining(field -> {
      if (field.getKey().startsWith(path + count)) {
        identificationNode.put(
            field.getKey().replace(path + count + "/", ""),
            field.getValue());
      }
    });
    return identificationNode;
  }

  private List<Identifications> gatherDwcaIdentifications(
      JsonNode data, boolean dwc) {
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
        .withDwcDateIdentified(termMapper.retrieveTerm(new DateIdentified(), data, dwc))
        .withDwcIdentifiedBy(termMapper.retrieveTerm(new IdentifiedBy(), data, dwc))
        .withDwcIdentificationRemarks(
            termMapper.retrieveTerm(new IdentificationRemarks(), data, dwc))
        .withDwcVerbatimIdentification(
            termMapper.retrieveTerm(new VerbatimIdentification(), data, dwc))
        .withTaxonIdentifications(List.of(mappedTaxonIdentification))
        .withCitations(assembleIdentificationCitations(data, dwc));
  }


  private List<eu.dissco.core.translator.schema.Occurrences> assembleOccurrenceTerms(JsonNode data,
      boolean dwc) {
    var georeference = new Georeference()
        .withDwcDecimalLatitude(
            parseToDouble(new DecimalLatitude(), data, dwc))
        .withDwcDecimalLongitude(parseToDouble(new DecimalLongitude(), data, dwc))
        .withDwcGeodeticDatum(termMapper.retrieveTerm(new GeodeticDatum(), data, dwc))
        .withDwcGeoreferenceProtocol(termMapper.retrieveTerm(new GeoreferenceProtocol(), data, dwc))
        .withDwcCoordinatePrecision(parseToDouble(new CoordinatePrecision(), data, dwc))
        .withDwcCoordinateUncertaintyInMeters(parseToDouble(new CoordinateUncertaintyInMeters(), data, dwc))
        .withDwcFootprintSpatialFit(parseToInteger(new FootprintSpatialFit(), data, dwc))
        .withDwcFootprintSrs(termMapper.retrieveTerm(new FootprintSpatialFit(), data, dwc))
        .withDwcFootprintWkt(termMapper.retrieveTerm(new FootprintWkt(), data, dwc))
        .withDwcGeodeticDatum(termMapper.retrieveTerm(new GeoreferencedDate(), data, dwc))
        .withDwcGeoreferenceRemarks(termMapper.retrieveTerm(new GeoreferenceRemarks(), data, dwc))
        .withDwcGeoreferenceSources(termMapper.retrieveTerm(new GeoreferenceSources(), data, dwc))
        .withDwcPointRadiusSpatialFit(parseToInteger(new PointRadiusSpatialFit(), data, dwc));
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
        .withDwcMaximumDepthInMeters(parseToDouble(new MaximumDepthInMeters(), data, dwc))
        .withDwcMaximumDistanceAboveSurfaceInMeters(parseToDouble(new MaximumDistanceAboveSurfaceInMeters(), data, dwc))
        .withDwcMaximumElevationInMeters(parseToDouble(new MaximumElevationInMeters(), data, dwc))
        .withDwcMinimumDepthInMeters(parseToDouble(new MinimumDepthInMeters(), data, dwc))
        .withDwcMinimumDistanceAboveSurfaceInMeters(parseToDouble(new MinimumDistanceAboveSurfaceInMeters(), data, dwc))
        .withDwcMinimumElevationInMeters(parseToDouble(new MinimumElevationInMeters(), data, dwc))
        .withDwcVerticalDatum(termMapper.retrieveTerm(new VerticalDatum(), data, dwc))
        .withDwcLocationAccordingTo(termMapper.retrieveTerm(new LocationAccordingTo(), data, dwc))
        .withDwcLocationRemarks(termMapper.retrieveTerm(new LocationRemarks(), data, dwc))
        .withGeoreference(georeference)
        .withGeologicalContext(geologicalContext);
    var assertions = new OccurrenceAssertions().gatherOccurrenceAssertions(data, dwc);
    var occurrence = new eu.dissco.core.translator.schema.Occurrences()
        .withDwcFieldNumber(termMapper.retrieveTerm(new FieldNumber(), data, dwc))
        .withDwcEventDate(termMapper.retrieveTerm(new DateCollected(), data, dwc))
        .withDwcSex(termMapper.retrieveTerm(new Sex(), data, dwc))
        .withDwcOrganismQuantity(termMapper.retrieveTerm(new OrganismQuantity(), data, dwc))
        .withDwcOrganismQuantityType(termMapper.retrieveTerm(new OrganismQuantityType(), data, dwc))
        .withDwcSamplingProtocol(termMapper.retrieveTerm(new SamplingProtocol(), data, dwc))
        .withDwcLifeStage(termMapper.retrieveTerm(new LifeStage(), data, dwc))
        .withDwcEventRemarks(termMapper.retrieveTerm(new EventRemark(), data, dwc))
        .withDwcFieldNumber(termMapper.retrieveTerm(new FieldNumber(), data, dwc))
        .withDwcFieldNotes(termMapper.retrieveTerm(new FieldNotes(), data, dwc))
        .withDwcHabitat(termMapper.retrieveTerm(new Habitat(), data, dwc))
        .withDwcReproductiveCondition(
            termMapper.retrieveTerm(new ReproductiveCondition(), data, dwc))
        .withDwcBehavior(termMapper.retrieveTerm(new Behavior(), data, dwc))
        .withDwcEstablishmentMeans(termMapper.retrieveTerm(new EstablishmentMeans(), data, dwc))
        .withDwcPathway(termMapper.retrieveTerm(new Pathway(), data, dwc))
        .withDwcDegreeOfEstablishment(
            termMapper.retrieveTerm(new DegreeOfEstablishment(), data, dwc))
        .withDwcDegreeOfEstablishment(
            termMapper.retrieveTerm(new GeoreferenceVerificationStatus(), data, dwc))
        .withDwcOccurrenceStatus(termMapper.retrieveTerm(new OccurrenceStatus(), data, dwc) != null
            ? DwcOccurrenceStatus.fromValue(
            termMapper.retrieveTerm(new OccurrenceStatus(), data, dwc).toLowerCase()) : null)
        .withDwcOccurrenceRemarks(termMapper.retrieveTerm(new OccurrenceRemarks(), data, dwc))
        .withDwcInformationWithheld(termMapper.retrieveTerm(new InformationWithheld(), data, dwc))
        .withDwcDataGeneralizations(termMapper.retrieveTerm(new DataGeneralizations(), data, dwc))
        .withDwcSampleSizeUnit(termMapper.retrieveTerm(new SampleSizeUnit(), data, dwc))
        .withDwcSampleSizeValue(termMapper.retrieveTerm(new SampleSizeValue(), data, dwc))
        .withLocation(location)
        .withAssertions(assertions);

    return List.of(occurrence);
  }

  private Integer parseToInteger(Term term, JsonNode data, boolean dwc) {
    var value = termMapper.retrieveTerm(term, data, dwc);
    try {
      if (value != null) {
        return Integer.valueOf(value);
      }
    } catch (NumberFormatException ex) {
      log.warn("Unable to parse value: {} to an integer for term: {}", value, term.getTerm());
    }
    return null;
  }

  private Double parseToDouble(Term term, JsonNode data, boolean dwc) {
    var value = termMapper.retrieveTerm(term, data, dwc);
    try {
      if (value != null) {
        return Double.valueOf(value);
      }
    } catch (NumberFormatException ex) {
      log.warn("Unable to parse value: {} to a double for term: {}", value, term.getTerm());
    }
    return null;
  }

  private String minifyOrganisationId(String organisationId) throws OrganisationNotRorId {
    if (organisationId.startsWith("https://ror.org")) {
      return organisationId.replace("https://ror.org/", "");
    } else {
      throw new OrganisationNotRorId(organisationId + " is not a valid ror");
    }
  }
}
