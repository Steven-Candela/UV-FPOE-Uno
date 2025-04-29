package com.uno.vista;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InstructionsView {
    public InstructionsView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uno/instructions-view.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Instrucciones");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
