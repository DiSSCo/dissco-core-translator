package eu.dissco.core.translator;

import eu.dissco.core.translator.service.TranslatorJobRecordService;
import eu.dissco.core.translator.service.WebClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class ProjectRunner implements CommandLineRunner {

  private final WebClientService webService;
  private final ConfigurableApplicationContext context;
  private final TranslatorJobRecordService tjrService;

  @Override
  public void run(String... args) {
    var jobId = tjrService.createNewJobRecord();
    var processingResult = webService.retrieveData();
    tjrService.updateJobState(jobId, processingResult);
    context.close();
  }
}
