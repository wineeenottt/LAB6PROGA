package org.wineeenottt.Collection;

import java.io.Serializable;

/**
 * Класс Location представляет координаты назначения.
 */
public class Location implements Serializable {

    /**
     * Координата "x". Поле не может быть null.
     */
    private Float x;

    /**
     * Координата "y". Поле не может быть null.
     */
    private Integer y;

    /**
     * Координата "z". Поле не может быть null.
     */
    private Double z;

    /**
     * Название места назначения. Поле не может быть null.
     */
    private String name;

    /**
     * Конструктор класса с параметрами.
     *
     * @param x    координата "x". Не может быть null.
     * @param y    координата "y". Не может быть null.
     * @param z    координата "z". Не может быть null.
     * @param name название места назначения. Не может быть null.
     */
    public Location(Float x, Integer y, Double z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    /**
     * Возвращает значение координаты "x".
     *
     * @return значение координаты "x". Не может быть null.
     */
    public Float getX() {
        return x;
    }

    /**
     * Устанавливает значение координаты "x".
     *
     * @param x значение координаты "x". Не может быть null.
     */
    public void setX(Float x) {
        this.x = x;
    }

    /**
     * Возвращает значение координаты "y".
     *
     * @return значение координаты "y". Не может быть null.
     */
    public Integer getY() {
        return y;
    }

    /**
     * Устанавливает значение координаты "y".
     *
     * @param y значение координаты "y". Не может быть null.
     */
    public void setY(Integer y) {
        this.y = y;
    }

    /**
     * Возвращает значение координаты "z".
     *
     * @return значение координаты "z". Не может быть null.
     */
    public Double getZ() {
        return z;
    }

    /**
     * Устанавливает значение координаты "z".
     *
     * @param z значение координаты "z". Не может быть null.
     */
    public void setZ(Double z) {
        this.z = z;
    }

    /**
     * Возвращает название места назначения.
     *
     * @return название места назначения. Не может быть null.
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название места назначения.
     *
     * @param name название места назначения. Не может быть null.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает строковое представление координат.
     *
     * @return строковое представление координат в формате.
     */
    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", name='" + name + '\'' +
                '}';
    }
}


