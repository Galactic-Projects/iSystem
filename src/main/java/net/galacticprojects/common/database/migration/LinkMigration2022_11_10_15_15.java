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
public class LinkMigration2022_11_10_15_15 extends MySQLMigration {

    public LinkMigration2022_11_10_15_15() {
        super(SQLTable.LINK_TABLE);
    }

    @Override
    public String getOldFormat() {
        return null;
    }


    @Override
    public String getNewFormat() {
        return "ID INT AUTO_INCREMENT, UUID VARCHAR(36), DISCORDTAG VARCHAR(64), DISCORDTIME VARCHAR(128), DISCORDBOOL BOOLEAN DEFAULT FALSE, TSIDENTIFIER VARCHAR(64), TSIP VARCHAR(128), TSTIME VARCHAR(128), TSBOOL BOOLEAN DEFAULT FALSE, PRIMARY KEY (ID)";
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
        return Date.of(15, 15,10,11,2022);
    }
}
