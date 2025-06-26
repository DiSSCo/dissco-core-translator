package eu.dissco.core.translator.component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import efg.DataSets;
import efg.DataSets.DataSet;
import efg.DataSets.DataSet.ContentContacts;
import efg.DataSets.DataSet.TechnicalContacts;
import efg.IPRStatements;
import efg.MetadataDescriptionRepr;
import efg.MicroAgentP;
import eu.dissco.core.translator.domain.eml.Address;
import eu.dissco.core.translator.domain.eml.Agent;
import eu.dissco.core.translator.domain.eml.BoundingCoordinates;
import eu.dissco.core.translator.domain.eml.Coverage;
import eu.dissco.core.translator.domain.eml.Dataset;
import eu.dissco.core.translator.domain.eml.Distribution;
import eu.dissco.core.translator.domain.eml.Eml;
import eu.dissco.core.translator.domain.eml.GeographicCoverage;
import eu.dissco.core.translator.domain.eml.License;
import eu.dissco.core.translator.domain.eml.Name;
import eu.dissco.core.translator.domain.eml.Online;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class EmlComponent {

  private EmlComponent() {
    // Private constructor to prevent instantiation
  }

  /**
   * Generates an EML XML representation from the given ABCD dataset.
   * Maps fields such as title, publication date, language, details.
   * ContentCreators are seen as contact and technical contacts are seen as associatedParty.
   *
   * @param abcdDataset the ABCD dataset to convert
   * @return the EML XML as a String
   * @throws IOException if there is an error converting the domain object to an XML string
   */
  public static String generateEML(DataSets.DataSet abcdDataset) throws IOException {
    var representation = abcdDataset.getMetadata().getDescription().getRepresentation().get(0);
    var emlDataset = new Dataset(
        representation.getTitle(),
        formatPubDate(abcdDataset),
        representation.getLanguage(),
        representation.getDetails(),
        parseContentCreators(abcdDataset.getContentContacts()),
        parseTechnicalContacts(abcdDataset.getTechnicalContacts()),
        getCopyright(abcdDataset.getMetadata().getIPRStatements()),
        parseLicense(abcdDataset.getMetadata().getIPRStatements()),
        getDistribution(representation),
        getGeographicCoverage(representation));
    var eml = new Eml(UUID.randomUUID().toString(), "https://dissco.eu", "en", emlDataset);
    var xmlMapper = new XmlMapper();
    xmlMapper.setSerializationInclusion(Include.NON_NULL);
    return xmlMapper.writeValueAsString(eml);
  }

  private static Distribution getDistribution(MetadataDescriptionRepr representation) {
    if (representation.getURI() != null) {
      return new Distribution(new Online(representation.getURI()));
    }
    return null;
  }

  private static Coverage getGeographicCoverage(MetadataDescriptionRepr representation) {
    if (representation.getCoverage() != null) {
      return new Coverage(new GeographicCoverage(representation.getCoverage(),
          new BoundingCoordinates("-180", "180", "-90", "90")));
    }
    return null;
  }

  private static String formatPubDate(DataSet datasets) {
    if (datasets.getMetadata().getRevisionData() != null
        && datasets.getMetadata().getRevisionData().getDateModified() != null) {
      return LocalDate.of(datasets.getMetadata().getRevisionData().getDateModified().getYear(),
          datasets.getMetadata().getRevisionData().getDateModified().getMonth(),
          datasets.getMetadata().getRevisionData().getDateModified().getDay()).toString();
    }
    return null;
  }

  private static License parseLicense(IPRStatements iprStatements) {
    if (iprStatements != null && iprStatements.getLicenses() != null
        && !iprStatements.getLicenses().getLicense().isEmpty()) {
      var abcdLicense = iprStatements.getLicenses().getLicense().get(0);
      return new License(abcdLicense.getText(), abcdLicense.getURI());
    }
    return null;
  }

  private static String getCopyright(IPRStatements iprStatement) {
    if (iprStatement != null && iprStatement.getCopyrights() != null
        && !iprStatement.getCopyrights().getCopyright().isEmpty()) {
      return iprStatement.getCopyrights().getCopyright().get(0).getText();
    } else if (iprStatement != null && iprStatement.getTermsOfUseStatements() != null
        && !iprStatement.getTermsOfUseStatements().getTermsOfUse().isEmpty()) {
      return iprStatement.getTermsOfUseStatements().getTermsOfUse().get(0).getText();
    }
    return null;
  }

  private static List<Agent> parseContentCreators(ContentContacts contentContacts) {
    if (contentContacts != null) {
      return contentContacts.getContentContact().stream()
          .map(abcdAgent -> parseAgent(abcdAgent, null)).toList();
    }
    return null;
  }

  private static List<Agent> parseTechnicalContacts(TechnicalContacts technicalContacts) {
    if (technicalContacts != null) {
      return technicalContacts.getTechnicalContact().stream()
          .map(abcdAgent -> parseAgent(abcdAgent, "TECHNICAL_POINT_OF_CONTACT")).toList();
    }
    return null;
  }

  private static Agent parseAgent(MicroAgentP abcdAgent, String role) {
    var emlAgent = Agent.builder();
    emlAgent.individualName(new Name(abcdAgent.getName()));
    if (abcdAgent.getEmail() != null) {
      emlAgent.electronicMailAddress(abcdAgent.getEmail());
    }
    if (abcdAgent.getPhone() != null) {
      emlAgent.phone(abcdAgent.getPhone());
    }
    if (abcdAgent.getAddress() != null) {
      emlAgent.address(new Address(abcdAgent.getAddress()));
    }
    if (role != null) {
      emlAgent.role(role);
    }
    return emlAgent.build();
  }
}
