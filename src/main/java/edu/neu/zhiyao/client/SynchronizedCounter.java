package edu.neu.zhiyao.client;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author allisonjin
 */
public class SynchronizedCounter {
    private int reqCnt = 0;
    private int respCnt = 0;
    List<Double> latencies = new ArrayList<>();
    
    public synchronized void reqIncrement() {
        reqCnt++;
    }
    
    public synchronized void respIncrement() {
        respCnt++;
    }
    
    public synchronized void addLatency(double latency) {
        latencies.add(latency);
    }

    public int getReqCnt() {
        return reqCnt;
    }

    public int getRespCnt() {
        return respCnt;
    }

    public List<Double> getLatencies() {
        return latencies;
    }    
    
}
