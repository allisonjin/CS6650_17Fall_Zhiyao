/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.neu.zhiyao.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:SimpleServer
 * [simpleserver]<br>
 * USAGE:
 * <pre>
 *        SimpleClient client = new SimpleClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author allisonjin
 */
public class MyClient {

    private static final String REST_PATH = "Assignment1-1.0-SNAPSHOT/rest";
    private static final String POST_RES_TEXT = "testPostText";
    private static final String EXPECTED_GET_RESP = "alive";
    private static final int EXPECTED_POST_RESP = POST_RES_TEXT.length();

    private final int nThreads;
    private final int nIterations;

    private final WebTarget webTarget;
    private final Client client;

    public MyClient(int nThreads, int nIterations, String ip, int port) {
        this.nThreads = nThreads;
        this.nIterations = nIterations;
        String uri = String.format("http://%s:%d/%s", ip, port, REST_PATH);
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(uri).path("simpleserver");
    }

    public void runTask() {
        final AtomicInteger reqCnt = new AtomicInteger(0);
        final AtomicInteger respCnt = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();
        System.out.println("Client starting... Time: " + startTime);
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < nIterations; j++) {
                        String getResp;
                        Integer postResp;
                        
                        getResp = getStatus();
                        reqCnt.incrementAndGet();
                        postResp = postText(POST_RES_TEXT, Integer.class);
                        reqCnt.incrementAndGet();
  
                        if (getResp.equals(EXPECTED_GET_RESP)) {
                            respCnt.incrementAndGet();
                        }
                        if (postResp.equals(EXPECTED_POST_RESP)) {
                            respCnt.incrementAndGet();
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
        System.out.println("Total number of requests sent: " + reqCnt.get());
        System.out.println("Total number of successful responses: "
                            + respCnt.get());
        double wallTime = (endTime - startTime) / 1000;
        System.out.format("Test wall time: %.1f seconds", wallTime);
        
    }

    public <T> T postText(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.TEXT_PLAIN), responseType);
    }

    public String getStatus() throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public void close() {
        client.close();
    }

    public static void main(String[] args) {
        MyClient client = null;
        try {
            int nThreads = Integer.parseInt(args[0]);
            int nIters = Integer.parseInt(args[1]);
            String ip = args[2];
            int port = Integer.parseInt(args[3]);
            client = new MyClient(nThreads, nIters, ip, port);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
        if (client != null) {
            client.runTask();
        }
    }

}
