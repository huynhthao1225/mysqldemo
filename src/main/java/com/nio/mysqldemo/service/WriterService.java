package com.nio.mysqldemo.service;

import com.nio.mysqldemo.file.FileWriter;
import org.springframework.beans.factory.InitializingBean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

abstract class WriterService implements Runnable, InitializingBean {

    private PipedInputStream pipedInputStream;
    private PipedOutputStream pipedOutputStream;
    private boolean closePipeSignal = false;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte delimiter;
    private String writerFileName;
    FileWriter fileWriter;

    public WriterService(String writerFileName) {
        this.writerFileName = writerFileName;
    }

    protected void initialize() throws IOException {
        pipedInputStream = new PipedInputStream();
        pipedOutputStream = new PipedOutputStream();
        pipedInputStream.connect(pipedOutputStream);
        byteArrayOutputStream = new ByteArrayOutputStream();
        String del = "|";
        byte[] bytes = del.getBytes();
        delimiter = bytes[0];
    }

    public void closePipeSignal() throws IOException {
        this.closePipeSignal = true;
        if (fileWriter != null) {
            fileWriter.closeFile();
        }
    }

    public PipedOutputStream getPipedOutputStream() {
        return pipedOutputStream;
    }

    protected void letGo() {
        byte readByte;
        byteArrayOutputStream = new ByteArrayOutputStream();
        while (!closePipeSignal) {
            try {
                if (pipedInputStream.available() > 0) {
                    readByte = (byte) pipedInputStream.read();
                    if (readByte != delimiter) {
                        byteArrayOutputStream.write(readByte);
                    } else {
                        String msg = byteArrayOutputStream.toString();
                        if (fileWriter == null) {
                            System.out.println(msg);
                        } else {
                            fileWriter.WriteData(msg);
                        }
                        byteArrayOutputStream.reset();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            pipedInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
