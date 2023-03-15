package eu.dissco.core.translator.service;

import eu.dissco.core.translator.exception.DiSSCoDataException;
import eu.dissco.core.translator.exception.OrganizationNotRorId;
import eu.dissco.core.translator.exception.UnknownPhysicalSpecimenIdType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IngestionUtility {

  private IngestionUtility() {
    // Utility class
  }

  public static String getPhysicalSpecimenId(String physicalSpecimenIdType, String organizationId,
      String physicalSpecimenId) throws DiSSCoDataException {
    if (physicalSpecimenIdType.equals("cetaf")) {
      return physicalSpecimenId;
    } else if (physicalSpecimenIdType.equals("combined")) {
      return physicalSpecimenId + ":" + minifyOrganizationId(organizationId);
    } else {
      log.warn("Unknown physicalSpecimenIdType specified");
      throw new UnknownPhysicalSpecimenIdType(physicalSpecimenIdType + " is not a known id type");
    }
  }

  private static String minifyOrganizationId(String organizationId) throws OrganizationNotRorId {
    if (organizationId.startsWith("https://ror.org")) {
      return organizationId.replace("https://ror.org/", "");
    } else {
      throw new OrganizationNotRorId(organizationId + " is not a valid ror");
    }
  }
}
