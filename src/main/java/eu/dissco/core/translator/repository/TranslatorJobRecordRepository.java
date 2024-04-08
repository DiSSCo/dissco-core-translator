package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.database.jooq.Tables.TRANSLATOR_JOB_RECORD;

import eu.dissco.core.translator.database.jooq.enums.ErrorCode;
import eu.dissco.core.translator.database.jooq.enums.JobState;
import eu.dissco.core.translator.domain.TranslatorJobResult;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class TranslatorJobRecordRepository {

  private final DSLContext context;

  public void createNewJobRecord(UUID jobId, String sourceSystemId) {
    context.insertInto(TRANSLATOR_JOB_RECORD)
        .set(TRANSLATOR_JOB_RECORD.JOB_ID, jobId)
        .set(TRANSLATOR_JOB_RECORD.SOURCE_SYSTEM_ID, sourceSystemId)
        .set(TRANSLATOR_JOB_RECORD.JOB_STATE, JobState.RUNNING)
        .set(TRANSLATOR_JOB_RECORD.TIME_STARTED, Instant.now())
        .execute();
  }

  public void updateJobState(UUID jobId, TranslatorJobResult processingResult,
      ErrorCode errorCode) {
    context.update(TRANSLATOR_JOB_RECORD)
        .set(TRANSLATOR_JOB_RECORD.JOB_STATE, processingResult.jobState())
        .set(TRANSLATOR_JOB_RECORD.TIME_COMPLETED, Instant.now())
        .set(TRANSLATOR_JOB_RECORD.PROCESSED_RECORDS, processingResult.processedRecords())
        .set(TRANSLATOR_JOB_RECORD.ERROR, errorCode)
        .where(TRANSLATOR_JOB_RECORD.JOB_ID.eq(jobId))
        .execute();
  }
}
