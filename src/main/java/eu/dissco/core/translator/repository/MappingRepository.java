package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.database.jooq.Tables.NEW_MAPPING;
import static eu.dissco.core.translator.database.jooq.Tables.NEW_SOURCE_SYSTEM;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.Record1;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MappingRepository {

  private final ObjectMapper mapper;
  private final DSLContext context;

  public JsonNode retrieveMapping(String sourceSystemId) {
    return context.select(NEW_MAPPING.MAPPING)
        .from(NEW_MAPPING)
        .join(NEW_SOURCE_SYSTEM)
        .on(NEW_SOURCE_SYSTEM.MAPPING_ID.eq(NEW_MAPPING.ID))
        .where(NEW_SOURCE_SYSTEM.ID.eq(sourceSystemId))
        .fetchOne(this::mapToJson);
  }

  private JsonNode mapToJson(Record1<JSONB> jsonbRecord1) {
    try {
      return mapper.readTree(jsonbRecord1.value1().data());
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
