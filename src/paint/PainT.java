/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import com.sun.corba.se.spi.ior.Writeable;
import java.awt.Desktop;
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Stack;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import javax.imageio.ImageIO;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;

/**
 *
 * @author shree
 */
public class PainT extends Application{
    
    /**
     *
     */
    public PainT(){
        
    }
    
    //Stores the opened Image so that it can be used while saving.
    private Image tempImage;
    
    //Constants for screen width and height.
    final int SCREEN_WIDTH = 1200;
    final int SCREEN_HEIGHT = 800;
    
    //Layouts
    private VBox myLayout = new VBox(10);
 
    /**
     *
     */
    public ImageView iView;
    
    //FileChooser
    FileChooser fileChooser = new FileChooser();
    File outputFile;
 
    //ScrollBar
    private ScrollPane scroll = new ScrollPane();
//    DrawingFunc drawingFunc
    CreateMenuBar menu = new CreateMenuBar();
    EditTools editTool = new EditTools();

    /**
     *
     */
    public StackPane canvasPane = new StackPane();
    
    Thread autosave;
    OpenSave os;
    Draw draw;
    
    
    CheckSelected cS;
//    DrawingFunc dF;
    @Override
    public void start(Stage stage) throws IOException { 
        LoggerFunc();
        //Setting up the scene
        Scene scene = new Scene(myLayout, SCREEN_WIDTH, SCREEN_HEIGHT);
        scene.getStylesheets().add((PainT.class.getResource("stylesheet.css").toExternalForm())); //
        
        
        os = new OpenSave(this);
        draw = new Draw(this);
        
        //Application funcionality and tools
        menu.createMenu(); //Creates the Menu Bar
        editTool.editTool(); //Contains tools such as color picker and brush width
        
        os.eventHandler(stage); //Handles the Menu Item clicks
       
        ///SmartSaves
        stage.setOnCloseRequest(e->{
            e.consume();
            closeProgram(stage, ConfirmExit.display("PainT", "Do you want to save changes?"));
        });
//        drawingFunc = new DrawingFunc(editTool, graphicsContext,canvas , pane, startX, startY, endX, endY, undoStack);
cS = new CheckSelected(editTool, this);
        canvasFunc();
        
        pane = new Pane(canvas);
//        dF = new DrawingFunc(this);
//        dF.dLine();
        dLine();
        
      
         editTool.autosaveBox.setOnAction(e->{
             if(editTool.autosaveBox.isSelected()){
                 autosave = new Autosave(10, this);
                 autosave.setDaemon(true);
            autosave.start();
            
             }
         });
            
//            addThreadsToPool();
        
        
        canvasPane.setMargin(canvas , new Insets(20,20,20,20));
//        pane.setPrefSize(canvas.getWidth(), canvas.getHeight());
        canvasPane.getChildren().add(pane);
       
        ObservableList<Double> canvasPoints = FXCollections.observableArrayList();
        canvasPoints.addAll(canvas.getWidth(), canvas.getHeight());
                
        textTool();
        polyTool();
        
        editTool.selectTool.setOnAction(e->{
            selectTrue = !selectTrue;      
        });
     
        zoomInOut();
        //Setting the basic layout 
        myLayout.getChildren().add(0,menu.menuBox); 
        myLayout.getChildren().add(1,editTool.editToolsBox);
        
        myLayout.getChildren().add(2,canvasPane);
        myLayout.getChildren().add(3,scroll);
        myLayout.getChildren().add(4, editTool.infoBar);
        pane.getChildren().addAll(createControlAnchorsFor(canvasPoints));
//        myLayout.getChildren().add(4, stackPane);
        //ScrollBar
        scroll.setStyle("-fx-background-insets: 0;");
        scroll.setContent(pane);
    
        stage.setTitle("PainT");
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     *
     */
    public void textTool(){
         editTool.textTool.setOnAction(e->{
            editTool.toolInfo.setText("Text Tool selected");
            canvas.setOnMouseClicked(point->{
                Stage textStage = new Stage();
                textStage.initModality(Modality.APPLICATION_MODAL);
                textStage.setTitle("Set Text");
                VBox setTextBox = new VBox();
                Label infoLabel = new Label("Text:");
                TextField editText = new TextField();
                Button okay = new Button("Ok");
                setTextBox.setAlignment(Pos.CENTER);
                setTextBox.getChildren().addAll(infoLabel, editText, okay);      
                okay.setOnAction(f->{graphicsContext.strokeText(editText.getText(), point.getX(), point.getY());textStage.close();});
                
                Scene textScene = new Scene(setTextBox, 300, 200);
                textStage.setScene(textScene);
                textStage.showAndWait();       
            });
        });
    }
    
    /**
     *
     */
    public void polyTool(){
        editTool.polygonTool.setOnAction(e->{
            editTool.toolInfo.setText("Polygon Tool selected");
            
                Stage textStage = new Stage();
                textStage.initModality(Modality.APPLICATION_MODAL);
                textStage.setTitle("Sides");
                VBox setTextBox = new VBox();
                Label infoLabel = new Label("Number of Sides:");
                TextField editText = new TextField();
                Button okay = new Button("Ok");
                setTextBox.setAlignment(Pos.CENTER);
                setTextBox.getChildren().addAll(infoLabel, editText, okay);      
                okay.setOnAction(f->{numberOfSides = Integer.parseInt(editText.getText());points = new double[numberOfSides];pointsY = new double[numberOfSides];textStage.close();});
                
                Scene textScene = new Scene(setTextBox, 300, 200);
                textStage.setScene(textScene);
                textStage.showAndWait();       
            
        });
    }
    
    /**
     *
     */
    public void zoomInOut(){
        editTool.zoomIn.setOnAction(e->{
            canvas.setScaleX(canvas.getScaleX() * 2);
            canvas.setScaleY(canvas.getScaleY() * 2);
        });
        
        editTool.zoomOut.setOnAction(e->{
            canvas.setScaleX(canvas.getScaleX() / 2);
            canvas.setScaleY(canvas.getScaleY() / 2);
        });    
    }
    
    private static final Logger logger = Logger.getLogger(PainT.class.getName());

    /**
     *
     * 
     */
    public void LoggerFunc() throws IOException{
        // Construct a default FileHandler.
      // "%t" denotes the system temp directory, kept in environment variable "tmp"
      Handler fh = new FileHandler("D:/NetBeans Projects/PainT/src/paint/logger.log", true);  // append is true
//    fh.setFormatter(new SimpleFormatter());  // Set the log format
      // Add the FileHandler to the logger.
      logger.addHandler(fh);
      // Set the logger level to produce logs at this level and above.
      logger.setLevel(Level.FINE);
       
    }
    
    /**
     *
     */
    public class OpenLog implements Runnable{
        String fileName;

        /**
         *
         * @param fileName
         */
        public OpenLog(String fileName){
            this.fileName = fileName;
        }

        @Override
        public void run() {
           
            while(fileName == null){
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PainT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           
            logger.info(fileName + " opened.");
        }   
    }
    
    /**
     *
     * 
     */
    public void oThread(String fileName){
        Thread openLog = new Thread(new OpenLog(fileName));
        openLog.start();
    }
    
    /**
     *
     */
    public void sThread(){
        autosave.interrupt();
        autosave = new Autosave(10, PainT.this);
        autosave.start();
    }
    
    /**
     *
     */
    public void slThread(){
        Thread saveLog = new Thread(new PainT.SaveLog(outputFile.getName()));
                saveLog.start();
    }
    
    /**
     *
     */
    public class SaveLog implements Runnable{
        String fileName;

        /**
         *
         * @param fileName = Name of the file
         */
        public SaveLog(String fileName){
            this.fileName = fileName;
        }

        @Override
        public void run() {
           
            while(fileName == null){
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PainT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           
            logger.info(fileName + " saved.");
        }
        
        
    }
    
    //Canvas

    /**
     *
     */
    public Canvas canvas;

    /**
     *
     */
    public GraphicsContext globalGC;

    /**
     *
     */
    public GraphicsContext graphicsContext;

    /**
     *
     */
    public double CANVAS_WIDTH = 1100;

    /**
     *
     */
    public double CANVAS_HEIGHT = 750;
    private Canvas layer = new Canvas();
    
    /**
     *
     */
    public int startX,

    /**
     *
     */
    startY,

    /**
     *
     */
    endX,

    /**
     *
     */
    endY;
    
    /**
     *
     */
    public Stack undoStack = new Stack();

    /**
     *
     */
    public Stack redoStack = new Stack();
    
    /**
     *
     */
    public void canvasFunc(){
        createCanvas(CANVAS_WIDTH, CANVAS_HEIGHT); //Creates a canvas
        
        cS.checkTrue();
        editTool.pencilTool.setOnAction((ActionEvent event) -> {
            cS.checkTrue();
        });
        
        editTool.lineTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
            
        });
        editTool.rectangleTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.squareTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.ellipseTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
        });
        editTool.circleTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.eraserTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.selectTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.moveTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.copyTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.triangleTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.polygonTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
       Image tempImge = canvas.snapshot(null, null);
            //Undo Stack
            undoStack.push(tempImge); 
            System.out.println("pushed");
        
        editTool.colorGrabberTool.setOnMouseClicked((MouseEvent event) ->{
            editTool.toolInfo.setText("Color Grabber selected");
            Image tempImage = canvas.snapshot(null, null);
            PixelReader pR = tempImage.getPixelReader();
            
            canvas.setOnMouseClicked(e ->{
                Color c = pR.getColor((int)e.getX(), (int)e.getY());
                editTool.colorPicker.setValue(c);
                canvas.setOnMouseClicked(null);
            });  
        });
        
        editTool.undoTool.setOnMouseClicked(e->{
            redoStack.push(undoStack.pop());
            Image tempImage = (Image)undoStack.peek();
            graphicsContext.drawImage(tempImage, 0, 0, tempImage.getWidth(), tempImage.getHeight());
            System.out.println("popped");
        });
        
        editTool.redoTool.setOnMouseClicked(e->{
            Image tempImage = (Image)redoStack.peek();
            undoStack.push(redoStack.pop());
            
            graphicsContext.drawImage(tempImage, 0, 0, tempImage.getWidth(), tempImage.getHeight());
            System.out.println("redone");
        });
    }
    
    /**
     *
     * @param selectedTool = Tool that's selected
     */
    public void tLogS(String selectedTool){
            Thread logSelection = new Thread(new LogSelection(selectedTool, logger, cS));
            logSelection.setDaemon(true);
            logSelection.start();
    }

    

    /**
     * Takes in width and height and creates a canvas
     * @param width Canvas's width
     * @param height Canvas's height
     */
    //Canvas
    public void createCanvas(double width, double height){
        //Creates a canvas with certain width, height.
        canvas = new Canvas();
        canvas.setWidth(width);
        canvas.setHeight(height);
        globalGC = canvas.getGraphicsContext2D();
        globalGC.setFill(Color.WHITE); //Fills the canvas with white color
        //Draws the canvas
        globalGC.fillRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                width,    //width of the rectangle
                height);  //height of the rectangle
    }
    
    /**
     *
     */
    public boolean freeLineTrue = false;

    /**
     *
     */
    public boolean lineTrue = false;

    /**
     *
     */
    public boolean rectangleTrue = false;

    /**
     *
     */
    public boolean squareTrue = false;

    /**
     *
     */
    public boolean ellipseTrue = false;

    /**
     *
     */
    public boolean circleTrue = false;

    /**
     *
     */
    public boolean eraserTrue = false;

    /**
     *
     */
    public boolean[] shapesBool = new boolean[7];

    /**
     *
     */
    public boolean selectTrue = false;

    /**
     *
     */
    public boolean moveTrue = false;
    
    /**
     *
     */
    public Pane pane;
    
    /**
     *
     */
    public Rectangle rectangle;

    /**
     *
     */
    public Rectangle square;

    /**
     *
     */
    public Ellipse ellipse;

    /**
     *
     */
    public Ellipse circle;

    /**
     *
     */
    public Polygon triangle;

    /**
     *
     */
    public Polygon polygon;

    /**
     *
     */
    public double[] points;

    /**
     *
     */
    public double[] pointsY;

    /**
     *
     */
    public int count = 0;
    
    ImageView selectedImg;
     int numberOfSides;
    //Brush

    /**
     *
     */
    public void dLine(){
        graphicsContext = canvas.getGraphicsContext2D();
        
        //Handles mouse events
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
//        canvasPane.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e)->{
            graphicsContext.setStroke(editTool.colorPicker.getValue()); //Sets the color for the brush
            graphicsContext.setLineWidth(editTool.slider.getValue()); //Sets line width
            graphicsContext.setFill(editTool.fillPicker.getValue());
            if(editTool.pencilTool.isSelected()){
                
                
                graphicsContext.beginPath();
                graphicsContext.moveTo(e.getX(), e.getY());
                graphicsContext.stroke();
            }else if(editTool.eraserTool.isSelected()){
                
                
                graphicsContext.beginPath();
                graphicsContext.moveTo(e.getX(), e.getY());
                graphicsContext.setStroke(Color.WHITE);
                graphicsContext.stroke();
            }else if(editTool.lineTool.isSelected()){
                 line = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                line.setStrokeWidth(editTool.lineWidth);
                line.setStroke(editTool.colorPicker.getValue());
                
                pane.getChildren().add(line);

//                graphicsContext.beginPath();
//                graphicsContext.lineTo(e.getX(), e.getY());
                startX = (int)e.getX();
                startY = (int)e.getY();
            }
            else if(editTool.rectangleTool.isSelected()){
                rectangle = new Rectangle(e.getX(), e.getY(), 0, 0);
                rectangle.setStrokeWidth(editTool.lineWidth);
                rectangle.setStroke(editTool.colorPicker.getValue());
                rectangle.setFill(editTool.fillPicker.getValue());          
                pane.getChildren().add(rectangle);
                
                startX = (int)e.getX();
                startY = (int)e.getY();
                System.out.println("Mouse pressed");
            }else if(editTool.squareTool.isSelected()){
                square = new Rectangle(e.getX(), e.getY(), 0, 0);
                square.setStrokeWidth(editTool.lineWidth);
                square.setStroke(editTool.colorPicker.getValue());
                square.setFill(editTool.fillPicker.getValue());          
                pane.getChildren().add(square);
                
                startX = (int)e.getX();
                startY = (int)e.getY();
                System.out.println("Mouse pressed");
            }else if(editTool.ellipseTool.isSelected()){
                ellipse = new Ellipse(e.getX(), e.getY(), 0, 0);
                ellipse.setStrokeWidth(editTool.lineWidth);
                ellipse.setStroke(editTool.colorPicker.getValue());
                ellipse.setFill(editTool.fillPicker.getValue());          
                pane.getChildren().add(ellipse);
                
                startX = (int)e.getX();
                startY = (int)e.getY();
                System.out.println("Mouse pressed");
            }else if(editTool.circleTool.isSelected()){
                circle = new Ellipse(e.getX(), e.getY(), 0, 0);
                circle.setStrokeWidth(editTool.lineWidth);
                circle.setStroke(editTool.colorPicker.getValue());
                circle.setFill(editTool.fillPicker.getValue());          
                pane.getChildren().add(circle);
                
                startX = (int)e.getX();
                startY = (int)e.getY();
                System.out.println("Mouse pressed");
            }else if(editTool.selectTool.isSelected()){
                rectangle = new Rectangle(e.getX(), e.getY(), 0, 0);
                rectangle.setStrokeWidth(1);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.TRANSPARENT);
                
             
                pane.getChildren().add(rectangle);
                
                startX = (int)e.getX();
                startY = (int)e.getY();
                System.out.println("Mouse pressed");
            }else if(editTool.triangleTool.isSelected()){
                triangle = new Polygon();
                triangle.getPoints().setAll(e.getX(),e.getY());
                triangle.setStrokeWidth(editTool.lineWidth);
                triangle.setStroke(editTool.colorPicker.getValue());
                triangle.setFill(editTool.fillPicker.getValue());          
                pane.getChildren().add(triangle);
                
                startX = (int)e.getX();
                startY = (int)e.getY();
                System.out.println("Mouse pressed");
            }else if(editTool.polygonTool.isSelected()){
               
                if(numberOfSides != 0){
                    points[count] = e.getX();
         
                    pointsY[count] = e.getY();
                    count++;
                    numberOfSides--;
                    System.out.println(pointsY[count - 1]);
                }
                 if(numberOfSides == 0){
                     System.out.println("Should execute polygon");
                     graphicsContext.setLineWidth(editTool.lineWidth);
                    graphicsContext.setStroke(editTool.colorPicker.getValue());
                    graphicsContext.setFill(editTool.fillPicker.getValue());
                    graphicsContext.strokePolygon(points, pointsY, points.length);
                    graphicsContext.fillPolygon(points, pointsY, points.length);
                    
                }
                
                
                
                
                
            }else if(editTool.moveTool.isSelected()){
                if(selectedImg != null){
                selectedImg.setX(e.getX());
                selectedImg.setY(e.getY());
                pane.getChildren().add(selectedImg);
                graphicsContext.setFill(Color.WHITE);
                graphicsContext.fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
                }
            }else if(editTool.copyTool.isSelected()){
                if(selectedImg != null){
                selectedImg.setX(e.getX());
                selectedImg.setY(e.getY());
                pane.getChildren().add(selectedImg);
//                graphicsContext.setFill(Color.WHITE);
//                graphicsContext.fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
                }
            }else if(editTool.emojiTool.isSelected()){
                ImageView emoView = (ImageView)editTool.emojiList.getValue();
                Image emo = emoView.getImage();
                graphicsContext.drawImage(emo, e.getX(), e.getY(), 50, 50);
            }
           
        });
        
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent e) -> {
//        canvasPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent e)->{
            if(editTool.pencilTool.isSelected()){
            graphicsContext.lineTo(e.getX(), e.getY());
            graphicsContext.stroke();
            graphicsContext.closePath();
            graphicsContext.beginPath();
            graphicsContext.moveTo(e.getX(), e.getY());
            }else if(editTool.eraserTool.isSelected()){
            graphicsContext.lineTo(e.getX(), e.getY());
            graphicsContext.stroke();
            graphicsContext.closePath();
            graphicsContext.beginPath();
            graphicsContext.moveTo(e.getX(), e.getY());
            }else if (editTool.lineTool.isSelected()){
                 line.setEndX(e.getX());
                 line.setEndY(e.getY());  
            }else if (editTool.rectangleTool.isSelected()){
                double tempX;
                double tempY;
                if(e.getX() - startX >= 0){
                    tempX = e.getX() - startX;
                }else{
                    tempX = startX - e.getX();
                    rectangle.setX(e.getX());
                }
                if(e.getY() - startY >= 0){
                    tempY = e.getY() - startY;
                }else{
                    tempY = startY - e.getY();
                    rectangle.setY(e.getY());
                }
                    
                    rectangle.setWidth(tempX);
                    rectangle.setHeight(tempY);  
            }else if (editTool.squareTool.isSelected()){
                double tempX;
              
                if(e.getX() - startX >= 0){
                    tempX = e.getX() - startX;
                }else{
                    tempX = startX - e.getX();
                    square.setX(e.getX());
                }
               
                    
                    square.setWidth(tempX);
                    square.setHeight(tempX);  
            }else if (editTool.ellipseTool.isSelected()){
                double tempX;
                double tempY;
                if(e.getX() - startX >= 0){
                    tempX = e.getX() - startX;
                }else{
                    tempX = startX - e.getX();
//                    ellipse.setCenterX(e.getX());
                }
                if(e.getY() - startY >= 0){
                    tempY = e.getY() - startY;
                }else{
                    tempY = startY - e.getY();
//                    ellipse.setCenterY(e.getY());
                }
                    
                    ellipse.setRadiusX(tempX);
                    ellipse.setRadiusY(tempY);  
            }else if (editTool.circleTool.isSelected()){
                double tempX;
               
                if(e.getX() - startX >= 0){
                    tempX = e.getX() - startX;
                }else{
                    tempX = startX - e.getX();
//                    circle.setCenterX(e.getX());
                }
                
                    
                    circle.setRadiusX(tempX);
                    circle.setRadiusY(tempX);  
            }else if (editTool.selectTool.isSelected()){
                double tempX;
                double tempY;
                if(e.getX() - startX >= 0){
                    tempX = e.getX() - startX;
                }else{
                    tempX = startX - e.getX();
                    rectangle.setX(e.getX());
                }
                if(e.getY() - startY >= 0){
                    tempY = e.getY() - startY;
                }else{
                    tempY = startY - e.getY();
                    rectangle.setY(e.getY());
                }
                    
                    rectangle.setWidth(tempX);
                    rectangle.setHeight(tempY);  
            }else if (editTool.triangleTool.isSelected()){
                double tempX;
                double tempY;
                if(e.getX() - startX >= 0){
                    tempX = e.getX();
                }else{
                    tempX = e.getX();
//                    triangle.setX(e.getX());
                }
                if(e.getY() - startY >= 0){
                    tempY = e.getY();
                }else{
                    tempY = e.getY();
//                    rectangle.setY(e.getY());
                }
                    
                    triangle.getPoints().setAll((double)startX, (double)startY, (double)startX, tempY, tempX, tempY );
            }else if(editTool.moveTool.isSelected()){
                if(selectedImg != null){
                selectedImg.setX(e.getX());
                selectedImg.setY(e.getY());
                }
                
            }else if(editTool.copyTool.isSelected()){
                if(selectedImg != null){
                selectedImg.setX(e.getX());
                selectedImg.setY(e.getY());
                }
                
            }
            
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent e) -> {
//        canvasPane.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent e)->{
            if(editTool.pencilTool.isSelected()){
            graphicsContext.lineTo(e.getX(), e.getY());
            graphicsContext.stroke();
            graphicsContext.closePath();
            }else if(editTool.eraserTool.isSelected()){
            graphicsContext.lineTo(e.getX(), e.getY());
            graphicsContext.stroke();
            graphicsContext.closePath();
            }else if(editTool.lineTool.isSelected()){
                pane.getChildren().remove(line);
//                graphicsContext.lineTo(e.getX(), e.getY());
//                graphicsContext.stroke();
//                graphicsContext.closePath();
                endX = (int)e.getX();
                endY = (int)e.getY();
                draw.paintLine();
            }
            else{
                endX = (int)e.getX();
                endY = (int)e.getY();
                System.out.println("Mouse Released");
                if(editTool.lineTool.isSelected()){
                    draw.paintLine();
                }else if(editTool.rectangleTool.isSelected()){
                    draw.paintRect();
                    pane.getChildren().remove(rectangle);
                    
                }else if(editTool.squareTool.isSelected()){
                    draw.paintSquare();
                    pane.getChildren().remove(square);
                    
                }else if(editTool.ellipseTool.isSelected()){
                    draw.paintEllipse();
                    pane.getChildren().remove(ellipse);
                    
                }else if(editTool.circleTool.isSelected()){
                    draw.paintCircle();
                    pane.getChildren().remove(circle);
                    
                }else if(editTool.selectTool.isSelected()){
                    
                    draw.selectRect();
                    Image imgToMove = (Image)undoStack.peek();
        PixelReader pR = imgToMove.getPixelReader();
        WritableImage newImg = new WritableImage(pR, startX, startY, (int)rectangle.getWidth(), (int)rectangle.getHeight());
        selectedImg = new ImageView(newImg);
        selectedImg.setX(rectangle.getX());
        selectedImg.setY(rectangle.getY());
                    pane.getChildren().remove(rectangle);
                }else if(editTool.triangleTool.isSelected()){
                    draw.paintTriangle();
                    pane.getChildren().remove(triangle);
                    
                    
                }else if(editTool.moveTool.isSelected()){
                    if(selectedImg != null){
                    pane.getChildren().remove(selectedImg);
                    
                    graphicsContext.drawImage(selectedImg.getImage(), selectedImg.getX(), selectedImg.getY());
                    selectedImg = null;
                    }
                }else if(editTool.copyTool.isSelected()){
                    if(selectedImg != null){
                    pane.getChildren().remove(selectedImg);
                    
                    graphicsContext.drawImage(selectedImg.getImage(), selectedImg.getX(), selectedImg.getY());
                    selectedImg = null;
                    }
                }else if(editTool.polygonTool.isSelected()){
//                    if(numberOfSides == 0){
////                    graphicsContext.setStroke(editTool.colorPicker.getValue());
////                    graphicsContext.setFill(editTool.fillPicker.getValue());
//                    graphicsContext.strokePolygon(points, pointsY, numberOfSides);
//                    graphicsContext.fillPolygon(points, pointsY, numberOfSides);
//                }
                }
                 
            }
            Image tempImage = canvas.snapshot(null, null );
            
            undoStack.push(tempImage);
            System.out.println("pushed");
            System.out.println("Mouse Released");
        });

    }
   
    /**
     *
     */
    public void colorGrabber(){
        
    }
    Line line;
    
    
  
    
    // @return a list of anchors which can be dragged around to modify points in the format [x1, y1, x2, y2...]

    /**
     *
     *
     */
    public ObservableList<Anchor> createControlAnchorsFor(final ObservableList<Double> points) {
    ObservableList<Anchor> anchors = FXCollections.observableArrayList();

    for (int i = 0; i < points.size(); i+=2) {
      final int idx = i;

      DoubleProperty xProperty = new SimpleDoubleProperty(points.get(i));
      DoubleProperty yProperty = new SimpleDoubleProperty(points.get(i + 1));

      xProperty.addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ov, Number oldX, Number x) {
          points.set(idx, (double) x);
        }
      });

      yProperty.addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ov, Number oldY, Number y) {
          points.set(idx + 1, (double) y);
        }
      });

      anchors.add(new Anchor(Color.GOLD, xProperty, yProperty));
    }

    return anchors;
  }

  // a draggable anchor displayed around a point.
  class Anchor extends Circle {
    private final DoubleProperty x, y;

    Anchor(Color color, DoubleProperty x, DoubleProperty y) {
      super(x.get(), y.get(), 10);
      setFill(color.deriveColor(1, 1, 1, 0.5));
      setStroke(color);
      setStrokeWidth(2);
      setStrokeType(StrokeType.OUTSIDE);

      this.x = x;
      this.y = y;

      x.bind(centerXProperty());
      y.bind(centerYProperty());
      enableDrag();
    }

    // make a node movable by dragging it around with the mouse.
    private void enableDrag() {
      final Delta dragDelta = new Delta();
      setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          // record a delta distance for the drag and drop operation.
          dragDelta.x = getCenterX() - mouseEvent.getX();
          dragDelta.y = getCenterY() - mouseEvent.getY();
          getScene().setCursor(Cursor.MOVE);
        }
      });
      setOnMouseReleased(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          getScene().setCursor(Cursor.HAND);
        }
      });
      setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          double newX = mouseEvent.getX() + dragDelta.x;
          canvas.setWidth(newX);
          if (newX > 0 && newX < getScene().getWidth()) {
            setCenterX(newX);
          }
          double newY = mouseEvent.getY() + dragDelta.y;
          canvas.setHeight(newY);
          if (newY > 0 && newY < getScene().getHeight()) {
            setCenterY(newY);
          }
        }
      });
      setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.HAND);
          }
        }
      });
      setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.DEFAULT);
          }
        }
      });
    }

    // records relative x and y co-ordinates.
    private class Delta { double x, y; }
  }
  
    /**
     *
     */
    public String saveFileExtension;
    
    /**
     *
     * @param stage
     * @param display
     */
    public void closeProgram(Stage stage, String display){
        String answer = display;
        switch(answer){
            case "save":
                if(outputFile != null){
                WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                canvas.snapshot(null, writableImage);
                
                try{
                    ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", outputFile);
                }catch(IOException ex){
                    System.out.println("Error in saving");
                }
                }else{                  
                    FileChooser.ExtensionFilter extPNG = new FileChooser.ExtensionFilter("PNG File (*.png)", "*.png");
                    fileChooser.getExtensionFilters().add(extPNG);
                    outputFile = fileChooser.showSaveDialog(stage);
                    WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                    canvas.snapshot(null, writableImage);

                    try {


                            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", outputFile);
                            

                    } catch (IOException ex) {
                        System.out.println("Error in saving as");
                    }
                    
                }
                if(autosave != null){
                    autosave.interrupt();
                }
                stage.close();
                break;
            case "dontSave":
                if(autosave != null){
                    autosave.interrupt();
                }
                stage.close();
                break;
            case "cancel":
                break;
        }
                
    }

    /**
     *
     * @param stage
     * @param display
     */
    public void saveWarning(Stage stage, String display){
        String answer = display;
        switch(answer){
            case "save":
                              
                    
                    WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                    canvas.snapshot(null, writableImage);

                    try {


                           ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), saveFileExtension, outputFile);
                            

                    } catch (IOException ex) {
                        System.out.println("Error in saving as");
                    }
                    
                
//                stage.close();
                break;
            case "dontSave":
//                stage.close();
                break;
            case "cancel":
                break;
        }
                
    }
 
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
//        PainT paint = new PainT();
    
      
        launch(args);
    }
    
}
