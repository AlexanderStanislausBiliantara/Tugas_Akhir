package ta_src.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import ta_src.data.Polyline;

public class SleeveFitting implements SimplificationAlgorithm {
    private double epsilon;

    public SleeveFitting(double epsilon) {
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
        int i = 0;
        
        while (i < coords.size()) {
            simplifiedCoords.add(coords.get(i));

            if (i == coords.size() - 1) {
                break;
            }

            double alphaMin = 0.0;
            double alphaMax = 2 * Math.PI;

            int j = i + 1;
            boolean sleeveValid = true;

            while (j < coords.size() && sleeveValid) {
                double[] sector = calcSectorBound(coords.get(i), coords.get(j));

                double newAlphaMin = Math.max(alphaMin, sector[0]);
                double newAlphaMax = Math.min(alphaMax, sector[1]);

                if (newAlphaMin <= newAlphaMax) {
                    alphaMin = newAlphaMin;
                    alphaMax = newAlphaMax;
                    ++j;
                } else {
                    sleeveValid = false;
                }
            }

            if (j - 1 > i) {
                i = j - 1;
            } else {
                ++i;
            }
        }

        return new Polyline(simplifiedCoords);
    }

    private double[] calcSectorBound(Coordinate p, Coordinate q) {
        double dx = q.x - p.x;
        double dy = q.y - p.y;
        double d = Math.sqrt(dx * dx + dy * dy);

        if (d <= this.epsilon) {
            return new double[]{0.0, 2 * Math.PI};
        }

        double baseAngle = Math.atan2(dy, dx);
        double delta = Math.asin(epsilon / d);

        double alphaMin = baseAngle - delta;
        double alphaMax = baseAngle + delta;

        return new double[]{alphaMin, alphaMax};
    }
}
