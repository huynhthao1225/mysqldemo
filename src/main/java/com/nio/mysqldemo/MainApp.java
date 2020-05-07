package com.nio.mysqldemo;

import com.nio.mysqldemo.job.WriterConsoleAndFile;
import com.nio.mysqldemo.service.WriterServiceToConsoleAndFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MainApp implements CommandLineRunner {


   // @Autowired
  //  private WriterConsoleAndFile writerConsoleAndFile;

    @Autowired
    private WriterServiceToConsoleAndFile writerServiceToConsoleAndFile;

    @Override
    public void run(String... args) throws Exception {

      //  writerConsoleAndFile.letDoIt();
        writerServiceToConsoleAndFile.letDoIt();

    }

}
