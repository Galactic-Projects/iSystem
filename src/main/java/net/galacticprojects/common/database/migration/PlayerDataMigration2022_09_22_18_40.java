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
public class PlayerDataMigration2022_09_22_18_40 extends MySQLMigration {

    public PlayerDataMigration2022_09_22_18_40() {
        super(SQLTable.PLAYER_TABLE);
    }

    @Override
    protected long getDate() {
        return Date.of(18, 42, 22, 9, 2022);
    }

    @Override
    public String getOldFormat() {
        return null;
    }

    @Override
    public String getNewFormat() {
        return "Player VARCHAR(36), Ip VARCHAR(21) NOT NULL, Coins BIGINT DEFAULT 0, Level INT DEFAULT 0, Language VARCHAR(10), OnlineTime LONG DEFAULT 0, CONSTRAINT UUPlayer PRIMARY KEY (Player)";
    }

    @Override
    public PreparedStatement startBatch(Connection connection, String table) throws SQLException {
        return null;
    }

    @Override
    public void migrateBatch(PreparedStatement statement, ResultSet entry) throws SQLException {

    }
}
