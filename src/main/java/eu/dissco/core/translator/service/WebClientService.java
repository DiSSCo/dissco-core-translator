package eu.dissco.core.translator.service;

import javax.xml.stream.events.XMLEvent;

public abstract class WebClientService {

  public abstract void retrieveData();

  protected boolean isStartElement(XMLEvent element, String field) {
    if (element != null) {
      return element.isStartElement() && element.asStartElement().getName().getLocalPart()
          .equals(field);
    } else {
      return false;
    }
  }
}
