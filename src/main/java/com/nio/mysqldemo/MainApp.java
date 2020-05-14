package com.nio.mysqldemo;

import com.nio.mysqldemo.job.WriterConsoleAndFile;
import com.nio.mysqldemo.service.pipe.WriterPipeServiceToConsoleAndFile;
import com.nio.mysqldemo.service.queue.WriterQueueServiceImpl;
import com.nio.mysqldemo.service.queue.WriterQueueServiceToConsoleAndFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public class MainApp implements CommandLineRunner {

    @Autowired
    private WriterConsoleAndFile writerConsoleAndFile;
    @Autowired
    @Qualifier("fixedThreadPool")
    private ExecutorService executorService;

    @Autowired
    private WriterPipeServiceToConsoleAndFile writerPipeServiceToConsoleAndFile;

    @Autowired private WriterQueueServiceToConsoleAndFile writerQueueServiceToConsoleAndFile;

    @Override
    public void run(String... args) throws Exception {

        writerConsoleAndFile.letDoIt();
        writerPipeServiceToConsoleAndFile.letDoIt();
        writerQueueServiceToConsoleAndFile.letDoIt();
        executorService.shutdown();

    }

}
