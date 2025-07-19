package com.uno.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CartaTest {

    @Test
    void getters() {
        Carta carta  = new Carta("rojo", 4);

        assertEquals(carta.getColor(),"rojo", "El color de la carta debe ser rojo");
        assertEquals(carta.getValor(), 4, "El valor de la carta debe ser 4");
        assertEquals(carta.getImagen(), "4_rojo.png", "La imagen de la carta debe ser 4_rojo.png");
        assertEquals(carta.getHabilidad(), "No hay habilidad", "Es una carta normal, no hay habilidad");
        assertFalse(carta.EsEspecial(), "No es una carta especial");
    }

    @Test
    void setImagen() {
        Carta carta = new Carta("amarillo", 9);
        carta.setImagen("3_rojo.png");

        assertEquals(carta.getImagen(), "3_rojo.png", "La imagen debe cambiar a 3_rojo");
    }

    @Test
    void setColor() {
        Carta carta = new Carta("azul", 2);
        carta.setColor("verde");

        assertEquals(carta.getColor(), "verde", "El color debe cambiar a verde");
    }

    @Test
    void testToString() {
        Carta carta = new Carta("rojo", 0);

        assertEquals(carta.toString(), "0 de rojo", "El toString debe formatear el valor y color de la carta");
    }
}