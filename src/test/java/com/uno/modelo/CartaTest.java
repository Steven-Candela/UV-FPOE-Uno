package com.uno.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CartaTest {

    @Test
    void getters() {
        Carta carta  = new Carta("red", 4);

        assertEquals(carta.getColor(),"red", "El color de la carta debe ser red");
        assertEquals(carta.getValor(), 4, "El valor de la carta debe ser 4");
        assertEquals(carta.getImagen(), "4_red.png", "La imagen de la carta debe ser 4_red.png");
        assertEquals(carta.getHabilidad(), "No hay habilidad", "Es una carta normal, no hay habilidad");
        assertFalse(carta.EsEspecial(), "No es una carta especial");
    }

    @Test
    void setImagen() {
        Carta carta = new Carta("yellow", 9);
        carta.setImagen("3_red.png");

        assertEquals(carta.getImagen(), "3_red.png", "La imagen debe cambiar a 3_red");
    }

    @Test
    void setColor() {
        Carta carta = new Carta("blue", 2);
        carta.setColor("green");

        assertEquals(carta.getColor(), "green", "El color debe cambiar a green");
    }

    @Test
    void testToString() {
        Carta carta = new Carta("red", 0);

        assertEquals(carta.toString(), "0 de red", "El toString debe formatear el valor y color de la carta");
    }
}