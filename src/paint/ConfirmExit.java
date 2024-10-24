/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author shree
 */
public class ConfirmExit{
        static String answer;
        public static String display(String title, String header){
            //For displaying the smart save dialog box after pressing close.
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle(title);
            
            Label head = new Label(header);
            Button save = new Button("Save");
            Button dontSave = new Button("Don't Save");
            Button cancel = new Button("Cancel");
            
            save.setOnAction(e-> {answer = "save"; window.close();});       
            dontSave.setOnAction(e-> {answer = "dontSave"; window.close();});
            cancel.setOnAction(e-> {answer = "cancel"; window.close();});
            
            VBox vBox = new VBox(10);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(head,save,dontSave,cancel);
            
            Scene scene = new Scene(vBox, 300, 200);
            window.setScene(scene);
            window.showAndWait();
            
            return answer;
        }
}
