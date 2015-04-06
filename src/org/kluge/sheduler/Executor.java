package org.kluge.sheduler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

/**
 * Created by giko on 4/6/2015.
 */
public class Executor implements Runnable {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        if (message == null) {
            throw new NullPointerException("Message is null");
        }
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(Message.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(message, new File(message.getId().toString()));
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new RuntimeException("Marhsalling Exception!");
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
