package net.galacticprojects.common.database.migration;

import me.lauriichan.minecraft.wildcard.migration.Date;
import me.lauriichan.minecraft.wildcard.migration.Migration;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.SQLTable;
import net.galacticprojects.common.database.migration.impl.MySQLMigration;
import net.galacticprojects.common.database.migration.impl.SQLMigrationType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Migration(source = SQLDatabase.class, type = SQLMigrationType.class)
public class BanMigration2022_10_11_21_48 extends MySQLMigration {

    public BanMigration2022_10_11_21_48() {
        super(SQLTable.BAN_TABLE);
    }

    @Override
    public String getOldFormat() {
        return null;
    }

    @Override
    public String getNewFormat() {
        return "ID INT AUTO_INCREMENT, UUID VARCHAR(36) NOT NULL, STAFF VARCHAR(36), REASON VARCHAR(128) NOT NULL, TIME VARCHAR(64), CREATIONTIME VARCHAR(64) NOT NULL, PRIMARY KEY (ID)";
    }

    @Override
    public PreparedStatement startBatch(Connection connection, String table) throws SQLException {
        return null;
    }

    @Override
    public void migrateBatch(PreparedStatement statement, ResultSet entry) throws SQLException {

    }

    @Override
    protected long getDate() {
        return Date.of(21, 48, 11, 10, 2022);
    }
}
