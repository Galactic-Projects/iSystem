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
public class PlayerHistoryMigration2022_10_11_22_02 extends MySQLMigration {

    public PlayerHistoryMigration2022_10_11_22_02() {
        super(SQLTable.PLAYER_HISTORY);
    }

    @Override
    public String getOldFormat() {
        return null;
    }

    @Override
    public String getNewFormat() {
        return "ID INT AUTO_INCREMENT, Player VARCHAR(36) NOT NULL, Owner VARCHAR(36), Type VARCHAR(32), Reason VARCHAR(128) NOT NULL, Time VARCHAR(64), CreationTime VARCHAR(64) NOT NULL, PRIMARY KEY (ID)";
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
        return Date.of(22, 02, 11, 10, 2022);
    }
}
