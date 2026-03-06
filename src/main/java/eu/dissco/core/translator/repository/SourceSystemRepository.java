package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.database.jooq.Tables.SOURCE_SYSTEM;

import eu.dissco.core.translator.domain.SourceSystemInformation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.Record3;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.json.JsonMapper;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SourceSystemRepository {

  private final DSLContext context;
  private final JsonMapper mapper;

  public SourceSystemInformation getSourceSystem(String sourceSystemId) {
    return context.select(SOURCE_SYSTEM.NAME, SOURCE_SYSTEM.ENDPOINT, SOURCE_SYSTEM.DATA)
        .from(SOURCE_SYSTEM)
        .where(SOURCE_SYSTEM.ID.eq(sourceSystemId))
        .fetchOne(this::mapToSourceSystemInformation);
  }

  private SourceSystemInformation mapToSourceSystemInformation(
      Record3<String, String, JSONB> dbRecord) {
    List<String> filters = List.of();
    var data = mapper.readTree(dbRecord.value3().data());
    if (data.get("ods:filters") != null) {
      filters = mapper.readerForListOf(String.class).readValue(data.get("ods:filters"));
    }
    return new SourceSystemInformation(dbRecord.value1(), dbRecord.value2(), filters);
  }

  public void storeEml(byte[] bytes, String sourceSystemID) {
    context.update(SOURCE_SYSTEM)
        .set(SOURCE_SYSTEM.EML, bytes)
        .where(SOURCE_SYSTEM.ID.eq(sourceSystemID))
        .execute();
  }
}
