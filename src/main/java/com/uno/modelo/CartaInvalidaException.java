package com.uno.modelo;

/**
 * Clase CartaInvalidaException, que maneja laxcepción que se lanza cuando
 * se intenta jugar una carta no válida según las reglas del juego UNO
 * (por ejemplo, color o valor no coincidente).
 *
 * @author Steven Candela
 */
public class CartaInvalidaException extends RuntimeException {
    /**
     * Crea una nueva instancia de la excepción con un mensaje específico.
     *
     * @param message, un mensaje que describe el motivo de la excepción.
     */
    public CartaInvalidaException(String message) {
        super(message);
    }
}