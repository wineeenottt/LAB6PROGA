package org.wineeenottt.WorkWithFile;

import org.wineeenottt.Collection.Coordinates;
import org.wineeenottt.Collection.Location;
import org.wineeenottt.Collection.Route;

import java.time.ZonedDateTime;

class RouteParser {
    public Route parse(String[] fields) {
        try {
            int id = Integer.parseInt(fields[0].trim());
            String name = fields[1].trim();
            double cordX = Double.parseDouble(fields[2].trim());
            float cordY = Float.parseFloat(fields[3].trim());
            ZonedDateTime creationDate = ZonedDateTime.parse(fields[4].trim());
            float locFromX = Float.parseFloat(fields[5].trim());
            int locFromY = Integer.parseInt(fields[6].trim());
            double locFromZ = Double.parseDouble(fields[7].trim());
            String locFromName = fields[8].trim();
            float locToX = Float.parseFloat(fields[9].trim());
            int locToY = Integer.parseInt(fields[10].trim());
            double locToZ = Double.parseDouble(fields[11].trim());
            String locToName = fields[12].trim();
            long distance = Long.parseLong(fields[13].trim());

            Coordinates coordinates = new Coordinates(cordX, cordY);
            Location from = new Location(locFromX, locFromY, locFromZ, locFromName);
            Location to = new Location(locToX, locToY, locToZ, locToName);
            return new Route(id, name, coordinates, creationDate, from, to, distance);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при разборе маршрута: " + e.getMessage());
        }
    }
}
