package eu.dissco.core.translator.terms;

import static eu.dissco.core.translator.domain.AgenRoleType.COLLECTOR;
import static eu.dissco.core.translator.domain.AgenRoleType.CREATOR;
import static eu.dissco.core.translator.domain.AgenRoleType.DATA_TRANSLATOR;
import static eu.dissco.core.translator.domain.AgenRoleType.GEOREFERENCER;
import static eu.dissco.core.translator.domain.AgenRoleType.IDENTIFIER;
import static eu.dissco.core.translator.domain.AgenRoleType.RIGHTS_OWNER;
import static eu.dissco.core.translator.domain.RelationshipType.HAS_FDO_TYPE;
import static eu.dissco.core.translator.domain.RelationshipType.HAS_LICENSE;
import static eu.dissco.core.translator.domain.RelationshipType.HAS_ORGANISATION_ID;
import static eu.dissco.core.translator.domain.RelationshipType.HAS_PHYSICAL_IDENTIFIER;
import static eu.dissco.core.translator.domain.RelationshipType.HAS_REFERENCE;
import static eu.dissco.core.translator.domain.RelationshipType.HAS_SOURCE;
import static eu.dissco.core.translator.domain.RelationshipType.HAS_SOURCE_SYSTEM_ID;
import static eu.dissco.core.translator.domain.RelationshipType.HAS_URL;
import static eu.dissco.core.translator.schema.Agent.Type.SCHEMA_PERSON;
import static eu.dissco.core.translator.schema.Agent.Type.SCHEMA_SOFTWARE_APPLICATION;
import static eu.dissco.core.translator.terms.utils.AgentsUtils.setAgent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.component.OrganisationNameComponent;
import eu.dissco.core.translator.component.SourceSystemComponent;
import eu.dissco.core.translator.domain.RelationshipType;
import eu.dissco.core.translator.exception.OrganisationException;
import eu.dissco.core.translator.exception.UnknownPhysicalSpecimenIdType;
import eu.dissco.core.translator.properties.FdoProperties;
import eu.dissco.core.translator.schema.Citation;
import eu.dissco.core.translator.schema.DigitalMedia;
import eu.dissco.core.translator.schema.DigitalMedia.DctermsType;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsLivingOrPreserved;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsPhysicalSpecimenIDType;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsStatus;
import eu.dissco.core.translator.schema.EntityRelationship;
import eu.dissco.core.translator.schema.Event;
import eu.dissco.core.translator.schema.GeologicalContext;
import eu.dissco.core.translator.schema.Georeference;
import eu.dissco.core.translator.schema.Identification;
import eu.dissco.core.translator.schema.Identifier;
import eu.dissco.core.translator.schema.Location;
import eu.dissco.core.translator.schema.TaxonIdentification;
import eu.dissco.core.translator.terms.media.AccessURI;
import eu.dissco.core.translator.terms.media.Available;
import eu.dissco.core.translator.terms.media.CaptureDevice;
import eu.dissco.core.translator.terms.media.Comments;
import eu.dissco.core.translator.terms.media.CreateDate;
import eu.dissco.core.translator.terms.media.Creator;
import eu.dissco.core.translator.terms.media.Description;
import eu.dissco.core.translator.terms.media.DigitizationDate;
import eu.dissco.core.translator.terms.media.Format;
import eu.dissco.core.translator.terms.media.FrameRate;
import eu.dissco.core.translator.terms.media.Iptc4xmpExtCVterm;
import eu.dissco.core.translator.terms.media.Language;
import eu.dissco.core.translator.terms.media.MediaAssertions;
import eu.dissco.core.translator.terms.media.MediaType;
import eu.dissco.core.translator.terms.media.MetadataLanguage;
import eu.dissco.core.translator.terms.media.MetadataLanguageLiteral;
import eu.dissco.core.translator.terms.media.PixelXDimension;
import eu.dissco.core.translator.terms.media.PixelYDimension;
import eu.dissco.core.translator.terms.media.ResourceCreationTechnique;
import eu.dissco.core.translator.terms.media.RightsOwner;
import eu.dissco.core.translator.terms.media.Source;
import eu.dissco.core.translator.terms.media.SubjectCategoryVocabulary;
import eu.dissco.core.translator.terms.media.SubjectOrientation;
import eu.dissco.core.translator.terms.media.SubjectOrientationLiteral;
import eu.dissco.core.translator.terms.media.SubjectPart;
import eu.dissco.core.translator.terms.media.SubjectPartLiteral;
import eu.dissco.core.translator.terms.media.Subtype;
import eu.dissco.core.translator.terms.media.SubtypeLiteral;
import eu.dissco.core.translator.terms.media.TimeOfDay;
import eu.dissco.core.translator.terms.media.UsageTerms;
import eu.dissco.core.translator.terms.media.Variant;
import eu.dissco.core.translator.terms.media.VariantDescription;
import eu.dissco.core.translator.terms.media.VariantLiteral;
import eu.dissco.core.translator.terms.media.WebStatement;
import eu.dissco.core.translator.terms.specimen.AccessRights;
import eu.dissco.core.translator.terms.specimen.BasisOfRecord;
import eu.dissco.core.translator.terms.specimen.CollectionID;
import eu.dissco.core.translator.terms.specimen.DataGeneralizations;
import eu.dissco.core.translator.terms.specimen.DatasetID;
import eu.dissco.core.translator.terms.specimen.DatasetName;
import eu.dissco.core.translator.terms.specimen.Disposition;
import eu.dissco.core.translator.terms.specimen.DynamicProperties;
import eu.dissco.core.translator.terms.specimen.InformationWithheld;
import eu.dissco.core.translator.terms.specimen.IsKnowToContainMedia;
import eu.dissco.core.translator.terms.specimen.IsMarkedAsType;
import eu.dissco.core.translator.terms.specimen.LivingOrPreserved;
import eu.dissco.core.translator.terms.specimen.OrganisationID;
import eu.dissco.core.translator.terms.specimen.OrganismID;
import eu.dissco.core.translator.terms.specimen.OrganismName;
import eu.dissco.core.translator.terms.specimen.OrganismRemarks;
import eu.dissco.core.translator.terms.specimen.OrganismScope;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenID;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenIDType;
import eu.dissco.core.translator.terms.specimen.Preparations;
import eu.dissco.core.translator.terms.specimen.RecordedBy;
import eu.dissco.core.translator.terms.specimen.RecordedByID;
import eu.dissco.core.translator.terms.specimen.RightsHolder;
import eu.dissco.core.translator.terms.specimen.SpecimenName;
import eu.dissco.core.translator.terms.specimen.TopicDiscipline;
import eu.dissco.core.translator.terms.specimen.TopicDomain;
import eu.dissco.core.translator.terms.specimen.TopicOrigin;
import eu.dissco.core.translator.terms.specimen.VerbatimLabel;
import eu.dissco.core.translator.terms.specimen.citation.BibliographicCitation;
import eu.dissco.core.translator.terms.specimen.citation.CitationDescription;
import eu.dissco.core.translator.terms.specimen.citation.Date;
import eu.dissco.core.translator.terms.specimen.citation.ReferenceIRI;
import eu.dissco.core.translator.terms.specimen.citation.Title;
import eu.dissco.core.translator.terms.specimen.citation.Type;
import eu.dissco.core.translator.terms.specimen.event.Behavior;
import eu.dissco.core.translator.terms.specimen.event.Caste;
import eu.dissco.core.translator.terms.specimen.event.Day;
import eu.dissco.core.translator.terms.specimen.event.DegreeOfEstablishment;
import eu.dissco.core.translator.terms.specimen.event.EstablishmentMeans;
import eu.dissco.core.translator.terms.specimen.event.EventAssertions;
import eu.dissco.core.translator.terms.specimen.event.EventDate;
import eu.dissco.core.translator.terms.specimen.event.EventRemark;
import eu.dissco.core.translator.terms.specimen.event.EventRemarks;
import eu.dissco.core.translator.terms.specimen.event.EventType;
import eu.dissco.core.translator.terms.specimen.event.FieldNotes;
import eu.dissco.core.translator.terms.specimen.event.FieldNumber;
import eu.dissco.core.translator.terms.specimen.event.GeoreferenceVerificationStatus;
import eu.dissco.core.translator.terms.specimen.event.Habitat;
import eu.dissco.core.translator.terms.specimen.event.LifeStage;
import eu.dissco.core.translator.terms.specimen.event.Month;
import eu.dissco.core.translator.terms.specimen.event.OrganismQuantity;
import eu.dissco.core.translator.terms.specimen.event.OrganismQuantityType;
import eu.dissco.core.translator.terms.specimen.event.Pathway;
import eu.dissco.core.translator.terms.specimen.event.ReproductiveCondition;
import eu.dissco.core.translator.terms.specimen.event.SampleSizeUnit;
import eu.dissco.core.translator.terms.specimen.event.SampleSizeValue;
import eu.dissco.core.translator.terms.specimen.event.SamplingProtocol;
import eu.dissco.core.translator.terms.specimen.event.Sex;
import eu.dissco.core.translator.terms.specimen.event.VerbatimEventDate;
import eu.dissco.core.translator.terms.specimen.event.Vitality;
import eu.dissco.core.translator.terms.specimen.event.Year;
import eu.dissco.core.translator.terms.specimen.identification.DateIdentified;
import eu.dissco.core.translator.terms.specimen.identification.IdentificationID;
import eu.dissco.core.translator.terms.specimen.identification.IdentificationRemarks;
import eu.dissco.core.translator.terms.specimen.identification.IdentificationVerificationStatus;
import eu.dissco.core.translator.terms.specimen.identification.IdentifiedBy;
import eu.dissco.core.translator.terms.specimen.identification.IdentifiedByID;
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
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.NamePublishedInYear;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.NomenclaturalCode;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.NomenclaturalStatus;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Order;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.OriginalNameUsage;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Phylum;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.ScientificName;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.ScientificNameAuthorship;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.ScientificNameID;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.SpecificEpithet;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Subfamily;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Subgenus;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Subtribe;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Superfamily;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.TaxonID;
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
import eu.dissco.core.translator.terms.specimen.location.LocationID;
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
import eu.dissco.core.translator.terms.specimen.location.georeference.FootprintSRS;
import eu.dissco.core.translator.terms.specimen.location.georeference.FootprintSpatialFit;
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
import java.time.Instant;
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
  private final OrganisationNameComponent organisationNameComponent;
  private final SourceSystemComponent sourceSystemComponent;
  private final FdoProperties fdoProperties;
  private final List<String> identifierTerms;

  public DigitalSpecimen assembleDigitalSpecimenTerm(JsonNode data, boolean dwc)
      throws OrganisationException, UnknownPhysicalSpecimenIdType {
    var ds = assembleDigitalSpecimenTerms(data, dwc);
    ds.withOdsHasEvents(assembleEventTerms(data, dwc));
    ds.withOdsHasIdentifications(assembleIdentifications(data, dwc));
    ds.withOdsHasIdentifiers(assembleIdentifiers(data));
    ds.withOdsHasCitations(assembleSpecimenCitations(data, dwc));
    ds.withOdsHasEntityRelationships(assembleDigitalSpecimenEntityRelationships(ds));
    setCalculatedFields(ds, data);
    return ds;
  }

  private void setCalculatedFields(DigitalSpecimen ds, JsonNode data) {
    ds.setOdsTopicDiscipline(new TopicDiscipline().calculate(ds));
    ds.setOdsTopicOrigin(new TopicOrigin().calculate(ds));
    ds.setOdsTopicDomain(new TopicDomain().calculate(ds));
    ds.setOdsSpecimenName(new SpecimenName().calculate(ds));
    ds.setOdsIsMarkedAsType(new IsMarkedAsType().calculate(ds));
    addStandardSpecificLogic(ds, data);
  }

  protected abstract void addStandardSpecificLogic(DigitalSpecimen ds, JsonNode data);

  protected abstract List<Citation> assembleSpecimenCitations(JsonNode data, boolean dwc);

  protected abstract List<Citation> assembleIdentificationCitations(JsonNode data, boolean dwc);

  protected abstract List<Identification> assembleIdentifications(JsonNode data, boolean dwc);

  protected Citation createCitation(JsonNode data, boolean dwc) {
    var citation = new Citation()
        .withId(termMapper.retrieveTerm(new ReferenceIRI(), data, dwc))
        .withType("ods:Citation")
        .withDctermsBibliographicCitation(
            termMapper.retrieveTerm(new BibliographicCitation(), data, dwc))
        .withDctermsDescription(termMapper.retrieveTerm(new CitationDescription(), data, dwc))
        .withDctermsIdentifier(termMapper.retrieveTerm(new ReferenceIRI(), data, dwc))
        .withDctermsType(termMapper.retrieveTerm(new Type(), data, dwc))
        .withDctermsDate(termMapper.retrieveTerm(new Date(), data, dwc))
        .withDctermsTitle(termMapper.retrieveTerm(new Title(), data, dwc));
    setAgent(citation.getOdsHasAgents(), termMapper.retrieveTerm(
        new eu.dissco.core.translator.terms.specimen.citation.Creator(),
        data, dwc), null, CREATOR, SCHEMA_PERSON);
    return citation;
  }

  private DigitalSpecimen assembleDigitalSpecimenTerms(JsonNode data, boolean dwc)
      throws OrganisationException, UnknownPhysicalSpecimenIdType {
    var physicalSpecimenIdTypeHarmonised = convertToPhysicalSpecimenIdTypeEnum(
        termMapper.retrieveTerm(new PhysicalSpecimenIDType(), data, dwc));
    var organisationId = termMapper.retrieveTerm(new OrganisationID(), data, dwc);
    var physicalSpecimenId = termMapper.retrieveTerm(new PhysicalSpecimenID(), data, dwc);
    var normalisedPhysicalSpecimenId = getNormalisedPhysicalSpecimenId(
        physicalSpecimenIdTypeHarmonised, physicalSpecimenId);
    return new DigitalSpecimen()
        .withType("ods:DigitalSpecimen")
        .withOdsStatus(OdsStatus.ACTIVE)
        .withOdsFdoType(fdoProperties.getDigitalSpecimenType())
        .withDctermsLicense(termMapper.retrieveTerm(new License(), data, dwc))
        .withOdsPhysicalSpecimenID(physicalSpecimenId)
        .withOdsNormalisedPhysicalSpecimenID(normalisedPhysicalSpecimenId)
        .withOdsPhysicalSpecimenIDType(physicalSpecimenIdTypeHarmonised)
        .withOdsOrganisationID(organisationId)
        .withOdsPhysicalSpecimenID(physicalSpecimenId)
        .withOdsIsKnownToContainMedia(parseToBoolean(new IsKnowToContainMedia(), data, dwc))
        .withOdsSourceSystemID(
            "https://hdl.handle.net/" + sourceSystemComponent.getSourceSystemID())
        .withOdsSourceSystemName(sourceSystemComponent.getSourceSystemName())
        .withOdsLivingOrPreserved(
            retrieveEnum(new LivingOrPreserved(), data, dwc, OdsLivingOrPreserved.class))
        .withDwcPreparations(termMapper.retrieveTerm(new Preparations(), data, dwc))
        .withDwcCollectionID(termMapper.retrieveTerm(new CollectionID(), data, dwc))
        .withDctermsModified(termMapper.retrieveTerm(new Modified(), data, dwc))
        .withOdsOrganisationName(getOrganisationName(organisationId))
        .withDwcOrganismQuantity(termMapper.retrieveTerm(new OrganismQuantity(), data, dwc))
        .withDwcOrganismQuantityType(termMapper.retrieveTerm(new OrganismQuantityType(), data, dwc))
        .withDwcBasisOfRecord(termMapper.retrieveTerm(new BasisOfRecord(), data, dwc))
        .withDctermsAccessRights(termMapper.retrieveTerm(new AccessRights(), data, dwc))
        .withDctermsRightsHolder(termMapper.retrieveTerm(new RightsHolder(), data, dwc))
        .withDwcDatasetID(termMapper.retrieveTerm(new DatasetID(), data, dwc))
        .withDwcOrganismID(termMapper.retrieveTerm(new OrganismID(), data, dwc))
        .withDwcOrganismName(termMapper.retrieveTerm(new OrganismName(), data, dwc))
        .withDwcOrganismScope(termMapper.retrieveTerm(new OrganismScope(), data, dwc))
        .withDwcOrganismRemarks(termMapper.retrieveTerm(new OrganismRemarks(), data, dwc))
        .withDwcDynamicProperties(termMapper.retrieveTerm(new DynamicProperties(), data, dwc))
        .withDwcDatasetName(termMapper.retrieveTerm(new DatasetName(), data, dwc))
        .withDwcDisposition(termMapper.retrieveTerm(new Disposition(), data, dwc))
        .withDwcInformationWithheld(termMapper.retrieveTerm(new InformationWithheld(), data, dwc))
        .withDwcVerbatimLabel(termMapper.retrieveTerm(new VerbatimLabel(), data, dwc))
        .withDwcDataGeneralizations(termMapper.retrieveTerm(new DataGeneralizations(), data, dwc));
  }

  private String getOrganisationName(String organisationId) throws OrganisationException {
    if (organisationId.startsWith("https://ror.org/")) {
      var rorId = organisationId.replace("https://ror.org/", "");
      return organisationNameComponent.getRorName(rorId);
    } else if (organisationId.startsWith("https://www.wikidata.org/")) {
      var wikidataId = organisationId.replace("https://www.wikidata.org/wiki/", "");
      return organisationNameComponent.getWikiDataName(wikidataId);
    } else {
      throw new OrganisationException(
          organisationId + " is not a valid ror or wikidata identifier");
    }
  }

  private List<EntityRelationship> assembleDigitalSpecimenEntityRelationships(
      DigitalSpecimen ds) {
    var relationships = new ArrayList<EntityRelationship>();
    relationships.add(getEntityRelationship(HAS_ORGANISATION_ID, ds.getOdsOrganisationID()));
    relationships.add(getEntityRelationship(HAS_SOURCE_SYSTEM_ID, ds.getOdsSourceSystemID()));
    relationships.add(getEntityRelationship(HAS_FDO_TYPE, fdoProperties.getDigitalSpecimenType()));
    if (ds.getOdsPhysicalSpecimenIDType().equals(OdsPhysicalSpecimenIDType.RESOLVABLE)) {
      relationships.add(
          getEntityRelationship(HAS_PHYSICAL_IDENTIFIER, ds.getOdsPhysicalSpecimenID()));
    }
    if (ds.getDctermsLicense() != null && ds.getDctermsLicense().startsWith("http")) {
      relationships.add(getEntityRelationship(HAS_LICENSE, ds.getDctermsLicense()));
    }
    if (ds.getOdsHasCitations() != null) {
      for (Citation citation : ds.getOdsHasCitations()) {
        if (citation.getId() != null && citation.getId().startsWith("http")) {
          relationships.add(getEntityRelationship(HAS_REFERENCE, citation.getId()));
        }
      }
    }
    return relationships;
  }

  private EntityRelationship getEntityRelationship(RelationshipType relationshipType,
      String relatedResource) {
    var entityRelationship = new EntityRelationship()
        .withType("ods:EntityRelationship")
        .withDwcRelationshipOfResource(relationshipType.getName())
        .withDwcRelatedResourceID(relatedResource)
        .withDwcRelationshipEstablishedDate(java.util.Date.from(Instant.now()));
    setAgent(entityRelationship.getOdsHasAgents(), fdoProperties.getApplicationName(),
        fdoProperties.getApplicationPID(), DATA_TRANSLATOR, SCHEMA_SOFTWARE_APPLICATION);
    return entityRelationship;
  }

  private List<eu.dissco.core.translator.schema.Identifier> assembleIdentifiers(JsonNode data) {
    var identifiers = new ArrayList<Identifier>();
    for (String identifierTerm : identifierTerms) {
      if (data.get(identifierTerm) != null) {
        var identifier = new Identifier()
            .withId(data.get(identifierTerm).asText())
            .withType("ods:Identifier")
            .withDctermsTitle(identifierTerm)
            .withDctermsIdentifier(data.get(identifierTerm).asText());
        identifiers.add(identifier);
      }
    }
    return identifiers;
  }

  protected Identification createIdentification(JsonNode data, boolean dwc) {
    var taxonId = termMapper.retrieveTerm(new TaxonID(), data, dwc);
    var mappedTaxonIdentification = new TaxonIdentification()
        .withId(taxonId)
        .withType("ods:TaxonIdentification")
        .withDwcTaxonID(taxonId)
        .withDwcKingdom(termMapper.retrieveTerm(new Kingdom(), data, dwc))
        .withDwcTaxonRank(termMapper.retrieveTerm(new TaxonRank(), data, dwc))
        .withDwcVerbatimTaxonRank(termMapper.retrieveTerm(new VerbatimTaxonRank(), data, dwc))
        .withDwcGenus(termMapper.retrieveTerm(new Genus(), data, dwc))
        .withDwcSubgenus(termMapper.retrieveTerm(new Subgenus(), data, dwc))
        .withDwcOrder(termMapper.retrieveTerm(new Order(), data, dwc))
        .withDwcScientificName(termMapper.retrieveTerm(new ScientificName(), data, dwc))
        .withDwcScientificNameAuthorship(
            termMapper.retrieveTerm(new ScientificNameAuthorship(), data, dwc))
        .withDwcScientificNameID(termMapper.retrieveTerm(new ScientificNameID(), data, dwc))
        .withDwcNamePublishedInYear(termMapper.retrieveTerm(new NamePublishedInYear(), data, dwc))
        .withDwcClass(termMapper.retrieveTerm(new Class(), data, dwc))
        .withDwcFamily(termMapper.retrieveTerm(new Family(), data, dwc))
        .withDwcSubfamily(termMapper.retrieveTerm(new Subfamily(), data, dwc))
        .withDwcPhylum(termMapper.retrieveTerm(new Phylum(), data, dwc))
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
    var identificationId = termMapper.retrieveTerm(new IdentificationID(), data, dwc);
    var identification = new Identification()
        .withId(identificationId)
        .withType("ods:Identification")
        .withDwcIdentificationID(identificationId)
        .withOdsIsVerifiedIdentification(
            parseToBoolean(new IdentificationVerificationStatus(), data, dwc))
        .withDwcTypeStatus(termMapper.retrieveTerm(new TypeStatus(), data, dwc))
        .withDwcDateIdentified(termMapper.retrieveTerm(new DateIdentified(), data, dwc))
        .withDwcIdentificationRemarks(
            termMapper.retrieveTerm(new IdentificationRemarks(), data, dwc))
        .withDwcVerbatimIdentification(
            termMapper.retrieveTerm(new VerbatimIdentification(), data, dwc))
        .withOdsHasTaxonIdentifications(List.of(mappedTaxonIdentification))
        .withOdsHasCitations(assembleIdentificationCitations(data, dwc));
    setAgent(identification.getOdsHasAgents(), termMapper.retrieveTerm(new IdentifiedBy(), data, dwc),
        termMapper.retrieveTerm(new IdentifiedByID(), data, dwc), IDENTIFIER, SCHEMA_PERSON);
    return identification;
  }


  private List<Event> assembleEventTerms(JsonNode data, boolean dwc) {
    var geoReference = new Georeference()
        .withType("ods:GeoReference")
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
        .withDwcFootprintSRS(termMapper.retrieveTerm(new FootprintSRS(), data, dwc))
        .withDwcFootprintSpatialFit(parseToInteger(new FootprintSpatialFit(), data, dwc))
        .withDwcFootprintWKT(termMapper.retrieveTerm(new FootprintWkt(), data, dwc))
        .withDwcGeoreferencedDate(termMapper.retrieveTerm(new GeoreferencedDate(), data, dwc))
        .withDwcGeoreferenceRemarks(termMapper.retrieveTerm(new GeoreferenceRemarks(), data, dwc))
        .withDwcGeoreferenceSources(termMapper.retrieveTerm(new GeoreferenceSources(), data, dwc))
        .withDwcPointRadiusSpatialFit(parseToDouble(new PointRadiusSpatialFit(), data, dwc));
    setAgent(geoReference.getOdsHasAgents(), termMapper.retrieveTerm(
        new GeoreferencedBy(), data, dwc), null, GEOREFERENCER, SCHEMA_PERSON);
    var geologicalContext = new GeologicalContext()
        .withType("ods:GeologicalContext")
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
    var locationId = termMapper.retrieveTerm(new LocationID(), data, dwc);
    var location = new Location()
        .withId(locationId)
        .withType("ods:Location")
        .withDwcLocationID(locationId)
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
        .withOdsHasGeoreference(geoReference)
        .withOdsHasGeologicalContext(geologicalContext);
    var assertions = new EventAssertions().gatherEventAssertions(mapper, data, dwc);
    var event = new Event()
        .withType("ods:Event")
        .withDwcEventType(termMapper.retrieveTerm(new EventType(), data, dwc))
        .withDwcFieldNumber(termMapper.retrieveTerm(new FieldNumber(), data, dwc))
        .withDwcEventDate(termMapper.retrieveTerm(new EventDate(), data, dwc))
        .withDwcVerbatimEventDate(termMapper.retrieveTerm(new VerbatimEventDate(), data, dwc))
        .withDwcYear(parseToInteger(new Year(), data, dwc))
        .withDwcMonth(parseToInteger(new Month(), data, dwc))
        .withDwcDay(parseToInteger(new Day(), data, dwc))
        .withDwcSex(termMapper.retrieveTerm(new Sex(), data, dwc))
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
        .withDwcGeoreferenceVerificationStatus(
            termMapper.retrieveTerm(new GeoreferenceVerificationStatus(), data, dwc))
        .withDwcEventRemarks(termMapper.retrieveTerm(new EventRemarks(), data, dwc))
        .withDwcSampleSizeUnit(termMapper.retrieveTerm(new SampleSizeUnit(), data, dwc))
        .withDwcSampleSizeValue(parseToDouble(new SampleSizeValue(), data, dwc))
        .withDwcCaste(termMapper.retrieveTerm(new Caste(), data, dwc))
        .withDwcVitality(termMapper.retrieveTerm(new Vitality(), data, dwc))
        .withOdsHasLocation(location)
        .withOdsHasAssertions(assertions);
    setAgent(event.getOdsHasAgents(), termMapper.retrieveTerm(new RecordedBy(), data, dwc),
        termMapper.retrieveTerm(new RecordedByID(), data, dwc), COLLECTOR, SCHEMA_PERSON);
    return List.of(event);
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

  public DigitalMedia assembleDigitalMedia(boolean dwc, JsonNode mediaRecord,
      String organisationId) throws OrganisationException {
    DigitalMedia digitalMedia = new DigitalMedia()
        .withType("ods:DigitalMedia")
        .withOdsStatus(DigitalMedia.OdsStatus.ACTIVE)
        .withOdsFdoType(fdoProperties.getDigitalMediaType())
        .withOdsSourceSystemID(
            "https://hdl.handle.net/" + sourceSystemComponent.getSourceSystemID())
        .withOdsSourceSystemName(sourceSystemComponent.getSourceSystemName())
        .withOdsOrganisationID(organisationId)
        .withOdsOrganisationName(getOrganisationName(organisationId))
        .withAcAccessURI(termMapper.retrieveTerm(new AccessURI(), mediaRecord, dwc))
        .withDctermsRights(termMapper.retrieveTerm(new License(), mediaRecord, dwc))
        .withXmpRightsUsageTerms(termMapper.retrieveTerm(new UsageTerms(), mediaRecord, dwc))
        .withXmpRightsWebStatement(termMapper.retrieveTerm(new WebStatement(), mediaRecord, dwc))
        .withDctermsFormat(termMapper.retrieveTerm(new Format(), mediaRecord, dwc))
        .withDctermsType(retrieveEnum(new MediaType(), mediaRecord, dwc, DctermsType.class))
        .withDctermsSource(termMapper.retrieveTerm(new Source(), mediaRecord, dwc))
        .withAcMetadataLanguage(termMapper.retrieveTerm(new MetadataLanguage(), mediaRecord, dwc))
        .withAcMetadataLanguageLiteral(
            termMapper.retrieveTerm(new MetadataLanguageLiteral(), mediaRecord, dwc))
        .withDctermsLanguage(termMapper.retrieveTerm(new Language(), mediaRecord, dwc))
        .withXmpRightsOwner(termMapper.retrieveTerm(new RightsOwner(), mediaRecord, dwc))
        .withDctermsAvailable(termMapper.retrieveTerm(new Available(), mediaRecord, dwc))
        .withAcComments(termMapper.retrieveTerm(new Comments(), mediaRecord, dwc))
        .withIptc4xmpExtCVterm(termMapper.retrieveTerm(new Iptc4xmpExtCVterm(), mediaRecord, dwc))
        .withAcSubjectCategoryVocabulary(
            termMapper.retrieveTerm(new SubjectCategoryVocabulary(), mediaRecord, dwc))
        .withAcVariant(termMapper.retrieveTerm(new Variant(), mediaRecord, dwc))
        .withAcVariantLiteral(termMapper.retrieveTerm(new VariantLiteral(), mediaRecord, dwc))
        .withAcVariantDescription(
            termMapper.retrieveTerm(new VariantDescription(), mediaRecord, dwc))
        .withExifPixelXDimension(
            parseToInteger(new PixelXDimension(), mediaRecord, dwc))
        .withExifPixelYDimension(
            parseToInteger(new PixelYDimension(), mediaRecord, dwc))
        .withXmpCreateDate(termMapper.retrieveTerm(new CreateDate(), mediaRecord, dwc))
        .withAcTimeOfDay(termMapper.retrieveTerm(new TimeOfDay(), mediaRecord, dwc))
        .withAcSubjectOrientation(
            termMapper.retrieveTerm(new SubjectOrientation(), mediaRecord, dwc))
        .withAcSubjectOrientationLiteral(
            termMapper.retrieveTerm(new SubjectOrientationLiteral(), mediaRecord, dwc))
        .withAcSubjectPart(termMapper.retrieveTerm(new SubjectPart(), mediaRecord, dwc))
        .withAcSubjectPartLiteral(
            termMapper.retrieveTerm(new SubjectPartLiteral(), mediaRecord, dwc))
        .withAcCaptureDevice(termMapper.retrieveTerm(new CaptureDevice(), mediaRecord, dwc))
        .withAcDigitizationDate(termMapper.retrieveTerm(new DigitizationDate(), mediaRecord, dwc))
        .withAcFrameRate(parseToDouble(new FrameRate(), mediaRecord, dwc))
        .withAcResourceCreationTechnique(
            termMapper.retrieveTerm(new ResourceCreationTechnique(), mediaRecord, dwc))
        .withDctermsSource(termMapper.retrieveTerm(new Source(), mediaRecord, dwc))
        .withAcSubtype(termMapper.retrieveTerm(new Subtype(), mediaRecord, dwc))
        .withAcSubtypeLiteral(termMapper.retrieveTerm(new SubtypeLiteral(), mediaRecord, dwc))
        .withDctermsTitle(termMapper.retrieveTerm(new Title(), mediaRecord, dwc))
        .withDctermsModified(
            termMapper.retrieveTerm(new Modified(), mediaRecord, dwc))
        .withDctermsDescription(termMapper.retrieveTerm(new Description(), mediaRecord, dwc))
        .withOdsHasIdentifiers(assembleIdentifiers(mediaRecord))
        .withOdsHasAssertions(new MediaAssertions().gatherAssertions(mediaRecord, dwc));
    setAgent(digitalMedia.getOdsHasAgents(), termMapper
        .retrieveTerm(new Creator(), mediaRecord, dwc), null, CREATOR, SCHEMA_PERSON);
    setAgent(digitalMedia.getOdsHasAgents(), termMapper
        .retrieveTerm(new RightsOwner(), mediaRecord, dwc), null, RIGHTS_OWNER, SCHEMA_PERSON);
    digitalMedia.withOdsHasEntityRelationships(
        assembleDigitalMediaEntityRelationships(digitalMedia));
    return digitalMedia;
  }

  private List<EntityRelationship> assembleDigitalMediaEntityRelationships(
      DigitalMedia digitalMedia) {
    var relationships = new ArrayList<EntityRelationship>();
    relationships.add(getEntityRelationship(HAS_URL, digitalMedia.getAcAccessURI()));
    relationships.add(
        getEntityRelationship(HAS_ORGANISATION_ID, digitalMedia.getOdsOrganisationID()));
    relationships.add(
        getEntityRelationship(HAS_FDO_TYPE, fdoProperties.getDigitalMediaType()));
    if (digitalMedia.getDctermsRights() != null && digitalMedia.getDctermsRights()
        .startsWith("http")) {
      relationships.add(
          getEntityRelationship(HAS_LICENSE, digitalMedia.getDctermsRights()));
    }
    if (digitalMedia.getDctermsSource() != null && digitalMedia.getDctermsSource()
        .startsWith("http")) {
      relationships.add(getEntityRelationship(HAS_SOURCE, digitalMedia.getDctermsSource()));
    }
    return relationships;
  }

  private String getNormalisedPhysicalSpecimenId(OdsPhysicalSpecimenIDType physicalSpecimenIDType,
      String physicalSpecimenId) {
    var sourceSystemId = sourceSystemComponent.getSourceSystemID();
    if (physicalSpecimenIDType.equals(OdsPhysicalSpecimenIDType.GLOBAL)
        || physicalSpecimenIDType.equals(OdsPhysicalSpecimenIDType.RESOLVABLE)) {
      return physicalSpecimenId;
    } else {
      var minifiedSourceSystemId = sourceSystemId.substring(sourceSystemId.lastIndexOf('/') + 1);
      return physicalSpecimenId + ":" + minifiedSourceSystemId;
    }
  }

  private OdsPhysicalSpecimenIDType convertToPhysicalSpecimenIdTypeEnum(
      String physicalSpecimenIDType) throws UnknownPhysicalSpecimenIdType {
    try {
      return OdsPhysicalSpecimenIDType.valueOf(physicalSpecimenIDType.toUpperCase());
    } catch (IllegalArgumentException e) {
      log.warn("Unknown physicalSpecimenIDType specified");
      throw new UnknownPhysicalSpecimenIdType(
          "Physical specimen ID type is: " + physicalSpecimenIDType
              + " which is not a known id type");
    } catch (NullPointerException e) {
      log.warn("No physicalSpecimenIDType specified");
      throw new UnknownPhysicalSpecimenIdType("Physical specimen ID type is empty");
    }
  }

}


