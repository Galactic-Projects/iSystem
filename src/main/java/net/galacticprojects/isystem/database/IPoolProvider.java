package net.galacticprojects.isystem.database;

import com.zaxxer.hikari.pool.HikariPool;

public interface IPoolProvider {

    HikariPool createPool();

}
