package kluge.sheduler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;

/**
 * Created by giko on 4/6/2015.
 */
public class Executor implements Runnable {
    private Message message;
    private Integer id;
    protected Dispatcher.OnExecutorFinishAction onExecutorFinishAction;

    public Executor(Integer id, Dispatcher.OnExecutorFinishAction onFinish) {
        this.id = id;
        this.onExecutorFinishAction = onFinish;
    }

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
            File file = new File("data/" + id.toString());
            file.mkdirs();
            file = new File("data/" + id.toString() + "/" + message.getId().toString());
            file.createNewFile();

            marshaller.marshal(message, file);
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new RuntimeException("Marhsalling Exception!", e);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred!", e);
        }

        try {
            Thread.sleep(3000);
            onExecutorFinishAction.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
