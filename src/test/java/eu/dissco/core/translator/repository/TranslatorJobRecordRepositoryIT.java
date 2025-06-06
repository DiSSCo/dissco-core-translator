package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.TestUtils.ENDPOINT;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_ID;
import static eu.dissco.core.translator.database.jooq.Tables.SOURCE_SYSTEM;
import static eu.dissco.core.translator.database.jooq.Tables.TRANSLATOR_JOB_RECORD;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.database.jooq.enums.ErrorCode;
import eu.dissco.core.translator.database.jooq.enums.JobState;
import eu.dissco.core.translator.database.jooq.enums.TranslatorType;
import eu.dissco.core.translator.domain.TranslatorJobResult;
import java.time.Instant;
import java.util.UUID;
import org.jooq.JSONB;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TranslatorJobRecordRepositoryIT extends BaseRepositoryIT {

  private TranslatorJobRecordRepository repository;

  @BeforeEach
  void setup() {
    repository = new TranslatorJobRecordRepository(context);
  }

  @AfterEach
  void destroy() {
    context.truncate(TRANSLATOR_JOB_RECORD).execute();
    context.truncate(SOURCE_SYSTEM).cascade().execute();
  }

  @Test
  void testCreateNewJob() {
    // Given
    var jobId = UUID.randomUUID();
    insertSourceSystem();

    // When
    repository.createNewJobRecord(jobId, SOURCE_SYSTEM_ID);

    // Then
    var result = context.fetchOne(TRANSLATOR_JOB_RECORD, TRANSLATOR_JOB_RECORD.JOB_ID.eq(jobId));
    assertThat(result).isNotNull();
    assertThat(result.getJobId()).isEqualTo(jobId);
    assertThat(result.getSourceSystemId()).isEqualTo(SOURCE_SYSTEM_ID);
    assertThat(result.getJobState()).isEqualTo(JobState.RUNNING);
    assertThat(result.getTimeStarted()).isNotNull();
  }

  @Test
  void testJobSuccessful() {
    // Given
    var jobId = UUID.randomUUID();
    insertSourceSystem();
    repository.createNewJobRecord(jobId, SOURCE_SYSTEM_ID);

    // When
    repository.updateJobState(jobId, new TranslatorJobResult(JobState.COMPLETED, 50000), null);

    // Then
    var result = context.fetchOne(TRANSLATOR_JOB_RECORD, TRANSLATOR_JOB_RECORD.JOB_ID.eq(jobId));
    assertThat(result).isNotNull();
    assertThat(result.getJobId()).isEqualTo(jobId);
    assertThat(result.getSourceSystemId()).isEqualTo(SOURCE_SYSTEM_ID);
    assertThat(result.getJobState()).isEqualTo(JobState.COMPLETED);
    assertThat(result.getProcessedRecords()).isEqualTo(50000);
    assertThat(result.getTimeCompleted()).isNotNull();
    assertThat(result.getTimeStarted()).isNotNull();
    assertThat(result.getError()).isNull();
  }

  @Test
  void testJobFailed() {
    // Given
    var jobId = UUID.randomUUID();
    insertSourceSystem();
    repository.createNewJobRecord(jobId, SOURCE_SYSTEM_ID);

    // When
    repository.updateJobState(jobId, new TranslatorJobResult(JobState.FAILED, 421312),
        ErrorCode.DISSCO_EXCEPTION);

    // Then
    var result = context.fetchOne(TRANSLATOR_JOB_RECORD, TRANSLATOR_JOB_RECORD.JOB_ID.eq(jobId));
    assertThat(result).isNotNull();
    assertThat(result.getJobId()).isEqualTo(jobId);
    assertThat(result.getSourceSystemId()).isEqualTo(SOURCE_SYSTEM_ID);
    assertThat(result.getJobState()).isEqualTo(JobState.FAILED);
    assertThat(result.getProcessedRecords()).isEqualTo(421312);
    assertThat(result.getTimeCompleted()).isNotNull();
    assertThat(result.getTimeStarted()).isNotNull();
    assertThat(result.getError()).isEqualTo(ErrorCode.DISSCO_EXCEPTION);
  }

  private void insertSourceSystem() {
    context.insertInto(SOURCE_SYSTEM)
        .set(SOURCE_SYSTEM.ID, SOURCE_SYSTEM_ID)
        .set(SOURCE_SYSTEM.NAME, "Royal Botanic Garden Edinburgh Living Plant Collections")
        .set(SOURCE_SYSTEM.ENDPOINT, ENDPOINT)
        .set(SOURCE_SYSTEM.CREATOR, "e2befba6-9324-4bb4-9f41-d7dfae4a44b0")
        .set(SOURCE_SYSTEM.CREATED, Instant.parse("2022-09-16T08:25:01.00Z"))
        .set(SOURCE_SYSTEM.MODIFIED, Instant.parse("2022-09-16T08:25:01.00Z"))
        .set(SOURCE_SYSTEM.TRANSLATOR_TYPE, TranslatorType.dwca)
        .set(SOURCE_SYSTEM.MAPPING_ID, "20.5000.1025/GW0-POP-XAS")
        .set(SOURCE_SYSTEM.DATA, JSONB.valueOf("{}"))
        .execute();
  }

}
