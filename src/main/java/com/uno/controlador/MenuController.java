package com.uno.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

/**
 * Controlador del menú principal del juego UNO.
 * Maneja la navegación hacia las distintas vistas del juego: jugar, créditos, instrucciones y salir.
 *
 * @author Nicolle Paz
 */
public class MenuController {
    /**
     * Maneja el evento del botón "Jugar".
     * Carga la vista del juego y la muestra en la ventana actual.
     *
     * @param event, el evento de acción generado por el botón
     * @throws IOException si ocurre un error al cargar la vista FXML del juego
     */
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

    /**
     * Maneja el evento del botón "Créditos".
     * Carga la vista de créditos y la muestra en la ventana actual.
     *
     * @param event el evento de acción generado por el botón
     * @throws IOException si ocurre un error al cargar la vista FXML de créditos
     */
    @FXML
    private void onActionCreditosButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/uno/credits-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Créditos");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Maneja el evento del botón "Salir".
     * Cierra la aplicación.
     *
     * @param event, el evento de acción generado por el botón
     */
    @FXML
    private void onActionSalirButton(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Maneja el evento del botón "Instrucciones".
     * Carga la vista de instrucciones y la muestra en la ventana actual.
     *
     * @param event, el evento de acción generado por el botón
     * @throws IOException si ocurre un error al cargar la vista FXML de instrucciones
     */
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