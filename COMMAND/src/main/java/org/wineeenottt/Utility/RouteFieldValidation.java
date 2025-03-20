package org.wineeenottt.Utility;


/**
 * Класс, предназначенный для валидации полей объекта Route.
 */
public class RouteFieldValidation {
    /**
     * Метод, осуществляющий валидацию полей класса Route.
     * @param field поле объекта Route.
     * @param value значение поля объекта Route.
     * @return boolean. true - валидация пройдена, false - валидация не пройдена.
     */
    public static boolean validate(String field, String value) {
        try {
            switch (field) {
                case "Id": {
                    if (value == null || value.trim().isEmpty()) throw new NullPointerException();
                    if (Integer.parseInt(value) > 0) return true;
                    break;
                }
                case "Name", "LocationFromName", "LocationToName": {
                    if (value == null || value.trim().isEmpty()) throw new NullPointerException();
                    return true;
                }
                case "CoordinateX": {
                    if (value == null || value.trim().isEmpty()) throw new NullPointerException();
                    if (Double.parseDouble(value) <= 750) return true;
                    break;
                }
                case "CoordinateY", "LocationFromX", "LocationToX": {
                    if (value == null || value.trim().isEmpty()) throw new NullPointerException();
                    Float.parseFloat(value); // Просто проверяем, что это float
                    return true;
                }
                case "LocationFromY", "LocationToY": {
                    if (value == null || value.trim().isEmpty()) throw new NullPointerException();
                    Integer.parseInt(value); // Просто проверяем, что это int
                    return true;
                }
                case "LocationFromZ", "LocationToZ": {
                    if (value == null || value.trim().isEmpty()) throw new NullPointerException();
                    Double.parseDouble(value); // Просто проверяем, что это double
                    return true;
                }
                case "Distance": {
                    if (value == null || value.trim().isEmpty()) throw new NullPointerException();
                    if (Long.parseLong(value) > 1) return true;
                    break;
                }
                case "": {
                    return false;
                }
            }
        } catch (NullPointerException | IllegalArgumentException ignored) {
        }
        return false;
    }
}