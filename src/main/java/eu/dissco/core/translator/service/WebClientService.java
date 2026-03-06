package eu.dissco.core.translator.service;

import static eu.dissco.core.translator.terms.utils.LicenseUtils.licenseComplies;

import eu.dissco.core.translator.domain.TranslatorJobResult;
import eu.dissco.core.translator.exception.DiSSCoDataException;
import java.util.List;
import javax.xml.stream.events.XMLEvent;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.DigitalMedia;

public abstract class WebClientService {

  protected static final List<String> ALLOWED_BASIS_OF_RECORD = List.of("PRESERVEDSPECIMEN",
      "PRESERVED_SPECIMEN", "FOSSIL", "OTHER", "ROCK", "MINERAL", "METEORITE", "FOSSILSPECIMEN",
      "LIVINGSPECIMEN", "MATERIALSAMPLE", "FOSSIL SPECIMEN", "ROCKSPECIMEN", "ROCK SPECIMEN",
      "MINERALSPECIMEN", "MINERAL SPECIMEN", "METEORITESPECIMEN", "METEORITE SPECIMEN",
      "HERBARIUM SHEET", "HERBARIUMSHEET", "DRIED");

  public abstract TranslatorJobResult retrieveData();

  protected boolean isStartElement(XMLEvent element, String field) {
    if (element != null) {
      return element.isStartElement() && element.asStartElement().getName().getLocalPart()
          .equals(field);
    } else {
      return false;
    }
  }


  protected void checkIfSpecimenComplies(DigitalSpecimen ds)
      throws DiSSCoDataException {
    if (ds.getOdsNormalisedPhysicalSpecimenID() == null || ds.getOdsOrganisationID() == null) {
      throw new DiSSCoDataException(
          "Record does not comply to MIDS level 0 (id and organisation), ignoring record");
    }
    if (!basisOfRecordComplies(ds.getDwcBasisOfRecord())
        || !licenseComplies(ds.getDctermsLicense())) {
      throw new DiSSCoDataException(
          "Record does not comply basis of record or license requirements");
    }
  }

    private static boolean basisOfRecordComplies(String basisOfRecord) {
        return ALLOWED_BASIS_OF_RECORD.contains(basisOfRecord.strip().replace(" ", "").toUpperCase());
    }


    protected void checkIfMediaComplies(DigitalMedia digitalMedia)
      throws DiSSCoDataException {
    if (digitalMedia.getAcAccessURI() == null) {
      throw new DiSSCoDataException(
          "Digital media object for specimen does not have an access uri, ignoring record");
    }
    if (!licenseComplies(digitalMedia.getDctermsRights())) {
      throw new DiSSCoDataException(
          "Digital media object does not have a valid license uri, ignoring record");
    }
  }
}
