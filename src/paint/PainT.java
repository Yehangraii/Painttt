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


/**
 *
 * @author shree
 */
public class PainT extends Application{
   
    //Stores the opened Image so that it can be used while saving.
    private Image tempImage;
    
    //Constants for screen width and height.
    final int SCREEN_WIDTH = 1200;
    final int SCREEN_HEIGHT = 800;
    
    //Layouts
    private VBox myLayout = new VBox(10);
    
    
    private ImageView iView;
    
    //FileChooser
    FileChooser fileChooser = new FileChooser();
    File outputFile;
    
  
    
    //ScrollBar
    private ScrollPane scroll = new ScrollPane();
  
    
    
    
    CreateMenuBar menu = new CreateMenuBar();
    EditTools editTool = new EditTools();
    public StackPane canvasPane = new StackPane();
    
  
    
     int numberOfSides;
    @Override
    public void start(Stage stage) {
      
        //Setting up the scene
        Scene scene = new Scene(myLayout, SCREEN_WIDTH, SCREEN_HEIGHT);
        scene.getStylesheets().add((PainT.class.getResource("stylesheet.css").toExternalForm())); //
        
        //Application funcionality and tools
        menu.createMenu(); //Creates the Menu Bar
        editTool.editTool(); //Contains tools such as color picker and brush width
        
        eventHandler(stage); //Handles the Menu Item clicks
        
        ///SmartSaves
        stage.setOnCloseRequest(e->{
            e.consume();
            closeProgram(stage);
        });
        
        canvasFunc();
        pane = new Pane(canvas);
        
        
            
        
        
        canvasPane.setMargin(canvas , new Insets(20,20,20,20));
//        pane.setPrefSize(canvas.getWidth(), canvas.getHeight());
        canvasPane.getChildren().add(pane);
       
        ObservableList<Double> canvasPoints = FXCollections.observableArrayList();
        canvasPoints.addAll(canvas.getWidth(), canvas.getHeight());
                
        
        
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
       
        editTool.selectTool.setOnAction(e->{
            selectTrue = !selectTrue;      
        });
        
        /*editTool.moveTool.setOnAction(e->{
            moveTrue = !moveTrue;
            if(moveTrue){
                  Image tempImage = canvas.snapshot(null, null);
            PixelReader pR = tempImage.getPixelReader();
            int wid = (int)endX - startX;
        int hei = (int)endY - startY;
	
        
        int[] pixels = new int[wid * hei];
        WritablePixelFormat<IntBuffer> wpf = WritablePixelFormat.getIntArgbInstance();
        pR.getPixels(startX, startY, wid, hei, wpf, pixels, 0, wid);
        
        WritableImage outputImage = new WritableImage(wid, hei);
	
	PixelWriter pixelWriter = outputImage.getPixelWriter();
        pixelWriter.setPixels(startX, startY, wid, hei, wpf, pixels, 0, wid);
        
        graphicsContext.drawImage(outputImage, 0, 0, outputImage.getWidth(), outputImage.getHeight());
            }
        });*/
        
        editTool.zoomIn.setOnAction(e->{
            canvas.setScaleX(canvas.getScaleX() * 2);
            canvas.setScaleY(canvas.getScaleY() * 2);
        });
        
        editTool.zoomOut.setOnAction(e->{
            canvas.setScaleX(canvas.getScaleX() / 2);
            canvas.setScaleY(canvas.getScaleY() / 2);
        });
        
  
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
    
    //Canvas
    public Canvas canvas;
    public GraphicsContext globalGC;
    public GraphicsContext graphicsContext;
    public double CANVAS_WIDTH = 1100;
    public double CANVAS_HEIGHT = 750;
    private Canvas layer = new Canvas();
    
    
    
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    
    Stack undoStack = new Stack();
    Stack redoStack = new Stack();
    
    public void canvasFunc(){
        createCanvas(CANVAS_WIDTH, CANVAS_HEIGHT); //Creates a canvas
        
        //Booleans to check which drawing tool is selected
        /*
        freeLineTrue = shapesBool[0];
        lineTrue = shapesBool[1];
        rectangleTrue = shapesBool[2];
        squareTrue = shapesBool[3];
        ellipseTrue = shapesBool[4];
        circleTrue = shapesBool[5];*/
        dLine();
        checkTrue();
        editTool.pencilTool.setOnAction((ActionEvent event) -> {
            checkTrue();
        });
        
        editTool.lineTool.setOnAction((ActionEvent event)->{
            checkTrue();
            
            
        });
        editTool.rectangleTool.setOnAction((ActionEvent event)->{
            checkTrue();
            
        });
        editTool.squareTool.setOnAction((ActionEvent event)->{
            checkTrue();
            
        });
        editTool.ellipseTool.setOnAction((ActionEvent event)->{
            checkTrue();
        });
        editTool.circleTool.setOnAction((ActionEvent event)->{
            checkTrue();
            
        });
        editTool.eraserTool.setOnAction((ActionEvent event)->{
            checkTrue();
            
        });
        editTool.selectTool.setOnAction((ActionEvent event)->{
            checkTrue();
            
        });
        editTool.moveTool.setOnAction((ActionEvent event)->{
            checkTrue();
            
        });
        editTool.triangleTool.setOnAction((ActionEvent event)->{
            checkTrue();
            
        });
        editTool.polygonTool.setOnAction((ActionEvent event)->{
            checkTrue();
            
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
    

    
    public void setBool(int index){
        shapesBool[index] = !shapesBool[index];
        for (int i = 0; i < shapesBool.length; i++) {
            if(i != index){
                shapesBool[i] = false;
            }
        }
        
        freeLineTrue = shapesBool[0];
        lineTrue = shapesBool[1];
        rectangleTrue = shapesBool[2];
        squareTrue = shapesBool[3];
        ellipseTrue = shapesBool[4];
        circleTrue = shapesBool[5];
        eraserTrue = shapesBool[6];
        if(selectTrue)
            selectTrue = false;
        if(moveTrue)
            moveTrue = false;
        checkTrue();
    }
    public void checkTrue(){
        if(editTool.pencilTool.isSelected()){
            editTool.toolInfo.setText("Free line selected");
        }else if(editTool.lineTool.isSelected()){
            editTool.toolInfo.setText("Line selected");
        }else if(editTool.rectangleTool.isSelected()){
            editTool.toolInfo.setText("Rectangle selected");
        }
        else if(editTool.squareTool.isSelected()){
            editTool.toolInfo.setText("Square selected");
        }
        else if(editTool.ellipseTool.isSelected()){
            editTool.toolInfo.setText("Ellipse selected");
        }
        else if(editTool.circleTool.isSelected()){
            editTool.toolInfo.setText("Circle selected");
        }else if(editTool.eraserTool.isSelected()){
            editTool.toolInfo.setText("Eraser selected");
        }else if(editTool.selectTool.isSelected()){
            editTool.toolInfo.setText("Select Tool selected");
        }else if(editTool.moveTool.isSelected()){
            editTool.toolInfo.setText("Move Tool selected");
        }else if(editTool.triangleTool.isSelected()){
            editTool.toolInfo.setText("Triangle Tool selected");
        }else if(editTool.polygonTool.isSelected()){
            editTool.toolInfo.setText("Polygon Tool selected");
        }else{
            editTool.toolInfo.setText("No Tool selected");
        }
    }
    //Canvas
    private void createCanvas(double width, double height){
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
    
    
    public boolean freeLineTrue = false;
    public boolean lineTrue = false;
    public boolean rectangleTrue = false;
    public boolean squareTrue = false;
    public boolean ellipseTrue = false;
    public boolean circleTrue = false;
    public boolean eraserTrue = false;
    public boolean[] shapesBool = new boolean[7];
    public boolean selectTrue = false;
    public boolean moveTrue = false;
    
    public Pane pane;
    
    public Rectangle rectangle;
    public Rectangle square;
    public Ellipse ellipse;
    public Ellipse circle;
    public Polygon triangle;
    public Polygon polygon;
    public double[] points;
    public double[] pointsY;
    public int count = 0;
    //Brush
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
                paintLine();
            }
            else{
                endX = (int)e.getX();
                endY = (int)e.getY();
                System.out.println("Mouse Released");
                if(editTool.lineTool.isSelected()){
                    paintLine();
                }else if(editTool.rectangleTool.isSelected()){
                    pane.getChildren().remove(rectangle);
                    paintRect();
                }else if(editTool.squareTool.isSelected()){
                    pane.getChildren().remove(square);
                    paintSquare();
                }else if(editTool.ellipseTool.isSelected()){
                    paintEllipse();
                    pane.getChildren().remove(ellipse);
                    
                }else if(editTool.circleTool.isSelected()){
                    paintCircle();
                    pane.getChildren().remove(circle);
                    
                }else if(editTool.selectTool.isSelected()){
                    
                    selectRect();
                    pane.getChildren().remove(rectangle);
                }else if(editTool.triangleTool.isSelected()){
                    pane.getChildren().remove(triangle);
                    
                    paintTriangle();
                }else if(editTool.moveTool.isSelected()){
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
   
    public void colorGrabber(){
        
    }
    Line line;
    
    private void paintTriangle(){
        double tempX;
        double tempY;
        if(endX - startX >= 0){
            tempX = endX - startX;
        }else{
            tempX = startX - endX;
//            startX = endX;
        }
        if(endY - startY >= 0){
            tempY = endY - startY;
        }else{
            tempY = startY - endY;
//            startY = endY;
        }
        
        graphicsContext.strokePolygon(new double[]{startX, startX, triangle.getPoints().get(4)}, new double[]{startY, triangle.getPoints().get(3), triangle.getPoints().get(5)}, 3);
        graphicsContext.fillPolygon(new double[]{startX, startX, triangle.getPoints().get(4)}, new double[]{startY, triangle.getPoints().get(3), triangle.getPoints().get(5)}, 3);
    }
    
    private void paintLine(){
        graphicsContext = canvas.getGraphicsContext2D();
        /*Canvas tempCanvas = new Canvas();
        graphicsContext = canvas.getGraphicsContext2D();
        GraphicsContext tempGc = graphicsContext;
//        
//        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//        graphicsContext.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        
        graphicsContext.setStroke(editTool.colorPicker.getValue());
        graphicsContext.setLineWidth(editTool.lineWidth);
        graphicsContext.strokeLine(startX, startY, endX, endY);
        graphicsContext = tempGc;*/
        
       graphicsContext.strokeLine(startX, startY, endX, endY);
        
    }
    public void paintRect(){
        double tempX;
        double tempY;
        if(endX - startX >= 0){
            tempX = endX - startX;
        }else{
            tempX = startX - endX;
            startX = endX;
        }
        if(endY - startY >= 0){
            tempY = endY - startY;
        }else{
            tempY = startY - endY;
            startY = endY;
        }
        
        graphicsContext.strokeRect(startX, startY, tempX, tempY);
        graphicsContext.fillRect(startX, startY, tempX, tempY);
    }
    public void paintSquare(){
        double tempX;
        
        if(endX - startX >= 0){
            tempX = endX - startX;
        }else{
            tempX = startX - endX;
            startX = endX;
        }
        
        graphicsContext.strokeRect(startX, startY, tempX, tempX);
        graphicsContext.fillRect(startX, startY, tempX, tempX);
    }
    public void paintEllipse(){
        double tempX;
        double tempY;
        if(endX - startX >= 0){
            tempX = endX - startX;
        }else{
            tempX = startX - endX;
//            startX = endX;
        }
        if(endY - startY >= 0){
            tempY = endY - startY;
        }else{
            tempY = startY - endY;
//            startY = endY;
        }
        graphicsContext.strokeOval(startX - ellipse.getRadiusX(), startY - ellipse.getRadiusY(), tempX + ellipse.getRadiusX(), tempY + ellipse.getRadiusY());
        graphicsContext.fillOval(startX - ellipse.getRadiusX(), startY - ellipse.getRadiusY(), tempX + ellipse.getRadiusX(), tempY + ellipse.getRadiusY());
    }
    public void paintCircle(){
        double tempX;
        double tempRadius = circle.getRadiusX();
        if(endX - startX >= 0){
            tempX = endX - startX;
        }else{
            tempX = startX - endX;
//            startX = endX;
        }
        graphicsContext.strokeOval(startX - tempRadius, startY - tempRadius, tempX + tempRadius, tempX + tempRadius);
        graphicsContext.fillOval(startX - tempRadius, startY - tempRadius, tempX + tempRadius, tempX + tempRadius);
        
    }
    public void selectRect(){
        double tempX;
        double tempY;
        if(endX - startX >= 0){
            tempX = endX - startX;
        }else{
            tempX = startX - endX;
            startX = endX;
        }
        if(endY - startY >= 0){
            tempY = endY - startY;
        }else{
            tempY = startY - endY;
            startY = endY;
        }
        
 
        Image imgToMove = (Image)undoStack.peek();
        PixelReader pR = imgToMove.getPixelReader();
        WritableImage newImg = new WritableImage(pR, startX, startY, (int)rectangle.getWidth(), (int)rectangle.getHeight());
        selectedImg = new ImageView(newImg);
        selectedImg.setX(rectangle.getX());
        selectedImg.setY(rectangle.getY());
    }
    
    // @return a list of anchors which can be dragged around to modify points in the format [x1, y1, x2, y2...]
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
    
    ImageView selectedImg;
    //EventHandler for menu
    public void eventHandler(Stage stage){
        //Handles the event when the user presses Open File.
        EventHandler<ActionEvent> openClick = new EventHandler<ActionEvent>() {
            @Override 
            public void handle(final ActionEvent e){
                FileChooser.ExtensionFilter extJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg,*.jpeg");
                FileChooser.ExtensionFilter extPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
                FileChooser.ExtensionFilter extGIF = new FileChooser.ExtensionFilter("TIIF files (*.tiff)", "*.tiff");
                FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter("All files", "*.*");
                fileChooser.getExtensionFilters().addAll(allFilter, extJPG, extPNG, extGIF);
                File file = fileChooser.showOpenDialog(stage);
                try {
                    
                BufferedImage bImage = ImageIO.read(file); //Reading the file
                Image image = SwingFXUtils.toFXImage(bImage, null);
//                tempImage = image;
                iView = new ImageView();
                iView.setImage(image);
                iView.setFitWidth(200);
                
                iView.setPreserveRatio(true);
               
                Image tempImage = iView.getImage();
                
                globalGC.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
                
                globalGC.fillRect(0,0,tempImage.getWidth(),tempImage.getHeight());
                canvas.setWidth(tempImage.getWidth());
                canvas.setHeight(tempImage.getHeight());
                globalGC.drawImage(tempImage, 0, 0, tempImage.getWidth(), tempImage.getHeight());
            
                
             
                System.out.println("Image opened successfully!");
            } catch (IOException ex) {
                System.out.println("Error in opening");
            }
            }
        };
        
        //saveClick
        EventHandler<ActionEvent> saveClick = new EventHandler<ActionEvent>(){
            
            @Override
            public void handle(final ActionEvent e){
                if(outputFile != null){
                WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                canvas.snapshot(null, writableImage);
                
                try{
                    ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", outputFile);
                }catch(IOException ex){
                    System.out.println("Error in saving");
                }
                }else{                  
                    FileChooser.ExtensionFilter extJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg,*.jpeg");
                    FileChooser.ExtensionFilter extPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
                    FileChooser.ExtensionFilter extGIF = new FileChooser.ExtensionFilter("GIF files (*.gif)", "*.gif");
                    fileChooser.getExtensionFilters().addAll(extPNG, extJPG, extGIF);
                    outputFile = fileChooser.showSaveDialog(stage);
                    WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                    canvas.snapshot(null, writableImage);

                    try {

                            
                            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", outputFile);

                        } catch (IOException ex) {
                            System.out.println("Error in saving as");
                        }

                }
            }
        };
        
        //When User presses Save File
        EventHandler<ActionEvent> saveAsClick = new EventHandler<ActionEvent>(){
            @Override
            public void handle(final ActionEvent e){
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
        };
        
        
        
        //When User presses Exit.
        EventHandler<ActionEvent> exitClick = new EventHandler<ActionEvent>(){
            @Override
            public void handle(final ActionEvent e){
                closeProgram(stage);
            }
        };
        
        
        menu.about.setOnAction(new EventHandler<ActionEvent>() {
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
        
        menu.toolHelp.setOnAction(new EventHandler<ActionEvent>() {
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
        menu.openFile.setOnAction(openClick);
        menu.saveFile.setOnAction(saveClick);
        menu.saveAsFile.setOnAction(saveAsClick);
        menu.exitFile.setOnAction(exitClick);
    }
    
    private void closeProgram(Stage stage){
        String answer = ConfirmExit.display("PainT", "Do you want to save changes?");
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
                stage.close();
                break;
            case "dontSave":
                stage.close();
                break;
            case "cancel":
                break;
        }
                
    }

    //For future purpose.
    /**
     * @param args the command line arguments
    
    */
    class ResizableCanvas extends Canvas {
 
        public ResizableCanvas() {
            // Redraw canvas when size changes.
            widthProperty().addListener(evt -> draw());
            heightProperty().addListener(evt -> draw());
        }
 
        private void draw() {
            double width = getWidth();
            double height = getHeight();
 
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, width, height);
 
            gc.setStroke(Color.RED);
            gc.strokeLine(0, 0, width, height);
            gc.strokeLine(0, height, width, 0);
        }
 
        @Override
        public boolean isResizable() {
            return true;
        }
 
        @Override
        public double prefWidth(double height) {
            return getWidth();
        }
 
        @Override
        public double prefHeight(double width) {
            return getHeight();
        }
    }
    
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
