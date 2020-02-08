package com.nio.mysqldemo.job;

import com.nio.mysqldemo.file.WriterFile;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Callable;

@Component
@Scope("prototype")
public class WriterTask implements Callable, InitializingBean {

    private PipedInputStream pipedInputStream;
    private PipedOutputStream pipedOutputStream;
    private boolean closePipeSignal = false;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte delimiter;
    private String writerFileName;


    @Autowired
    ApplicationContext applicationContext;

    public WriterTask(String writerFileName) {
        this.writerFileName = writerFileName;
    }

    @Override
    public Object call() {
        byte readByte;
        byteArrayOutputStream = new ByteArrayOutputStream();
        while (!closePipeSignal) {
            try {
                readByte = (byte) pipedInputStream.read();
                if (readByte != delimiter) {
                    byteArrayOutputStream.write(readByte);
                } else {
                    String msg = byteArrayOutputStream.toString();
                    System.out.println(msg);
                    writerFile.WriteData(msg);
                    byteArrayOutputStream.reset();
                }
                //Thread.sleep(10);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            pipedInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closePipeSignal() throws IOException {
        this.closePipeSignal = true;
        writerFile.closeFile();
    }

    public PipedOutputStream getPipedOutputStream() {
        return pipedOutputStream;
    }

    WriterFile writerFile;
    @Override
    public void afterPropertiesSet() throws Exception {
        pipedInputStream = new PipedInputStream();
        pipedOutputStream = new PipedOutputStream();
        pipedInputStream.connect(pipedOutputStream);
        byteArrayOutputStream = new ByteArrayOutputStream();
        String del = "|";
        byte[] bytes = del.getBytes();
        delimiter = bytes[0];
        writerFile = applicationContext.getBean(WriterFile.class, writerFileName);
    }
}
