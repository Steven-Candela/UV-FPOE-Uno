package com.uno.modelo;

/**
 * Clase Carta, representa una carta numérica del juego UNO.
 * Las cartas tienen un color, un valor numérico (del 0 al 9) y una imagen asociada.
 *
 * @author Camilo Portilla
 */
public class Carta {

    private String color;
    private int valor;
    private String imagen; // nombre del archivo, ej: "3_red.png"

    /**
     * Constructor de una carta numérica.
     *
     * @param color, el color de la carta (red, blue, green, yellow).
     * @param valor, el valor numérico de la carta (0-9).
     */
    public Carta(String color, int valor) {
        this.color = color;
        this.valor = valor;
        this.imagen = valor + "_" + color + ".png";
    }

    /**
     * Devuelve la habilidad especial de la carta.
     * Por defecto, las cartas numéricas no tienen habilidades.
     *
     * @return texto indicando que no hay habilidad.
     */
    public String getHabilidad(){return "No hay habilidad";}

    /**
     * Obtiene el color de la carta.
     *
     * @return Color de la carta.
     */
    public String getColor() {return color;}

    /**
     * Obtiene el valor numérico de la carta.
     *
     * @return Valor de la carta (0-9).
     */
    public int getValor() {return valor;}

    /**
     * Obtiene el nombre del archivo de imagen asociado a la carta.
     *
     * @return Nombre del archivo de imagen.
     */
    public String getImagen() {return imagen;}

    /**
     * Establece el nombre del archivo de imagen de la carta.
     *
     * @param imagen, el nombre del archivo de imagen.
     */
    public void setImagen(String imagen) {this.imagen = imagen;}

    /**
     * Establece el color de la carta.
     *
     * @param color, el nuevo color de la carta.
     */
    public void setColor(String color) {this.color = color;}

    @Override
    public String toString() {
        return valor + " de " + color;
    }

    /**
     * Indica si la carta es especial.
     * Las cartas numéricas no lo son, por lo que devuelve false.
     *
     * @return false para cartas numéricas.
     */
    public boolean EsEspecial() {
        return false;
    }
}