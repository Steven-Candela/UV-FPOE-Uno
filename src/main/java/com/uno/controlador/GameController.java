package com.uno.controlador;

import com.uno.modelo.Baraja;
import com.uno.modelo.Carta;
import com.uno.modelo.CartaEspecial;
import com.uno.modelo.CartaInvalidaException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
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

    @FXML private HBox manoJugador;

    @FXML private HBox manoMaquina;

    @FXML private ImageView cartaCentro;

    @FXML private Label turnoLabel;

    @FXML private StackPane selecionaColorPane;

    @FXML private Button unoButton;

    @FXML private Label unoVisualLabel;

    @FXML private Button jugarCartaButton;

    @FXML private Button pasarTurnoButton;

    @FXML
    private void initialize() {
        selecionaColorPane.setVisible(false);
        baraja = new Baraja();

        // Repartir cartas
        manoHumano = baraja.robarVarias(5);
        manoCPU = baraja.robarVarias(5);

        // Carta inicial al centro
        Carta cartaInicial;
        do {
            cartaInicial = baraja.robarCarta();
        } while (cartaInicial.EsEspecial());

        mostrarCartaCentral(cartaInicial);

        // Mostrar mano del jugador humano
        mostrarCartasJugador();
        // Mostrar mano de la máquina
        mostrarCartasMaquina();
        String textoValor = (cartaCentroActual.getValor() == -999999999) ? cartaCentroActual.getHabilidad() : String.valueOf(cartaCentroActual.getValor());
        turnoLabel.setText("Turno: Jugador" + " / Color Actual: " + cartaCentroActual.getColor() + " / Última Carta: " + textoValor);
    }

    private void mensajeEstadoActual() {
        String jugadorActual = turnoHumano ? "Jugador" : "Maquina";;
        String textoValor = (cartaCentroActual.getValor() == -999999999) ? cartaCentroActual.getHabilidad() : String.valueOf(cartaCentroActual.getValor());
        turnoLabel.setText("Turno: " + jugadorActual + " / Color Actual: " + cartaCentroActual.getColor() + " / Última Carta: " + textoValor);
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
        unoButton.setDisable(manoHumano.size() != 1);
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

    private void mostrarUnoVisual() {
        unoVisualLabel.setVisible(true);
        unoVisualLabel.setOpacity(1.0);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> unoVisualLabel.setVisible(false));
        pause.play();
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
                mostrarCartasJugador();

                if (manoHumano.isEmpty()) {
                    finalizarJuego("¡El jugador humano ha ganado!");
                    return;
                }

                if (cartaSeleccionada.getColor().equals("All")) {
                    selecionaColorPane.setVisible(true);
                    return; // Espera que el jugador elija un color
                }

                if (cartaSeleccionada.EsEspecial()) {
                    return;
                }

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
                                    List<Carta> cartasPenalizacion = intentarRobarCartas(1);
                                    manoHumano.addAll(cartasPenalizacion);
                                    mostrarCartasJugador();
                                    System.out.println("¡No dijiste UNO a tiempo! Robas 1 carta.");
                                });
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
                /*if (!turnoCambio) {
                    turnoHumano = false;
                    jugarTurnoCPU();
                }*/
                turnoHumano = false;
                mensajeEstadoActual();
                jugarTurnoCPU();
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
        if (!turnoHumano) {
            //turnoLabel.setText("Turno: " + jugadorActual + " / Color Actual: " + cartaCentroActual.getColor() + " / Última Carta: " + textoValor);

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
                            cartaCentroActual.setColor(cartaSeleccionada.getColor());
                            mensajeEstadoActual();
                        }
                        manoCPU.remove(carta);
                        mostrarCartasMaquina();
                        mostrarCartasJugador();

                        if (manoCPU.isEmpty()) {
                            finalizarJuego("¡La máquina ha ganado!");
                            return;
                        }

                        if (manoCPU.size() == 1) {
                            unoButton.setDisable(false);
                            cpuDijoUNO = false;

                            new Thread(() -> {
                                try {
                                    Thread.sleep(2000 + new Random().nextInt(2000)); // 2 a 4 segundos
                                    if (!cpuDijoUNO) {
                                        cpuDijoUNO = true;
                                        Platform.runLater(() -> System.out.println("La máquina dice UNO a tiempo"));
                                        mostrarUnoVisual();
                                    }
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                            }).start();
                        }

                        if (cartaSeleccionada.EsEspecial()) {
                            return;
                        }
                        /*if (!turnoCambio){
                            turnoLabel.setText("Turno: Jugador");
                            turnoHumano = true;
                        } else {
                            turnoCambio = false;
                            jugarTurnoCPU();
                        }*/
                        //turnoLabel.setText("Turno: " + jugadorActual + " / Color Actual: " + cartaCentroActual.getColor() + " / Última Carta: " + textoValor);
                        turnoHumano = true;
                        mensajeEstadoActual();
                        return;
                    }
                }
                // Si no tiene carta valida, roba una
                List<Carta> robadasCPU = intentarRobarCartas(1);
                manoCPU.addAll(robadasCPU);
                System.out.println("La maquina no tiene carta valida, roba una de la baraja");
                turnoHumano = true;
                mensajeEstadoActual();
                //turnoLabel.setText("Turno: " + jugadorActual + " / Color Actual: " + cartaCentroActual.getColor() + " / Última Carta: " + textoValor);
                mostrarCartasMaquina();
            });
            pausa.play();
        }
    }

    private boolean tieneCartaValida(List<Carta> mano) {
        for (Carta carta : mano) {
            try {
                validarCarta(carta);
                return true;
            } catch (CartaInvalidaException e) {
            }
        }
        return false;
    }


    @FXML
    private void onActionPasarTurnoButton(ActionEvent event) {
        if (turnoHumano) {
            if (tieneCartaValida(manoHumano)) {
                System.out.println("Tienes cartas jugables. No puedes pasar turno ni robar.");
                return;
            }
            List<Carta> cartasRobadas = intentarRobarCartas(1);
            manoHumano.addAll(cartasRobadas);
            mostrarCartasJugador();
            // Cambia el turno
            turnoHumano = false;
            mensajeEstadoActual();
            jugarTurnoCPU();
        } else {
            System.out.println("No puedes robar cartas mientras no es tu turno");
        }
    }

    @FXML
    private void onActionUnoButton(ActionEvent event) {
        if (manoHumano.size() == 1) {
            unoPresionado = true;
            System.out.println("¡UNO presionado a tiempo!");
            mostrarUnoVisual();
        }
        if (manoCPU.size() == 1 && !cpuDijoUNO) {
            List<Carta> cartasPenalizacionMaquina = intentarRobarCartas(1);
            manoCPU.addAll(cartasPenalizacionMaquina);
            cpuDijoUNO = true;
            mostrarCartasMaquina();
            System.out.println("¡El jugador fue más rápido que la máquina! La máquina roba 1 carta.");
            mostrarUnoVisual();
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
                mensajeEstadoActual();
                turnoCambio = true;
            } else {
                System.out.println("La maquina ha usado una carta de skip");
                System.out.println("El jugador pierde su turno");
                turnoHumano = false;
                mensajeEstadoActual();
                turnoCambio = true;
                jugarTurnoCPU();
            }
        }
        if (habilidad.equals("+2")){
            if (!TurnoesHumano) {
                List<Carta> robadasHumano = intentarRobarCartas(2);
                manoHumano.addAll(robadasHumano);
                mostrarCartasJugador();
                System.out.println("La maquina jugó un +2, el jugador roba 2 cartas");
                System.out.println("El jugador pierde su turno");
                turnoHumano = false;
                mensajeEstadoActual();
                turnoCambio = true;
                jugarTurnoCPU();
            } else {
                List<Carta> robadasMaquina = intentarRobarCartas(2);
                manoCPU.addAll(robadasMaquina);
                mostrarCartasMaquina();
                System.out.println("El jugador jugó un +2, la maquina roba 2 cartas");
                System.out.println("La maquina pierde su turno");
                turnoHumano = true;
                mensajeEstadoActual();
                turnoCambio = true;
            }
        }
        if (habilidad.equals("+4")) {
            if (!TurnoesHumano) {
                List<Carta> robadasHumano = intentarRobarCartas(4);
                manoHumano.addAll(robadasHumano);
                mostrarCartasJugador();
                System.out.println("La maquina jugó un +4, el jugador roba 4 cartas");
                System.out.println("La maquina está eligiendo un color...");
                Random rand = new Random();
                int indice = rand.nextInt(colores.length);
                String colorElegido = colores[indice];
                System.out.println("Color elegido: " + colorElegido);
                color = colorElegido;
                System.out.println("El jugador pierde su turno");
                turnoHumano = false;
                jugarTurnoCPU();
            } else {
                List<Carta> robadasMaquina = intentarRobarCartas(4);
                manoCPU.addAll(robadasMaquina);
                mostrarCartasMaquina();
                //Elegir color manualmente (Jugador)

                System.out.println("El jugador jugó un +4, la maquina roba 4 cartas");
                System.out.println("La maquina pierde su turno");
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
                turnoHumano = true;
                mensajeEstadoActual();
            } else {
                System.out.println("el humano elije un color...");
                turnoHumano = false;
                mensajeEstadoActual();
                jugarTurnoCPU();
            }
        }
        System.out.println(color);
        return color;
    }

    @FXML
    private void onSeleccionColor (ActionEvent event) {
        Button botonColor = (Button) event.getSource();
        String id = botonColor.getId();


        switch (id) {
            case "rojoButton":
                cartaCentroActual.setColor("red");
                break;
            case "azulButton":
                cartaCentroActual.setColor("blue");
                break;
            case "amarilloButton":
                cartaCentroActual.setColor("yellow");
                break;
            case "verdeButton":
                cartaCentroActual.setColor("green");
                break;
        }
        selecionaColorPane.setVisible(false);
        System.out.println("El jugador selecciono el color " + cartaCentroActual.getColor());
        mensajeEstadoActual();

        if (cartaCentroActual.getHabilidad().equals("SelectColor") && turnoHumano == true) {
            turnoHumano = false;
            jugarTurnoCPU();
        } else {
            turnoHumano = true;
        }

        //turnoHumano = true;
        //jugarTurnoCPU();
    }

    private List<Carta> intentarRobarCartas(int cantidad) {
        if (cantidad <= 0) {
            return new ArrayList<>();
        }

        List<Carta> cartasRobadas = new ArrayList<>();
        boolean seNecesitaReinciar = false;

        for (int i = 0; i < cantidad; i++) {
            if (baraja.tamaño() == 0) {
                if (!seNecesitaReinciar) {
                    System.out.println("La baraja de robo está vacia");
                    baraja.reiniciarCartas(manoHumano, manoCPU, cartaCentroActual);
                    seNecesitaReinciar = true;
                }
                break;
            }
            Carta robada = baraja.robarCarta();
            cartasRobadas.add(robada);
        }
        return cartasRobadas;
    }

    private void finalizarJuego(String mensaje) {
        turnoLabel.setText(mensaje);

        // Desactivar botones
        unoButton.setDisable(true);
        jugarCartaButton.setDisable(true);
        pasarTurnoButton.setDisable(true);

        //Se desactivan las cartas del jugador
        for (javafx.scene.Node node : manoJugador.getChildren()) {
            node.setDisable(true);
        }

        // Se oculta el panel de selección de color (por si estaba abierto)
        selecionaColorPane.setVisible(false);
    }
}