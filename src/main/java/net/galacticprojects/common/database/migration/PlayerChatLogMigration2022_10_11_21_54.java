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
public class PlayerChatLogMigration2022_10_11_21_54 extends MySQLMigration {

    public PlayerChatLogMigration2022_10_11_21_54() {
        super(SQLTable.PLAYER_CHATLOG);
    }

    @Override
    public String getOldFormat() {
        return null;
    }

    @Override
    public String getNewFormat() {
        return "ID INT AUTO_INCREMENT, UUID VARCHAR(36) NOT NULL, NAME VARCHAR(18) NOT NULL, IP VARCHAR(16) NOT NULL, SERVER VARCHAR(32) NOT NULL, TIMESTAMP VARCHAR(64) NOT NULL, MESSAGE LONGTEXT, PRIMARY KEY (ID)";
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
        return Date.of(21, 54, 11, 10, 2022);
    }
}
