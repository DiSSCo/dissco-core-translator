/*
 * This file is generated by jOOQ.
 */
package eu.dissco.core.translator.database.jooq;


import eu.dissco.core.translator.database.jooq.tables.NewMapping;
import eu.dissco.core.translator.database.jooq.tables.NewSourceSystem;
import java.util.Arrays;
import java.util.List;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.new_mapping</code>.
     */
    public final NewMapping NEW_MAPPING = NewMapping.NEW_MAPPING;

    /**
     * The table <code>public.new_source_system</code>.
     */
    public final NewSourceSystem NEW_SOURCE_SYSTEM = NewSourceSystem.NEW_SOURCE_SYSTEM;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            NewMapping.NEW_MAPPING,
            NewSourceSystem.NEW_SOURCE_SYSTEM);
    }
}
