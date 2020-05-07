package com.nio.mysqldemo.dao;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;

@Component
@Lazy
public class ActorDao {

    private String getAllSql = "SELECT actor_id, first_name, last_name, last_update FROM actor ORDER BY actor_id";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public SqlRowSet getAll() {

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(getAllSql);
        return sqlRowSet;

    }

}
