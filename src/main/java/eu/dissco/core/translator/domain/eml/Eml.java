package eu.dissco.core.translator.domain.eml;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(namespace = "https://eml.ecoinformatics.org/eml-2.2.0", localName = "eml")
public record Eml(
    @JacksonXmlProperty(isAttribute = true, localName = "packageId")
    String packageId,
    @JacksonXmlProperty(isAttribute = true, localName = "system")
    String system,
    @JacksonXmlProperty(isAttribute = true, localName = "xml:lang")
    String lang,
    Dataset dataset
) { }
