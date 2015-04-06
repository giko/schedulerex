package kluge.sheduler;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by giko on 4/6/2015.
 */
public class Dispatcher {
    private Integer messagesDispatched = 0;
    protected List<Executor> executors = new ArrayList<>();
    protected final Deque<Message> messages = new ConcurrentLinkedDeque<>();
    protected Set<Integer> freeExecutors = Collections.synchronizedSet(new HashSet<>());

    ExecutorService executorService = Executors.newFixedThreadPool(110);

    public Dispatcher() {
        for (int i = 0; i < 100; ++i) {
            executors.add(new Executor(i, new OnExecutorFinishAction(i, this)));
            freeExecutors.add(i);
        }
        executorService.execute(getDispatcherRunnable());
    }
    
    int a =0;

    public class OnExecutorFinishAction {
        private Integer executorId;
        private Dispatcher dispatcher;

        public OnExecutorFinishAction(Integer executorId, Dispatcher dispatcher) {
            this.executorId = executorId;
            this.dispatcher = dispatcher;
        }

        protected void finish() {
            ++dispatcher.a;
            dispatcher.freeExecutors.add(executorId);
        }
    }

    public int dispatch(Message message) {
        int messageId;
        synchronized (this) {
            messageId = messagesDispatched;
            ++messagesDispatched;
        }

        message.setId(messageId);
        synchronized (messages) {
            messages.add(message);
        }
        
        return messageId;
    }

    public void finish() throws InterruptedException {
        executorService.awaitTermination(35, TimeUnit.SECONDS);
    }

    protected Runnable getDispatcherRunnable() {
        return () -> {
            while (true) {
                if (!messages.isEmpty()) {
                    synchronized (messages) {
                        Message message = messages.peekLast();
                        if (!freeExecutors.contains(message.getExecutorId())) {
                            messages.addFirst(message);
                            messages.removeLast();
                            continue;
                        }
                        Executor executor = executors.get(message.getExecutorId());
                        executor.setMessage(message);
                        freeExecutors.remove(message.getExecutorId());
                        executorService.submit(executor);
                        messages.removeLast();
                        System.out.println("Dispatching " + message.getId() + ", for executor " + message.getExecutorId() + ", " + messages.size() + " ahead");
                    }
                } else {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException("An error occurred!", e);
                    }
                }
            }
        };
    }
}
