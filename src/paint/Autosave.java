/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Platform;

/**
 *
 * @author shree
 */
public class Autosave extends Thread{
    private PainT paint;
    private int timer;
    private EditTools editTool;
    private volatile boolean flag = true;
    ReentrantLock lock = new ReentrantLock();
    
    public Autosave(int timer, PainT paint){
        this.timer = timer;
        this.paint = paint;
        editTool = paint.editTool;
//        setDaemon(true);  
    }
    
    public void stopThread(){
        flag = false;
    }
    public void startThread(){
        flag = true;
    }
    @Override
    public void run() {
//        lock.lock();
boolean isTrue;
        isTrue = editTool.autosaveBox.isSelected();
        while(isTrue && !Thread.interrupted()){
        try{
            Thread.sleep(timer * 1000); 
            isTrue = editTool.autosaveBox.isSelected();
            Platform.runLater(new Runnable() {
                
                @Override
                public void run() {
                    try{
                   
                    WritableImage writableImage = new WritableImage((int)paint.canvas.getWidth(), (int)paint.canvas.getHeight());
                paint.canvas.snapshot(null, writableImage);
                
                
                    ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", paint.outputFile);
                }catch(IOException ex){
                    System.out.println("Error in saving");
                }
                    }
                
            });
//             paint.saving();
             System.out.println("Autosaved");
//                WritableImage writableImage = new WritableImage((int)paint.canvas.getWidth(), (int)paint.canvas.getHeight());
//                paint.canvas.snapshot(null, writableImage);
//                
//                try{
//                    ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", paint.outputFile);
//                }catch(Exception ex){
//                    System.out.println("Error in saving");
//                }
                
        }catch(InterruptedException e){
            return;
        }
        
        }
//        lock.unlock();
        
    }
    
}
