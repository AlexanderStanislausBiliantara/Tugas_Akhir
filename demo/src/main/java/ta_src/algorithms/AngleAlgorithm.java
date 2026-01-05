package ta_src.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import ta_src.data.Polyline;

public class AngleAlgorithm implements SimplificationAlgorithm {
    private double angleTolerance;

    public AngleAlgorithm(double angleTolerance) {
        this.angleTolerance = Math.toRadians(angleTolerance);
    }

    public void setAngleTolerance(double angleTolerance) {
        this.angleTolerance = angleTolerance;
    }

    public double getAngleTolernce() {
        return this.angleTolerance;
    }

    @Override
    public Polyline simplify(Polyline input) {
        List<Coordinate> coords = input.getCoordinates();

        if (coords.size() <= 2) {
            return new Polyline(new ArrayList<>(coords));
        }

        List<Coordinate> simplifiedCoords = new ArrayList<>();
        simplifiedCoords.add(coords.get(0));

        for (int i = 1;i < coords.size() - 1;++i) {
            double angle = calcAngleAt(coords.get(i - 1), coords.get(i), coords.get(i + 1));
            if (angle > angleTolerance) {
                simplifiedCoords.add(coords.get(i));
            }
        }

        simplifiedCoords.add(coords.get(coords.size() - 1));
        return new Polyline(simplifiedCoords);
    }

    private double calcAngleAt(Coordinate a, Coordinate b, Coordinate c) {
        double baX = a.x - b.x;
        double baY = a.y - b.y;
        double bcX = c.x - b.x;
        double bcY = c.y - b.y;
        
        double dotProduct = baX * bcX + baY * bcY;
        double crossProduct = baX * bcY - baY * bcX;
        double angle = Math.abs(Math.atan2(crossProduct, dotProduct));

        return Math.min(angle, Math.PI - angle);
    }
}
