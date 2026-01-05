package ta_src.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineSegment;

import ta_src.data.Polyline;

public class LangSimplification implements SimplificationAlgorithm {
    private double tau;
    private int lookAhead;

    public LangSimplification(double tau, int lookAhead) {
        this.tau = tau;
        this.lookAhead = Math.max(2, lookAhead);
    }

    public void setTau(double tau) {
        this.tau = tau;
    }

    public void setLookAhead(int lookAhead) {
        this.lookAhead = lookAhead;
    }

    public double getTau() {
        return this.tau;
    }

    public int getLookAhead() {
        return this.lookAhead;
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
            int j = Math.min(i + lookAhead - 1, coords.size() - 1);
            boolean simplifiedSegment = false;

            while (j > i + 1) {
                if (allPointWithinTolerance(coords, j, i)) {
                    simplifiedCoords.add(coords.get(j));
                    i = j;
                    simplifiedSegment = true;
                    break;
                }
                --j;
            }

            if (!simplifiedSegment) {
                ++i;
                if (i < coords.size()) {
                    simplifiedCoords.add(coords.get(i));
                }
            }
        }

        if (!simplifiedCoords.get(simplifiedCoords.size() - 1).equals(coords.get(coords.size() - 1))) {
            simplifiedCoords.add(coords.get(coords.size() - 1));
        }

        return new Polyline(simplifiedCoords);
    }

    private boolean allPointWithinTolerance(List<Coordinate> coords, int start, int end) {
        Coordinate p1 = coords.get(start);
        Coordinate p2 = coords.get(end);
        LineSegment segment = new LineSegment(p1, p2);

        for (int k = start + 1;k < end;++k) {
            double dist = segment.distance(coords.get(k));
            if (dist > tau) {
                return false;
            }
        }

        return true;
    }
}