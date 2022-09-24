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
public class BanMigration2022_09_23_18_20 extends MySQLMigration {

    public BanMigration2022_09_23_18_20() {
        super(SQLTable.BAN_TABLE);
    }

    @Override
    protected long getDate() {
        return Date.of(18, 20, 23, 9, 2022);
    }

    @Override
    public String getOldFormat() {
        return "Player VARCHAR(36) NOT NULL, Owner VARCHAR(36), Reason VARCHAR(128) NOT NULL, Time VARCHAR(64), CreationTime VARCHAR(64) NOT NULL, PRIMARY KEY (Player)";
    }

    @Override
    public String getNewFormat() {
        return "Player VARCHAR(36) NOT NULL, Owner VARCHAR(36), Id VARCHAR(3), Reason VARCHAR(128) NOT NULL, Time VARCHAR(64), CreationTime VARCHAR(64) NOT NULL, PRIMARY KEY (Player)";
    }

    @Override
    public PreparedStatement startBatch(Connection connection, String table) throws SQLException {
        return connection.prepareStatement(SQLDatabase.INSERT_PLAYER);
    }

    @Override
    public void migrateBatch(PreparedStatement statement, ResultSet entry) throws SQLException {
    statement.setString(1, entry.getString(1));
    statement.setString(2, entry.getString(2));
    statement.setInt(3, entry.getInt(6));
    statement.setString(4, entry.getString(3));
    statement.setString(5, entry.getString(4));
    statement.setString(6, entry.getString(5));
    }
}
