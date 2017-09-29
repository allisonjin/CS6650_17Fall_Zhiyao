package edu.neu.zhiyao.client;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyClientMain2 {

    private static final String POST_RES_TEXT = "testPostText";
    private static final String EXPECTED_GET_RESP = "alive";
    private static final int EXPECTED_POST_RESP = POST_RES_TEXT.length();

    private final int nThreads;
    private final int nIterations;
    
    private final MyClient client;

    public MyClientMain2(int nThreads, int nIterations, MyClient client) {
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
                        long reqStartTime = System.currentTimeMillis();
                        String getResp = client.getStatus();
                        long reqEndTime = System.currentTimeMillis();
                        counter.addLatency(elapsedTime(reqStartTime, reqEndTime));
                        counter.reqIncrement();
                        if (getResp.equals(EXPECTED_GET_RESP)) {
                            counter.respIncrement();
                        }
                    }
                    for (int j = 0;j < nIterations; j++) {
                        long reqStartTime = System.currentTimeMillis();
                        Integer postResp = client.postText(POST_RES_TEXT, Integer.class);
                        long reqEndTime = System.currentTimeMillis();
                        counter.addLatency(elapsedTime(reqStartTime, reqEndTime));
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
        double wallTime = elapsedTime(startTime, endTime);
        System.out.format("Test wall time: %.1f seconds\n", wallTime);
        System.out.println("Number of latency: " + counter.getLatencies().size());
        
        processLatencies(counter.getLatencies());
    }
    
    private double elapsedTime(long startTime, long endTime) {
        return (endTime - startTime) / 1000.0;
    }

    private void processLatencies(List<Double> latencyList) {
        double[] latencies = new double[latencyList.size()];
        for (int i = 0; i < latencyList.size(); i++) {
            latencies[i] = latencyList.get(i);
        }
        System.out.format("Mean lantency: %.3f seconds\n", mean(latencies));
        System.out.format("Median latency: %.3f seconds\n", median(latencies));
        System.out.format("99th percentile latency: %.3f seconds\n", 
                percentile(latencies, 99));
        System.out.format("95th percentile latency: %.3f seconds\n", 
                percentile(latencies, 95));
    }
    
    private double mean(double[] values) {
        double ans = 0;
        for (double v : values) {
            ans += v;
        }
        return ans / values.length;
    }
    
    private double median(double[] values) {
        int n = values.length;
        int mid = (n - 1) / 2;
        return n % 2 == 0 ? (values[mid] - values[mid + 1]) / 2 + values[mid + 1] :
                values[mid];
    }
    
    private double percentile(double[] values, double percentile) {
        Percentile p = new Percentile();
        return p.evaluate(values, percentile);
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
        MyClientMain2 instance = new MyClientMain2(nThreads, nIters, client);
        instance.runTask();
    }

}
