/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

/**
 *
 * @author shree
 */
public class CheckSelected {
    public String selectedTool;
    public EditTools editTool;
    public PainT p;
    
    public CheckSelected(EditTools editTool, PainT p){
        this.editTool = editTool;
        this.p = p;
    }
    
    /**
     * Shows which tool is selected based on the toggle selects of the buttons.
     */
    public void checkTrue(){
        if(editTool.pencilTool.isSelected()){
            editTool.toolInfo.setText("Free line selected");
            selectedTool = "Free line selected";
            p.tLogS(selectedTool);
        }else if(editTool.lineTool.isSelected()){
            editTool.toolInfo.setText("Line selected");
            selectedTool = "Line selected";
            p.tLogS(selectedTool);
        }else if(editTool.rectangleTool.isSelected()){
            editTool.toolInfo.setText("Rectangle selected");
            selectedTool = "Rectangle selected";
            p.tLogS(selectedTool);
        }
        else if(editTool.squareTool.isSelected()){
            editTool.toolInfo.setText("Square selected");
            selectedTool = "Square selected";
            p.tLogS(selectedTool);
        }
        else if(editTool.ellipseTool.isSelected()){
            editTool.toolInfo.setText("Ellipse selected");
            selectedTool = "Ellipse selected";
            p.tLogS(selectedTool);
        }
        else if(editTool.circleTool.isSelected()){
            editTool.toolInfo.setText("Circle selected");
            selectedTool = "Circle selected";
            p.tLogS(selectedTool);
        }else if(editTool.eraserTool.isSelected()){
            editTool.toolInfo.setText("Eraser selected");
            selectedTool = "Eraser selected";
            p.tLogS(selectedTool);
        }else if(editTool.selectTool.isSelected()){
            editTool.toolInfo.setText("Select Tool selected");
            selectedTool = "Select Tool selected";
            p.tLogS(selectedTool);
        }else if(editTool.moveTool.isSelected()){
            editTool.toolInfo.setText("Move Tool selected");
            selectedTool = "Move Tool selected";
            p.tLogS(selectedTool);
        }else if(editTool.copyTool.isSelected()){
            editTool.toolInfo.setText("Copy Tool selected");
            selectedTool = "Copy Tool selected";
            p.tLogS(selectedTool);
        }else if(editTool.triangleTool.isSelected()){
            editTool.toolInfo.setText("Triangle Tool selected");
            selectedTool = "Triangle selected";
            p.tLogS(selectedTool);
        }else if(editTool.polygonTool.isSelected()){
            editTool.toolInfo.setText("Polygon Tool selected");
            selectedTool = "Polygon selected";
            p.tLogS(selectedTool);
        }else{
            editTool.toolInfo.setText("No Tool selected");
            selectedTool = "No Tool selected";
        }
    }
}
