package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.TestUtils.ENDPOINT;
import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.MAPPING_JSON;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_ID;
import static eu.dissco.core.translator.database.jooq.Tables.DATA_MAPPING;
import static eu.dissco.core.translator.database.jooq.Tables.SOURCE_SYSTEM;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.database.jooq.enums.TranslatorType;
import java.time.Instant;
import org.jooq.JSONB;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataMappingRepositoryIT extends BaseRepositoryIT {

  private DataMappingRepository repository;

  @BeforeEach
  void setup() {
    repository = new DataMappingRepository(MAPPER, context);
  }

  @AfterEach
  void cleanup() {
    context.truncate(SOURCE_SYSTEM).cascade().execute();
    context.truncate(DATA_MAPPING).execute();
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
    context.insertInto(DATA_MAPPING)
        .set(DATA_MAPPING.ID, "20.5000.1025/GW0-POP-XAS")
        .set(DATA_MAPPING.VERSION, 1)
        .set(DATA_MAPPING.NAME, "Royal Botanic Garden Edinburgh Living Plant Collections Mapping")
        .set(DATA_MAPPING.CREATOR, "e2befba6-9324-4bb4-9f41-d7dfae4a44b0")
        .set(DATA_MAPPING.CREATED, Instant.parse("2022-09-16T08:25:01.00Z"))
        .set(DATA_MAPPING.MODIFIED, Instant.parse("2022-09-16T08:25:01.00Z"))
        .set(DATA_MAPPING.MAPPING_DATA_STANDARD, "dwc")
        .set(DATA_MAPPING.DATA, JSONB.valueOf(mapping))
        .execute();

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
