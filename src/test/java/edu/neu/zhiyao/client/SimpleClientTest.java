/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.neu.zhiyao.client;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author allisonjin
 */
public class SimpleClientTest {
    
    public SimpleClientTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of postText method, of class SimpleClient.
     */
    @Test
    public void testPostText() {
        System.out.println("postText");
        SimpleClient instance = new SimpleClient();
        int expResult = 4;
        int result = instance.postText("test", Integer.class);
        assertEquals(expResult, result);
    }

    /**
     * Test of getStatus method, of class SimpleClient.
     */
    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        SimpleClient instance = new SimpleClient();
        String expResult = "alive";
        String result = instance.getStatus();
        assertEquals(expResult, result);
    }

    /**
     * Test of close method, of class SimpleClient.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        SimpleClient instance = new SimpleClient();
        instance.close();
    }
    
}
