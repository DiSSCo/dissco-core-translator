/*
 * This file is generated by jOOQ.
 */
package eu.dissco.core.translator.database.jooq;


import eu.dissco.core.translator.database.jooq.tables.Mapping;
import eu.dissco.core.translator.database.jooq.tables.SourceSystem;
import eu.dissco.core.translator.database.jooq.tables.records.MappingRecord;
import eu.dissco.core.translator.database.jooq.tables.records.SourceSystemRecord;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in public.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Keys {

  // -------------------------------------------------------------------------
  // UNIQUE and PRIMARY KEY definitions
  // -------------------------------------------------------------------------

  public static final UniqueKey<MappingRecord> NEW_MAPPING_PK = Internal.createUniqueKey(
      Mapping.MAPPING, DSL.name("new_mapping_pk"),
      new TableField[]{Mapping.MAPPING.ID, Mapping.MAPPING.VERSION}, true);
  public static final UniqueKey<SourceSystemRecord> NEW_SOURCE_SYSTEM_PKEY = Internal.createUniqueKey(
      SourceSystem.SOURCE_SYSTEM, DSL.name("new_source_system_pkey"),
      new TableField[]{SourceSystem.SOURCE_SYSTEM.ID}, true);
}
