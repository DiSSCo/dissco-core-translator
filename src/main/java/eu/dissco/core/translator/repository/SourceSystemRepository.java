package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.database.jooq.Tables.SOURCE_SYSTEM;

import eu.dissco.core.translator.domain.SourceSystemInformation;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SourceSystemRepository {

  private final DSLContext context;

  public SourceSystemInformation getSourceSystem(String sourceSystemId) {
    return context.select(SOURCE_SYSTEM.NAME, SOURCE_SYSTEM.ENDPOINT)
        .from(SOURCE_SYSTEM)
        .where(SOURCE_SYSTEM.ID.eq(sourceSystemId))
        .fetchOne(this::mapToSourceSystemInformation);
  }

  private SourceSystemInformation mapToSourceSystemInformation(
      Record2<String, String> dbRecord) {
    return new SourceSystemInformation(dbRecord.value1(), dbRecord.value2());
  }
}
