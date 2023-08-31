package eu.dissco.core.translator.service;

import eu.dissco.core.translator.exception.DiSSCoDataException;
import eu.dissco.core.translator.exception.OrganisationNotRorId;
import eu.dissco.core.translator.exception.UnknownPhysicalSpecimenIdType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IngestionUtility {

  private IngestionUtility() {
    // Utility class
  }

  protected static String getPhysicalSpecimenId(String physicalSpecimenIdType, String sourceSystemId,
      String physicalSpecimenId) throws DiSSCoDataException {
    if (physicalSpecimenIdType.equals("global") || physicalSpecimenIdType.equals("resolvable")) {
      return physicalSpecimenId;
    } else if (physicalSpecimenIdType.equals("local")) {
      return physicalSpecimenId + ":" + sourceSystemId;
    } else {
      log.warn("Unknown physicalSpecimenIdType specified");
      throw new UnknownPhysicalSpecimenIdType(physicalSpecimenIdType + " is not a known id type");
    }
  }

}
