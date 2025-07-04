/*
 * This file is generated by jOOQ.
 */
package eu.dissco.core.translator.database.jooq;


import eu.dissco.core.translator.database.jooq.tables.DataMapping;
import eu.dissco.core.translator.database.jooq.tables.SourceSystem;
import eu.dissco.core.translator.database.jooq.tables.TranslatorJobRecord;
import eu.dissco.core.translator.database.jooq.tables.records.DataMappingRecord;
import eu.dissco.core.translator.database.jooq.tables.records.SourceSystemRecord;
import eu.dissco.core.translator.database.jooq.tables.records.TranslatorJobRecordRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<DataMappingRecord> DATA_MAPPING_PK = Internal.createUniqueKey(DataMapping.DATA_MAPPING, DSL.name("data_mapping_pk"), new TableField[] { DataMapping.DATA_MAPPING.ID, DataMapping.DATA_MAPPING.VERSION }, true);
    public static final UniqueKey<SourceSystemRecord> SOURCE_SYSTEM_PKEY = Internal.createUniqueKey(SourceSystem.SOURCE_SYSTEM, DSL.name("source_system_pkey"), new TableField[] { SourceSystem.SOURCE_SYSTEM.ID }, true);
    public static final UniqueKey<TranslatorJobRecordRecord> TRANSLATOR_JOB_RECORD_PKEY = Internal.createUniqueKey(TranslatorJobRecord.TRANSLATOR_JOB_RECORD, DSL.name("translator_job_record_pkey"), new TableField[] { TranslatorJobRecord.TRANSLATOR_JOB_RECORD.JOB_ID, TranslatorJobRecord.TRANSLATOR_JOB_RECORD.SOURCE_SYSTEM_ID }, true);
}
