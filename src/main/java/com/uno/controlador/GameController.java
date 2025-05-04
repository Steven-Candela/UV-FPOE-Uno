package com.uno.controlador;

import com.uno.modelo.Baraja;
import com.uno.modelo.Carta;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;

import java.util.List;

public class GameController {

    private Baraja baraja;
    private List<Carta> manoHumano;
    private List<Carta> manoCPU;
    private Carta cartaSeleccionada;

    @FXML
    private HBox manoJugador;

    @FXML
    private ImageView cartaCentro;

    @FXML
    private void initialize() {
        baraja = new Baraja();

        // Repartir cartas
        manoHumano = baraja.robarVarias(5);
        manoCPU = baraja.robarVarias(5);

        // Carta inicial al centro
        Carta cartaInicial = baraja.robarCarta();
        mostrarCartaCentral(cartaInicial);

        // Mostrar mano del jugador humano
        mostrarCartasJugador();
    }

    private void mostrarCartaCentral(Carta carta) {
        if (carta != null) {
            String ruta = "/com/uno/imagenes/" + carta.getImagen();
            Image img = new Image(getClass().getResourceAsStream(ruta));
            cartaCentro.setImage(img);
        }
    }

    private void mostrarCartasJugador() {
        manoJugador.getChildren().clear();

        for (Carta carta : manoHumano) {
            String ruta = "/com/uno/imagenes/" + carta.getImagen();
            Image img = new Image(getClass().getResourceAsStream(ruta));
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(80);
            imgView.setFitHeight(120);

            // Acción de clic para seleccionar carta
            imgView.setOnMouseClicked(e -> {
                cartaSeleccionada = carta;
                System.out.println("Carta seleccionada: " + cartaSeleccionada);
            });

            manoJugador.getChildren().add(imgView);
        }
    }

    @FXML
    private void onActionJugarCartaButton(ActionEvent event) {
        if (cartaSeleccionada != null) {
            // Jugar la carta
            mostrarCartaCentral(cartaSeleccionada);
            manoHumano.remove(cartaSeleccionada);
            cartaSeleccionada = null;
            mostrarCartasJugador();
            System.out.println("Carta jugada con éxito.");
        } else {
            System.out.println("Ninguna carta seleccionada.");
        }
    }

    @FXML
    private void onActionPasarTurnoButton(ActionEvent event) {
        System.out.println("Botón 'Pasar Turno' presionado.");
    }

    @FXML
    private void onActionUnoButton(ActionEvent event) {
        System.out.println("Botón 'UNO' presionado.");
    }
}