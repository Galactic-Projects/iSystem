package net.galacticprojects.common.database.migration.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;

import me.lauriichan.laylib.logger.ISimpleLogger;
import me.lauriichan.minecraft.wildcard.migration.IMigrationManager;
import me.lauriichan.minecraft.wildcard.migration.MigrationTarget;
import me.lauriichan.minecraft.wildcard.migration.MigrationType;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.SQLTable;

public final class SQLMigrationType extends MigrationType<SQLDatabase, SQLMigration> {

    private static final long ROW_LIMIT = 500;

    private static final String CREATE_TABLE = "CREATE TABLE %s(%s)";
    private static final String RENAME_TABLE = "ALTER TABLE %s RENAME TO %s";
    private static final String SELECT_TABLE = "SELECT * FROM %s ";
    private static final String DROP_TABLE = "DROP TABLE %s";

    private static final Function<SQLTable, ArrayList<SQLMigration>> BUILDER = i -> new ArrayList<>();

    private final ISimpleLogger logger;

    public SQLMigrationType(final ISimpleLogger logger) {
        super(SQLDatabase.class, SQLMigration.class);
        this.logger = logger;
    }

    private EnumMap<SQLTable, ArrayList<SQLMigration>> collect(final IMigrationManager manager, final Class<?> source) {
        final EnumMap<SQLTable, ArrayList<SQLMigration>> migrations = new EnumMap<>(SQLTable.class);
        final List<MigrationTarget<SQLMigration>> targets = getTargets(manager);
        if (targets.isEmpty()) {
            return migrations;
        }
        for (int i = 0; i < targets.size(); i++) {
            final MigrationTarget<SQLMigration> target = targets.get(i);
            if (!target.getPoint().source().isAssignableFrom(source)) {
                continue;
            }
            final SQLMigration migration = target.getMigration();
            migrations.computeIfAbsent(migration.getTable(), BUILDER).add(migration);
        }
        return migrations;
    }

    @Override
    public void migrate(final IMigrationManager manager, final SQLDatabase source) throws Exception {
        final EnumMap<SQLTable, ArrayList<SQLMigration>> tableMigrations = collect(manager, source.getClass());
        if (tableMigrations.isEmpty()) {
            return;
        }
        loop:
        for (final SQLTable table : SQLTable.values()) {
            if (!tableMigrations.containsKey(table)) {
                return;
            }
            final ArrayList<SQLMigration> migrations = tableMigrations.get(table);
            if (migrations.isEmpty()) {
                return; // How?
            }
            Collections.sort(migrations);
            final SQLMigration first = migrations.get(0);
            try (Connection connection = source.getConnection()) {
                final State state = getFormatState(first, table.tableName(), first.getNewFormat(), connection, false);
                switch (state) {
                case LEGACY:
                    break;
                case NOT_AVAILABLE:
                    logger.info("Table '" + table.tableName() + "' doesn't exist, creating...");
                    final PreparedStatement statement = connection
                        .prepareStatement(String.format(CREATE_TABLE, table.tableName(), first.getNewFormat()));
                    statement.closeOnCompletion();
                    statement.executeUpdate();
                    logger.info("Table '" + table.tableName() + "' was successfully created!");
                    continue loop;
                default: // We're up2date, no migration required
                    logger.info("Table '" + table.tableName() + "' is up2date!");
                    continue loop;
                }
                logger.warning("Table '" + table.tableName() + "' has an old format, migrating...");
                // We're not up2date so we check for the oldest version which is compatible
                boolean migrate = false;
                for (int i = migrations.size() - 1; i >= 0; i--) {
                    final SQLMigration migration = migrations.get(i);
                    if (!migrate
                        && getFormatState(migration, table.tableName(), migration.getNewFormat(), connection, true) == State.UP2DATE) {
                        migrate = true; // Now we know which version is the oldest and can start to migrate to newer
                                        // ones
                        continue;
                    }
                    if (!migrate) {
                        if (i == 0) { // Force migrate oldest migration
                            i = migrations.size();
                            migrate = true;
                        }
                        continue;
                    }
                    if (!applyMigration(connection, source, migration)) {
                        throw new IllegalStateException("Failed to migrate database '" + source.getClass().getSimpleName() + "'!");
                    }
                }
            } catch (final SQLException e) {
                logger.error("Failed to migrate table '" + table.tableName() + "'", e);
            }
        }
    }

    private boolean applyMigration(final Connection connection, final SQLDatabase source, final SQLMigration migration)
        throws SQLException {
        final String table = migration.getTable().tableName();
        final String oldFormat = migration.getOldFormat();
        final String newFormat = migration.getNewFormat();
        final String tableLegacy = table + "_LEGACY";
        // Rename table
        PreparedStatement statement = connection.prepareStatement(String.format(RENAME_TABLE, table, tableLegacy));
        statement.execute();
        statement.close();
        // Create new table
        statement = connection.prepareStatement(String.format(CREATE_TABLE, table, newFormat));
        statement.executeUpdate();
        // Request table data
        final String selectLegacyTable = String.format(SELECT_TABLE, tableLegacy);
        try {
            logger.info("[" + migration.getId() + "] Migrated 0 entries of Table '" + table + "'...");
            long amount = 0;
            final PreparedStatement batch = migration.startBatch(connection, table);
            while (true) {
                statement = connection.prepareStatement(selectLegacyTable + migration.getLimit(amount, ROW_LIMIT));
                statement.closeOnCompletion();
                final ResultSet set = statement.executeQuery();
                int next = 0;
                batch.clearBatch();
                while (set.next()) {
                    migration.migrateBatch(batch, set);
                    next++;
                }
                if (!set.isClosed()) {
                    set.close();
                }
                if (next == 0) {
                    batch.close();
                    break;
                }
                batch.executeBatch();
                amount += next;
                logger.info("[" + migration.getId() + "] Migrated " + amount + " entries of Table '" + table + "'...");
            }
            logger.info("[" + migration.getId() + "] Migrated a total of " + amount + " entries of Table '" + table + "'!");
        } catch (final SQLException exp) {
            logger.error(
                "Failed to migrate table '" + table + "' from (" + oldFormat + ") to (" + newFormat + ") [" + migration.getId() + "]", exp);
            return false;
        }
        logger.warning("[" + migration.getId() + "] Migration of Table '" + table + "' was done successfully");
        logger.warning("[" + migration.getId() + "] Dropping old table '" + tableLegacy + "'!");
        try {
            statement = connection.prepareStatement(String.format(DROP_TABLE, tableLegacy));
            statement.execute();
            statement.close();
        } catch (final SQLException exp) {
        }
        logger.warning("[" + migration.getId() + "] Old table '" + tableLegacy + "' dropped successfully!");
        return true;
    }

    private State getFormatState(final SQLMigration migration, final String table, final String format, final Connection connection,
        final boolean flag) throws SQLException {
        final ResultSet set = migration.requestTableSql(table, connection);
        if (!set.next()) {
            return State.NOT_AVAILABLE;
        }
        final String tableFormat = extractFormat(migration.getFormat(set));
        if (!set.isClosed()) {
            set.close();
        }
        if (tableFormat.equals(format) == flag) {
            return State.LEGACY;
        }
        return State.UP2DATE;
    }

    private String extractFormat(String input) {
        return (input = input.split("\\(", 2)[1]).substring(0, input.length() - 1).trim();
    }

    private enum State {

        LEGACY,
        UP2DATE,
        NOT_AVAILABLE;

    }

}