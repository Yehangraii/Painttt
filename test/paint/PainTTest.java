/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author shree
 */
public class PainTTest {
    
    public PainTTest() {
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
     * Test of setBool method, of class PainT.
     */
    @Test
    public void testSetBool() {
        System.out.println("setBool");
        int index = 0;
        PainT instance = new PainT();
        instance.setBool(index);
        assertEquals(instance.shapesBool[index], true);
     
    }

    /**
     * Test of createCanvas method, of class PainT.
     */
    @Test
    public void testCreateCanvas() {
        System.out.println("createCanvas");
        double width = 10.0;
        double height = 5.0;
        PainT instance = new PainT();
        instance.createCanvas(width, height);
        assertEquals(10, instance.canvas.getWidth());
        assertEquals(5, instance.canvas.getHeight());
        
    }
    
}
