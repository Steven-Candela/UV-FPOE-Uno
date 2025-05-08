package com.uno.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baraja {

    private List<Carta> cartas;
    private List<CartaEspecial> cartasEspeciales;

    public Baraja() {
        cartas = new ArrayList<>();
        cartasEspeciales = new ArrayList<>();
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
        String[] habilidades = {"reverse", "skip"};
        for (String habilidad : habilidades) {
            for (String color : colores) {
                cartasEspeciales.add(new CartaEspecial(color, habilidad));
            }
        }
        CartaEspecial wild = new CartaEspecial("All", "SelectColor");
        wild.setImagen("wild.png");
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
