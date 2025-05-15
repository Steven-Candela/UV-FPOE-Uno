package com.uno.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baraja {

    private List<Carta> cartas;

    public Baraja() {
        cartas = new ArrayList<>();
        crearCartas();
        mezclar();
    }

    private void crearCartas() {
        String[] colores = {"blue", "red", "green", "yellow"};
        for (int i = 0; i < 10; i++) {
            for (String color : colores) {
                cartas.add(new Carta(color, i));
            }
        }
        for (String color : colores) {
            cartas.add(new CartaEspecial(color, "skip"));
        }
        for (String color : colores) {
            for (int i = 0; i < 2; i++) {
                CartaEspecial masDos = new CartaEspecial(color, "+2");
                masDos.setImagen("2_wild_draw_" + color + ".png");
                cartas.add(masDos);
            }
        }

        for (int i = 0; i < 4; i++ ) {
            cartas.add(new CartaEspecial("All", "+4"));
            Carta UltimaCarta = cartas.get(cartas.size() - 1);
            UltimaCarta.setImagen("4_wild_draw.png");
        }

        for (int i = 0; i < 4; i++) {
            cartas.add(new CartaEspecial("All", "SelectColor"));
            Carta UltimaCarta = cartas.get(cartas.size() - 1);
            UltimaCarta.setImagen("wild.png");
        }
    }

    private void mezclar() {
        Collections.shuffle(cartas);
    }

    public Carta robarCarta() {
        if (cartas.isEmpty()) {
            return null;

        }
        return cartas.remove(0);
    }

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

    public int tamaño() {
        return cartas.size();
    }
}
