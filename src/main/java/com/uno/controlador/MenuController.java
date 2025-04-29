package com.uno.controlador;

import com.uno.vista.CreditsView;
import com.uno.vista.GameView;
import com.uno.vista.InstructionsView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private Button creditosButton;

    @FXML
    private Button jugarButton;

    @FXML
    private Button salirButton;

    @FXML
    private void onActionSalirButton() {System.exit(0);
    }

    @FXML
    private void onActionInstruccionesButton(ActionEvent event) throws IOException {
        new InstructionsView();
    }

    @FXML
    void onActionJugarButton(ActionEvent event) {
    }

    @FXML
    private void onActionCreditosButton(ActionEvent event) throws IOException {
    }
}
