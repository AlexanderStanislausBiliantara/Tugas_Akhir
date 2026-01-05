package ta_src.algorithms;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineSegment;

import ta_src.data.Polyline;

import java.util.ArrayList;
import java.util.List;

public class DouglasPeucker implements SimplificationAlgorithm {
    private double epsilon;

    public DouglasPeucker(double epsilon) {
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
        if (input.getCoordinates() == null || input.getCoordinates().size() <= 2) {
            return new Polyline(new ArrayList<>(input.getCoordinates()));
        }

        List<Coordinate> coords = input.getCoordinates();
        List<Coordinate> simplifiedCoords = new ArrayList<>();

        simplifiedCoords.add(coords.get(0));
        dpRecursive(coords, 0, coords.size() - 1, epsilon, simplifiedCoords);
        
        if (!simplifiedCoords.get(simplifiedCoords.size() - 1).equals(coords.get(coords.size() - 1))) {
            simplifiedCoords.add(coords.get(coords.size() - 1));
        }

        return new Polyline(simplifiedCoords);
    }

    private void dpRecursive(List<Coordinate> coords, int start, int end, double epsilon, List<Coordinate> simplifiedCoords) {
        if (end - start <= 1) {
            return;
        }

        Coordinate startPoint = coords.get(start);
        Coordinate endPoint = coords.get(end);

        int maxIdx = start + 1;
        double maxDist = 0;

        for (int i = start + 1;i < end;++i) {
            double currDist = perpendicularDistance(coords.get(i), startPoint, endPoint);
            if (currDist > maxDist) {
                maxDist = currDist;
                maxIdx = i;
            }
        }

        if (maxDist > epsilon) {
            dpRecursive(coords, start, maxIdx, epsilon, simplifiedCoords);
            simplifiedCoords.add(coords.get(maxIdx));
            dpRecursive(coords, maxIdx, end, epsilon, simplifiedCoords);
        }
    }

    private static double perpendicularDistance(Coordinate p, Coordinate start, Coordinate end) {
        LineSegment segment = new LineSegment(start, end);
        Coordinate closest = segment.closestPoint(p);
        return p.distance(closest);
    }
}
