package com.uno.modelo;

public class CartaEspecial {

    private String habilidad;
    private String color;
    private String imagen; // nombre del archivo, ej: "reverse_blue.png"

    public CartaEspecial(String color, String hablidad) {
        this.habilidad = hablidad;
        this.color = color;
        this.imagen = habilidad + "_" + color + ".png";
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getHabilidad() {return habilidad;}

    public String getColor() {return color;}

    public String getImagen() {return imagen;}

    @Override
    public String toString() {return habilidad + " de " + color;}
}