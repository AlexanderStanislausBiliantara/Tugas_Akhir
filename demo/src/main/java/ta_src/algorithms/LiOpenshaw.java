package ta_src.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import ta_src.data.Polyline;

public class LiOpenshaw implements SimplificationAlgorithm {
    private double svo;

    public LiOpenshaw(double svo) {
        this.svo = svo;
    }
    
    public void setSVO(double svo) {
        this.svo = svo;
    }

    public double getSVO() {
        return this.svo;
    }

    @Override
    public Polyline simplify(Polyline input) {
        List<Coordinate> coords = input.getCoordinates();
        if (coords.size() < 2) {
            return new Polyline(new ArrayList<>(coords));
        }

        List<Coordinate> simplifiedCoords = new ArrayList<>();
        int i = 0;
        Coordinate curr = coords.get(i);
        simplifiedCoords.add(curr);

        while (i < coords.size() - 1) {
            double radius = 2 * svo;
            int j = i;
            Coordinate intersection = null;

            while (j < coords.size() - 1) {
                Coordinate a = coords.get(j);
                Coordinate b = coords.get(j + 1);

                double distA = curr.distance(a);
                double distB = curr.distance(b);

                if (distA <= radius && distB > radius) {
                    intersection = findCircleSegmentIntersection(curr, radius, a, b);
                    break;
                }
                ++j;
            }

            if (intersection == null) {
                intersection = coords.get(coords.size() - 1);
            }

            Coordinate mid = new Coordinate((curr.x + intersection.x) / 2.0, (curr.y + intersection.y) / 2.0);
            simplifiedCoords.add(mid);

            i = j + 1;
            if (i >= coords.size()) {
                break;
            }

            curr = coords.get(i);
        }

        return new Polyline(simplifiedCoords);        
    }

    private Coordinate findCircleSegmentIntersection(Coordinate center, double radius, Coordinate a, Coordinate b) {
        double cX = center.x;
        double cY = center.y;
        double aX = a.x;
        double aY = a.y;
        double bX = b.x;
        double bY = b.y;

        double abX = bX - aX;
        double abY = bY - aY;
        double caX = aX - cX;
        double caY = aY - cY;

        double aCoeff = abX * abX + abY * abY;
        double bCoeff = 2 * (caX * abX + caY * abY);
        double cCoeff = (caX * caX + caY * caY) - radius * radius;

        double discriminant = bCoeff * bCoeff - 4 * aCoeff * cCoeff;
        if (discriminant < 0) {
            return b;
        }

        double sqrtDisc = Math.sqrt(discriminant);
        double t1 = (-bCoeff - sqrtDisc) / (2 * aCoeff);
        double t2 = (-bCoeff + sqrtDisc) / (2 * aCoeff);

        double t = Double.MAX_VALUE;
        if (t1 > 1e-9 && t1 <= 1.0) {
            t = Math.min(t, t1);
        }

        if (t2 > 1e-9 && t2 <= 1.0) {
            t = Math.min(t, t2);
        }

        if (t == Double.MAX_VALUE) {
            return b;
        }

        return new Coordinate(aX + t * abX, aY + t * abY);
    }
}
