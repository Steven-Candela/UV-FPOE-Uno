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
    // Getters
    public String getHabilidad(){return "No hay habilidad";}
    public String getColor() {return color;}
    public int getValor() {return valor;}
    public String getImagen() {return imagen;}

    // Setters
    public void setImagen(String imagen) {this.imagen = imagen;}
    public void setColor(String color) {this.color = color;}


    @Override
    public String toString() {
        return valor + " de " + color;
    }

    // Validación para jugar funciones de cartas especiales
    public boolean EsEspecial() {
        return false;
    }
}