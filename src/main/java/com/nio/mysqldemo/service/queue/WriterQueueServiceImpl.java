package com.nio.mysqldemo.service.queue;

import com.nio.mysqldemo.file.FileWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class WriterQueueServiceImpl extends WriterQueueService {
    @Autowired
    private ApplicationContext applicationContext;
    @Override
    public void run() {
        if (writerFileName != null) {
            fileWriter = applicationContext.getBean(FileWriter.class, writerFileName);
        }
        letGo();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initialize();

    }
}
