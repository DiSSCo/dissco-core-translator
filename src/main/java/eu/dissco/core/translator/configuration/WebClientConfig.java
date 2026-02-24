package eu.dissco.core.translator.configuration;

import eu.dissco.core.translator.client.RorClient;
import eu.dissco.core.translator.client.WikidataClient;
import io.netty.channel.ChannelOption;
import javax.xml.stream.XMLInputFactory;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

@Configuration
@AllArgsConstructor
public class WebClientConfig {

  private static final int TIMEOUT = 1000;

  @Bean
  @Primary
  public WebClient webClientXml() {
    var size = 16 * 1024 * 1024;
    final ExchangeStrategies strategies = ExchangeStrategies.builder()
        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
        .build();
    return WebClient.builder()
        .exchangeStrategies(strategies)
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
            .followRedirect(true)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)))
        .build();
  }

  @Bean
  public XMLInputFactory xmlEventReader() {
    var factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    return factory;
  }

  @Bean
  public RorClient rorClient() {
    // Initialize HTTP client
    RestClient restClient = RestClient.create("https://api.ror.org/v2/organizations/");

    // Create factory for client proxies
    HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builder()
        .exchangeAdapter(RestClientAdapter.create(restClient))
        .build();

    // Create client proxy
    return proxyFactory.createClient(RorClient.class);
  }

  @Bean
  public WikidataClient wikidataClient() {
    // Initialize HTTP client
    RestClient restClient = RestClient.create(
        "https://www.wikidata.org/w/rest.php/wikibase/v1/entities/items/");

    // Create factory for client proxies
    HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builder()
        .exchangeAdapter(RestClientAdapter.create(restClient))
        .build();

    // Create client proxy
    return proxyFactory.createClient(WikidataClient.class);
  }

}
