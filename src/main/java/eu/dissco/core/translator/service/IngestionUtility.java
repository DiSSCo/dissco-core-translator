package eu.dissco.core.translator.service;

import eu.dissco.core.translator.exception.DiSSCoDataException;
import eu.dissco.core.translator.exception.UnknownPhysicalSpecimenIdType;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsPhysicalSpecimenIdType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IngestionUtility {

  private IngestionUtility() {
    // Utility class
  }

  protected static String getPhysicalSpecimenId(String physicalSpecimenIdType,
      String sourceSystemId,
      String physicalSpecimenId) throws DiSSCoDataException {
    if (physicalSpecimenIdType.equals("global") || physicalSpecimenIdType.equals("resolvable")
        || physicalSpecimenIdType.equals("cetaf")) {
      return physicalSpecimenId;
    } else if (physicalSpecimenIdType.equals("local") || physicalSpecimenIdType.equals(
        "combined")) {
      var minifiedSourceSystemId = sourceSystemId.substring(sourceSystemId.indexOf('/') + 1);
      return physicalSpecimenId + ":" + minifiedSourceSystemId;
    } else {
      log.warn("Unknown physicalSpecimenIdType specified");
      throw new UnknownPhysicalSpecimenIdType(physicalSpecimenIdType + " is not a known id type");
    }
  }

  protected static eu.dissco.core.translator.schema.DigitalSpecimen.OdsPhysicalSpecimenIdType convertToPhysicalSpecimenIdTypeEnum(
      String physicalSpecimenIdType) {
    if (physicalSpecimenIdType.equals("cetaf") || physicalSpecimenIdType.equals("global")) {
      return OdsPhysicalSpecimenIdType.GLOBAL;
    } else if (physicalSpecimenIdType.equals("resolvable")) {
      return OdsPhysicalSpecimenIdType.RESOLVABLE;
    } else {
      return OdsPhysicalSpecimenIdType.LOCAL;
    }
  }
}
