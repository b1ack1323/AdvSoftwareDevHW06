package SQL_Manager;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 *
 *  Created by tblack on 8/31/16.
 *
 * Custom Popup for FX related Projects
 *
 */



public class CstPopup extends Stage {
    //Constructor creates a label and puts it in VBox, scene is set and
    ArrayList<String> options;

    public CstPopup(ArrayList<String> in){
        options=in;
    }

    public String getResponse(){

        ListView x = new ListView<>();
        final String[] ans = new String[1];
        Button btn = new Button("OK");
        CstPopup self = this;
        btn.setOnAction(actionEvent -> {
            ans[0] = (String) x.getSelectionModel().getSelectedItem();
            self.close();
            System.out.println("==>"+ans[0]);
        });
        x.setItems(FXCollections.observableList(options));
        VBox y = new VBox();
        y.getChildren().add(x);
        y.getChildren().add(btn);
        this.setScene(new Scene(y, 200, 100));
        this.showAndWait();
        return ans[0];
    }
}