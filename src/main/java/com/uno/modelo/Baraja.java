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
            cartas.add(new CartaEspecial(color, "+2"));
            Carta UltimaCarta = cartas.get(cartas.size() - 1);
            UltimaCarta.setImagen("2_wild_draw_" + color + ".png");

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
        if (!cartas.isEmpty()) {
            return cartas.remove(0);
        }
        return null;
    }

    public List<Carta> robarVarias(int cantidad) {
        List<Carta> mano = new ArrayList<>();
        for (int i = 0; i < cantidad && !cartas.isEmpty(); i++) {
            mano.add(robarCarta());
        }
        return mano;
    }

    public int tamaño() {
        return cartas.size();
    }
}
