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
 * Clase CreditsController, controlador para la vista de créditos del juego.
 * Se encarga de gestionar las acciones del usuario en la pantalla de créditos.
 *
 * @author Nicolle Paz
 */
public class CreditsController {

    @FXML
    private Button volverAlMenuButton;

    /**
     * Maneja el evento del botón "Volver al Menú".
     * Carga la vista del menú principal y cambia la escena actual por ella.
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