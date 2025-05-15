package com.uno.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class MenuController {

    @FXML
    private void onActionJugarButton(ActionEvent event) throws IOException {
        System.out.println("El juego inicia");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/uno/uno-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Juego");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onActionCreditosButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/uno/credits-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Créditos");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onActionSalirButton(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void onActionInstruccionesButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/uno/instructions-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Instrucciones");
        stage.setScene(scene);
        stage.show();
    }
}