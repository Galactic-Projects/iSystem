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
public class ReportMigration2022_10_23_20_04 extends MySQLMigration {

    public ReportMigration2022_10_23_20_04() {
        super(SQLTable.REPORT_TABLE);
    }

    @Override
    public String getOldFormat() {
        return null;
    }


    @Override
    public String getNewFormat() {
        return "ID INT AUTO_INCREMENT, UUID VARCHAR(36), CREATOR VARCHAR(64), REASON VARCHAR(128), STATUS BOOLEAN DEFAULT FALSE, TIMESTAMP VARCHAR(128) NOT NULL, PRIMARY KEY (ID)";
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
        return Date.of(20, 4,23,10,2022);
    }
}
