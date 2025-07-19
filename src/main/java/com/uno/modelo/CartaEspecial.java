package com.uno.modelo;

/**
 * Clase CartaEspecial, representa una carta especial en el juego UNO.
 * Las cartas especiales tienen una habilidad en lugar de un valor numérico.
 * Ejemplos de habilidades: "+2", "saltar", "+4", "Seleccionar color".
 */
public class CartaEspecial extends Carta{

    private String habilidad;

    /**
     * Constructor para una carta especial.
     *
     * @param color, el color de la carta (puede ser un color estándar o "All" si es un comodín).
     * @param habilidad, el nombre de la habilidad especial de la carta (ej: "+2", "saltar").
     */
    public CartaEspecial(String color, String habilidad) {
        super(color, -999999999); // El valor puede ser cualquiera, que no sea 0-9
        this.habilidad = habilidad;
        super.setImagen(habilidad + "_" + color + ".png");
    }

    /**
     * Obtiene la habilidad especial de esta carta.
     *
     * @return La habilidad de la carta (por ejemplo, "+2", "saltar", "+4").
     */
    @Override
    public String getHabilidad() {return habilidad;}

    @Override
    public String toString() {return habilidad + " de " + getColor();}

    /**
     * Indica que esta carta es especial.
     *
     * @return true, ya que esta clase representa cartas especiales.
     */
    @Override
    public boolean EsEspecial() {return true;}

    /**
     * Valida si esta carta especial puede ser jugada sobre otra carta central.
     * Las reglas permiten jugar una carta especial si:
     * - Coincide el color
     * - Coincide la habilidad
     * - El color es "Cualquiera" (comodín).
     *
     * @param cartaCentro la carta actualmente en el centro.
     * @return true si la carta es válida para ser jugada.
     * @throws CartaInvalidaException si la carta no cumple las reglas de coincidencia.
     */
    @Override
    public boolean esValido(Carta cartaCentro) throws CartaInvalidaException {
        if (!this.getColor().equals(cartaCentro.getColor()) &&
                !this.habilidad.equals(cartaCentro.getHabilidad()) &&
                !this.getColor().equals("Cualquiera")) {
            throw new CartaInvalidaException("Carta especial inválida");
        }
        return true;
    }
}