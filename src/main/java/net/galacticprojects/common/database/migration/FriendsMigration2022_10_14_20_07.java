package net.galacticprojects.common.database.migration;

import me.lauriichan.minecraft.wildcard.migration.Date;
import me.lauriichan.minecraft.wildcard.migration.Migration;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.SQLTable;
import net.galacticprojects.common.database.migration.impl.MySQLMigration;
import net.galacticprojects.common.database.migration.impl.SQLMigration;
import net.galacticprojects.common.database.migration.impl.SQLMigrationType;
import net.galacticprojects.common.database.model.Friends;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

@Migration(source = SQLDatabase.class, type = SQLMigrationType.class)
public class FriendsMigration2022_10_14_20_07 extends MySQLMigration {

    public FriendsMigration2022_10_14_20_07() {
        super(SQLTable.FRIENDS_TABLE);
    }

    @Override
    protected long getDate() {
        return Date.of(20, 11, 14, 10, 2022);
    }

    @Override
    public String getOldFormat() {
        return null;
    }

    @Override
    public String getNewFormat() {
        return "ID INT AUTO_INCREMENT PRIMARY KEY, UUID VARCHAR(36), FRIENDUUID VARCHAR(36), DATE VARCHAR(36)";
    }

    @Override
    public PreparedStatement startBatch(Connection connection, String table) throws SQLException {
        return null;
    }

    @Override
    public void migrateBatch(PreparedStatement statement, ResultSet entry) throws SQLException {

    }
}
