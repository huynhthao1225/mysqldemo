package com.nio.mysqldemo.job;

import org.springframework.beans.factory.InitializingBean;
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

    public void closePipeSignal() {
        this.closePipeSignal = true;
    }

    public PipedOutputStream getPipedOutputStream() {
        return pipedOutputStream;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pipedInputStream = new PipedInputStream();
        pipedOutputStream = new PipedOutputStream();
        pipedInputStream.connect(pipedOutputStream);
        byteArrayOutputStream = new ByteArrayOutputStream();
        String del = "|";
        byte[] bytes = del.getBytes();
        delimiter = bytes[0];
    }
}
