package eu.dissco.core.translator.service;

import static eu.dissco.core.translator.terms.utils.LicenseUtils.licenseComplies;

import eu.dissco.core.translator.domain.TranslatorJobReport;
import eu.dissco.core.translator.domain.TranslatorJobResult;
import eu.dissco.core.translator.exception.DiSSCoDataException;
import eu.dissco.core.translator.schema.DigitalMedia;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javax.xml.stream.events.XMLEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractDataRetrievalService {

  private static final String NOT_ACCEPTED = "' is not accepted";

  protected static final Set<String> ALLOWED_BASIS_OF_RECORD = Set.of("PRESERVEDSPECIMEN",
      "PRESERVED_SPECIMEN", "FOSSIL", "OTHER", "ROCK", "MINERAL", "METEORITE", "FOSSILSPECIMEN",
      "LIVINGSPECIMEN", "MATERIALSAMPLE", "ROCKSPECIMEN", "MINERALSPECIMEN", "METEORITESPECIMEN",
      "HERBARIUMSHEET",
      "DRIED");

  protected final TranslatorJobReport report;
  private final List<Predicate<DigitalSpecimen>> specimenComplianceChecks =
      List.of(this::isNormalisedPhysicalSpecimenIDPresent,
          this::isOrganisationIDPresent,
          this::basisOfRecordComplies,
          this::digitalSpecimenLicenseComplies);
  private final List<Predicate<DigitalMedia>> mediaComplianceChecks =
      List.of(this::digitalMediaLicenseComplies,
          this::isAccessURIPresent);

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
    for (var check : specimenComplianceChecks) {
      if (!check.test(ds)) {
        throw new DiSSCoDataException("Specimen with id " + ds.getOdsNormalisedPhysicalSpecimenID()
            + " does not comply with the requirements, ignoring record");
      }
    }
  }

  private boolean digitalSpecimenLicenseComplies(DigitalSpecimen ds) {
    var license = ds.getDctermsLicense();
    if (!licenseComplies(license)) {
      report.addSpecimenIssues("License '" + license + NOT_ACCEPTED);
      return false;
    } else {
      return true;
    }
  }

  private boolean isOrganisationIDPresent(DigitalSpecimen ds) {
    if (ds.getOdsOrganisationID() != null) {
      return true;
    } else {
      report.addSpecimenIssues("Missing Organisation ID");
      return false;
    }
  }

  private boolean isNormalisedPhysicalSpecimenIDPresent(DigitalSpecimen ds) {
    if (ds.getOdsNormalisedPhysicalSpecimenID() != null) {
      return true;
    } else {
      report.addSpecimenIssues("Missing Normalised Physical Specimen Identifier");
      return false;
    }
  }

  private boolean basisOfRecordComplies(DigitalSpecimen ds) {
    String basisOfRecord = ds.getDwcBasisOfRecord();
    if (basisOfRecord == null) {
      report.addSpecimenIssues("Missing Basis of Record");
      return false;
    }
    if (ALLOWED_BASIS_OF_RECORD.contains(basisOfRecord.strip().replace(" ", "").toUpperCase())) {
      return true;
    } else {
      report.addSpecimenIssues("BasisOfRecord '" + basisOfRecord + NOT_ACCEPTED);
      return false;
    }
  }

  protected void checkIfMediaComplies(DigitalMedia dm)
      throws DiSSCoDataException {
    for (var check : mediaComplianceChecks) {
      if (!check.test(dm)) {
        throw new DiSSCoDataException("Media with id " + dm.getAcAccessURI()
            + " does not comply with the requirements, ignoring record");
      }
    }
  }

  private boolean isAccessURIPresent(DigitalMedia dm) {
    if (dm.getAcAccessURI() != null) {
      return true;
    } else {
      report.addMediaIssues("Missing Access URI");
      return false;
    }
  }

  private boolean digitalMediaLicenseComplies(DigitalMedia dm) {
    var license = dm.getDctermsRights();
    if (!licenseComplies(license)) {
      report.addMediaIssues("License '" + license + NOT_ACCEPTED);
      return false;
    } else {
      return true;
    }
  }
}
