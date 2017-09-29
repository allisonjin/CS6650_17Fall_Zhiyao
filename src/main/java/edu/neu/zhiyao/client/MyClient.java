/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.neu.zhiyao.client;

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

    private final WebTarget webTarget;
    private final Client client;

    public MyClient() {
        this("54.193.122.64", 8080);
    }

    public MyClient(String ip, int port) {
        String uri = String.format("http://%s:%d/%s", ip, port, REST_PATH);
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(uri).path("simpleserver");
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

}
