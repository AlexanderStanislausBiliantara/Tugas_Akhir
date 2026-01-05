package ta_src.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineSegment;

import ta_src.data.Polyline;

public class ReumannWitkam implements SimplificationAlgorithm {
    private double epsilon;

    public ReumannWitkam(double epsilon) {
        this.epsilon = epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public double getEpsilon() {
        return this.epsilon;
    }

    @Override
    public Polyline simplify(Polyline input) {
        List<Coordinate> coords = input.getCoordinates();
        if (coords.size() <= 2) {
            return new Polyline(new ArrayList<>(coords));
        }

        List<Coordinate> simplifiedCoords = new ArrayList<>();
        simplifiedCoords.add(coords.get(0));

        int i = 0;
        while (i < coords.size() - 1) {
            Coordinate baseStart = coords.get(i);
            Coordinate baseEnd = coords.get(i + 1);
            LineSegment baseline = new LineSegment(baseStart, baseEnd);

            int k = i + 2;

            while (k < coords.size() && baseline.distance(coords.get(k)) <= epsilon) {
                ++k;
            }

            if (k - 1 > i) {
                simplifiedCoords.add(coords.get(k - 1));
                i = k - 1;
            } else {
                ++i;
                simplifiedCoords.add(coords.get(i));
            }
        }

        if (!coords.get(coords.size() - 1).equals(simplifiedCoords.get(simplifiedCoords.size() - 1))) {
            simplifiedCoords.add(coords.get(coords.size() - 1));
        }

        return new Polyline(simplifiedCoords);
    }
} 
