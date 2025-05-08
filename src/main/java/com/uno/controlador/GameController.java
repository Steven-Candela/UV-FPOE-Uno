package com.uno.controlador;

import com.uno.modelo.Baraja;
import com.uno.modelo.Carta;
import com.uno.modelo.CartaInvalidaException;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

public class GameController {

    private Baraja baraja;
    private List<Carta> manoHumano;
    private List<Carta> manoCPU;
    private Carta cartaSeleccionada;
    private Carta cartaCentroActual;
    private boolean turnoHumano = true;

    @FXML
    private HBox manoJugador;

    @FXML
    private HBox manoMaquina;

    @FXML
    private ImageView cartaCentro;

    @FXML
    private Label turnoLabel;

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
        // Mostrar mano de la máquina
        mostrarCartasMaquina();
    }

    private void mostrarCartaCentral(Carta carta) {
        if (carta != null) {
            cartaCentroActual = carta;
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

    private void mostrarCartasMaquina() {
        manoMaquina.getChildren().clear();

        for (int i = 0; i < manoCPU.size(); i++) {
            Image img = new Image(getClass().getResourceAsStream("/com/uno/imagenes/card_uno.png")); // reverso
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(80);
            imgView.setFitHeight(120);
            manoMaquina.getChildren().add(imgView);
        }
    }

    @FXML
    private void onActionJugarCartaButton(ActionEvent event) {
        if (!turnoHumano) {
            System.out.println("No es tu turno");
            return;
        }

        if (cartaSeleccionada != null) {
            // Jugar la carta
            try {
                validarCarta(cartaSeleccionada);
                mostrarCartaCentral(cartaSeleccionada);
                manoHumano.remove(cartaSeleccionada);
                cartaSeleccionada = null;
                mostrarCartasJugador();
                System.out.println("Carta jugada con éxito.");

                turnoHumano = false;
                jugarTurnoCPU();
            } catch (CartaInvalidaException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Ninguna carta seleccionada.");
        }
    }

    @FXML
    private void validarCarta(Carta carta) throws CartaInvalidaException {
        if (!carta.getColor().equals(cartaCentroActual.getColor()) &&
        carta.getValor() != cartaCentroActual.getValor()) {
        throw new CartaInvalidaException("Carta Invalida");
        }
    }

    private void jugarTurnoCPU() {
        turnoLabel.setText("Turno: Máquina");

        Random random = new Random();
        int tiempoPensamiento = 1 + random.nextInt(3);
        PauseTransition pausa = new PauseTransition(Duration.seconds(tiempoPensamiento));
        pausa.setOnFinished(e -> {
            System.out.println("La maquina está pensando");

            // Busca una carta valida
            for (Carta carta : manoCPU) {
                if (carta.getColor().equals(cartaCentroActual.getColor()) ||
                carta.getValor() == cartaCentroActual.getValor()) {

                    System.out.println("La maquina juega: " + carta);
                    mostrarCartaCentral(carta);
                    manoCPU.remove(carta);
                    mostrarCartasMaquina();

                    turnoLabel.setText("Turno: Jugador");
                    turnoHumano = true;
                    return;
                }
            }

            // Si no tiene carta valida, roba una
            Carta robada = baraja.robarCarta();
            if (robada != null) {
                manoCPU.add(robada);
                mostrarCartasMaquina();
                System.out.println("La maquina no tiene carta valida, tons roba una de la baraja");
            }

            turnoLabel.setText("Turno: Jugador");
            turnoHumano = true;
        });
        pausa.play();
    }

    @FXML
    private void onActionPasarTurnoButton(ActionEvent event) {
        // Roba una carta
        Carta cartaRobada = baraja.robarCarta();
        if (cartaRobada != null) {
            manoHumano.add(cartaRobada);
            mostrarCartasJugador();
        } else {
            System.out.println("No hay cartas para robar");
        }

        // Cambia el turno
        turnoHumano = false;
        jugarTurnoCPU();
    }

    @FXML
    private void onActionUnoButton(ActionEvent event) {
        System.out.println("Botón 'UNO' presionado.");
    }
}