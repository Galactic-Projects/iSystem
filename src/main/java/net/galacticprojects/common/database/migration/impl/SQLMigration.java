package net.galacticprojects.common.database.migration.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.lauriichan.minecraft.wildcard.migration.MigrationProvider;
import net.galacticprojects.common.database.SQLTable;

public abstract class SQLMigration extends MigrationProvider {

    private final SQLTable table;

    public SQLMigration(final SQLTable table) {
        this.table = table;
    }

    public final SQLTable getTable() {
        return table;
    }

    public abstract String getOldFormat();

    public abstract String getNewFormat();

    public abstract String getLimit(long offset, long limit);

    public abstract String getFormat(ResultSet set) throws SQLException;

    public abstract ResultSet requestTableSql(String table, Connection connection) throws SQLException;

    public abstract PreparedStatement startBatch(Connection connection, String table) throws SQLException;

    public abstract void migrateBatch(PreparedStatement statement, ResultSet entry) throws SQLException;

}