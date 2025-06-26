package eu.dissco.core.translator.domain.eml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;

@JacksonXmlRootElement(namespace = "https://eml.ecoinformatics.org/dataset-2.2.0", localName = "dataset")
public record Dataset(
    String title,
    String pubDate,
    String language,
    @JacksonXmlProperty(localName = "abstract")
    String abstractText,
    @JacksonXmlElementWrapper(useWrapping = false)
    List<Agent> contact,
    @JacksonXmlElementWrapper(useWrapping = false)
    List<Agent> associatedParty,
    String intellectualRights,
    License licensed,
    Distribution distribution,
    Coverage coverage
) { }
