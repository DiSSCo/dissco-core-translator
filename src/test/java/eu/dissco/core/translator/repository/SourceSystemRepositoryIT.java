package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.TestUtils.ENDPOINT;
import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_ID;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_NAME;
import static eu.dissco.core.translator.TestUtils.givenSourceSystemInformation;
import static eu.dissco.core.translator.database.jooq.Tables.SOURCE_SYSTEM;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.database.jooq.enums.TranslatorType;
import eu.dissco.core.translator.domain.SourceSystemInformation;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.List;
import org.jooq.JSONB;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

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
    givenInsertRecords("{}");

    // When
    var result = repository.getSourceSystem(SOURCE_SYSTEM_ID);

    // Then
    assertThat(result).isEqualTo(givenSourceSystemInformation());
  }

  @Test
  void testGetSourceSystemInformationWithFilter() {
    // Given
    givenInsertRecords("""
        {
          "ods:filters": ["dataset=AVES"]
        }
        """);
    var expected = new SourceSystemInformation(SOURCE_SYSTEM_NAME, ENDPOINT,
        List.of("dataset=AVES"));

    // When
    var result = repository.getSourceSystem(SOURCE_SYSTEM_ID);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void testStoreEml() throws IOException {
    //Given
    givenInsertRecords("{}");
    var expected = Files.readAllBytes(new ClassPathResource("sample-eml.xml").getFile().toPath());

    // When
    repository.storeEml(expected, SOURCE_SYSTEM_ID);

    // Then
    var result = context.select(SOURCE_SYSTEM.EML)
        .from(SOURCE_SYSTEM)
        .where(SOURCE_SYSTEM.ID.eq(SOURCE_SYSTEM_ID))
        .fetchOne(SOURCE_SYSTEM.EML);
    assertThat(result).isEqualTo(expected);
  }

  private void givenInsertRecords(String data) {
    context.insertInto(SOURCE_SYSTEM)
        .set(SOURCE_SYSTEM.ID, SOURCE_SYSTEM_ID)
        .set(SOURCE_SYSTEM.NAME, SOURCE_SYSTEM_NAME)
        .set(SOURCE_SYSTEM.ENDPOINT, ENDPOINT)
        .set(SOURCE_SYSTEM.CREATOR, "e2befba6-9324-4bb4-9f41-d7dfae4a44b0")
        .set(SOURCE_SYSTEM.CREATED, Instant.parse("2022-09-16T08:25:01.00Z"))
        .set(SOURCE_SYSTEM.MODIFIED, Instant.parse("2022-09-16T08:25:01.00Z"))
        .set(SOURCE_SYSTEM.TRANSLATOR_TYPE, TranslatorType.dwca)
        .set(SOURCE_SYSTEM.MAPPING_ID, "20.5000.1025/GW0-POP-XAS")
        .set(SOURCE_SYSTEM.DATA, JSONB.valueOf(data))
        .execute();
  }
}
