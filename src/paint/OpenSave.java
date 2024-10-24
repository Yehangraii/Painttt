/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author shree
 */
public class OpenSave {
    public String fileName;
    public String fileExtension;
    public String saveFileName;
    public String saveFileExtension;
    public EventHandler<ActionEvent> saveClick;
    public PainT p;
    
    public OpenSave(PainT paint){
        p = paint;
    }
    /**
     * Handles all the events for open, save and save as. 
     * @param stage Takes in input the current stage.
     */
    //EventHandler for menu
    public void eventHandler(Stage stage){
        FileChooser.ExtensionFilter extJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
                FileChooser.ExtensionFilter extPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
                FileChooser.ExtensionFilter extGIF = new FileChooser.ExtensionFilter("GIF files (*.gif)", "*.gif");
                FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter("All files", "*.*");
                p.fileChooser.getExtensionFilters().addAll(allFilter, extJPG, extPNG, extGIF);
        //Handles the event when the user presses Open File.
        EventHandler<ActionEvent> openClick = new EventHandler<ActionEvent>() {
            @Override 
            public void handle(final ActionEvent e){
                
                
                File file = p.fileChooser.showOpenDialog(stage);
                try {
                    
                BufferedImage bImage = ImageIO.read(file); //Reading the file
                
                Image image = SwingFXUtils.toFXImage(bImage, null);
                fileName = file.getName();          
                fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, file.getName().length());
                System.out.println(">> fileExtension " + fileExtension);
                p.oThread(fileName);
                
//                tempImage = image;
                p.iView = new ImageView();
                p.iView.setImage(image);
                p.iView.setFitWidth(200);
                
                p.iView.setPreserveRatio(true);
               
                Image tempImage = p.iView.getImage();
                
                p.globalGC.clearRect(0, 0, p.CANVAS_WIDTH, p.CANVAS_HEIGHT);
                
                p.globalGC.fillRect(0,0,tempImage.getWidth(),tempImage.getHeight());
                p.canvas.setWidth(tempImage.getWidth());
                p.canvas.setHeight(tempImage.getHeight());
                p.globalGC.drawImage(tempImage, 0, 0, tempImage.getWidth(), tempImage.getHeight());
            
                
             
                System.out.println("Image opened successfully!");
            } catch (IOException ex) {
                System.out.println("Error in opening");
            }
            }
        };
        
        //saveClick
        saveClick = new EventHandler<ActionEvent>(){
            
            @Override
            public void handle(final ActionEvent e){
                if(p.outputFile != null){
                WritableImage writableImage = new WritableImage((int)p.canvas.getWidth(), (int)p.canvas.getHeight());
                p.canvas.snapshot(null, writableImage);
                
                try{
                    ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), saveFileName, p.outputFile);
                    p.sThread();
                    
                }catch(IOException ex){
                    System.out.println("Error in saving");
                }  
                
                }else{                  
//                    FileChooser.ExtensionFilter extJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
//                    FileChooser.ExtensionFilter extPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
//                    FileChooser.ExtensionFilter extGIF = new FileChooser.ExtensionFilter("GIF files (*.gif)", "*.gif");
//                    fileChooser.getExtensionFilters().addAll(extPNG, extJPG, extGIF);
                    p.outputFile = p.fileChooser.showSaveDialog(stage);
                    WritableImage writableImage = new WritableImage((int)p.canvas.getWidth(), (int)p.canvas.getHeight());
                    p.canvas.snapshot(null, writableImage);

                    try {
                            saveFileName = p.outputFile.getName();          
                            saveFileExtension = saveFileName.substring(saveFileName.lastIndexOf(".") + 1, p.outputFile.getName().length());
                            
                            if(saveFileExtension.equals(fileExtension)){
                            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), saveFileExtension, p.outputFile);
                            System.out.println(">> SavefileExtension " + saveFileExtension);
                            }else{
                              
                               
                                p.saveWarning(stage, ConfirmExit.display("WARNING", "WARNING:\nData loss can happen\nDo you want to save?"));
                                System.out.println(">> SavefileExtension " + saveFileExtension);
                            }
                            if(p.autosave != null){
                                p.sThread();
                            }
                        } catch (IOException ex) {
                            System.out.println("Error in saving as");
                        }
                    
                }
                p.slThread();
            }
        };
        
        //When User presses Save File
        EventHandler<ActionEvent> saveAsClick = new EventHandler<ActionEvent>(){
            @Override
            public void handle(final ActionEvent e){
//                FileChooser.ExtensionFilter extPNG = new FileChooser.ExtensionFilter("PNG File (*.png)", "*.png");
//                fileChooser.getExtensionFilters().add(extPNG);
                p.outputFile = p.fileChooser.showSaveDialog(stage);
                WritableImage writableImage = new WritableImage((int)p.canvas.getWidth(), (int)p.canvas.getHeight());
                p.canvas.snapshot(null, writableImage);
                         
                try {
                    saveFileName = p.outputFile.getName();          
                            saveFileExtension = saveFileName.substring(saveFileName.lastIndexOf(".") + 1, p.outputFile.getName().length());
                            
                            if(saveFileExtension.equals(fileExtension)){
                            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), saveFileExtension, p.outputFile);
                            System.out.println(">> SavefileExtension " + saveFileExtension);
                            }else{
                              
                               
                                p.saveWarning(stage, ConfirmExit.display("WARNING", "WARNING:\nData loss can happen\nDo you want to save?"));
                                System.out.println(">> SavefileExtension " + saveFileExtension);
                            }
                        p.sThread();
                    } catch (IOException ex) {
                        System.out.println("Error in saving as");
                    }
            }
        };
        
        
        
        //When User presses Exit.
        EventHandler<ActionEvent> exitClick = new EventHandler<ActionEvent>(){
            @Override
            public void handle(final ActionEvent e){
                p.closeProgram(stage, ConfirmExit.display("PainT", "Do you want to save changes?"));
            }
        };
        
        
        p.menu.about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("About"));
                dialogVbox.getChildren().add(new Text("\nv1.1\nPaint Application\nShree Software Solutions\n"));
                Button releaseNotes = new Button("Release Notes");
                Button ok = new Button("Ok");
                dialogVbox.getChildren().add(releaseNotes);
                dialogVbox.getChildren().add(ok);
                dialogVbox.setAlignment(Pos.CENTER);
                releaseNotes.setOnAction((ActionEvent e)->{
                    File releaseFile = new File("D:\\NetBeans Projects\\PainT\\src\\paint\\Release Notes.txt");
                    try {
                        Desktop.getDesktop().open(releaseFile);
                    } catch (IOException ex) {
                        Logger.getLogger(PainT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                ok.setOnAction((ActionEvent e)->{
                    dialog.close();
                });
                Scene dialogScene = new Scene(dialogVbox, 400, 300);
                dialog.setScene(dialogScene);
                dialog.show();
            }
         });
        
        p.menu.toolHelp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("Tools Help:"));
                dialogVbox.getChildren().add(new Text("\nFree Line Tool -> Draw a line freely.\nLine Tool -> Draw a straight line.\nRectangle Tool -> Draw a rectangle\nSquare Tool: Draw a square.\nEllipse Tool: Draw an ellipse.\nCircle Tool: Draw a circle.\nEraser Tool: Erase the contents on the canvas freely.\nText Tool: Press anywhere on the canvas and fill out the text on the pop up window.\nColor Grabber: Sample any color from the canvas and changes the stroke to the newly sampled color.\n"));               
                Button ok = new Button("Ok");
                dialogVbox.getChildren().add(ok);
                dialogVbox.setAlignment(Pos.CENTER);
                
                ok.setOnAction((ActionEvent e)->{
                    dialog.close();
                });
                Scene dialogScene = new Scene(dialogVbox, 800, 500);
                dialog.setScene(dialogScene);
                dialog.show();
            }
         });
        
     
        
        //Listeners for opening, saving and exiting
        p.menu.openFile.setOnAction(openClick);
        p.menu.saveFile.setOnAction(saveClick);
        p.menu.saveAsFile.setOnAction(saveAsClick);
        p.menu.exitFile.setOnAction(exitClick);
    }
    
    public void saving(){
        if(p.outputFile != null){
                WritableImage writableImage = new WritableImage((int)p.canvas.getWidth(), (int)p.canvas.getHeight());
                p.canvas.snapshot(null, writableImage);
                
                try{
                    ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", p.outputFile);
                }catch(IOException ex){
                    System.out.println("Error in saving");
                }
                }
        System.out.println("Autosaved in PainT");
    }
}
