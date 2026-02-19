package eu.dissco.core.translator.service;

import eu.dissco.core.translator.domain.TranslatorJobResult;
import java.util.List;
import javax.xml.stream.events.XMLEvent;

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
}
