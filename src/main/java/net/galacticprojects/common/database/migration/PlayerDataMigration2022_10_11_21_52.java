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
public class PlayerDataMigration2022_10_11_21_52 extends MySQLMigration {

    public PlayerDataMigration2022_10_11_21_52() {
        super(SQLTable.PLAYER_TABLE);
    }

    @Override
    public String getOldFormat() {
        return null;
    }

    @Override
    public String getNewFormat() {
        return "ID INT AUTO_INCREMENT, UUID VARCHAR(36), IP VARCHAR(24) NOT NULL, COINS VARCHAR(64) DEFAULT '0', LEVEL VARCHAR(64) DEFAULT '0', LANGUAGE VARCHAR(12), ONLINETIME LONGTEXT, VERIFIED VARCHAR(12), PRIMARY KEY (ID)";
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
        return Date.of(21, 52, 11, 10, 2022);
    }
}
