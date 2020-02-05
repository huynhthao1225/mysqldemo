package com.nio.mysqldemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nio.mysqldemo.dao.ActorDao;
import com.nio.mysqldemo.job.WriterJob;
import com.nio.mysqldemo.model.Actor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class MainApp implements CommandLineRunner, InitializingBean {

    @Autowired
    DataSource dataSource;

    @Autowired
    ApplicationContext applicationContext;

    WriterJob writeThread = null;

    @Override
    public void run(String... args) throws Exception {
        Connection con = null;
        System.out.println("I am here ...");
        try {
            con = dataSource.getConnection();
            System.out.println("I got connection ...");
            printActors();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    private void printActors() throws IOException {

        ActorDao actorDao = applicationContext.getBean(ActorDao.class);
        SqlRowSet sqlRowSet = actorDao.getAll();
        Actor actor = new Actor();
        String json;
        Thread writerJob = new Thread(writeThread);
        writerJob.start();

        while (sqlRowSet.next()) {
            actor.setID(sqlRowSet.getInt(1));
            actor.setFirstName(sqlRowSet.getString(2));
            actor.setLastName(sqlRowSet.getString(3));
            actor.setLastUpdate(sqlRowSet.getTimestamp(4));
            try {
                json = toJson(actor);
                pipedOutputStream.write(json.getBytes());
                pipedOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        pipedOutputStream.close();
        System.out.println("I am about to signal end pipeOutputStream");
        writeThread.closePipeSignal();
    }

    private String toJson(Actor actor) {

        try {
            return String.format("%s|",Obj.writeValueAsString(actor));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    ObjectMapper Obj;
    PipedOutputStream pipedOutputStream;

    @Override
    public void afterPropertiesSet() throws Exception {
        Obj = new ObjectMapper();
        writeThread = applicationContext.getBean(WriterJob.class);
        pipedOutputStream = writeThread.getPipedOutputStream();
    }
}
