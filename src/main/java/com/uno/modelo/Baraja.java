package com.uno.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase Baraja, representa la baraja del juego UNO.
 * Contiene tanto cartas normales como especiales, permite mezclarlas,
 * robar cartas, reiniciar la baraja, etc.
 *
 * @author Camilo Portilla y Steven Candela
 */
public class Baraja {

    private List<Carta> cartas;

    /**
     * Constructor de la baraja. Crea todas las cartas y las mezcla.
     */
    public Baraja() {
        cartas = new ArrayList<>();
        crearCartas();
        mezclar();
    }

    /**
     * Crea las cartas del juego, incluyendo:
     * - Cartas numéricas del 0 al 9 para cada color.
     * - Cartas especiales como "saltar", "+2", "+4" y "Seleccionar color".
     * Asigna imágenes específicas a las cartas especiales.
     */
    private void crearCartas() {
        String[] colores = {"azul", "rojo", "verde", "amarillo"};
        for (int i = 0; i < 10; i++) {
            for (String color : colores) {
                cartas.add(new Carta(color, i));
            }
        }
        for (String color : colores) {
            cartas.add(new CartaEspecial(color, "saltar"));
        }
        for (String color : colores) {
            for (int i = 0; i < 2; i++) {
                CartaEspecial masDos = new CartaEspecial(color, "+2");
                masDos.setImagen("2_wild_draw_" + color + ".png");
                cartas.add(masDos);
            }
        }

        for (int i = 0; i < 4; i++ ) {
            cartas.add(new CartaEspecial("Cualquiera", "+4"));
            Carta UltimaCarta = cartas.get(cartas.size() - 1);
            UltimaCarta.setImagen("4_wild_draw.png");
        }

        for (int i = 0; i < 4; i++) {
            cartas.add(new CartaEspecial("Cualquiera", "Seleccionar color"));
            Carta UltimaCarta = cartas.get(cartas.size() - 1);
            UltimaCarta.setImagen("wild.png");
        }
    }

    /**
     * Mezcla las cartas de la baraja de forma aleatoria.
     */
    private void mezclar() {
        Collections.shuffle(cartas);
    }

    /**
     * Roba una carta de la parte superior de la baraja.
     *
     * @return la carta robada o null si no hay cartas disponibles.
     */
    public Carta robarCarta() {
        if (cartas.isEmpty()) {
            return null;

        }
        return cartas.remove(0);
    }

    /**
     * Roba varias cartas de la baraja.
     *
     * @param cantidad, el número de cartas a robar.
     * @return la lista con las cartas robadas (puede ser menor si no hay suficientes).
     */
    public List<Carta> robarVarias(int cantidad) {
        List<Carta> mano = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            Carta cartaRobada = robarCarta();
            if (cartaRobada != null) {
                mano.add(cartaRobada);
            } else {
                break;
            }
        }
        return mano;
    }

    /**
     * Reinicia la baraja excluyendo las cartas que están en juego actualmente.
     * Se eliminan de la nueva baraja las cartas en la mano del jugador, la máquina y la del centro.
     * Luego se mezclan las cartas restantes.
     *
     * @param manoJugador, la lista de cartas del jugador.
     * @param manoMaquina, la lista de cartas de la máquina.
     * @param cartaCentro, la carta que está actualmente en el centro.
     */
    public void reiniciarCartas(List<Carta> manoJugador, List<Carta> manoMaquina, Carta cartaCentro) {

        System.out.println("Reiniciando cartas");

        System.out.println("Mano Jugador: " + manoJugador);
        System.out.println("Mano Maquina: " + manoMaquina);
        System.out.println("Carta Centro: " + cartaCentro);

        cartas = new ArrayList<>();
        crearCartas();

        System.out.println("Baraja sin modificaciones: " + cartas);

        List<Carta> cartasEnJuegoEliminar = new ArrayList<>();
        cartasEnJuegoEliminar.addAll(manoJugador);
        cartasEnJuegoEliminar.addAll(manoMaquina);
        cartasEnJuegoEliminar.add(cartaCentro);

        System.out.println("Carta en Juego: " + cartasEnJuegoEliminar);

        for (Carta cartaAEliminar : cartasEnJuegoEliminar) {
            cartas.removeIf(carta ->
                    carta.getValor() == cartaAEliminar.getValor() &&
                            carta.getColor().equals(cartaAEliminar.getColor())
            );
        }

        System.out.println("Baraja con modificaciones: " + cartas);
        mezclar();

    }

    /**
     * Devuelve la cantidad de cartas restantes en la baraja.
     *
     * @return el número de cartas en la baraja.
     */
    public int tamaño() {
        return cartas.size();
    }
}
