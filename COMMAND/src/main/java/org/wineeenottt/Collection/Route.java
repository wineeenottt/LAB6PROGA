package org.wineeenottt.Collection;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Класс Route представляет маршрут с координатами, пунктами отправления и назначения.
 * Хранит информацию о дате создания и расстоянии.
 */
public class Route implements Serializable {
    /**
     * Идентификатор маршрута. Поле не может быть null. Значение поля должно быть больше 0 и уникальным.
     * Значение этого поля генерируется автоматически.
     */
    private Integer id;

    /**
     * Название маршрута. Поле не может быть null. Строка не может быть пустой.
     */
    private String name;

    /**
     * Координаты маршрута. Поле не может быть null.
     */
    private Coordinates coordinates;

    /**
     * Дата создания маршрута. Поле не может быть null. Значение этого поля генерируется автоматически.
     */
    private ZonedDateTime creationDate;

    /**
     * Место отправления. Поле не может быть null.
     */
    private Location from;

    /**
     * Место назначения. Поле не может быть null.
     */
    private Location to;

    /**
     * Расстояние маршрута. Поле не может быть null и должно быть больше 1.
     */
    private Long distance;

    /**
     * Конструктор класса Route с параметрами.
     *
     * @param id           Идентификатор маршрута. Не может быть null, должен быть больше 0 и уникальным.
     * @param name         Название маршрута. Не может быть null или пустой строкой.
     * @param coordinates  Координаты маршрута. Не может быть null.
     * @param creationDate Дата создания маршрута. Не может быть null.
     * @param from         Место отправления. Не может быть null.
     * @param to           Место назначения. Не может быть null.
     * @param distance     Расстояние маршрута. Не может быть null и должно быть больше 1.
     */
    public Route(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Location from, Location to, Long distance) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    /**
     * Возвращает идентификатор маршрута.
     *
     * @return Идентификатор маршрута.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Возвращает название маршрута.
     *
     * @return Название маршрута.
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название маршрута.
     *
     * @param name Название маршрута. Не может быть null или пустой строкой.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает координаты маршрута.
     *
     * @return Координаты маршрута.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Устанавливает координату X маршрута.
     *
     * @param x Координата X. Не может быть null.
     */
    public void setCoordinateX(Double x) {
        coordinates.setX(x);
    }

    /**
     * Устанавливает координату Y маршрута.
     *
     * @param y Координата Y. Не может быть null.
     */
    public void setCoordinateY(Float y) {
        coordinates.setY(y);
    }

    /**
     * Возвращает дату создания маршрута.
     *
     * @return Дата создания маршрута.
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Устанавливает дату создания маршрута.
     *
     * @param creationDate Дата создания маршрута. Не может быть null.
     */
    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Возвращает место отправления.
     *
     * @return Место отправления.
     */
    public Location getFrom() {
        return from;
    }

    /**
     * Устанавливает место отправления.
     *
     * @param from Место отправления. Не может быть null.
     */
    public void setFrom(Location from) {
        this.from = from;
    }

    /**
     * Возвращает место назначения.
     *
     * @return Место назначения.
     */
    public Location getTo() {
        return to;
    }

    /**
     * Устанавливает место назначения.
     *
     * @param to Место назначения. Не может быть null.
     */
    public void setTo(Location to) {
        this.to = to;
    }

    /**
     * Возвращает расстояние маршрута.
     *
     * @return Расстояние маршрута.
     */
    public Long getDistance() {
        return distance;
    }

    /**
     * Устанавливает расстояние маршрута.
     *
     * @param distance Расстояние маршрута. Не может быть null и должно быть больше 1.
     */
    public void setDistance(Long distance) {
        this.distance = distance;
    }

    /**
     * Возвращает строковое представление маршрута.
     *
     * @return строковое представление маршрута.
     */
    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", from=" + from +
                ", to=" + to +
                ", distance=" + distance +
                '}';
    }

    public String toCsvString() {
        return id + "," +
                name + "," +
                coordinates.getX() + "," +
                coordinates.getY() + "," +
                creationDate + "," +
                from.getX() + "," +
                from.getY() + "," +
                from.getZ() + "," +
                from.getName() + "," +
                to.getX() + "," +
                to.getY() + "," +
                to.getZ() + "," +
                to.getName() + "," +
                distance;
    }
}
