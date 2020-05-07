package com.nio.mysqldemo.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nio.mysqldemo.dao.ActorDao;
import com.nio.mysqldemo.model.Actor;
import com.nio.mysqldemo.service.WriterServiceToConsoleAndFile;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Lazy
public class WriterConsoleAndFile implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier("fixedThreadPool")
    private ExecutorService executorService;

    private WriterToConsole writerToConsole;
    private ObjectMapper objectMapper;
    private WriterToFile writerToFile;

    public void letDoIt() throws IOException, InterruptedException {
        System.out.println(String.format("***** START %s *****",  this.getClass().getSimpleName()));
        ActorDao actorDao = applicationContext.getBean(ActorDao.class);
        SqlRowSet sqlRowSet = actorDao.getAll();
        Actor actor = new Actor();
        String json;
        PipedOutputStream pipedOutputStreamToFile = writerToFile.getPipedOutputStream();
        PipedOutputStream pipedOutputStreamToConsole = writerToConsole.getPipedOutputStream();
        executorService.submit(writerToFile);
        executorService.submit(writerToConsole);
        while (sqlRowSet.next()) {
            actor.setID(sqlRowSet.getInt(1));
            actor.setFirstName(sqlRowSet.getString(2));
            actor.setLastName(sqlRowSet.getString(3));
            actor.setLastUpdate(sqlRowSet.getTimestamp(4));
            try {
                json = toJson(actor);
                pipedOutputStreamToFile.write(json.getBytes());
                pipedOutputStreamToFile.flush();
                pipedOutputStreamToConsole.write(json.getBytes());
                pipedOutputStreamToConsole.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pipedOutputStreamToConsole.close();
        pipedOutputStreamToFile.close();

        System.out.println("I am about to signal end pipeOutputStream");
        writerToFile.closePipeSignal();
        writerToConsole.closePipeSignal();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println(String.format("***** END %s *****",  this.getClass().getSimpleName()));
    }

    private String toJson(Actor actor) {

        try {
            return String.format("%s|",objectMapper.writeValueAsString(actor));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        objectMapper = new ObjectMapper();
        writerToConsole = applicationContext.getBean(WriterToConsole.class);
        writerToFile = applicationContext.getBean(WriterToFile.class,"testfile1.dat");

    }
}
