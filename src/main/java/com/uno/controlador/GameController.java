package com.uno.controlador;

import com.uno.modelo.Baraja;
import com.uno.modelo.Carta;
import com.uno.modelo.CartaEspecial;
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
    private boolean turnoCambio = false;
    private boolean maquinaPenalizada = false;

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
                if(cartaSeleccionada.EsEspecial()){
                   cartaSeleccionada.setColor(EspecialJugada(cartaSeleccionada, turnoHumano, cartaSeleccionada.getColor()));
                }
                manoHumano.remove(cartaSeleccionada);
                cartaSeleccionada = null;
                mostrarCartasMaquina();
                mostrarCartasJugador();
                System.out.println("Carta jugada con éxito.");

                if (manoHumano.size() == 1) {
                    unoPresionado = false;

                    new Thread(() -> {
                        try {
                            // El hilo duerme por 2 segundos
                            Thread.sleep(2000);

                            // Espera entre 0 y 2 segundos más
                            int extraDelay = new Random().nextInt(2000);
                            Thread.sleep(extraDelay);

                            // Revisar si presionó UNO en ese lapso
                            if (!unoPresionado) {
                                Platform.runLater(() -> {
                                    manoHumano.addAll(baraja.robarVarias(1));
                                    mostrarCartasJugador();
                                    System.out.println("¡No dijiste UNO a tiempo! Robas 1 carta.");
                                });
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
                if (!turnoCambio) {
                    turnoHumano = false;
                    jugarTurnoCPU();
                }
                turnoCambio = false;
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

            // Busca una carta valida
            for (Carta carta : manoCPU) {
                if (carta.getColor().equals(cartaCentroActual.getColor()) ||
                        carta.getValor() == cartaCentroActual.getValor()) {

                    System.out.println("La maquina juega: " + carta);
                    mostrarCartaCentral(carta);
                    cartaSeleccionada = carta;
                    if(cartaSeleccionada.EsEspecial()){
                        cartaSeleccionada.setColor(EspecialJugada(cartaSeleccionada, turnoHumano, cartaSeleccionada.getColor()));
                    }
                    manoCPU.remove(carta);
                    mostrarCartasMaquina();
                    mostrarCartasJugador();

                    if (manoCPU.size() == 1) {
                        cpuDijoUNO = false;

                        new Thread(() -> {
                            try {
                                Thread.sleep(2000 + new Random().nextInt(2000)); // 2 a 4 segundos
                                if (!cpuDijoUNO) {
                                    cpuDijoUNO = true;
                                    Platform.runLater(() -> System.out.println("La máquina dice UNO a tiempo"));
                                }
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }).start();
                    }
                    if (!turnoCambio){
                        turnoLabel.setText("Turno: Jugador");
                        turnoHumano = true;
                    } else {
                        turnoCambio = false;
                        jugarTurnoCPU();
                    }
                    return;
                }
            }
            // Si no tiene carta valida, roba una
            Carta robada = baraja.robarCarta();
            if (robada != null) {
                manoCPU.add(robada);
                System.out.println("La maquina no tiene carta valida, roba una de la baraja");
            }
            turnoLabel.setText("Turno: Jugador");
            turnoHumano = true;
            mostrarCartasMaquina();
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
        if (manoHumano.size() == 1) {
            unoPresionado = true;
            System.out.println("¡UNO presionado a tiempo!");
        }
        if (manoCPU.size() == 1 && !cpuDijoUNO) {
            manoCPU.addAll(baraja.robarVarias(1));
            cpuDijoUNO = true;
            mostrarCartasMaquina();
            System.out.println("¡El jugador fue más rápido que la máquina! La máquina roba 1 carta.");
        }
    }


    private String EspecialJugada(Carta cartaEspecial, boolean TurnoesHumano, String color) {
        String[] colores = {"blue", "red", "green", "yellow"};
        String habilidad = cartaEspecial.getHabilidad();
        if (habilidad.equals("skip")){
            if (TurnoesHumano) {
                System.out.println("El jugador ha usado una carta de skip");
                System.out.println("La maquina pierde su turno");
                turnoHumano = true;
                turnoCambio = true;
            } else {
                System.out.println("La maquina ha usado una carta de skip");
                System.out.println("El jugador pierde su turno");
                turnoHumano = false;
                turnoCambio = true;
            }
        }
        if (habilidad.equals("+2")){
            if (!TurnoesHumano) {
                for (int i = 0; i < 2; i++){
                    Carta robada = baraja.robarCarta();
                    manoHumano.add(robada);
                }
                System.out.println("La maquina jugó un +2, el jugador roba 2 cartas");
                System.out.println("El jugador pierde su turno");
                turnoHumano = false;
                turnoCambio = true;

            } else {
                for (int i = 0; i < 2; i++) {
                    Carta robada = baraja.robarCarta();
                    manoCPU.add(robada);
                }
                System.out.println("La jugador jugó un +2, la maquina roba 2 cartas");
                System.out.println("La maquina pierde su turno");
                turnoHumano = true;
                turnoCambio = true;
            }
        }
        if (habilidad.equals("+4")) {
            if (!TurnoesHumano) {
                for (int i = 0; i < 4; i++) {
                    Carta robada = baraja.robarCarta();
                    manoHumano.add(robada);
                }
                System.out.println("La maquina jugó un +4, el jugador roba 4 cartas");
                System.out.println("La maquina está eligiendo un color...");
                Random rand = new Random();
                int indice = rand.nextInt(colores.length);
                String colorElegido = colores[indice];
                System.out.println("Color elegido: " + colorElegido);
                color = colorElegido;
                System.out.println("El jugador pierde su turno");
                turnoHumano = false;
                turnoCambio = true;

            } else {
                for (int i = 0; i < 4; i++) {
                    Carta robada = baraja.robarCarta();
                    manoCPU.add(robada);
                }
                //Elegir color manualmente (Jugador)

                System.out.println("El jugador jugó un +4, la maquina roba 4 cartas");
                System.out.println("La maquina pierde su turno");
                turnoHumano = true;
                turnoCambio = true;
            }
        }

        if (habilidad.equals("SelectColor")) {
            if(!TurnoesHumano) {
                System.out.println("La maquina está eligiendo un color...");
                Random rand = new Random();
                int indice = rand.nextInt(colores.length);
                String colorElegido = colores[indice];
                System.out.println("Color elegido: " + colorElegido);
                color = colorElegido;
            } else {
                System.out.println("el humano elije un color...");
            }
        }
        System.out.println(color);
        return color;
    }
}