package com.nio.mysqldemo.file;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.*;

@Component
@Scope("prototype")
@Lazy
public class WriterFile implements InitializingBean {
    private File outFile;
    private FileWriter fileWriter;
    private String fileName;

    public WriterFile(String fileName) {
        this.fileName = fileName;
    }

    public void WriteData(String value) throws IOException {
        fileWriter.write(value);
        fileWriter.write("\n");
    }
    public void closeFile() throws IOException {
        if (fileWriter != null) {
            fileWriter.flush();
            fileWriter.close();
            System.out.println("closing file " + fileName);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        outFile = new File(fileName);
        outFile.createNewFile();
        fileWriter = new java.io.FileWriter(outFile);
    }
}
