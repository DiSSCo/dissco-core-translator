package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.TestUtils.ENDPOINT;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_ID;
import static eu.dissco.core.translator.database.jooq.Tables.NEW_SOURCE_SYSTEM;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SourceSystemRepositoryIT extends BaseRepositoryIT {

  private SourceSystemRepository repository;

  @BeforeEach
  void setup() {
    repository = new SourceSystemRepository(context);
  }

  @AfterEach
  void cleanup() {
    context.truncate(NEW_SOURCE_SYSTEM).execute();
  }

  @Test
  void testGetEndpoint() {
    // Given
    givenInsertRecords();

    // When
    var result = repository.getEndpoint(SOURCE_SYSTEM_ID);

    // Then
    assertThat(result).isEqualTo(ENDPOINT);
  }

  private void givenInsertRecords() {
    context.insertInto(NEW_SOURCE_SYSTEM)
        .set(NEW_SOURCE_SYSTEM.ID, SOURCE_SYSTEM_ID)
        .set(NEW_SOURCE_SYSTEM.NAME, "Royal Botanic Garden Edinburgh Living Plant Collections")
        .set(NEW_SOURCE_SYSTEM.ENDPOINT, ENDPOINT)
        .set(NEW_SOURCE_SYSTEM.DESCRIPTION,
            "Source system for the DWCA of the Living Plant Collections")
        .set(NEW_SOURCE_SYSTEM.CREATED, Instant.parse("2022-09-16T08:25:01.00Z"))
        .set(NEW_SOURCE_SYSTEM.MAPPING_ID, "20.5000.1025/GW0-POP-XAS")
        .execute();
  }
}
