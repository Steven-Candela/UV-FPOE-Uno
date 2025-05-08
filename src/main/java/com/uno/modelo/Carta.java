package com.uno.modelo;

public class Carta {

    private String color;
    private int valor;
    private String imagen; // nombre del archivo, ej: "3_red.png"

    public Carta(String color, int valor) {
        this.color = color;
        this.valor = valor;
        this.imagen = valor + "_" + color + ".png";
    }

    public String getColor() {
        return color;
    }

    public int getValor() {
        return valor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {this.imagen = imagen;}


    @Override
    public String toString() {
        return valor + " de " + color;
    }
}