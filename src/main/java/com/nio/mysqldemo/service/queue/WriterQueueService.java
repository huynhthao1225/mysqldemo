package com.nio.mysqldemo.service.queue;

import com.nio.mysqldemo.file.FileWriter;
import org.springframework.beans.factory.InitializingBean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

abstract class WriterQueueService implements Runnable, InitializingBean {


    private boolean moreInputSignal = true;

    private byte delimiter;
    private BlockingQueue<String> queue;
    protected String writerFileName;
    protected FileWriter fileWriter;
    private final static String END = "END";

    public void setWriterFileName(String writerFileName) {
        this.writerFileName = writerFileName;
    }

    protected void initialize() throws IOException {
        queue = new LinkedBlockingDeque<String>();
    }

    public BlockingQueue<String> getQueue() {
        return queue;
    }

    protected void letGo() {

        while (true) {
            try {
                String msg = queue.take();
                if (msg.equals(END)) {
                    return;
                }
                if (fileWriter == null) {
                    System.out.println(msg);
                } else {
                    fileWriter.WriteData(msg);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
