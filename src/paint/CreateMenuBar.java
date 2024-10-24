/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.VBox;

/**
 *
 * @author shree
 */
public class CreateMenuBar {
     //Menu
    public MenuBar mBar;
    public MenuItem openFile;
    public MenuItem saveFile;
    public MenuItem saveAsFile;
    public MenuItem exitFile;
    public MenuItem toolHelp;
    public MenuItem about;
    public VBox menuBox = new VBox();
    
    public void createMenu(){
        mBar = new MenuBar();
        
        //All the menus, menuBar and menuItems
        javafx.scene.control.Menu file = new javafx.scene.control.Menu("_File");
        openFile = new MenuItem("Open");
        saveFile = new MenuItem("Save");
        saveAsFile = new MenuItem("Save As");
        exitFile = new MenuItem("Exit");
        
        //Shortcuts
        saveFile.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN));
        saveAsFile.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN));
        openFile.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN));
        
        //Aidding menu Items
        file.getItems().add(openFile);
        file.getItems().add(saveFile);
        file.getItems().add(saveAsFile);
        file.getItems().add(new SeparatorMenuItem());
        file.getItems().add(exitFile);
        mBar.getMenus().add(file);
        
        javafx.scene.control.Menu edit = new javafx.scene.control.Menu("_Edit");
        mBar.getMenus().add(edit);
        
        javafx.scene.control.Menu tools = new javafx.scene.control.Menu("_Tools");
        mBar.getMenus().add(tools);
        
        javafx.scene.control.Menu help = new javafx.scene.control.Menu("_Help");
        toolHelp = new MenuItem("Tools Help");
        about = new MenuItem("About");
        help.getItems().add(toolHelp);
        help.getItems().add(about);
        mBar.getMenus().add(help);
        menuBox.getChildren().add(mBar);
    }
}
