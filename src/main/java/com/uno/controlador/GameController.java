package com.uno.controlador;

import com.uno.modelo.Baraja;
import com.uno.modelo.Carta;
import com.uno.modelo.CartaInvalidaException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
    private boolean unoPresionado = false;
    private boolean cpuDijoUNO = false;

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

                if (manoHumano.size() == 1) {
                    unoPresionado = false;

                    // Iniciar el temporizador de castigo en un hilo
                    new Thread(() -> {
                        try {
                            int tiempo = 2000 + new Random().nextInt(2000);
                            Thread.sleep(tiempo);
                            if (!unoPresionado) {
                                Platform.runLater(() -> {
                                    // Castigar al jugador: robar 2 cartas
                                    manoHumano.addAll(baraja.robarVarias(2));
                                    mostrarCartasJugador();
                                    System.out.println("¡No dijiste UNO a tiempo! Robas 2 cartas.");
                                });
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }

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
            if (!carta.getColor().equals("All")) {
                throw new CartaInvalidaException("Carta Invalida");
            }
        }
    }

    private void jugarTurnoCPU() {
        turnoLabel.setText("Turno: Máquina");
        Random random = new Random();
        int tiempoPensamiento = 1 + random.nextInt(3);
        PauseTransition pausa = new PauseTransition(Duration.seconds(tiempoPensamiento));

        pausa.setOnFinished(e -> {
            System.out.println("La maquina está pensando");

            for (Carta carta : manoCPU) {
                if (carta.getColor().equals(cartaCentroActual.getColor()) ||
                        carta.getValor() == cartaCentroActual.getValor()) {

                    System.out.println("La maquina juega: " + carta);
                    mostrarCartaCentral(carta);
                    manoCPU.remove(carta);
                    mostrarCartasMaquina();

                    if (manoCPU.size() == 1) {
                        cpuDijoUNO = false;

                        new Thread(() -> {
                            try {
                                int delay = 500 + random.nextInt(3000);
                                Thread.sleep(delay);

                                if (random.nextBoolean()) {
                                    cpuDijoUNO = true;
                                    System.out.println("La máquina dice UNO a tiempo.");
                                }

                                if (!cpuDijoUNO) {
                                    Platform.runLater(() -> {
                                        manoCPU.addAll(baraja.robarVarias(2));
                                        System.out.println("¡La máquina no dijo UNO! Roba 2 cartas.");
                                        mostrarCartasMaquina();
                                    });
                                }

                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }).start();
                    }

                    turnoLabel.setText("Turno: Jugador");
                    turnoHumano = true;
                    return;
                }
            }

            Carta robada = baraja.robarCarta();
            if (robada != null) {
                manoCPU.add(robada);
                mostrarCartasMaquina();
                System.out.println("La máquina no tiene carta válida, roba una de la baraja.");
            }

            turnoLabel.setText("Turno: Jugador");
            turnoHumano = true;
        });

        pausa.play();
    }

    @FXML
    private void onActionPasarTurnoButton(ActionEvent event) {
        // Roba una carta
        if(turnoHumano){
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
        } else{
            System.out.println("No puedes robar cartas mientras no es tu turno");
        }

    }

    @FXML
    private void onActionUnoButton(ActionEvent event) {
        if (manoHumano.size() == 1 && !unoPresionado) {
            unoPresionado = true;
            System.out.println("¡UNO presionado a tiempo!");
        }
    }
}