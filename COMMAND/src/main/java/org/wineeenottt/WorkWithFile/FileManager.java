package org.wineeenottt.WorkWithFile;

import org.wineeenottt.Collection.Route;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс для работы с файлами, содержащими данные о маршрутах.
 * Использует BufferedInputStream для чтения данных из CSV-файла.
 */
public class FileManager {

    private RouteParser routeParser = new RouteParser();

    public Set<Route> parseCsvFile(String filePath) throws IOException {
        Set<Route> routes = new HashSet<>();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath))) {
            StringBuilder currentLine = new StringBuilder();
            boolean isFirstLine = true;
            int byteRead;

            // Читаем байты из файла
            while ((byteRead = bis.read()) != -1) {
                char ch = (char) byteRead;

                // Собираем строку до символа новой строки
                if (ch == '\n') {
                    if (isFirstLine) {
                        isFirstLine = false; // Пропускаем заголовок
                        currentLine.setLength(0); // Очищаем буфер
                        continue;
                    }

                    String line = currentLine.toString().trim();
                    if (!line.isEmpty()) {
                        String[] fields = line.split(",");
                        if (fields.length < 14) {
                            System.err.println("Ошибка: некорректный формат строки в CSV файле: " + line);
                            currentLine.setLength(0);
                            continue;
                        }

                        try {
                            Route route = routeParser.parse(fields);
                            routes.add(route);
                        } catch (Exception e) {
                            System.err.println("Ошибка при разборе строки: " + e.getMessage());
                        }
                    }
                    currentLine.setLength(0); // Очищаем буфер для следующей строки
                } else {
                    currentLine.append(ch);
                }
            }

            // Обработка последней строки, если файл не заканчивается \n
            if (currentLine.length() > 0 && !isFirstLine) {
                String line = currentLine.toString().trim();
                if (!line.isEmpty()) {
                    String[] fields = line.split(",");
                    if (fields.length >= 14) {
                        try {
                            Route route = routeParser.parse(fields);
                            routes.add(route);
                        } catch (Exception e) {
                            System.err.println("Ошибка при разборе последней строки: " + e.getMessage());
                        }
                    } else {
                        System.err.println("Ошибка: некорректный формат последней строки в CSV файле: " + line);
                    }
                }
            }
        }
        return routes;
    }

    public void parseToCsv(String filePath, Set<Route> routes) {
        try (FileWriter writer = new FileWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write("id,name,coordinateX,coordinateY,creationDate,fromX,fromY,fromZ,fromName,toX,toY,toZ,toName,distance\n");
            for (Route route : routes) {
                writer.write(route.toCsvString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    public int findMaxId(Set<Route> routes) {
        return routes.stream().mapToInt(Route::getId).max().orElse(-1);
    }
}