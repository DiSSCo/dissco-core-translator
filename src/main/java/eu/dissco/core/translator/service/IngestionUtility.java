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

  protected static String getPhysicalSpecimenId(String physicalSpecimenIdType, String organisationId,
      String physicalSpecimenId) throws DiSSCoDataException {
    if (physicalSpecimenIdType.equals("cetaf")) {
      return physicalSpecimenId;
    } else if (physicalSpecimenIdType.equals("combined")) {
      return physicalSpecimenId + ":" + minifyOrganisationId(organisationId);
    } else {
      log.warn("Unknown physicalSpecimenIdType specified");
      throw new UnknownPhysicalSpecimenIdType(physicalSpecimenIdType + " is not a known id type");
    }
  }

  public static String minifyOrganisationId(String organisationId) throws OrganisationNotRorId {
    if (organisationId.startsWith("https://ror.org")) {
      return organisationId.replace("https://ror.org/", "");
    } else {
      throw new OrganisationNotRorId(organisationId + " is not a valid ror");
    }
  }
}
