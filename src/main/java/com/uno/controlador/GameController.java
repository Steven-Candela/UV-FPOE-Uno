package com.uno.controlador;

import com.uno.modelo.Baraja;
import com.uno.modelo.Carta;
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

/**
 * Clase GameController que gestiona la lógica del juego, incluyendo el manejo de turnos,
 * acciones de las cartas especiales, interacción con la interfaz de usuario y el flujo
 * general del juego.
 *
 * @author Nicolle Paz, Steven Candela y Camilo Portilla
 */
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

    /**
     * Método que se llama automáticamente al iniciar la interfaz FXML.
     * Inicializa el juego, reparte las cartas y actualiza la vista.
     */
    @FXML
    private void initialize() {
        selecionaColorPane.setVisible(false);
        baraja = new Baraja();

        // Repartir cartas
        manoHumano = baraja.robarVarias(5);
        manoCPU = baraja.robarVarias(5);

        // Carta inicial al centro
        Carta cartaInicial = baraja.robarCarta();
        mostrarCartaCentral(cartaInicial);
        if (cartaInicial.getHabilidad().equals("+2")) {
            for (int i = 0; i < 2; i++) {
                manoHumano.add(baraja.robarCarta());
            }
        } else if (cartaInicial.getHabilidad().equals("+4")) {
            for (int i = 0; i < 4; i++) {
                manoHumano.add(baraja.robarCarta());
                selecionaColorPane.setVisible(true);
            }
        }   else if (cartaInicial.getHabilidad().equals("SelectColor")) {
            selecionaColorPane.setVisible(true);
        }   else if (cartaInicial.getHabilidad().equals("skip")) {
            System.out.println("El jugador pierde su turno, la primera carta es un skip");
            turnoHumano = false;
            jugarTurnoCPU();
        }

        // Mostrar mano del jugador humano
        mostrarCartasJugador();
        // Mostrar mano de la máquina
        mostrarCartasMaquina();
    }

    /**
     * Muestra la carta actual en el centro del juego.
     * @param carta, es la carta que se mostrará en el centro.
     */
    private void mostrarCartaCentral(Carta carta) {
        if (carta != null) {
            cartaCentroActual = carta;
            String ruta = "/com/uno/imagenes/" + carta.getImagen();
            Image img = new Image(getClass().getResourceAsStream(ruta));
            cartaCentro.setImage(img);
        }
    }

    /**
     * Muestra las cartas actuales en la mano del jugador humano.
     * También asigna el evento de selección para cada carta.
     */
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

    /**
     * Muestra la cantidad de cartas actuales de la CPU, usando la imagen del reverso.
     */
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

    /**
     * Muestra por un lapso de tiempo una etiqueta visual cuando se presiona el botón UNO.
     */
    private void mostrarUnoVisual() {
        unoVisualLabel.setVisible(true);
        unoVisualLabel.setOpacity(1.0);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> unoVisualLabel.setVisible(false));
        pause.play();
    }

    /**
     * Evento que se ejecuta al presionar el botón "Jugar Carta".
     * Valida la carta seleccionada, actualiza el estado del juego
     * y maneja los hilos con un lapso de tiempo determinado
     * @param event, un evento de acción (ActionEvent).
     */
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

    /**
     * Valida si una carta puede ser jugada según la carta central.
     * @param carta, la carta que se quiere jugar.
     * @throws CartaInvalidaException si la carta no cumple las reglas.
     */
    @FXML
    private void validarCarta(Carta carta) throws CartaInvalidaException {
        if (!carta.getColor().equals(cartaCentroActual.getColor()) &&
        carta.getValor() != cartaCentroActual.getValor()) {
            if (!carta.getColor().equals("All")) {
                throw new CartaInvalidaException("Carta Invalida");
            }
        }
    }

    /**
     * Lógica de turno de la CPU. Busca una carta válida o roba si no tiene.
     * También maneja el momento en que la CPU dice "UNO" si le queda una sola carta.
     * Al final del turno, verifica si la CPU ha ganado.
     */
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
                        cartaCentroActual.setColor(cartaSeleccionada.getColor());
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
            List<Carta> robadasCPU = intentarRobarCartas(1);
            manoCPU.addAll(robadasCPU);
            System.out.println("La maquina no tiene carta valida, roba una de la baraja");
            turnoLabel.setText("Turno: Jugador");
            turnoHumano = true;
            mostrarCartasMaquina();
        });
        pausa.play();
    }

    /**
     * Verifica si la mano dada contiene al menos una carta válida para jugar.
     * @param mano, la mano de cartas a verificar.
     * @return true si hay al menos una carta válida, false si no.
     */
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

    /**
     * Evento para manejar la acción del botón "Pasar turno".
     * Roba una carta si no hay jugadas posibles.
     * @param event, un evento de acción (ActionEvent).
     */
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
            jugarTurnoCPU();
        } else {
            System.out.println("No puedes robar cartas mientras no es tu turno");
        }
    }

    /**
     * Evento al presionar el botón "UNO". Verifica condiciones y penaliza
     * si la CPU no lo dijo a tiempo.
     * @param event, un evento de acción (ActionEvent).
     */
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

    /**
     * Aplica los efectos especiales de una carta (como +2, +4, skip, elegir color).
     * @param cartaEspecial, la carta especial jugada.
     * @param TurnoesHumano, indica si el turno es del jugador humano.
     * @param color, el color actual antes de aplicar la carta especial.
     * @return el nuevo color si la carta lo cambia, o el mismo si no.
     */
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
                List<Carta> robadasHumano = intentarRobarCartas(2);
                manoHumano.addAll(robadasHumano);
                System.out.println("La maquina jugó un +2, el jugador roba 2 cartas");
                System.out.println("El jugador pierde su turno");
                turnoHumano = false;
                turnoCambio = true;

            } else {
                List<Carta> robadasMaquina = intentarRobarCartas(2);
                manoCPU.addAll(robadasMaquina);
                System.out.println("La jugador jugó un +2, la maquina roba 2 cartas");
                System.out.println("La maquina pierde su turno");
                turnoHumano = true;
                turnoCambio = true;
            }
        }
        if (habilidad.equals("+4")) {
            if (!TurnoesHumano) {
                List<Carta> robadasHumano = intentarRobarCartas(4);
                manoHumano.addAll(robadasHumano);
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
                List<Carta> robadasMaquina = intentarRobarCartas(2);
                manoCPU.addAll(robadasMaquina);
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

    /**
     * Evento para manejar la selección de color cuando se juega una carta comodín.
     * El color se elige mediante un botón (rojo, azul, verde, amarillo).
     * @param event, un evento de acción (ActionEvent).
     */
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

        turnoHumano = false;
        jugarTurnoCPU();
    }

    /**
     * Intenta robar una cantidad determinada de cartas de la baraja.
     * Si la baraja está vacía, se reinicia usando las cartas jugadas.
     * @param cantidad, el número de cartas a robar.
     * @return la lista de cartas robadas.
     */
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
        }
        Carta robada = baraja.robarCarta();
        cartasRobadas.add(robada);
        return cartasRobadas;
    }

    /**
     * Finaliza el juego mostrando un mensaje y desactivando la interfaz.
     * @param mensaje Mensaje que indica quién ganó.
     */
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