/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.util.Stack;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author shree
 */
public class DrawingFunc {
    public DrawingFunc(EditTools eT, GraphicsContext gC, Canvas c, Pane p, int X, int Y, int XX, int YY, Stack undo){
        editTool = eT;
        
        graphicsContext = gC;
      
        canvas = c;
        pane = p;
        startX = X;
        startY = Y;
        endX = XX;
        endY = YY;
        undoStack = undo;
     
    }
    
    public void update(){
        
    }
    

    EditTools editTool;
    GraphicsContext graphicsContext;
    Canvas canvas;
    Pane pane;
    int startX;
    int startY;
    int endX;
    int endY;
    
    public Rectangle rectangle;
    public Rectangle square;
    public Ellipse ellipse;
    public Ellipse circle;
    public Polygon triangle;
    public Polygon polygon;
    public double[] points;
    public double[] pointsY;
    public int count = 0;
    
    int numberOfSides;
    ImageView selectedImg;
    Stack undoStack; 
    
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
}
