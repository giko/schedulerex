package org.kluge.sheduler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by giko on 4/6/2015.
 */
public class Dispatcher {
    private Integer messagesDispatched = 0;
    protected List<Thread> executors = new ArrayList<>();
    protected Queue<Message> messages = new LinkedList<>();

    public Dispatcher() {
        for (int i=0; i<100;++i) {
            executors.add(new Thread(new Executor()));
        }

        Thread dispathcerThread = new Thread(() -> {

        });
    }

    public int dispatch(Message message) {
        int messageId;
        synchronized (messagesDispatched) {
            messageId = messagesDispatched;
        }

        message.setId(messageId);
        messages.add(message);
        return messageId;
    }
}
