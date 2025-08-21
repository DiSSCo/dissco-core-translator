package eu.dissco.core.translator.service;

import eu.dissco.core.translator.domain.TranslatorJobResult;
import javax.xml.stream.events.XMLEvent;

public abstract class WebClientService {

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
