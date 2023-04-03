package net.galacticprojects.common.database.migration;

import me.lauriichan.minecraft.wildcard.migration.Date;
import me.lauriichan.minecraft.wildcard.migration.Migration;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.SQLTable;
import net.galacticprojects.common.database.migration.impl.MySQLMigration;
import net.galacticprojects.common.database.migration.impl.SQLMigrationType;

import java.sql.*;

@Migration(source = SQLDatabase.class, type = SQLMigrationType.class)
public class FriendsMigration2023_04_03_16_38 extends MySQLMigration {

    public FriendsMigration2023_04_03_16_38() {
        super(SQLTable.FRIENDS_TABLE);
    }

    @Override
    protected long getDate() {
        return Date.of(16, 38, 3, 4, 2023);
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
