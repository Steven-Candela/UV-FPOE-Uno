package com.uno.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BarajaTest {
    private Baraja baraja;

    @Test
    void testTamaño() {
        baraja = new Baraja();

        assertEquals(baraja.tamaño(), 60, "Debe haber 60 cartas en juego");
    }

    @Test
    void testRobarCarta() {
        baraja = new Baraja();
        int tamañoInicial = baraja.tamaño();
        baraja.robarCarta();

        assertEquals(baraja.tamaño(), tamañoInicial-1, "Despues de robar una carta, el tamaño debe reducir 1");
    }

    @Test
    void testRobarVariasCartas() {
        baraja = new Baraja();
        int tamañoInicial = baraja.tamaño();
        baraja.robarVarias(5);

        assertEquals(baraja.tamaño(), tamañoInicial-5, "Se debe reducir 5 cartas, despues de robar esa cantidad de cartas");
    }
}