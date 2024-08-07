package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.database.jooq.Tables.DATA_MAPPING;
import static eu.dissco.core.translator.database.jooq.Tables.SOURCE_SYSTEM;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.exception.DisscoJsonMappingException;
import eu.dissco.core.translator.exception.DisscoRepositoryException;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.Record1;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DataMappingRepository {

  private final ObjectMapper mapper;
  private final DSLContext context;

  public JsonNode retrieveMapping(String sourceSystemId) throws DisscoRepositoryException {
    try {
      return context.select(DATA_MAPPING.DATA)
          .distinctOn(DATA_MAPPING.ID)
          .from(DATA_MAPPING)
          .join(SOURCE_SYSTEM)
          .on(SOURCE_SYSTEM.MAPPING_ID.eq(DATA_MAPPING.ID))
          .where(SOURCE_SYSTEM.ID.eq(sourceSystemId))
          .orderBy(DATA_MAPPING.ID, DATA_MAPPING.VERSION.desc())
          .fetchOne(this::mapToJson);
    } catch (DataAccessException ex) {
      throw new DisscoRepositoryException("Failed to get mapping from repository", ex);
    }
  }

  private JsonNode mapToJson(Record1<JSONB> jsonbRecord1) {
    try {
      return mapper.readTree(jsonbRecord1.value1().data());
    } catch (JsonProcessingException e) {
      throw new DisscoJsonMappingException(
          "Failed to parse jsonb field to json: " + jsonbRecord1.value1().data(), e);
    }
  }
}
