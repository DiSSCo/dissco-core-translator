package eu.dissco.core.translator.terms;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.component.MappingComponent;
import eu.dissco.core.translator.terms.specimen.BasisOfRecord;
import eu.dissco.core.translator.terms.specimen.CollectingNumber;
import eu.dissco.core.translator.terms.specimen.Collector;
import eu.dissco.core.translator.terms.specimen.DatasetId;
import eu.dissco.core.translator.terms.specimen.DateCollected;
import eu.dissco.core.translator.terms.specimen.HasMedia;
import eu.dissco.core.translator.terms.specimen.LivingOrPreserved;
import eu.dissco.core.translator.terms.specimen.Modified;
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
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Member;
import eu.dissco.core.translator.terms.specimen.taxonomy.Class;
import eu.dissco.core.translator.terms.specimen.taxonomy.Family;
import eu.dissco.core.translator.terms.specimen.taxonomy.Genus;
import eu.dissco.core.translator.terms.specimen.taxonomy.InfraspecificEpithet;
import eu.dissco.core.translator.terms.specimen.taxonomy.Kingdom;
import eu.dissco.core.translator.terms.specimen.taxonomy.Order;
import eu.dissco.core.translator.terms.specimen.taxonomy.Phylum;
import eu.dissco.core.translator.terms.specimen.taxonomy.ScientificNameAuthorship;
import eu.dissco.core.translator.terms.specimen.taxonomy.SpecificEpithet;
import eu.dissco.core.translator.terms.specimen.taxonomy.SpecimenName;
import eu.dissco.core.translator.terms.specimen.taxonomy.TaxonRank;
import eu.dissco.core.translator.terms.specimen.taxonomy.TypeStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TermMapper {

  private final MappingComponent mapping;

  public static List<Term> locationTerms() {
    var list = new ArrayList<Term>();
    list.add(new Continent());
    list.add(new Country());
    list.add(new CountryCode());
    list.add(new County());
    list.add(new DecimalLatitude());
    list.add(new DecimalLongitude());
    list.add(new GeodeticDatum());
    list.add(new Island());
    list.add(new IslandGroup());
    list.add(new Locality());
    list.add(new StateProvince());
    list.add(new WaterBody());
    return list;
  }

  private static List<Term> stratigraphyTerms() {
    var list = new ArrayList<Term>();
    list.add(new EarliestAgeOrLowestStage());
    list.add(new EarliestEonOrLowestEonothem());
    list.add(new EarliestEpochOrLowestSeries());
    list.add(new EarliestEraOrLowestErathem());
    list.add(new EarliestPeriodOrLowestSystem());
    list.add(new LatestAgeOrHighestStage());
    list.add(new LatestEonOrHighestEonothem());
    list.add(new LatestEpochOrHighestSeries());
    list.add(new LatestEraOrHighestErathem());
    list.add(new LatestPeriodOrHighestSystem());
    list.add(new Bed());
    list.add(new Formation());
    list.add(new Group());
    list.add(new Member());
    list.add(new HighestBiostratigraphicZone());
    list.add(new LowestBiostratigraphicZone());
    return list;
  }

  public static List<Term> harmonisedTerms() {
    var list = new ArrayList<Term>();
    list.add(new PhysicalSpecimenCollection());
    list.add(new DatasetId());
    list.add(new ObjectType());
    list.add(new DateCollected());
    list.add(new CollectingNumber());
    list.add(new Collector());
    list.add(new TypeStatus());
    list.add(new HasMedia());
    list.add(new BasisOfRecord());
    list.add(new LivingOrPreserved());
    list.add(new TopicDiscipline());
    list.addAll(locationTerms());
    list.addAll(stratigraphyTerms());
    list.addAll(taxonomyTerms());
    return list;
  }

  public static List<Term> taxonomyTerms() {
    var list = new ArrayList<Term>();
    list.add(new SpecimenName());
    list.add(new Class());
    list.add(new Family());
    list.add(new Genus());
    list.add(new InfraspecificEpithet());
    list.add(new Kingdom());
    list.add(new Order());
    list.add(new Phylum());
    list.add(new ScientificNameAuthorship());
    list.add(new SpecificEpithet());
    list.add(new TaxonRank());
    return list;
  }

  public static List<Term> abcdHarmonisedTerms() {
    return harmonisedTerms();
  }

  public static List<Term> dwcaHarmonisedTerms() {
    var terms = harmonisedTerms();
    terms.add(new License());
    terms.add(new Modified());
    return terms;
  }

  public String retrieveFromDWCA(Term term, JsonNode unit) {
    var termName = term.getTerm();
    if (mapping.getDefaultMappings().containsKey(termName)) {
      return mapping.getDefaultMappings().get(termName);
    } else if (mapping.getFieldMappings().containsKey(termName)) {
      var value = unit.get(mapping.getFieldMappings().get(termName));
      if (value != null && value.isTextual()) {
        return value.asText();
      }
    }
    return term.retrieveFromDWCA(unit);
  }

  public String retrieveFromABCD(Term term, JsonNode unit) {
    var termName = term.getTerm();
    if (mapping.getDefaultMappings().containsKey(termName)) {
      return mapping.getDefaultMappings().get(termName);
    } else if (mapping.getFieldMappings().containsKey(termName)) {
      var value = unit.get(mapping.getFieldMappings().get(termName));
      if (value != null && value.isTextual()) {
        return value.asText();
      }
    }
    return term.retrieveFromABCD(unit);
  }

  public String retrieveFromABCD(Term term, JsonNode dataset, JsonNode unit) {
    return term.retrieveFromABCD(dataset, unit);
  }
}
