package com.uno.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartaEspecialTest {

    @Test
    void testSkip() {
        CartaEspecial cartaEspecial = new CartaEspecial("green", "skip");

        assertEquals(cartaEspecial.getColor(),"green",  "El color debe ser green");
        assertEquals(cartaEspecial.getHabilidad(),"skip","La habilidad debe ser skip");
        assertEquals(cartaEspecial.getValor(),-999999999, "El valor de la habilidad debe ser diferente entre 0 y 9");
        assertEquals(cartaEspecial.getImagen(), "skip_green.png", "La imagen debe ser skip_green.png");
        assertTrue(cartaEspecial.EsEspecial(), "Esta carta debe ser especial");
    }

    @Test
    void testMasDos() {
        CartaEspecial cartaEspecial = new CartaEspecial("red", "+2");

        assertEquals(cartaEspecial.getColor(),"red",  "El color debe ser red");
        assertEquals(cartaEspecial.getHabilidad(),"+2","La habilidad debe ser +2");
        assertEquals(cartaEspecial.getValor(),-999999999, "El valor de la habilidad debe ser diferente entre 0 y 9");
        assertEquals(cartaEspecial.getImagen(), "+2_red.png", "La imagen debe ser +2_red.png");
        assertTrue(cartaEspecial.EsEspecial(), "Esta carta debe ser especial");
    }

    @Test
    void testMasCuatro() {
        CartaEspecial cartaEspecial = new CartaEspecial("All", "+4");

        assertEquals(cartaEspecial.getColor(),"All",  "El color debe ser All");
        assertEquals(cartaEspecial.getHabilidad(),"+4","La habilidad debe ser +4");
        assertEquals(cartaEspecial.getValor(),-999999999, "El valor de la habilidad debe ser diferente entre 0 y 9");
        assertEquals(cartaEspecial.getImagen(), "+4_All.png", "La imagen debe ser +4_All.png");
        assertTrue(cartaEspecial.EsEspecial(), "Esta carta debe ser especial");
    }

    @Test
    void testSelectColor() {
        CartaEspecial cartaEspecial = new CartaEspecial("All", "SelectColor");

        assertEquals(cartaEspecial.getColor(),"All",  "El color debe ser All");
        assertEquals(cartaEspecial.getHabilidad(),"SelectColor","La habilidad debe ser SelectColor");
        assertEquals(cartaEspecial.getValor(),-999999999, "El valor de la habilidad debe ser diferente entre 0 y 9");
        assertEquals(cartaEspecial.getImagen(), "SelectColor_All.png", "La imagen debe ser SelectColor_All.png");
        assertTrue(cartaEspecial.EsEspecial(), "Esta carta debe ser especial");
    }

    @Test
    void testToString() {
        CartaEspecial cartaEspecial = new CartaEspecial("yellow", "skip");

        assertEquals(cartaEspecial.toString(), "skip de yellow", "El toString debe decir skip de yellow");
    }
}