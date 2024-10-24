/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author shree
 */
public class EditTools {
    //EditTools
    public TextField widthEdit;
    public int lineWidth = 5;
    public Label strokeLabel;
    public Label fillLabel;
    public ColorPicker colorPicker;
    public ColorPicker fillPicker;
    public Label sliderLabel;
    public Slider slider;
    public int s_MinValue = 1, s_MaxValue = 10;
    public ToolBar drawTools = new ToolBar();
    public HBox editToolsBox = new HBox(10);
    
    public ToggleButton pencilTool;
    public ToggleButton lineTool;
    public ToggleButton rectangleTool;
    public ToggleButton squareTool;
    public ToggleButton ellipseTool;
    public ToggleButton circleTool;
    public ToggleButton triangleTool;
    public ToggleButton polygonTool;
    public ToggleButton colorGrabberTool;
    public Button undoTool;
    public Button redoTool;
    public ToggleButton eraserTool;
    public ToggleButton textTool;
    public ToolBar infoBar = new ToolBar();
    public Text toolInfo = new Text();
    public ToggleButton selectTool;
    public ToggleButton moveTool;
    public ToggleButton copyTool;
    ToggleButton[] toggleButtons;
    public ToggleGroup toggleGroup = new ToggleGroup();
    public Button zoomIn, zoomOut;
    public CheckBox autosaveBox;
    public ToggleButton emojiTool;
    public ComboBox emojiList;
    
    public GridPane toolsGrid = new GridPane();
    //EditTools
    
    public void editTool(){
        //Colorpicker and label for stroke
        strokeLabel = new Label("Stroke: ");
        colorPicker = new ColorPicker(Color.RED); 
        colorPicker.getStyleClass().add("button");
        colorPicker.setStyle("-fx-pref-width: 100px; -fx-pref-height: 35px");
        fillLabel = new Label("Fill: "); //Fill Label
        fillPicker = new ColorPicker(Color.BLUE); //Fill color picker
        fillPicker.getStyleClass().add("button");
        fillPicker.setStyle("-fx-pref-width: 100px; -fx-pref-height: 35px");
        //Slider and it's functions
        sliderLabel = new Label("Line Width");
        slider = new Slider(s_MinValue, s_MaxValue, lineWidth);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(1);
        slider.setBlockIncrement(1);
        
        infoBar.getItems().add(toolInfo);
        
        //ToolBar
        Image freeImg = new Image(getClass().getResourceAsStream("free.png"));
        Image lineImg = new Image(getClass().getResourceAsStream("line.png"));
        Image rectImg = new Image(getClass().getResourceAsStream("rect.png"));
        Image squareImg = new Image(getClass().getResourceAsStream("square.png"));
        Image ellipseImg = new Image(getClass().getResourceAsStream("ellipse.png"));
        Image circleImg = new Image(getClass().getResourceAsStream("circle.png"));
        ImageView freeView = new ImageView(freeImg);
        ImageView lineView = new ImageView(lineImg);
        ImageView rectView = new ImageView(rectImg);       
        ImageView squareView = new ImageView(squareImg);
        ImageView ellipseView = new ImageView(ellipseImg);
        ImageView circleView = new ImageView(circleImg);
        setToolSize(freeView);
        setToolSize(lineView);
        setToolSize(rectView);
        setToolSize(squareView);
        setToolSize(ellipseView);
        setToolSize(circleView);
        
        Image dropperImg = new Image(getClass().getResourceAsStream("dropper.png"));
        ImageView dropperView = new ImageView(dropperImg);
        setToolSize(dropperView);
        Image eraserImg = new Image(getClass().getResourceAsStream("eraser.png"));
        ImageView eraserView = new ImageView(eraserImg);
        setToolSize(eraserView);
        Image undoImg = new Image(getClass().getResourceAsStream("undo.png"));
        ImageView undoView = new ImageView(undoImg);
        setToolSize(undoView);
        Image redoImg = new Image(getClass().getResourceAsStream("redo.png"));
        ImageView redoView = new ImageView(redoImg);
        setToolSize(redoView);
        Image textImg = new Image(getClass().getResourceAsStream("text.png"));
        ImageView textView = new ImageView(textImg);
        setToolSize(textView);
        Image selectImg = new Image(getClass().getResourceAsStream("select.png"));
        ImageView selectView = new ImageView(selectImg);
        setToolSize(selectView);
        Image moveImg = new Image(getClass().getResourceAsStream("move.png"));
        ImageView moveView = new ImageView(moveImg);
        setToolSize(moveView);
        Image triangleImg = new Image(getClass().getResourceAsStream("triangle.png"));
        ImageView triangleView = new ImageView(triangleImg);
        setToolSize(triangleView);
        Image polygonImg = new Image(getClass().getResourceAsStream("polygon.png"));
        ImageView polygonView = new ImageView(polygonImg);
        setToolSize(polygonView);
        Image emojiImg = new Image(getClass().getResourceAsStream("laughcry.png"));
       
        ImageView emojiView = new ImageView(emojiImg);
        setToolSize(emojiView);
        
        pencilTool = new ToggleButton("", freeView);
        lineTool = new ToggleButton("", lineView);
        rectangleTool = new ToggleButton("", rectView);
        
       
        squareTool = new ToggleButton("", squareView);
        ellipseTool = new ToggleButton("", ellipseView);
        circleTool = new ToggleButton("", circleView);
        triangleTool = new ToggleButton("", triangleView);
        polygonTool = new ToggleButton("", polygonView);
        undoTool = new Button("", undoView);
        redoTool = new Button("", redoView);
        textTool = new ToggleButton("", textView);
        emojiTool = new ToggleButton("", emojiView);
        //Color Grabber/Dropper
        colorGrabberTool = new ToggleButton("", dropperView);
        autosaveBox = new CheckBox("Autosave");
        autosaveBox.setSelected(false);
        
        eraserTool = new ToggleButton("", eraserView);
        selectTool = new ToggleButton("", selectView);
        moveTool = new ToggleButton("", moveView);
        copyTool = new ToggleButton("Copy");
        ToggleButton[] tempButtons = {pencilTool, lineTool, rectangleTool, squareTool, ellipseTool, circleTool, textTool, colorGrabberTool, eraserTool, selectTool, moveTool, triangleTool, polygonTool, copyTool, emojiTool};
        pencilTool.setTooltip(new Tooltip("Brush"));
        lineTool.setTooltip(new Tooltip("Line"));
        rectangleTool.setTooltip(new Tooltip("Rectangle"));
        squareTool.setTooltip(new Tooltip("Square"));
        ellipseTool.setTooltip(new Tooltip("Ellipse"));
        circleTool.setTooltip(new Tooltip("Circle"));
        textTool.setTooltip(new Tooltip("Text"));
        colorGrabberTool.setTooltip(new Tooltip("Color Dropper"));
        eraserTool.setTooltip(new Tooltip("Eraser"));
        selectTool.setTooltip(new Tooltip("Select"));
        moveTool.setTooltip(new Tooltip("Move"));
        triangleTool.setTooltip(new Tooltip("Triangle"));
        polygonTool.setTooltip(new Tooltip("Polygon"));
        copyTool.setTooltip(new Tooltip("Copy"));
        undoTool.setTooltip(new Tooltip("Undo"));
        redoTool.setTooltip(new Tooltip("Redo"));
        emojiTool.setTooltip(new Tooltip("Emoji"));
        
        emojiList = new ComboBox();
        
        Image laughcryEmo = new Image(getClass().getResourceAsStream("laughcry.png"));
        Image smileEmo = new Image(getClass().getResourceAsStream("happy.png"));
        Image cryEmo = new Image(getClass().getResourceAsStream("cry.png"));
        ImageView laughcryView = new ImageView(laughcryEmo);
        ImageView smileView = new ImageView(smileEmo);
        ImageView cryView = new ImageView(cryEmo);
        setToolSize(laughcryView);
        setToolSize(smileView);
        setToolSize(cryView);
        emojiList.getItems().addAll(laughcryView, smileView, cryView);
        emojiList.setValue(laughcryView);
        emojiList.setPrefWidth(emojiList.getWidth() - 10);
//        emojiList.setDisable(true);
        
        toggleButtons = new ToggleButton[15];
        for (int i = 0; i < toggleButtons.length; i++) {
            toggleButtons[i] = tempButtons[i];
        }
        
        
        for(ToggleButton t : toggleButtons){
            t.setToggleGroup(toggleGroup);
            t.setCursor(Cursor.HAND);
           
        }
        drawTools.getItems().addAll(pencilTool, lineTool, rectangleTool, squareTool, ellipseTool, circleTool);
        toolsGrid.add(pencilTool, 0, 0);
        toolsGrid.add(lineTool, 1, 0);
        toolsGrid.add(rectangleTool, 2, 0);
        toolsGrid.add(squareTool, 0, 1);
        toolsGrid.add(ellipseTool, 1, 1);
        toolsGrid.add(circleTool, 2, 1);
        toolsGrid.add(triangleTool, 0, 2);
        toolsGrid.add(polygonTool, 1, 2);
        
        //Button Event Handler
        VBox fillBox = new VBox();
        fillBox.getChildren().addAll(fillLabel, fillPicker);
        VBox strokeBox = new VBox();
        strokeBox.getChildren().addAll(strokeLabel, colorPicker);
        VBox sliderBox = new VBox();
        sliderBox.getChildren().addAll(sliderLabel, slider);
        
        GridPane fssGrid = new GridPane();
        fssGrid.add(fillBox, 0, 0);
        fssGrid.add(strokeBox, 1, 0);
        fssGrid.add(sliderBox, 2, 0);
        
        zoomOut = new Button("-");
        zoomIn = new Button("+");
        //Sets property for the toolbar
//        editToolsBox.setMinHeight(40);
//        editToolsBox.setMaxHeight(40);
        editToolsBox.getChildren().add(0, toolsGrid);
        editToolsBox.getChildren().add(1, fssGrid);
//        editToolsBox.getChildren().add(2, fillBox);
//        editToolsBox.getChildren().add(3, fillPicker);
//        editToolsBox.getChildren().add(4, slider); 
//        editToolsBox.getChildren().add(3, sliderBox);
        editToolsBox.getChildren().add(2, colorGrabberTool);
        editToolsBox.getChildren().add(3, eraserTool);
        editToolsBox.getChildren().add(4, undoTool);
        editToolsBox.getChildren().add(5, redoTool);
        editToolsBox.getChildren().add(6, textTool);
        editToolsBox.getChildren().add(7, selectTool);
        editToolsBox.getChildren().add(8, moveTool);
        editToolsBox.getChildren().add(9, copyTool);
        editToolsBox.getChildren().add(10, emojiTool);
        editToolsBox.getChildren().add(11, emojiList);
        editToolsBox.getChildren().add(12, zoomOut);
        editToolsBox.getChildren().add(13, zoomIn);
        editToolsBox.getChildren().add(14, autosaveBox);
    }
    
    public void setToolSize(ImageView iView){
        iView.setFitHeight(20);
        iView.setFitWidth(20);
        iView.setPreserveRatio(true);
    }
}
