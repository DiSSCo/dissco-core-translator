package eu.dissco.core.translator.terms;

import com.fasterxml.jackson.databind.JsonNode;
import efg.DataSets.DataSet;
import eu.dissco.core.translator.component.MappingComponent;
import eu.dissco.core.translator.terms.specimen.CollectingNumber;
import eu.dissco.core.translator.terms.specimen.Collector;
import eu.dissco.core.translator.terms.specimen.DatasetId;
import eu.dissco.core.translator.terms.specimen.DateCollected;
import eu.dissco.core.translator.terms.specimen.Modified;
import eu.dissco.core.translator.terms.specimen.ObjectType;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenCollection;
import eu.dissco.core.translator.terms.specimen.SpecimenName;
import eu.dissco.core.translator.terms.specimen.TypeStatus;
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
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
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

  public static List<Term> harmonisedTerms() {
    var list = new ArrayList<Term>();
    list.add(new SpecimenName());
    list.add(new PhysicalSpecimenCollection());
    list.add(new DatasetId());
    list.add(new ObjectType());
    list.add(new Modified());
    list.add(new DateCollected());
    list.add(new CollectingNumber());
    list.add(new Collector());
    list.add(new TypeStatus());
    list.addAll(locationTerms());
    return list;
  }

  public static List<Term> abcdHarmonisedTerms() {
    return harmonisedTerms();
  }

  public static List<Term> dwcaHarmonisedTerms() {
    var terms = harmonisedTerms();
    terms.add(new License());
    return terms;
  }

  public String retrieveFromDWCA(Term term, ArchiveFile archiveFile, Record rec) {
    var harmonisedTerm = term.getTerm();
    if (mapping.getDefaultMappings().containsKey(harmonisedTerm)) {
      return mapping.getDefaultMappings().get(harmonisedTerm);
    }
    if (mapping.getFieldMappings().containsKey(harmonisedTerm)) {
      return rec.value(
          archiveFile.getField(mapping.getFieldMappings().get(harmonisedTerm)).getTerm());
    }
    return term.retrieveFromDWCA(archiveFile, rec);
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

  public String retrieveFromABCD(Term term, DataSet datasets) {
    return term.retrieveFromABCD(datasets);
  }
}
