package eu.dissco.core.translator.service;

import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import eu.dissco.core.translator.TestUtils;
import eu.dissco.core.translator.database.jooq.enums.ErrorCode;
import eu.dissco.core.translator.database.jooq.enums.JobState;
import eu.dissco.core.translator.domain.TranslatorJobResult;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.repository.TranslatorJobRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TranslatorJobRecordServiceTest {

  @Mock
  private TranslatorJobRecordRepository repository;
  @Mock
  private WebClientProperties properties;

  private TranslatorJobRecordService service;

  @BeforeEach
  void setup() {
    service = new TranslatorJobRecordService(repository, properties);
  }

  @Test
  void testCreateNewJobRecord() {
    // Given
    given(properties.getSourceSystemId()).willReturn(SOURCE_SYSTEM_ID);

    // When
    var jobId = service.createNewJobRecord();

    // Then
    assertThat(jobId).isNotNull();
    then(repository).should().createNewJobRecord(jobId, SOURCE_SYSTEM_ID);
  }

  @Test
  void testUpdateSuccessfulJob() {
    // Given
    var result = new TranslatorJobResult(JobState.COMPLETED, 50000);

    // When
    service.updateJobState(TestUtils.JOB_ID, result);

    // Then
    then(repository).should().updateJobState(TestUtils.JOB_ID, result, null);
  }

  @Test
  void testUpdateFailedJob() {
    // Given
    var result = new TranslatorJobResult(JobState.FAILED, 50000);

    // When
    service.updateJobState(TestUtils.JOB_ID, result);

    // Then
    then(repository).should().updateJobState(TestUtils.JOB_ID, result, ErrorCode.DISSCO_EXCEPTION);
  }
}
