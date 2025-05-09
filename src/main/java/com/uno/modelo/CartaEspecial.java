package com.uno.modelo;

public class CartaEspecial extends Carta{

    private String habilidad;

    public CartaEspecial(String color, String hablidad) {
        super(color, -999999999); // El valor puede ser cualquiera, que no sea 0-9
        this.habilidad = hablidad;
        super.setImagen(habilidad + "_" + color + ".png");
    }

    public void skipHabilidad(String turno) {

    }
    @Override
    public String getHabilidad() {return habilidad;}

    @Override
    public String toString() {return habilidad + " de " + getColor();}

    @Override
    public boolean EsEspecial() {
        return true;
    }

}