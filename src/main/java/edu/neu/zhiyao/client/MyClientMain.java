package edu.neu.zhiyao.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MyClientMain {

    private static final String POST_RES_TEXT = "testPostText";
    private static final String EXPECTED_GET_RESP = "alive";
    private static final int EXPECTED_POST_RESP = POST_RES_TEXT.length();

    private int nThreads;
    private int nIterations;
    
    private MyClient client;

    public MyClientMain(MyClient client) {
        this(10, 100, client);
    }

    public MyClientMain(int nThreads, int nIterations, MyClient client) {
        this.nThreads = nThreads;
        this.nIterations = nIterations;
        this.client = client;
    }

    public void runTask() {
        final SynchronizedCounter counter = new SynchronizedCounter();
        long startTime = System.currentTimeMillis();
        System.out.println("Client starting... Time: " + startTime);
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < nIterations; j++) {
                        String getResp = client.getStatus();
                        counter.reqIncrement();
                        if (getResp.equals(EXPECTED_GET_RESP)) {
                            counter.respIncrement();
                        }
                        Integer postResp = client.postText(POST_RES_TEXT, Integer.class);
                        counter.reqIncrement();
                        if (postResp.equals(EXPECTED_POST_RESP)) {
                            counter.respIncrement();
                        }
                    }
                }
            });
        }
        System.out.println("All threads running...");
        executor.shutdown();
        while (!executor.isTerminated());
        long endTime = System.currentTimeMillis();
        System.out.println("All threads complete... Time: " + endTime);
        System.out.println("Total number of requests sent: "
                + counter.getRespCnt());
        System.out.println("Total number of successful responses: "
                + counter.getRespCnt());
        double wallTime = (endTime - startTime) / 1000.0;
        System.out.format("Test wall time: %.1f seconds\n", wallTime);
    }

    public static void main(String[] args) {
        MyClientMain instance = null;
        try {
            int nThreads = Integer.parseInt(args[0]);
            int nIters = Integer.parseInt(args[1]);
            String ip = args[2];
            int port = Integer.parseInt(args[3]);
            MyClient client = new MyClient(ip, port);
            instance = new MyClientMain(nThreads, nIters, client);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
        if (instance != null) {
            instance.runTask();
        }
    }

}
