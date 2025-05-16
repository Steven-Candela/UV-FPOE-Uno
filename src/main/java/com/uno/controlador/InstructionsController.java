package com.uno.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase InstructionsController, controlador para la vista de instrucciones del juego UNO.
 * Se encarga de gestionar la navegación desde la pantalla de instrucciones de regreso al
 * menú principal.
 *
 * @author Nicolle Paz
 */
public class InstructionsController {

    @FXML
    private Button volverAlMenuButton;

    /**
     * Maneja el evento del botón "Volver al Menú".
     * Carga la vista del menú principal y reemplaza la escena actual.
     *
     * @param event, el evento de acción generado por el botón.
     * @throws IOException si ocurre un error al cargar el archivo FXML del menú.
     */
    @FXML
    private void onActionVolverAlMenuButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/uno/menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Menú Principal");
        stage.setScene(scene);
        stage.show();
    }
}