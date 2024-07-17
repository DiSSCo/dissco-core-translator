/*
 * This file is generated by jOOQ.
 */
package eu.dissco.core.translator.database.jooq.tables.records;


import eu.dissco.core.translator.database.jooq.tables.DataMapping;

import java.time.Instant;

import org.jooq.JSONB;
import org.jooq.Record2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DataMappingRecord extends UpdatableRecordImpl<DataMappingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.data_mapping.id</code>.
     */
    public void setId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.data_mapping.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.data_mapping.version</code>.
     */
    public void setVersion(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.data_mapping.version</code>.
     */
    public Integer getVersion() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.data_mapping.name</code>.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.data_mapping.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.data_mapping.date_created</code>.
     */
    public void setDateCreated(Instant value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.data_mapping.date_created</code>.
     */
    public Instant getDateCreated() {
        return (Instant) get(3);
    }

    /**
     * Setter for <code>public.data_mapping.date_modified</code>.
     */
    public void setDateModified(Instant value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.data_mapping.date_modified</code>.
     */
    public Instant getDateModified() {
        return (Instant) get(4);
    }

    /**
     * Setter for <code>public.data_mapping.date_tombstoned</code>.
     */
    public void setDateTombstoned(Instant value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.data_mapping.date_tombstoned</code>.
     */
    public Instant getDateTombstoned() {
        return (Instant) get(5);
    }

    /**
     * Setter for <code>public.data_mapping.creator</code>.
     */
    public void setCreator(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.data_mapping.creator</code>.
     */
    public String getCreator() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.data_mapping.mapping_data_standard</code>.
     */
    public void setMappingDataStandard(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.data_mapping.mapping_data_standard</code>.
     */
    public String getMappingDataStandard() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.data_mapping.data</code>.
     */
    public void setData(JSONB value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.data_mapping.data</code>.
     */
    public JSONB getData() {
        return (JSONB) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, Integer> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached DataMappingRecord
     */
    public DataMappingRecord() {
        super(DataMapping.DATA_MAPPING);
    }

    /**
     * Create a detached, initialised DataMappingRecord
     */
    public DataMappingRecord(String id, Integer version, String name, Instant dateCreated, Instant dateModified, Instant dateTombstoned, String creator, String mappingDataStandard, JSONB data) {
        super(DataMapping.DATA_MAPPING);

        setId(id);
        setVersion(version);
        setName(name);
        setDateCreated(dateCreated);
        setDateModified(dateModified);
        setDateTombstoned(dateTombstoned);
        setCreator(creator);
        setMappingDataStandard(mappingDataStandard);
        setData(data);
        resetChangedOnNotNull();
    }
}
