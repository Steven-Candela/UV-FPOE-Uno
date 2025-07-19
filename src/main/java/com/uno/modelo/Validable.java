package com.uno.modelo;

/**
 * Interfaz que representa un comportamiento de validación para una carta del juego UNO.
 * Permite verificar si una carta es válida para ser jugada sobre otra carta central.
 */
public interface Validable {
    /**
     * Determina si la carta actual puede ser jugada sobre la carta central.
     *
     * @param cartaCentro la carta actualmente en el centro de la mesa.
     * @return true si la carta es válida para ser jugada.
     * @throws CartaInvalidaException si la carta no cumple con las reglas para ser jugada.
     */
    boolean esValido(Carta cartaCentro) throws CartaInvalidaException;
}