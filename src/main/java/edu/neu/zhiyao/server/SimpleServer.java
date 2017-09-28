/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.neu.zhiyao.server;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author allisonjin
 */
@Path("simpleserver")
public class SimpleServer {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SimpleServer
     */
//    public SimpleServer() {
//    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getStatus() {
        return ("alive");
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public int postText(String content) {
        return (content.length());
    }
}
