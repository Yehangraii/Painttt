/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import com.sun.javafx.application.PlatformImpl;
import com.sun.prism.paint.Color;
import javafx.scene.image.ImageView;
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
public class EditToolsTest {
    
    public EditToolsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws Exception{
        PlatformImpl.startup(() ->{});
        
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of editTool method, of class EditTools.
     */
    @Test
    public void testEditTool() {
        System.out.println("editTool");
        EditTools instance = new EditTools();
        
        instance.editTool();
        assertEquals(instance.strokeLabel.getText(), "Stroke: ");
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setToolSize method, of class EditTools.
     */
//    @Test
//    public void testSetToolSize() {
//        System.out.println("setToolSize");
//        ImageView iView = null;
//        EditTools instance = new EditTools();
//        instance.setToolSize(iView);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
