package net.galacticprojects.common.database.migration.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.galacticprojects.common.database.SQLTable;

public abstract class MySQLMigration extends SQLMigration {

    private static final String TEST_EXISTENCE = "SHOW TABLES LIKE '%s'";
    private static final String SELECT_TABLE = "SHOW CREATE TABLE %s";
    private static final String LIMIT_FORMAT = "LIMIT %s, %s";

    public MySQLMigration(final SQLTable table) {
        super(table);
    }

    @Override
    public ResultSet requestTableSql(final String table, final Connection connection) throws SQLException {
        final ResultSet set = connection.prepareStatement(String.format(TEST_EXISTENCE, table)).executeQuery();
        if (!set.next()) {
            return set;
        }
        return connection.prepareStatement(String.format(SELECT_TABLE, table)).executeQuery();
    }

    @Override
    public String getFormat(final ResultSet set) throws SQLException {
        String string = set.getString(2).replace("`", "").replace("\r", "");
        String[] lines = string.split("\n");
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < lines.length; index++) {
            String line = lines[index];
            if (line.isBlank() || (index + 1 == lines.length)) {
                continue;
            }
            String[] parts = line.trim().split(" ");
            if (parts[1].endsWith(")") && !parts[1].startsWith("(")) {
                parts[1] = parts[1].toUpperCase();
            }
            for (int idx = 0; idx < parts.length; idx++) {
                builder.append(parts[idx]);
                if (idx + 1 != parts.length) {
                    builder.append(' ');
                }
            }
            if (index + 2 != lines.length) {
                builder.append(' ');
            }
        }
        return builder.append(')').toString();
    }

    @Override
    public String getLimit(final long offset, final long limit) {
        return String.format(LIMIT_FORMAT, offset, limit);
    }

}