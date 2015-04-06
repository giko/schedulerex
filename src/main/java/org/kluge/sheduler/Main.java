package kluge.sheduler;

import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static List<Message> generateTestData() {
        List<Message> result = new LinkedList<Message>();

        Random random = new Random();
        for (int i = 0; i < 1000; ++i) {
            Message message = new Message();
            message.setExecutorId(random.nextInt(100));
            List<String> randomData = new LinkedList<String>();
            for (int dataIterator = 0; dataIterator < 1000; ++dataIterator) {
                randomData.add(UUID.randomUUID().toString());
            }
            message.setDataList(randomData);

            result.add(message);
        }

        return result;
    }

    public static void main(String[] args) throws ExecutionException {
        List<Message> data = generateTestData();

        Dispatcher dispatcher = new Dispatcher();

        ExecutorService clientsThreadPool = Executors.newCachedThreadPool();
        for (Message aData : data) {
            clientsThreadPool.submit(() -> dispatcher.dispatch(aData));
        }

        try {
            dispatcher.finish();
        } catch (InterruptedException e) {
            throw new RuntimeException("An error occurred!", e);
        }
    }
}
