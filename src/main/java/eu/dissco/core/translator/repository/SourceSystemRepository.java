package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.database.jooq.Tables.SOURCE_SYSTEM;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SourceSystemRepository {

  private final DSLContext context;

  public String getEndpoint(String sourceSystemId) {
    return context.select(SOURCE_SYSTEM.ENDPOINT)
        .from(SOURCE_SYSTEM)
        .where(SOURCE_SYSTEM.ID.eq(sourceSystemId))
        .fetchOne(Record1::value1);
  }
}
