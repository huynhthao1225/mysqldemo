package com.nio.mysqldemo.service;

import com.nio.mysqldemo.file.FileWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Lazy
@Scope("prototype")
public class WriterServiceImpl extends WriterService {

    @Autowired
    ApplicationContext applicationContext;

    public WriterServiceImpl(String fileName) {
        super(fileName);
        if (fileName != null) {
            fileWriter = applicationContext.getBean(FileWriter.class, fileName);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
    }

    @Override
    public void run() {
        letGo();
    }
}
