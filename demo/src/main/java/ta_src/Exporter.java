package ta_src;

import java.io.FileWriter;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import ta_src.data.Polyline;

public class Exporter {
    // private static final double CANVAS_WIDTH = 500.0;
    // private static final double CANVAS_HEIGHT = 500.0;

    public static void geoJSONExporter(List<Polyline> polylines, String outputPath) {
        StringBuilder geojson = new StringBuilder();
        geojson.append("{\n");
        geojson.append("  \"type\": \"FeatureCollection\",\n");
        geojson.append("  \"features\": [\n");

        for (int i = 0;i < polylines.size();++i) {
            Polyline polyline = polylines.get(i);
            List<Coordinate> coords = polyline.getCoordinates();

            geojson.append("    {\n");
            geojson.append("      \"type\": \"Feature\",\n");
            geojson.append("      \"properties\": {},\n");
            geojson.append("      \"geometry\": {\n");
            geojson.append("        \"type\": \"LineString\",\n");
            geojson.append("        \"coordinates\": [\n");

            for (int j = 0;j < coords.size();++j) {
                Coordinate c = coords.get(j);
                geojson.append(String.format("          [%.6f, %.6f]", c.x, c.y));
                
                if (j < coords.size() - 1) {
                    geojson.append(",");
                }
                geojson.append("\n");
            }

            geojson.append("        ]\n");
            geojson.append("      }\n");
            geojson.append("    }");
            
            if (i < polylines.size() - 1) {
                geojson.append(",");
            }
            geojson.append("\n");
        }

        geojson.append("  ]\n");
        geojson.append("}\n");

        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(geojson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // public static String toSimpleIpeString(Polyline polyline) {
    //     List<Coordinate> coords = polyline.getCoordinates();
    //     if (coords == null || coords.isEmpty()) {
    //         return "";
    //     }

    //     StringBuilder sb = new StringBuilder();
    //     for (int i = 0; i < coords.size(); i++) {
    //         Coordinate c = coords.get(i);
    //         if (i == 0) {
    //             sb.append(String.format("%.1f %.1f m", c.x, c.y));
    //             sb.append("\n");
    //             continue;
    //         }
    //         sb.append(String.format("%.1f %.1f l", c.x, c.y));
    //         sb.append("\n");
    //     }

    //     if (sb.length() > 0) {
    //         sb.setLength(sb.length() - 1);
    //     }

    //     return sb.toString();
    // }   

    // public static String toIpeString(Polyline polyline) {
    //     List<Coordinate> coords = polyline.getCoordinates();
    //     if (coords == null || coords.isEmpty()) {
    //         return "";
    //     }

    //     double minX = coords.get(0).x;
    //     double maxX = coords.get(0).x;
    //     double minY = coords.get(0).y;
    //     double maxY = coords.get(0).y;

    //     for (int i = 1; i < coords.size(); i++) {
    //         Coordinate c = coords.get(i);
    //         if (c.x < minX) {
    //             minX = c.x;
    //         }
            
    //         if (c.x > maxX) {
    //             maxX = c.x;
    //         }

    //         if (c.y < minY) {
    //             minY = c.y;
    //         }

    //         if (c.y > maxY) {
    //             maxY = c.y;
    //         }
    //     }

    //     boolean needsScaling = (maxX - minX > CANVAS_WIDTH) || (maxY - minY > CANVAS_HEIGHT);

    //     double scaleX = 1.0;
    //     double scaleY = 1.0;
    //     double offsetX = 0.0;
    //     double offsetY = 0.0;

    //     if (needsScaling) {
    //         double rangeX = Math.max(maxX - minX, 1e-9);
    //         double rangeY = Math.max(maxY - minY, 1e-9);
    //         double scale = Math.min(CANVAS_WIDTH / rangeX, CANVAS_HEIGHT / rangeY);
    //         scaleX = scale;
    //         scaleY = scale;
    //         offsetX = minX;
    //         offsetY = minY;
    //     } else {
    //         double centerX = (minX + maxX) / 2.0;
    //         double centerY = (minY + maxY) / 2.0;
    //         offsetX = centerX - CANVAS_WIDTH / 2.0;
    //         offsetY = centerY - CANVAS_HEIGHT / 2.0;
    //     }

    //     StringBuilder sb = new StringBuilder();
    //     for (int i = 0; i < coords.size(); i++) {
    //         Coordinate c = coords.get(i);
    //         double x = (c.x - offsetX) * scaleX;
    //         double y = CANVAS_HEIGHT - (c.y - offsetY) * scaleY;
    //         if (i == 0) {
    //             sb.append(String.format("%.1f %.1f m", x, y));
    //             sb.append("\n");
    //             continue;
    //         }
    //         sb.append(String.format("%.1f %.1f l", x, y));
    //         sb.append("\n");
    //     }

    //     if (sb.length() > 0) {
    //         sb.setLength(sb.length() - 1);
    //     }

    //     return sb.toString();
    // }
}
