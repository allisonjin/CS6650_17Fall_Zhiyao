package edu.neu.zhiyao.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyClientMain {

    private static final String POST_RES_TEXT = "testPostText";
    private static final String EXPECTED_GET_RESP = "alive";
    private static final int EXPECTED_POST_RESP = POST_RES_TEXT.length();

    private final int nThreads;
    private final int nIterations;
    
    private final MyClient client;

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
        int nThreads = 10;
        int nIters = 100;
        String ip = "54.193.122.64";
        int port = 8080;
        if (args.length > 0) {
            try {
                nThreads = Integer.parseInt(args[0]);
                nIters = Integer.parseInt(args[1]);
                ip = args[2];
                port = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            }
        }
        MyClient client = new MyClient(ip, port);
        MyClientMain instance = new MyClientMain(nThreads, nIters, client);
        instance.runTask();
    }

}
