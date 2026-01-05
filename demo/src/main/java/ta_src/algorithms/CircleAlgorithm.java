package ta_src.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import ta_src.data.Polyline;

public class CircleAlgorithm implements SimplificationAlgorithm {
    private double radius;

    public CircleAlgorithm(double radius) {
        this.radius = radius;
    }

    @Override
    public Polyline simplify(Polyline input) {
        List<Coordinate> coords = input.getCoordinates();

        if (coords.size() <= 2) {
            return new Polyline(new ArrayList<>(coords));
        }

        List<Coordinate> simplifiedCoords = new ArrayList<>();
        simplifiedCoords.add(coords.get(0));

        int curr = 0;

        for (int i = 1;i < coords.size() - 1;++i) {
            double distance = coords.get(curr).distance(coords.get(i));
            if (distance > radius) {
                simplifiedCoords.add(coords.get(i));
                curr = i;
            }
        }

        simplifiedCoords.add(coords.get(coords.size() - 1));
        return new Polyline(simplifiedCoords);
    }
}
