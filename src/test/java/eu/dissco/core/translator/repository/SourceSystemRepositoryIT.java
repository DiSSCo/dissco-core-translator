package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.TestUtils.ENDPOINT;
import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_ID;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_NAME;
import static eu.dissco.core.translator.TestUtils.givenSourceSystemInformation;
import static eu.dissco.core.translator.database.jooq.Tables.SOURCE_SYSTEM;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.database.jooq.enums.TranslatorType;
import java.time.Instant;
import org.jooq.JSONB;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SourceSystemRepositoryIT extends BaseRepositoryIT {

  private SourceSystemRepository repository;

  @BeforeEach
  void setup() {
    repository = new SourceSystemRepository(context, MAPPER);
  }

  @AfterEach
  void cleanup() {
    context.truncate(SOURCE_SYSTEM).cascade().execute();
  }

  @Test
  void testGetEndpoint() {
    // Given
    givenInsertRecords();

    // When
    var result = repository.getSourceSystem(SOURCE_SYSTEM_ID);

    // Then
    assertThat(result).isEqualTo(givenSourceSystemInformation());
  }

  private void givenInsertRecords() {
    context.insertInto(SOURCE_SYSTEM)
        .set(SOURCE_SYSTEM.ID, SOURCE_SYSTEM_ID)
        .set(SOURCE_SYSTEM.NAME, SOURCE_SYSTEM_NAME)
        .set(SOURCE_SYSTEM.ENDPOINT, ENDPOINT)
        .set(SOURCE_SYSTEM.CREATOR, "e2befba6-9324-4bb4-9f41-d7dfae4a44b0")
        .set(SOURCE_SYSTEM.DATE_CREATED, Instant.parse("2022-09-16T08:25:01.00Z"))
        .set(SOURCE_SYSTEM.DATE_MODIFIED, Instant.parse("2022-09-16T08:25:01.00Z"))
        .set(SOURCE_SYSTEM.TRANSLATOR_TYPE, TranslatorType.dwca)
        .set(SOURCE_SYSTEM.MAPPING_ID, "20.5000.1025/GW0-POP-XAS")
        .set(SOURCE_SYSTEM.DATA, JSONB.valueOf("{}"))
        .execute();
  }
}
