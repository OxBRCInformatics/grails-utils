package uk.ac.ox.ndm.utils.grails.hibernate

import org.grails.orm.hibernate.cfg.HibernateMappingContextConfiguration

/**
 * @since 12/10/2015
 */
public class GormConfiguration extends HibernateMappingContextConfiguration {
    public static final long serialVersionUID = 1

    private boolean _alreadyProcessed
    /*
        @Override
        protected void secondPassCompile() throws MappingException {
            super.secondPassCompile()

            if (_alreadyProcessed) return

            checkColumnNameLengths()
            checkSchemaNames()


            _alreadyProcessed = true
        }

        void renameForeignKeys() {
            List<ForeignKey> warnings = []
            classes.values().each {PersistentClass pc ->
                pc.table.foreignKeyIterator.each {ForeignKey key ->

                    String reducedName = key.referencedTable.name
                            .replaceFirst(/_/, '__')
                            .replaceAll(/_([a-z])[a-z]* /, '$1')

                    key.name = "FK_${key.columns.first().name}__$reducedName"
                    if (key.name.size() >= 64) warnings += key

                }
            }
            warnings.each {ForeignKey key ->
                logger.warn "Foreign key name ${key.name} is longer than 64 bytes, it will be truncated to ${key.name[0..63]}"
            }
        }

        private static final Logger logger = LoggerFactory.getLogger(GormConfiguration)

        void checkColumnNameLengths() {
            List<Column> warnings = []
            classes.values().each {PersistentClass pc ->
                for (int i = 0; i < pc.table.columnSpan; i++) {
                    Column col = pc.table.getColumn(i)
                    if (col.name.size() >= 64) warnings += col
                }
            }

            warnings.each {Column col ->
                logger.warn "Column name ${col.name} is longer than 64 bytes, it will be truncated to ${col.name[0..63]}"
            }
        }

        void checkSchemaNames() {
            List<PersistentClass> warnings = []
            classes.values().each {PersistentClass pc ->

                String expected = StandardNamingStrategy.INSTANCE.canonicalNameToSchemaName(pc.className)

                if (pc.table.schema != expected) {
                    warnings += pc
                    pc.table.schema = expected
                }
            }

            warnings.each {PersistentClass pc ->
                logger.warn 'Schema for domain {} has been set to {}', pc.className, pc.table.schema

            }
        }
    */
}


