package com.uno.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartaEspecialTest {

    @Test
    void testSkip() {
        CartaEspecial cartaEspecial = new CartaEspecial("verde", "saltar");

        assertEquals(cartaEspecial.getColor(),"verde",  "El color debe ser verde");
        assertEquals(cartaEspecial.getHabilidad(),"saltar","La habilidad debe ser saltar");
        assertEquals(cartaEspecial.getValor(),-999999999, "El valor de la habilidad debe ser diferente entre 0 y 9");
        assertEquals(cartaEspecial.getImagen(), "saltar_verde.png", "La imagen debe ser saltar_verde.png");
        assertTrue(cartaEspecial.EsEspecial(), "Esta carta debe ser especial");
    }

    @Test
    void testMasDos() {
        CartaEspecial cartaEspecial = new CartaEspecial("rojo", "+2");

        assertEquals(cartaEspecial.getColor(),"rojo",  "El color debe ser rojo");
        assertEquals(cartaEspecial.getHabilidad(),"+2","La habilidad debe ser +2");
        assertEquals(cartaEspecial.getValor(),-999999999, "El valor de la habilidad debe ser diferente entre 0 y 9");
        assertEquals(cartaEspecial.getImagen(), "+2_rojo.png", "La imagen debe ser +2_rojo.png");
        assertTrue(cartaEspecial.EsEspecial(), "Esta carta debe ser especial");
    }

    @Test
    void testMasCuatro() {
        CartaEspecial cartaEspecial = new CartaEspecial("Cualquiera", "+4");

        assertEquals(cartaEspecial.getColor(),"Cualquiera",  "El color debe ser Cualquiera");
        assertEquals(cartaEspecial.getHabilidad(),"+4","La habilidad debe ser +4");
        assertEquals(cartaEspecial.getValor(),-999999999, "El valor de la habilidad debe ser diferente entre 0 y 9");
        assertEquals(cartaEspecial.getImagen(), "+4_Cualquiera.png", "La imagen debe ser +4_Cualquiera.png");
        assertTrue(cartaEspecial.EsEspecial(), "Esta carta debe ser especial");
    }

    @Test
    void testSelectColor() {
        CartaEspecial cartaEspecial = new CartaEspecial("Cualquiera", "Seleccionar color");

        assertEquals(cartaEspecial.getColor(),"Cualquiera",  "El color debe ser Cualquiera");
        assertEquals(cartaEspecial.getHabilidad(),"Seleccionar color","La habilidad debe ser Seleccionar color");
        assertEquals(cartaEspecial.getValor(),-999999999, "El valor de la habilidad debe ser diferente entre 0 y 9");
        assertEquals(cartaEspecial.getImagen(), "Seleccionar color_Cualquiera.png", "La imagen debe ser Seleccionar color_Cualquiera.png");
        assertTrue(cartaEspecial.EsEspecial(), "Esta carta debe ser especial");
    }

    @Test
    void testToString() {
        CartaEspecial cartaEspecial = new CartaEspecial("amarillo", "saltar");

        assertEquals(cartaEspecial.toString(), "saltar de amarillo", "El toString debe decir saltar de amarillo");
    }
}