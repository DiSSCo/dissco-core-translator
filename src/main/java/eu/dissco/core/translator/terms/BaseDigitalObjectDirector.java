package eu.dissco.core.translator.terms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.component.InstitutionNameComponent;
import eu.dissco.core.translator.exception.OrganisationException;
import eu.dissco.core.translator.exception.UnknownPhysicalSpecimenIdType;
import eu.dissco.core.translator.properties.FdoProperties;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.schema.Citations;
import eu.dissco.core.translator.schema.DigitalEntity;
import eu.dissco.core.translator.schema.DigitalEntity.DctermsType;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsLivingOrPreserved;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsPhysicalSpecimenIdType;
import eu.dissco.core.translator.schema.EntityRelationships;
import eu.dissco.core.translator.schema.GeoReference;
import eu.dissco.core.translator.schema.Identifications;
import eu.dissco.core.translator.schema.Identifiers;
import eu.dissco.core.translator.schema.Occurrences;
import eu.dissco.core.translator.schema.Occurrences.DwcOccurrenceStatus;
import eu.dissco.core.translator.schema.TaxonIdentification;
import eu.dissco.core.translator.terms.media.AccessUri;
import eu.dissco.core.translator.terms.media.Created;
import eu.dissco.core.translator.terms.media.Creator;
import eu.dissco.core.translator.terms.media.Description;
import eu.dissco.core.translator.terms.media.Format;
import eu.dissco.core.translator.terms.media.MediaAssertions;
import eu.dissco.core.translator.terms.media.MediaType;
import eu.dissco.core.translator.terms.media.Rights;
import eu.dissco.core.translator.terms.media.Source;
import eu.dissco.core.translator.terms.media.WebStatement;
import eu.dissco.core.translator.terms.specimen.AccessRights;
import eu.dissco.core.translator.terms.specimen.BasisOfRecord;
import eu.dissco.core.translator.terms.specimen.CollectionId;
import eu.dissco.core.translator.terms.specimen.DataGeneralizations;
import eu.dissco.core.translator.terms.specimen.DatasetName;
import eu.dissco.core.translator.terms.specimen.Disposition;
import eu.dissco.core.translator.terms.specimen.HasMedia;
import eu.dissco.core.translator.terms.specimen.InformationWithheld;
import eu.dissco.core.translator.terms.specimen.LivingOrPreserved;
import eu.dissco.core.translator.terms.specimen.MarkedAsType;
import eu.dissco.core.translator.terms.specimen.Modified;
import eu.dissco.core.translator.terms.specimen.OrganisationId;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenId;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenIdType;
import eu.dissco.core.translator.terms.specimen.Preparations;
import eu.dissco.core.translator.terms.specimen.RecordedBy;
import eu.dissco.core.translator.terms.specimen.RecordedById;
import eu.dissco.core.translator.terms.specimen.RightsHolder;
import eu.dissco.core.translator.terms.specimen.SpecimenName;
import eu.dissco.core.translator.terms.specimen.TopicDiscipline;
import eu.dissco.core.translator.terms.specimen.TopicDomain;
import eu.dissco.core.translator.terms.specimen.TopicOrigin;
import eu.dissco.core.translator.terms.specimen.VerbatimLabel;
import eu.dissco.core.translator.terms.specimen.citation.BibliographicCitation;
import eu.dissco.core.translator.terms.specimen.citation.CitationRemarks;
import eu.dissco.core.translator.terms.specimen.citation.Date;
import eu.dissco.core.translator.terms.specimen.citation.ReferenceIri;
import eu.dissco.core.translator.terms.specimen.citation.Title;
import eu.dissco.core.translator.terms.specimen.citation.Type;
import eu.dissco.core.translator.terms.specimen.identification.DateIdentified;
import eu.dissco.core.translator.terms.specimen.identification.IdentificationId;
import eu.dissco.core.translator.terms.specimen.identification.IdentificationRemarks;
import eu.dissco.core.translator.terms.specimen.identification.IdentificationVerificationStatus;
import eu.dissco.core.translator.terms.specimen.identification.IdentifiedBy;
import eu.dissco.core.translator.terms.specimen.identification.TypeStatus;
import eu.dissco.core.translator.terms.specimen.identification.VerbatimIdentification;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.AcceptedNameUsage;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.AcceptedNameUsageId;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Class;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.CultivarEpithet;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Family;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.GenericName;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Genus;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.InfraGenericEpithet;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.InfraspecificEpithet;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Kingdom;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.NameAccordingTo;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.NamePublishedInYear;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.NomenclaturalCode;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.NomenclaturalStatus;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Order;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.OriginalNameUsage;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Phylum;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.ScientificName;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.ScientificNameAuthorship;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.ScientificNameId;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.SpecificEpithet;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Subfamily;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Subgenus;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Subtribe;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Superfamily;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.TaxonId;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.TaxonRank;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.TaxonRemarks;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.TaxonomicStatus;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Tribe;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.VerbatimTaxonRank;
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
import eu.dissco.core.translator.terms.specimen.location.LocationId;
import eu.dissco.core.translator.terms.specimen.location.LocationRemarks;
import eu.dissco.core.translator.terms.specimen.location.MaximumDepthInMeters;
import eu.dissco.core.translator.terms.specimen.location.MaximumDistanceAboveSurfaceInMeters;
import eu.dissco.core.translator.terms.specimen.location.MaximumElevationInMeters;
import eu.dissco.core.translator.terms.specimen.location.MinimumDepthInMeters;
import eu.dissco.core.translator.terms.specimen.location.MinimumDistanceAboveSurfaceInMeters;
import eu.dissco.core.translator.terms.specimen.location.MinimumElevationInMeters;
import eu.dissco.core.translator.terms.specimen.location.Municipality;
import eu.dissco.core.translator.terms.specimen.location.StateProvince;
import eu.dissco.core.translator.terms.specimen.location.VerbatimDepth;
import eu.dissco.core.translator.terms.specimen.location.VerbatimElevation;
import eu.dissco.core.translator.terms.specimen.location.VerbatimLocality;
import eu.dissco.core.translator.terms.specimen.location.VerticalDatum;
import eu.dissco.core.translator.terms.specimen.location.WaterBody;
import eu.dissco.core.translator.terms.specimen.location.georeference.CoordinatePrecision;
import eu.dissco.core.translator.terms.specimen.location.georeference.CoordinateUncertaintyInMeters;
import eu.dissco.core.translator.terms.specimen.location.georeference.DecimalLatitude;
import eu.dissco.core.translator.terms.specimen.location.georeference.DecimalLongitude;
import eu.dissco.core.translator.terms.specimen.location.georeference.FootprintSpatialFit;
import eu.dissco.core.translator.terms.specimen.location.georeference.FootprintSrs;
import eu.dissco.core.translator.terms.specimen.location.georeference.FootprintWkt;
import eu.dissco.core.translator.terms.specimen.location.georeference.GeodeticDatum;
import eu.dissco.core.translator.terms.specimen.location.georeference.GeoreferenceProtocol;
import eu.dissco.core.translator.terms.specimen.location.georeference.GeoreferenceRemarks;
import eu.dissco.core.translator.terms.specimen.location.georeference.GeoreferenceSources;
import eu.dissco.core.translator.terms.specimen.location.georeference.GeoreferencedBy;
import eu.dissco.core.translator.terms.specimen.location.georeference.GeoreferencedDate;
import eu.dissco.core.translator.terms.specimen.location.georeference.PointRadiusSpatialFit;
import eu.dissco.core.translator.terms.specimen.location.georeference.VerbatimCoordinateSystem;
import eu.dissco.core.translator.terms.specimen.location.georeference.VerbatimCoordinates;
import eu.dissco.core.translator.terms.specimen.location.georeference.VerbatimLatitude;
import eu.dissco.core.translator.terms.specimen.location.georeference.VerbatimLongitude;
import eu.dissco.core.translator.terms.specimen.location.georeference.VerbatimSRS;
import eu.dissco.core.translator.terms.specimen.occurence.Behavior;
import eu.dissco.core.translator.terms.specimen.occurence.Caste;
import eu.dissco.core.translator.terms.specimen.occurence.Day;
import eu.dissco.core.translator.terms.specimen.occurence.DegreeOfEstablishment;
import eu.dissco.core.translator.terms.specimen.occurence.EstablishmentMeans;
import eu.dissco.core.translator.terms.specimen.occurence.EventDate;
import eu.dissco.core.translator.terms.specimen.occurence.EventRemark;
import eu.dissco.core.translator.terms.specimen.occurence.FieldNotes;
import eu.dissco.core.translator.terms.specimen.occurence.FieldNumber;
import eu.dissco.core.translator.terms.specimen.occurence.GeoreferenceVerificationStatus;
import eu.dissco.core.translator.terms.specimen.occurence.Habitat;
import eu.dissco.core.translator.terms.specimen.occurence.LifeStage;
import eu.dissco.core.translator.terms.specimen.occurence.Month;
import eu.dissco.core.translator.terms.specimen.occurence.OccurrenceAssertions;
import eu.dissco.core.translator.terms.specimen.occurence.OccurrenceRemarks;
import eu.dissco.core.translator.terms.specimen.occurence.OccurrenceStatus;
import eu.dissco.core.translator.terms.specimen.occurence.OrganismQuantity;
import eu.dissco.core.translator.terms.specimen.occurence.OrganismQuantityType;
import eu.dissco.core.translator.terms.specimen.occurence.Pathway;
import eu.dissco.core.translator.terms.specimen.occurence.RecordNumber;
import eu.dissco.core.translator.terms.specimen.occurence.ReproductiveCondition;
import eu.dissco.core.translator.terms.specimen.occurence.SampleSizeUnit;
import eu.dissco.core.translator.terms.specimen.occurence.SampleSizeValue;
import eu.dissco.core.translator.terms.specimen.occurence.SamplingProtocol;
import eu.dissco.core.translator.terms.specimen.occurence.Sex;
import eu.dissco.core.translator.terms.specimen.occurence.VerbatimEventDate;
import eu.dissco.core.translator.terms.specimen.occurence.Vitality;
import eu.dissco.core.translator.terms.specimen.occurence.Year;
import eu.dissco.core.translator.terms.specimen.stratigraphy.biostratigraphic.HighestBiostratigraphicZone;
import eu.dissco.core.translator.terms.specimen.stratigraphy.biostratigraphic.LowestBiostratigraphicZone;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.EarliestAgeOrLowestStage;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.EarliestEonOrLowestEonothem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.EarliestEpochOrLowestSeries;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.EarliestEraOrLowestErathem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.EarliestPeriodOrLowestSystem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.LatestAgeOrHighestStage;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.LatestEonOrHighestEonothem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.LatestEpochOrHighestSeries;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.LatestEraOrHighestErathem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.LatestPeriodOrHighestSystem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Bed;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Formation;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Group;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.LithostratigraphicTerms;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class BaseDigitalObjectDirector {

  protected final ObjectMapper mapper;
  protected final TermMapper termMapper;
  private final InstitutionNameComponent institutionNameComponent;
  private final WebClientProperties webClientProperties;
  private final FdoProperties fdoProperties;
  private final List<String> identifierTerms;

  public DigitalSpecimen assembleDigitalSpecimenTerm(JsonNode data, boolean dwc)
      throws OrganisationException, UnknownPhysicalSpecimenIdType {
    var ds = assembleDigitalSpecimenTerms(data, dwc);
    ds.withDwcOccurrence(assembleOccurrenceTerms(data, dwc));
    ds.withDwcIdentification(assembleIdentifications(data, dwc));
    ds.withIdentifier(assembleIdentifiers(data));
    ds.withCitation(assembleSpecimenCitations(data, dwc));
    ds.withEntityRelationship(assembleDigitalSpecimenEntityRelationships(ds));
    setCalculatedFields(ds, data);
    return ds;
  }

  private void setCalculatedFields(DigitalSpecimen ds, JsonNode data) {
    ds.setOdsTopicDiscipline(new TopicDiscipline().calculate(ds));
    ds.setOdsTopicOrigin(new TopicOrigin().calculate(ds));
    ds.setOdsTopicDomain(new TopicDomain().calculate(ds));
    ds.setOdsSpecimenName(new SpecimenName().calculate(ds));
    ds.setOdsMarkedAsType(new MarkedAsType().calculate(ds));
    addStandardSpecificLogic(ds, data);
  }

  protected abstract void addStandardSpecificLogic(DigitalSpecimen ds, JsonNode data);

  protected abstract List<Citations> assembleSpecimenCitations(JsonNode data, boolean dwc);

  protected abstract List<Citations> assembleIdentificationCitations(JsonNode data, boolean dwc);

  protected abstract List<Identifications> assembleIdentifications(JsonNode data, boolean dwc);

  protected Citations createCitation(JsonNode data,
      boolean dwc) {
    return new eu.dissco.core.translator.schema.Citations()
        .withDctermsBibliographicCitation(
            termMapper.retrieveTerm(new BibliographicCitation(), data, dwc))
        .withCitationRemarks(termMapper.retrieveTerm(new CitationRemarks(), data, dwc))
        .withReferenceIri(termMapper.retrieveTerm(new ReferenceIri(), data, dwc))
        .withDctermsCreator(
            termMapper.retrieveTerm(new eu.dissco.core.translator.terms.specimen.citation.Creator(),
                data, dwc))
        .withDctermsType(termMapper.retrieveTerm(new Type(), data, dwc))
        .withDctermsDate(termMapper.retrieveTerm(new Date(), data, dwc))
        .withDctermsTitle(termMapper.retrieveTerm(new Title(), data, dwc));
  }

  private DigitalSpecimen assembleDigitalSpecimenTerms(JsonNode data, boolean dwc)
      throws OrganisationException, UnknownPhysicalSpecimenIdType {
    var physicalSpecimenIdTypeHarmonised = convertToPhysicalSpecimenIdTypeEnum(
        termMapper.retrieveTerm(new PhysicalSpecimenIdType(), data, dwc));
    var organisationId = termMapper.retrieveTerm(new OrganisationId(), data, dwc);
    var physicalSpecimenId = termMapper.retrieveTerm(new PhysicalSpecimenId(), data, dwc);
    var normalisedPhysicalSpecimenId = getNormalisedPhysicalSpecimenId(
        physicalSpecimenIdTypeHarmonised, organisationId, physicalSpecimenId);
    return new DigitalSpecimen()
        .withDctermsLicense(termMapper.retrieveTerm(new License(), data, dwc))
        .withOdsPhysicalSpecimenId(physicalSpecimenId)
        .withOdsNormalisedPhysicalSpecimenId(normalisedPhysicalSpecimenId)
        .withOdsPhysicalSpecimenIdType(physicalSpecimenIdTypeHarmonised)
        .withDwcInstitutionId(organisationId)
        .withOdsPhysicalSpecimenId(physicalSpecimenId)
        .withOdsHasMedia(parseToBoolean(new HasMedia(), data, dwc))
        .withOdsSourceSystem(
            "https://hdl.handle.net/" + webClientProperties.getSourceSystemId())
        .withOdsLivingOrPreserved(
            retrieveEnum(new LivingOrPreserved(), data, dwc, OdsLivingOrPreserved.class))
        .withDwcPreparations(termMapper.retrieveTerm(new Preparations(), data, dwc))
        .withDwcCollectionId(termMapper.retrieveTerm(new CollectionId(), data, dwc))
        .withDctermsModified(termMapper.retrieveTerm(new Modified(), data, dwc))
        .withDwcInstitutionName(
            getInstitutionName(organisationId))
        .withDwcRecordedBy(termMapper.retrieveTerm(new RecordedBy(), data, dwc))
        .withDwcRecordedById(termMapper.retrieveTerm(new RecordedById(), data, dwc))
        .withDwcBasisOfRecord(termMapper.retrieveTerm(new BasisOfRecord(), data, dwc))
        .withDctermsAccessRights(termMapper.retrieveTerm(new AccessRights(), data, dwc))
        .withDctermsRightsHolder(termMapper.retrieveTerm(new RightsHolder(), data, dwc))
        .withDwcDatasetName(termMapper.retrieveTerm(new DatasetName(), data, dwc))
        .withDwcDisposition(termMapper.retrieveTerm(new Disposition(), data, dwc))
        .withDwcInformationWithheld(termMapper.retrieveTerm(new InformationWithheld(), data, dwc))
        .withDwcVerbatimLabel(termMapper.retrieveTerm(new VerbatimLabel(), data, dwc))
        .withDwcDataGeneralizations(termMapper.retrieveTerm(new DataGeneralizations(), data, dwc));
  }

  private String getInstitutionName(String organisationId) throws OrganisationException {
    if (organisationId.startsWith("https://ror.org/")) {
      var rorId = organisationId.replace("https://ror.org/", "");
      return institutionNameComponent.getRorName(rorId);
    } else if (organisationId.startsWith("https://www.wikidata.org/")) {
      var wikidataId = organisationId.replace("https://www.wikidata.org/wiki/", "");
      return institutionNameComponent.getWikiDataName(wikidataId);
    } else {
      throw new OrganisationException(
          organisationId + " is not a valid ror or wikidata identifier");
    }
  }

  private List<EntityRelationships> assembleDigitalSpecimenEntityRelationships(
      DigitalSpecimen ds) {
    var relationships = new ArrayList<EntityRelationships>();
    relationships.add(new EntityRelationships().withEntityRelationshipType("hasOrganisationId")
        .withObjectEntityIri(ds.getDwcInstitutionId()));
    relationships.add(new EntityRelationships().withEntityRelationshipType("hasSourceSystemId")
        .withObjectEntityIri(ds.getOdsSourceSystem()));
    relationships.add(new EntityRelationships().withEntityRelationshipType("hasFdoType")
        .withObjectEntityIri(fdoProperties.getDigitalSpecimenType()));
    if (ds.getOdsPhysicalSpecimenIdType().equals(OdsPhysicalSpecimenIdType.RESOLVABLE)) {
      relationships.add(
          new EntityRelationships().withEntityRelationshipType("hasPhysicalIdentifier")
              .withObjectEntityIri(ds.getOdsPhysicalSpecimenId()));
    }
    if (ds.getDctermsLicense() != null && ds.getDctermsLicense().startsWith("http")) {
      relationships.add(new EntityRelationships().withEntityRelationshipType("hasLicense")
          .withObjectEntityIri(ds.getDctermsLicense()));
    }
    if (ds.getCitation() != null) {
      for (Citations citation : ds.getCitation()) {
        if (citation.getReferenceIri() != null) {
          relationships.add(new EntityRelationships().withEntityRelationshipType("hasReference")
              .withObjectEntityIri(citation.getReferenceIri()));
        }
      }
    }
    return relationships;
  }

  private List<Identifiers> assembleIdentifiers(JsonNode data) {
    var identifiers = new ArrayList<Identifiers>();
    for (String identifierTerm : identifierTerms) {
      if (data.get(identifierTerm) != null) {
        var identifier = new Identifiers()
            .withIdentifierType(identifierTerm)
            .withIdentifierValue(data.get(identifierTerm).asText());
        identifiers.add(identifier);
      }
    }
    return identifiers;
  }

  protected Identifications createIdentification(JsonNode data, boolean dwc) {
    var mappedTaxonIdentification = new TaxonIdentification()
        .withDwcTaxonID(termMapper.retrieveTerm(new TaxonId(), data, dwc))
        .withDwcKingdom(termMapper.retrieveTerm(new Kingdom(), data, dwc))
        .withDwcTaxonRank(termMapper.retrieveTerm(new TaxonRank(), data, dwc))
        .withDwcVerbatimTaxonRank(termMapper.retrieveTerm(new VerbatimTaxonRank(), data, dwc))
        .withDwcGenus(termMapper.retrieveTerm(new Genus(), data, dwc))
        .withDwcSubgenus(termMapper.retrieveTerm(new Subgenus(), data, dwc))
        .withDwcOrder(termMapper.retrieveTerm(new Order(), data, dwc))
        .withDwcScientificName(termMapper.retrieveTerm(new ScientificName(), data, dwc))
        .withDwcScientificNameAuthorship(
            termMapper.retrieveTerm(new ScientificNameAuthorship(), data, dwc))
        .withDwcScientificNameId(termMapper.retrieveTerm(new ScientificNameId(), data, dwc))
        .withDwcNamePublishedInYear(termMapper.retrieveTerm(new NamePublishedInYear(), data, dwc))
        .withDwcClass(termMapper.retrieveTerm(new Class(), data, dwc))
        .withDwcFamily(termMapper.retrieveTerm(new Family(), data, dwc))
        .withDwcSubfamily(termMapper.retrieveTerm(new Subfamily(), data, dwc))
        .withDwcPhylum(termMapper.retrieveTerm(new Phylum(), data, dwc))
        .withDwcNameAccordingTo(termMapper.retrieveTerm(new NameAccordingTo(), data, dwc))
        .withDwcSpecificEpithet(termMapper.retrieveTerm(new SpecificEpithet(), data, dwc))
        .withDwcTaxonomicStatus(termMapper.retrieveTerm(new TaxonomicStatus(), data, dwc))
        .withDwcNomenclaturalCode(termMapper.retrieveTerm(new NomenclaturalCode(), data, dwc))
        .withDwcTaxonRemarks(termMapper.retrieveTerm(new TaxonRemarks(), data, dwc))
        .withDwcVernacularName(termMapper.retrieveTerm(new VernacularName(), data, dwc))
        .withDwcAcceptedNameUsage(termMapper.retrieveTerm(new AcceptedNameUsage(), data, dwc))
        .withDwcAcceptedNameUsageID(termMapper.retrieveTerm(new AcceptedNameUsageId(), data, dwc))
        .withDwcCultivarEpithet(termMapper.retrieveTerm(new CultivarEpithet(), data, dwc))
        .withDwcGenericName(termMapper.retrieveTerm(new GenericName(), data, dwc))
        .withDwcInfragenericEpithet(termMapper.retrieveTerm(new InfraGenericEpithet(), data, dwc))
        .withDwcInfraspecificEpithet(termMapper.retrieveTerm(new InfraspecificEpithet(), data, dwc))
        .withDwcNomenclaturalStatus(termMapper.retrieveTerm(new NomenclaturalStatus(), data, dwc))
        .withDwcOriginalNameUsage(termMapper.retrieveTerm(new OriginalNameUsage(), data, dwc))
        .withDwcSubtribe(termMapper.retrieveTerm(new Subtribe(), data, dwc))
        .withDwcSuperfamily(termMapper.retrieveTerm(new Superfamily(), data, dwc))
        .withDwcTribe(termMapper.retrieveTerm(new Tribe(), data, dwc));
    return new Identifications()
        .withDwcIdentificationID(termMapper.retrieveTerm(new IdentificationId(), data, dwc))
        .withDwcIdentificationVerificationStatus(
            parseToBoolean(new IdentificationVerificationStatus(), data, dwc))
        .withDwcTypeStatus(termMapper.retrieveTerm(new TypeStatus(), data, dwc))
        .withDwcDateIdentified(termMapper.retrieveTerm(new DateIdentified(), data, dwc))
        .withDwcIdentifiedBy(termMapper.retrieveTerm(new IdentifiedBy(), data, dwc))
        .withDwcIdentificationRemarks(
            termMapper.retrieveTerm(new IdentificationRemarks(), data, dwc))
        .withDwcVerbatimIdentification(
            termMapper.retrieveTerm(new VerbatimIdentification(), data, dwc))
        .withTaxonIdentifications(List.of(mappedTaxonIdentification))
        .withCitation(assembleIdentificationCitations(data, dwc));
  }


  private List<Occurrences> assembleOccurrenceTerms(JsonNode data,
      boolean dwc) {
    var georeference = new GeoReference()
        .withDwcDecimalLatitude(
            parseToDouble(new DecimalLatitude(), data, dwc))
        .withDwcVerbatimLatitude(termMapper.retrieveTerm(new VerbatimLatitude(), data, dwc))
        .withDwcDecimalLongitude(parseToDouble(new DecimalLongitude(), data, dwc))
        .withDwcVerbatimLongitude(termMapper.retrieveTerm(new VerbatimLongitude(), data, dwc))
        .withDwcGeodeticDatum(termMapper.retrieveTerm(new GeodeticDatum(), data, dwc))
        .withDwcVerbatimCoordinates(termMapper.retrieveTerm(new VerbatimCoordinates(), data, dwc))
        .withDwcVerbatimCoordinateSystem(
            termMapper.retrieveTerm(new VerbatimCoordinateSystem(), data, dwc))
        .withDwcVerbatimSRS(termMapper.retrieveTerm(new VerbatimSRS(), data, dwc))
        .withDwcGeoreferenceProtocol(termMapper.retrieveTerm(new GeoreferenceProtocol(), data, dwc))
        .withDwcCoordinatePrecision(parseToDouble(new CoordinatePrecision(), data, dwc))
        .withDwcCoordinateUncertaintyInMeters(
            parseToDouble(new CoordinateUncertaintyInMeters(), data, dwc))
        .withDwcFootprintSrs(termMapper.retrieveTerm(new FootprintSrs(), data, dwc))
        .withDwcFootprintSpatialFit(parseToInteger(new FootprintSpatialFit(), data, dwc))
        .withDwcGeoreferencedBy(termMapper.retrieveTerm(new GeoreferencedBy(), data, dwc))
        .withDwcFootprintWkt(termMapper.retrieveTerm(new FootprintWkt(), data, dwc))
        .withDwcGeoreferencedDate(termMapper.retrieveTerm(new GeoreferencedDate(), data, dwc))
        .withDwcGeoreferenceRemarks(termMapper.retrieveTerm(new GeoreferenceRemarks(), data, dwc))
        .withDwcGeoreferenceSources(termMapper.retrieveTerm(new GeoreferenceSources(), data, dwc))
        .withDwcPointRadiusSpatialFit(parseToDouble(new PointRadiusSpatialFit(), data, dwc));
    var geologicalContext = new eu.dissco.core.translator.schema.DwcGeologicalContext()
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
        .withDwcLatestEonOrHighestEonothem(
            termMapper.retrieveTerm(new LatestEonOrHighestEonothem(), data, dwc))
        .withDwcEarliestEonOrLowestEonothem(
            termMapper.retrieveTerm(new EarliestEonOrLowestEonothem(), data, dwc))
        .withDwcLithostratigraphicTerms(
            termMapper.retrieveTerm(new LithostratigraphicTerms(), data, dwc))
        .withDwcBed(termMapper.retrieveTerm(new Bed(), data, dwc))
        .withDwcFormation(termMapper.retrieveTerm(new Formation(), data, dwc))
        .withDwcGroup(termMapper.retrieveTerm(new Group(), data, dwc))
        .withDwcMember(termMapper.retrieveTerm(new Member(), data, dwc));
    var location = new eu.dissco.core.translator.schema.Location()
        .withDwcLocationID(termMapper.retrieveTerm(new LocationId(), data, dwc))
        .withDwcContinent(termMapper.retrieveTerm(new Continent(), data, dwc))
        .withDwcCountry(termMapper.retrieveTerm(new Country(), data, dwc))
        .withDwcCountryCode(termMapper.retrieveTerm(new CountryCode(), data, dwc))
        .withDwcCounty(termMapper.retrieveTerm(new County(), data, dwc))
        .withDwcIsland(termMapper.retrieveTerm(new Island(), data, dwc))
        .withDwcIslandGroup(termMapper.retrieveTerm(new IslandGroup(), data, dwc))
        .withDwcMunicipality(termMapper.retrieveTerm(new Municipality(), data, dwc))
        .withDwcLocality(termMapper.retrieveTerm(new Locality(), data, dwc))
        .withDwcVerbatimLocality(termMapper.retrieveTerm(new VerbatimLocality(), data, dwc))
        .withDwcStateProvince(termMapper.retrieveTerm(new StateProvince(), data, dwc))
        .withDwcWaterBody(termMapper.retrieveTerm(new WaterBody(), data, dwc))
        .withDwcHigherGeography(termMapper.retrieveTerm(new HigherGeography(), data, dwc))
        .withDwcMaximumDepthInMeters(parseToDouble(new MaximumDepthInMeters(), data, dwc))
        .withDwcMaximumDistanceAboveSurfaceInMeters(
            parseToDouble(new MaximumDistanceAboveSurfaceInMeters(), data, dwc))
        .withDwcMaximumElevationInMeters(parseToDouble(new MaximumElevationInMeters(), data, dwc))
        .withDwcMinimumDepthInMeters(parseToDouble(new MinimumDepthInMeters(), data, dwc))
        .withDwcMinimumDistanceAboveSurfaceInMeters(
            parseToDouble(new MinimumDistanceAboveSurfaceInMeters(), data, dwc))
        .withDwcMinimumElevationInMeters(parseToDouble(new MinimumElevationInMeters(), data, dwc))
        .withDwcVerbatimDepth(termMapper.retrieveTerm(new VerbatimDepth(), data, dwc))
        .withDwcVerbatimElevation(termMapper.retrieveTerm(new VerbatimElevation(), data, dwc))
        .withDwcVerticalDatum(termMapper.retrieveTerm(new VerticalDatum(), data, dwc))
        .withDwcLocationAccordingTo(termMapper.retrieveTerm(new LocationAccordingTo(), data, dwc))
        .withDwcLocationRemarks(termMapper.retrieveTerm(new LocationRemarks(), data, dwc))
        .withGeoReference(georeference)
        .withDwcGeologicalContext(geologicalContext);
    var assertions = new OccurrenceAssertions().gatherOccurrenceAssertions(mapper, data, dwc);
    var occurrence = new eu.dissco.core.translator.schema.Occurrences()
        .withDwcFieldNumber(termMapper.retrieveTerm(new FieldNumber(), data, dwc))
        .withDwcEventDate(termMapper.retrieveTerm(new EventDate(), data, dwc))
        .withDwcVerbatimEventDate(termMapper.retrieveTerm(new VerbatimEventDate(), data, dwc))
        .withDwcYear(parseToInteger(new Year(), data, dwc))
        .withDwcMonth(parseToInteger(new Month(), data, dwc))
        .withDwcDay(parseToInteger(new Day(), data, dwc))
        .withDwcSex(termMapper.retrieveTerm(new Sex(), data, dwc))
        .withDwcOrganismQuantity(termMapper.retrieveTerm(new OrganismQuantity(), data, dwc))
        .withDwcOrganismQuantityType(termMapper.retrieveTerm(new OrganismQuantityType(), data, dwc))
        .withDwcSamplingProtocol(termMapper.retrieveTerm(new SamplingProtocol(), data, dwc))
        .withDwcLifeStage(termMapper.retrieveTerm(new LifeStage(), data, dwc))
        .withDwcEventRemarks(termMapper.retrieveTerm(new EventRemark(), data, dwc))
        .withDwcFieldNumber(termMapper.retrieveTerm(new FieldNumber(), data, dwc))
        .withDwcRecordNumber(termMapper.retrieveTerm(new RecordNumber(), data, dwc))
        .withDwcFieldNotes(termMapper.retrieveTerm(new FieldNotes(), data, dwc))
        .withDwcHabitat(termMapper.retrieveTerm(new Habitat(), data, dwc))
        .withDwcReproductiveCondition(
            termMapper.retrieveTerm(new ReproductiveCondition(), data, dwc))
        .withDwcBehavior(termMapper.retrieveTerm(new Behavior(), data, dwc))
        .withDwcEstablishmentMeans(termMapper.retrieveTerm(new EstablishmentMeans(), data, dwc))
        .withDwcPathway(termMapper.retrieveTerm(new Pathway(), data, dwc))
        .withDwcDegreeOfEstablishment(
            termMapper.retrieveTerm(new DegreeOfEstablishment(), data, dwc))
        .withDwcGeoreferenceVerificationStatus(
            termMapper.retrieveTerm(new GeoreferenceVerificationStatus(), data, dwc))
        .withDwcOccurrenceStatus(
            retrieveEnum(new OccurrenceStatus(), data, dwc, DwcOccurrenceStatus.class))
        .withDwcOccurrenceRemarks(termMapper.retrieveTerm(new OccurrenceRemarks(), data, dwc))
        .withDwcSampleSizeUnit(termMapper.retrieveTerm(new SampleSizeUnit(), data, dwc))
        .withDwcSampleSizeValue(termMapper.retrieveTerm(new SampleSizeValue(), data, dwc))
        .withDwcCaste(termMapper.retrieveTerm(new Caste(), data, dwc))
        .withDwcVitality(termMapper.retrieveTerm(new Vitality(), data, dwc))
        .withDctermsLocation(location)
        .withAssertion(assertions);
    return List.of(occurrence);
  }

  private <T extends Enum<T>> T retrieveEnum(Term term, JsonNode data, boolean dwc,
      java.lang.Class<T> enumClass) {
    var value = termMapper.retrieveTerm(term, data, dwc);
    try {
      if (value != null) {
        return Enum.valueOf(enumClass, value.toUpperCase());
      }
    } catch (IllegalArgumentException ex) {
      log.warn("Unable to parse value: {} to an enum for term: {}", value, term.getTerm());
    }
    return null;
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

  private Boolean parseToBoolean(Term term, JsonNode data, boolean dwc) {
    var value = termMapper.retrieveTerm(term, data, dwc);
    try {
      if (value != null) {
        return Boolean.valueOf(value);
      }
    } catch (NumberFormatException ex) {
      log.warn("Unable to parse value: {} to a boolean for term: {}", value, term.getTerm());
    }
    return null;
  }

  public DigitalEntity assembleDigitalMediaObjects(boolean dwc,
      JsonNode mediaRecord, String organisationId) throws OrganisationException {
    var digitalMedioObject = new DigitalEntity()
        .withDwcInstitutionId(organisationId)
        .withDwcInstitutionName(getInstitutionName(organisationId))
        .withAcAccessUri(termMapper.retrieveTerm(new AccessUri(), mediaRecord, dwc))
        .withDctermsLicense(termMapper.retrieveTerm(new License(), mediaRecord, dwc))
        .withDctermsFormat(termMapper.retrieveTerm(new Format(), mediaRecord, dwc))
        .withDctermsType(retrieveEnum(new MediaType(), mediaRecord, dwc, DctermsType.class))
        .withXmpRightsWebStatement(termMapper.retrieveTerm(new WebStatement(), mediaRecord, dwc))
        .withDctermsRights(termMapper.retrieveTerm(new Rights(), mediaRecord, dwc))
        .withDctermsAccessRights(
            termMapper.retrieveTerm(new AccessRights(), mediaRecord, dwc))
        .withDctermsRightsHolder(
            termMapper.retrieveTerm(new RightsHolder(), mediaRecord, dwc))
        .withDctermsSource(termMapper.retrieveTerm(new Source(), mediaRecord, dwc))
        .withDctermsCreator(termMapper.retrieveTerm(new Creator(), mediaRecord, dwc))
        .withDctermsCreated(termMapper.retrieveTerm(new Created(), mediaRecord, dwc))
        .withDctermsModified(
            termMapper.retrieveTerm(new eu.dissco.core.translator.terms.media.Modified(),
                mediaRecord, dwc))
        .withDctermsDescription(termMapper.retrieveTerm(new Description(), mediaRecord, dwc))
        .withIdentifiers(assembleIdentifiers(mediaRecord))
        .withAssertions(new MediaAssertions().gatherAssertions(mediaRecord, dwc));
    digitalMedioObject.withEntityRelationships(
        assembleDigitalMediaObjectEntityRelationships(digitalMedioObject));
    return digitalMedioObject;
  }

  private List<EntityRelationships> assembleDigitalMediaObjectEntityRelationships(
      eu.dissco.core.translator.schema.DigitalEntity digitalMediaObject) {
    var relationships = new ArrayList<EntityRelationships>();
    relationships.add(new EntityRelationships().withEntityRelationshipType("hasUrl")
        .withObjectEntityIri(digitalMediaObject.getAcAccessUri()));
    relationships.add(new EntityRelationships().withEntityRelationshipType("hasOrganisationId")
        .withObjectEntityIri(digitalMediaObject.getDwcInstitutionId()));
    relationships.add(new EntityRelationships().withEntityRelationshipType("hasFdoType")
        .withObjectEntityIri(fdoProperties.getDigitalMediaObjectType()));
    if (digitalMediaObject.getDctermsLicense() != null && digitalMediaObject.getDctermsLicense()
        .startsWith("http")) {
      relationships.add(
          new EntityRelationships().withEntityRelationshipType("hasLicense")
              .withObjectEntityIri(digitalMediaObject.getDctermsLicense()));
    }
    if (digitalMediaObject.getDctermsSource() != null && digitalMediaObject.getDctermsSource()
        .startsWith("http")) {
      relationships.add(new EntityRelationships().withEntityRelationshipType("hasSource")
          .withObjectEntityIri(digitalMediaObject.getDctermsSource()));
    }
    return relationships;
  }

  private String getNormalisedPhysicalSpecimenId(OdsPhysicalSpecimenIdType physicalSpecimenIdType,
      String sourceSystemId, String physicalSpecimenId) {
    if (physicalSpecimenIdType.equals(OdsPhysicalSpecimenIdType.GLOBAL)
        || physicalSpecimenIdType.equals(OdsPhysicalSpecimenIdType.RESOLVABLE)) {
      return physicalSpecimenId;
    } else {
      var minifiedSourceSystemId = sourceSystemId.substring(sourceSystemId.lastIndexOf('/') + 1);
      return physicalSpecimenId + ":" + minifiedSourceSystemId;
    }
  }

  private OdsPhysicalSpecimenIdType convertToPhysicalSpecimenIdTypeEnum(
      String physicalSpecimenIdType) throws UnknownPhysicalSpecimenIdType {
    try {
      return OdsPhysicalSpecimenIdType.valueOf(physicalSpecimenIdType.toUpperCase());
    } catch (IllegalArgumentException e) {
      log.warn("Unknown physicalSpecimenIdType specified");
      throw new UnknownPhysicalSpecimenIdType(
          "Physical specimen ID type is: " + physicalSpecimenIdType
              + " which is not a known id type");
    } catch (NullPointerException e) {
      log.warn("No physicalSpecimenIdType specified");
      throw new UnknownPhysicalSpecimenIdType("Physical specimen ID type is empty");
    }
  }

}


