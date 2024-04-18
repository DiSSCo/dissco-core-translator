package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.TestUtils.ENDPOINT;
import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.MAPPING_JSON;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_ID;
import static eu.dissco.core.translator.database.jooq.Tables.MAPPING;
import static eu.dissco.core.translator.database.jooq.Tables.SOURCE_SYSTEM;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import org.jooq.JSONB;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MappingRepositoryIT extends BaseRepositoryIT {

  private MappingRepository repository;

  @BeforeEach
  void setup() {
    repository = new MappingRepository(MAPPER, context);
  }

  @AfterEach
  void cleanup() {
    context.truncate(SOURCE_SYSTEM).cascade().execute();
    context.truncate(MAPPING).execute();
  }

  @Test
  void testRetrieveMapping() throws Exception {
    // Given
    givenInsertRecords(MAPPING_JSON);
    var expected = givenExpected();

    // When
    var result = repository.retrieveMapping(SOURCE_SYSTEM_ID);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  private JsonNode givenExpected() throws JsonProcessingException {
    return MAPPER.readTree(MAPPING_JSON);
  }

  private void givenInsertRecords(String mapping) {
    context.insertInto(MAPPING)
        .set(MAPPING.ID, "20.5000.1025/GW0-POP-XAS")
        .set(MAPPING.VERSION, 1)
        .set(MAPPING.NAME, "Royal Botanic Garden Edinburgh Living Plant Collections Mapping")
        .set(MAPPING.DESCRIPTION, "Mapping create based on the Living Plant Collections dwca")
        .set(MAPPING.MAPPING_, JSONB.valueOf(mapping))
        .set(MAPPING.CREATED, Instant.parse("2022-09-16T08:25:01.00Z"))
        .set(MAPPING.CREATOR, "e2befba6-9324-4bb4-9f41-d7dfae4a44b0")
        .execute();

    context.insertInto(SOURCE_SYSTEM)
        .set(SOURCE_SYSTEM.ID, SOURCE_SYSTEM_ID)
        .set(SOURCE_SYSTEM.NAME, "Royal Botanic Garden Edinburgh Living Plant Collections")
        .set(SOURCE_SYSTEM.ENDPOINT, ENDPOINT)
        .set(SOURCE_SYSTEM.DESCRIPTION,
            "Source system for the DWCA of the Living Plant Collections")
        .set(SOURCE_SYSTEM.CREATED, Instant.parse("2022-09-16T08:25:01.00Z"))
        .set(SOURCE_SYSTEM.MAPPING_ID, "20.5000.1025/GW0-POP-XAS")
        .execute();
  }

}
