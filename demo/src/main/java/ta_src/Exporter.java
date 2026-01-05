package ta_src;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import ta_src.data.Polyline;

public class Exporter {
    // private static final double CANVAS_WIDTH = 500.0;
    // private static final double CANVAS_HEIGHT = 500.0;

    public static String toIpeString(Polyline polyline) {
        List<Coordinate> coords = polyline.getCoordinates();
        if (coords == null || coords.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coords.size(); i++) {
            Coordinate c = coords.get(i);
            if (i == 0) {
                sb.append(String.format("%.1f %.1f m", c.x, c.y));
                sb.append("\n");
                continue;
            }
            sb.append(String.format("%.1f %.1f l", c.x, c.y));
            sb.append("\n");
        }

        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
        // List<Coordinate> coords = polyline.getCoordinates();
        // if (coords.isEmpty()) {
        //     return "";
        // }

        // double minX = coords.get(0).x;
        // double maxX = coords.get(0).x;
        // double minY = coords.get(0).y;
        // double maxY = coords.get(0).y;

        // for (int i = 0;i < coords.size();++i) {
        //     Coordinate c = coords.get(i);
        //     if (c.x < minX) {
        //         minX = c.x;
        //     }

        //     if (c.x > maxX) {
        //         maxX = c.x;
        //     }

        //     if (c.y < minY) {
        //         minY = c.y;
        //     }

        //     if (c.y > maxY) {
        //         maxY = c.y;
        //     }
        // }

        // double rangeX = maxX - minX;
        // if (rangeX < 1e-9) {
        //     rangeX = 1e-9;
        // }

        // double rangeY = maxY - minY;
        // if (rangeY < 1e-9) {
        //     rangeY = 1e-9;
        // }

        // double scaleX = CANVAS_WIDTH / rangeX;
        // double scaleY = CANVAS_HEIGHT / rangeY;
        // double scale = scaleX;
        // if (scaleY < scaleX) {
        //     scale = scaleY;
        // }

        // StringBuilder sb = new StringBuilder();
        // for (int i = 0;i < coords.size();++i) {
        //     Coordinate c = coords.get(i);
        //     double x = (c.x - minX) * scale;
        //     double y = CANVAS_HEIGHT - (c.y - minY) * scale;

        //     sb.append(String.format("%.1f %.1f l", x, y));
        //     sb.append("\n");
        // }

        // if (sb.length() > 0) {
        //     sb.setLength(sb.length() - 1);
        // }

        // return sb.toString();
    }
}
