package eu.dissco.core.translator.properties;

import eu.dissco.core.translator.domain.Enrichment;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("enrichment")
public class EnrichmentProperties {

  private List<Enrichment> list;

}
