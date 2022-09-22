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
public class BanMigration2022_09_22_18_38 extends MySQLMigration {

    public BanMigration2022_09_22_18_38() {
        super(SQLTable.BAN_TABLE);
    }

    @Override
    protected long getDate() {
        return Date.of(18, 38, 22, 9, 2022);
    }

    @Override
    public String getOldFormat() {
        return null;
    }

    @Override
    public String getNewFormat() {
        return "Player VARCHAR(36) NOT NULL, Owner VARCHAR(36), Reason VARCHAR(128) NOT NULL, Time VARCHAR(64), CreationTime VARCHAR(64) NOT NULL, PRIMARY KEY (Player)";
    }

    @Override
    public PreparedStatement startBatch(Connection connection, String table) throws SQLException {
        return null;
    }

    @Override
    public void migrateBatch(PreparedStatement statement, ResultSet entry) throws SQLException {

    }
}
