package eu.dissco.core.translator.service;

import eu.dissco.core.translator.database.jooq.enums.ErrorCode;
import eu.dissco.core.translator.database.jooq.enums.JobState;
import eu.dissco.core.translator.domain.TranslatorJobResult;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.repository.TranslatorJobRecordRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TranslatorJobRecordService {

  private final TranslatorJobRecordRepository repository;
  private final WebClientProperties properties;

  public UUID createNewJobRecord() {
    var jobId = UUID.randomUUID();
    repository.createNewJobRecord(jobId, properties.getSourceSystemId());
    log.info("Created new job record with id: {}", jobId);
    return jobId;
  }

  public void updateJobState(UUID jobId, TranslatorJobResult processingResult) {
    var jobState = processingResult.jobState();
    if (jobState == JobState.COMPLETED) {
      log.info("Job with id {} finished successfully. Processed {} records", jobId,
          processingResult.processedRecords());
      repository.updateJobState(jobId, processingResult, null);
    } else if (jobState == JobState.FAILED) {
      log.info("Job with id {} failed. Processed {} records", jobId,
          processingResult.processedRecords());
      repository.updateJobState(jobId, processingResult, ErrorCode.DISSCO_EXCEPTION);
    }
  }
}
