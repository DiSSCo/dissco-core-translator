package eu.dissco.core.translator.service;

import eu.dissco.core.translator.component.SourceSystemComponent;
import eu.dissco.core.translator.domain.TranslatorJobResult;
import eu.dissco.core.translator.properties.MasProperties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

  protected static Set<String> getMachineAnnotationServices(boolean mediaObject, MasProperties masProperties,
      SourceSystemComponent sourceSystemComponent) {
    if (mediaObject) {
      return Stream.concat(masProperties.getAdditionalMediaMass().stream(),
          sourceSystemComponent.getMediaMass().stream()).collect(
          Collectors.toSet());
    } else {
      return Stream.concat(masProperties.getAdditionalSpecimenMass().stream(),
          sourceSystemComponent.getSpecimenMass().stream()).collect(
          Collectors.toSet());
    }
  }
}
