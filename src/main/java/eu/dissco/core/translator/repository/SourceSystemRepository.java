package eu.dissco.core.translator.repository;

import static eu.dissco.core.translator.database.jooq.Tables.NEW_SOURCE_SYSTEM;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SourceSystemRepository {

  private final DSLContext context;

  public String getEndpoint(String sourceSystemId) {
    return context.select(NEW_SOURCE_SYSTEM.ENDPOINT)
        .from(NEW_SOURCE_SYSTEM)
        .where(NEW_SOURCE_SYSTEM.ID.eq(sourceSystemId))
        .fetchOne(Record1::value1);
  }
}
