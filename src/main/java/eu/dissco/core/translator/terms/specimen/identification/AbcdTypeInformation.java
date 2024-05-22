package eu.dissco.core.translator.terms.specimen.identification;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.Identifications;
import eu.dissco.core.translator.terms.Term;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbcdTypeInformation extends Term {

  private static final Logger log = LoggerFactory.getLogger(AbcdTypeInformation.class);

  public void addTypeInformation(DigitalSpecimen ds, JsonNode unit, ObjectMapper mapper) {
    var typeDesignationNodes = getTypeDesignationNodes(unit, mapper);
    if (typeDesignationNodes.isEmpty()) {
      return;
    }
    var identifications = ds.getDwcIdentification();
    if (typeDesignationNodes.size() == 1 && identifications.size() == 1) {
      setAdditionalIdentificationInfo(identifications.get(0), typeDesignationNodes.get(0));
    } else {
      identifications.forEach(identification -> typeDesignationNodes.stream()
          .filter(node -> isNameMatch(identification, node)).findFirst()
          .ifPresent(jsonNode -> setAdditionalIdentificationInfo(identification, jsonNode)));
    }
  }

  private boolean isNameMatch(Identifications identification, JsonNode node) {
    if (node.get("typifiedName/fullScientificNameString") != null) {
      return node.get("typifiedName/fullScientificNameString").asText()
          .equals(identification.getTaxonIdentifications().get(0).getDwcScientificName());
    } else {
      log.warn("No typifiedName found in typeDesignationNode: {}", node);
      return false;
    }
  }

  private void setAdditionalIdentificationInfo(Identifications identification,
      JsonNode typeDesignationNodes) {
    identification.setTypeDesignatedBy(
        new TypeDesignatedBy().retrieveFromABCD(typeDesignationNodes));
    identification.setDwcTypeStatus(new TypeStatus().retrieveFromABCD(typeDesignationNodes));
  }

  private List<JsonNode> getTypeDesignationNodes(JsonNode unit, ObjectMapper mapper) {
    var typeDesignationNodes = new ArrayList<JsonNode>();
    var iterateOverElements = true;
    var count = 0;
    while (iterateOverElements) {
      var typeDesignationNode = getSubJsonAbcd(mapper, unit, count,
          "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/");
      if (!typeDesignationNode.isEmpty()) {
        typeDesignationNodes.add(typeDesignationNode);
        count++;
      } else {
        iterateOverElements = false;
      }
    }
    return typeDesignationNodes;
  }

  @Override
  public String getTerm() {
    return null;
  }
}
