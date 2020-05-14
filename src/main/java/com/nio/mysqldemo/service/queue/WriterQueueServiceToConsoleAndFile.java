package com.nio.mysqldemo.service.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nio.mysqldemo.dao.ActorDao;
import com.nio.mysqldemo.model.Actor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Lazy
public class WriterQueueServiceToConsoleAndFile implements InitializingBean {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier("fixedThreadPool")
    private ExecutorService executorService;

    private WriterQueueServiceImpl writerToFile;
    private WriterQueueServiceImpl writerToConsole;
    private ObjectMapper objectMapper;
    private BlockingDeque<String> fileQueue;
    private BlockingDeque<String> consoleQueue;
    private final static String END = "END";

    @Override
    public void afterPropertiesSet() throws Exception {

        String fileName = "testFile_queue.dat";
        objectMapper = new ObjectMapper();
        writerToConsole = applicationContext.getBean(WriterQueueServiceImpl.class);
        writerToFile = applicationContext.getBean(WriterQueueServiceImpl.class);
        writerToFile.setWriterFileName(fileName);
        consoleQueue = (BlockingDeque<String>) writerToConsole.getQueue();
        fileQueue = (BlockingDeque) writerToFile.getQueue();
    }

    public void letDoIt() throws InterruptedException {

        System.out.println(String.format("***** START %s *****", WriterQueueServiceToConsoleAndFile.class.getSimpleName()));
        ActorDao actorDao = applicationContext.getBean(ActorDao.class);
        SqlRowSet sqlRowSet = actorDao.getAll();
        Actor actor = new Actor();
        String json;

        executorService.submit(writerToFile);
        executorService.submit(writerToConsole);
        while (sqlRowSet.next()) {
            actor.setID(sqlRowSet.getInt(1));
            actor.setFirstName(sqlRowSet.getString(2));
            actor.setLastName(sqlRowSet.getString(3));
            actor.setLastUpdate(sqlRowSet.getTimestamp(4));
            json = toJson(actor);
            consoleQueue.add(json);
            fileQueue.add(json);
        }

        consoleQueue.add(END);
        fileQueue.add(END);

        System.out.println("I am about to signal end blockQueue job");

        executorService.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println(String.format("***** END %s *****", WriterQueueServiceToConsoleAndFile.class.getSimpleName()));
    }


    private String toJson(Actor actor) {
        try {
            return String.format("%s|",objectMapper.writeValueAsString(actor));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
