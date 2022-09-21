package net.galacticprojects.common.database;

import com.zaxxer.hikari.pool.HikariPool;

public interface IPoolProvider {

    HikariPool createPool();

}
