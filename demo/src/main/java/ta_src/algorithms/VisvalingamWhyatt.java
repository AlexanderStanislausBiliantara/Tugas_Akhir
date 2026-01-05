package ta_src.algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.locationtech.jts.geom.Coordinate;

import ta_src.data.Polyline;

public class VisvalingamWhyatt implements SimplificationAlgorithm {
    private double areaThreshold;

    public VisvalingamWhyatt(double areaThreshold) {
        this.areaThreshold = areaThreshold;
    }

    @Override
    public Polyline simplify(Polyline input) {
        List<Coordinate> coords = input.getCoordinates();
        if (coords.size() <= 2) {
            return new Polyline(new ArrayList<>(coords));
        }

        boolean[] deleted = new boolean[coords.size()];
        deleted[0] = false;
        deleted[coords.size() - 1] = false;

        double[] area = new double[coords.size()];
        
        for (int i = 1;i < coords.size() - 1;++i) {
            area[i] = calcTriangleArea(coords.get(i - 1), coords.get(i), coords.get(i + 1));
        }

        PriorityQueue<PointWithArea> pq = new PriorityQueue<>(Comparator.comparingDouble(p -> p.area));

        for (int i = 1;i < coords.size() - 1;++i) {
            pq.add(new PointWithArea(i, area[i]));
        }

        while (!pq.isEmpty()) {
            PointWithArea curr = pq.poll();
            if (deleted[curr.idx]) {
                continue;
            }

            if (curr.area >= areaThreshold) {
                break;
            }

            deleted[curr.idx] = true;

            int left = findPrevActive(deleted, curr.idx);
            if (left > 0) {
                int prevLeft = findPrevActive(deleted, left);
                if (prevLeft >= 0) {
                    double newArea = calcTriangleArea(coords.get(prevLeft), coords.get(left), coords.get(findNextActive(deleted, left)));
                    area[left] = newArea;
                    pq.add(new PointWithArea(left, newArea));
                }
            }

            int right = findNextActive(deleted, curr.idx);
            if (right < coords.size() - 1) {
                int nextRight = findNextActive(deleted, right);
                if (nextRight < coords.size()) {
                    double newArea = calcTriangleArea(coords.get(findPrevActive(deleted, right)), coords.get(right), coords.get(nextRight));
                    area[right] = newArea;
                    pq.add(new PointWithArea(right, newArea));
                }
            }
        }

        List<Coordinate> simplifiedCoords = new ArrayList<>();
        for (int i = 0;i < coords.size();++i) {
            if (!deleted[i]) {
                simplifiedCoords.add(coords.get(i));
            }
        }

        return new Polyline(simplifiedCoords);
    }

    private double calcTriangleArea(Coordinate a, Coordinate b, Coordinate c) {
        return Math.abs((((a.x + b.x) * (b.y - a.y)) + ((b.x + c.x) * (c.y - b.y))) / 2.0);
    }

    private int findPrevActive(boolean[] deleted, int i) {
        for (int j = i - 1;j >= 0;--j) {
            if (!deleted[j]) {
                return j;
            }
        }

        return -1;
    }

    private int findNextActive(boolean[] deleted, int i) {
        for (int j = i + 1;j < deleted.length;++j) {
            if (!deleted[j]) {
                return j;
            }
        }

        return deleted.length;
    }

    private static class PointWithArea {
        int idx;
        double area;
        PointWithArea(int idx, double area) {
            this.idx = idx;
            this.area = area;
        }
    }
}
