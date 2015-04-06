package org.kluge.sheduler;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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

            result.add(message);
        }

        return result;
    }

    public static void main(String[] args) {
        List<Message> data = generateTestData();
    }
}
