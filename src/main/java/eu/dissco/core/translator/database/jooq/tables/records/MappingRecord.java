/*
 * This file is generated by jOOQ.
 */
package eu.dissco.core.translator.database.jooq.tables.records;


import eu.dissco.core.translator.database.jooq.tables.Mapping;

import java.time.Instant;

import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record2;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MappingRecord extends UpdatableRecordImpl<MappingRecord> implements Record9<String, Integer, String, String, JSONB, Instant, String, Instant, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.mapping.id</code>.
     */
    public void setId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.mapping.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.mapping.version</code>.
     */
    public void setVersion(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.mapping.version</code>.
     */
    public Integer getVersion() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.mapping.name</code>.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.mapping.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.mapping.description</code>.
     */
    public void setDescription(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.mapping.description</code>.
     */
    public String getDescription() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.mapping.mapping</code>.
     */
    public void setMapping(JSONB value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.mapping.mapping</code>.
     */
    public JSONB getMapping() {
        return (JSONB) get(4);
    }

    /**
     * Setter for <code>public.mapping.created</code>.
     */
    public void setCreated(Instant value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.mapping.created</code>.
     */
    public Instant getCreated() {
        return (Instant) get(5);
    }

    /**
     * Setter for <code>public.mapping.creator</code>.
     */
    public void setCreator(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.mapping.creator</code>.
     */
    public String getCreator() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.mapping.deleted</code>.
     */
    public void setDeleted(Instant value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.mapping.deleted</code>.
     */
    public Instant getDeleted() {
        return (Instant) get(7);
    }

    /**
     * Setter for <code>public.mapping.sourcedatastandard</code>.
     */
    public void setSourcedatastandard(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.mapping.sourcedatastandard</code>.
     */
    public String getSourcedatastandard() {
        return (String) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, Integer> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row9<String, Integer, String, String, JSONB, Instant, String, Instant, String> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<String, Integer, String, String, JSONB, Instant, String, Instant, String> valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Mapping.MAPPING.ID;
    }

    @Override
    public Field<Integer> field2() {
        return Mapping.MAPPING.VERSION;
    }

    @Override
    public Field<String> field3() {
        return Mapping.MAPPING.NAME;
    }

    @Override
    public Field<String> field4() {
        return Mapping.MAPPING.DESCRIPTION;
    }

    @Override
    public Field<JSONB> field5() {
        return Mapping.MAPPING.MAPPING_;
    }

    @Override
    public Field<Instant> field6() {
        return Mapping.MAPPING.CREATED;
    }

    @Override
    public Field<String> field7() {
        return Mapping.MAPPING.CREATOR;
    }

    @Override
    public Field<Instant> field8() {
        return Mapping.MAPPING.DELETED;
    }

    @Override
    public Field<String> field9() {
        return Mapping.MAPPING.SOURCEDATASTANDARD;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public Integer component2() {
        return getVersion();
    }

    @Override
    public String component3() {
        return getName();
    }

    @Override
    public String component4() {
        return getDescription();
    }

    @Override
    public JSONB component5() {
        return getMapping();
    }

    @Override
    public Instant component6() {
        return getCreated();
    }

    @Override
    public String component7() {
        return getCreator();
    }

    @Override
    public Instant component8() {
        return getDeleted();
    }

    @Override
    public String component9() {
        return getSourcedatastandard();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public Integer value2() {
        return getVersion();
    }

    @Override
    public String value3() {
        return getName();
    }

    @Override
    public String value4() {
        return getDescription();
    }

    @Override
    public JSONB value5() {
        return getMapping();
    }

    @Override
    public Instant value6() {
        return getCreated();
    }

    @Override
    public String value7() {
        return getCreator();
    }

    @Override
    public Instant value8() {
        return getDeleted();
    }

    @Override
    public String value9() {
        return getSourcedatastandard();
    }

    @Override
    public MappingRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public MappingRecord value2(Integer value) {
        setVersion(value);
        return this;
    }

    @Override
    public MappingRecord value3(String value) {
        setName(value);
        return this;
    }

    @Override
    public MappingRecord value4(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public MappingRecord value5(JSONB value) {
        setMapping(value);
        return this;
    }

    @Override
    public MappingRecord value6(Instant value) {
        setCreated(value);
        return this;
    }

    @Override
    public MappingRecord value7(String value) {
        setCreator(value);
        return this;
    }

    @Override
    public MappingRecord value8(Instant value) {
        setDeleted(value);
        return this;
    }

    @Override
    public MappingRecord value9(String value) {
        setSourcedatastandard(value);
        return this;
    }

    @Override
    public MappingRecord values(String value1, Integer value2, String value3, String value4, JSONB value5, Instant value6, String value7, Instant value8, String value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached MappingRecord
     */
    public MappingRecord() {
        super(Mapping.MAPPING);
    }

    /**
     * Create a detached, initialised MappingRecord
     */
    public MappingRecord(String id, Integer version, String name, String description, JSONB mapping, Instant created, String creator, Instant deleted, String sourcedatastandard) {
        super(Mapping.MAPPING);

        setId(id);
        setVersion(version);
        setName(name);
        setDescription(description);
        setMapping(mapping);
        setCreated(created);
        setCreator(creator);
        setDeleted(deleted);
        setSourcedatastandard(sourcedatastandard);
        resetChangedOnNotNull();
    }
}
