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
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author shree
 */
public class Draw {
    public PainT p;
    GraphicsContext graphicsContext;
    int endX, endY, startX, startY;
    Polygon triangle;
    Ellipse ellipse;
    Ellipse circle;
    Canvas canvas;
    Rectangle rectangle;
    ImageView selectedImg;
    Stack undoStack;
    
    public Draw(PainT paint){
        p = paint;
        init();
    }
    
    public void init(){
        canvas = p.canvas;
        graphicsContext = p.graphicsContext;
        endX = p.endX;
        endY = p.endY;
        startX = p.startX;
        startY = p.startY;
        
        triangle = p.triangle;
        ellipse = p.ellipse;
        circle = p.circle;
        rectangle = p.rectangle;
        selectedImg = p.selectedImg;
        undoStack = p.undoStack;
       
       
    }
    
    public void paintTriangle(){
        init();
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
    
    public void paintLine(){
        init();
        
        graphicsContext = p.graphicsContext;
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
    
    /**
     * Strokes and fills the rectangle using the coordinates.
     */
    public void paintRect(){
        init();
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
    
     /**
     * Strokes and fills the square using the coordinates.
     * 
     */
    public void paintSquare(){
        init();
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
        init();
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
        init();
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
    
    
     /**
     * Selects the part of the image using a rectangle
     
     */
    public void selectRect(){
        init();
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
        
 
        
    }
    
}
