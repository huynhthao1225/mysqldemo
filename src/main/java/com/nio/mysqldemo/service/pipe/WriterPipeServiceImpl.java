package com.nio.mysqldemo.service.pipe;

import com.nio.mysqldemo.file.FileWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Lazy
@Scope("prototype")
public class WriterPipeServiceImpl extends WriterPipeService {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
    }

    @Override
    public void run() {
        if (writerFileName != null) {
            fileWriter = applicationContext.getBean(FileWriter.class, writerFileName);
        }
        letGo();
    }
}
