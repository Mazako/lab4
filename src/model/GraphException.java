/*
 *  Laboratorium 4
 *
 *   Autor: Michal Maziarz, 263913
 *    Data: listopad 2022 r.
 */
package model;

/**
 * Klasa wyjątku rzucanego w wypadku, gdy coś w metodach pakietu model pójdzie nie tak
 * @author Michał Maziarz
 * @version Listopad 2022
 */
public class GraphException extends RuntimeException{
    public GraphException(String message) {
        super(message);
    }
}
