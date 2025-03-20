package org.wineeenottt.Collection;

import java.io.Serializable;

/**
 * Класс Coordinates представляет координаты с двумя значениями: "x" и "y".
 */
public class Coordinates implements Serializable {

    /**
     * Координата "x". Максимальное значение поля: 750.
     */
    private Double x;

    /**
     * Координата "y". Поле не может быть null.
     */
    private Float y;

    /**
     * Конструктор класса с параметрами.
     *
     * @param x координата "x". Максимальное значение: 750.
     * @param y координата "y". Не может быть null.
     */
    public Coordinates(Double x, Float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает значение координаты "x".
     *
     * @return значение координаты "x". Максимальное значение: 750.
     */
    public Double getX() {
        return x;
    }

    /**
     * Устанавливает значение координаты "x".
     *
     * @param x значение координаты "x". Максимальное значение: 750.
     */
    public void setX(Double x) {
        this.x = x;
    }

    /**
     * Возвращает значение координаты "y".
     *
     * @return значение координаты "y". Не может быть null.
     */
    public Float getY() {
        return y;
    }

    /**
     * Устанавливает значение координаты "y".
     *
     * @param y значение координаты "y". Не может быть null.
     */
    public void setY(Float y) {
        this.y = y;
    }

    /**
     * Возвращает строковое представление координат.
     *
     * @return строковое представление координат.
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
