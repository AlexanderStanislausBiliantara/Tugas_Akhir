package ta_src.algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.locationtech.jts.geom.Coordinate;

import ta_src.data.Polyline;

public class ProgressiveAlgorithm implements SimplificationAlgorithm {
    private double areaThreshold;

    public ProgressiveAlgorithm(double areaThreshold) {
        this.areaThreshold = areaThreshold;
    }

    public void setAreaThreshold(double areaThreshold) {
        this.areaThreshold = areaThreshold;
    }

    public double getAreaThreshold() {
        return this.areaThreshold;
    }

    @Override
    public Polyline simplify(Polyline input) {
        List<Coordinate> coords = input.getCoordinates();

        if (input.getCoordinates() == null || input.getCoordinates().size() <= 2) {
            return new Polyline(new ArrayList<>(coords));
        }

        boolean[] protectedPoints = new boolean[coords.size()];
        protectedPoints[0] = true;
        protectedPoints[coords.size() - 1] = true;

        for (int i = 1;i < coords.size() - 1;++i) {
            if (isLocalMaxOrMin(coords, i) || isDirectionChange(coords, i)) {
                protectedPoints[i] = true;
            }
        }

        double[] area = new double[coords.size()];
        for (int i = 1;i < coords.size() - 1;++i) {
            area[i] = calcTriangleArea(coords.get(i-1), coords.get(i), coords.get(i+1));
        }

        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingDouble(i -> area[i]));

        for (int i = 1;i < coords.size() - 1;++i) {
            if (!protectedPoints[i]) {
                queue.add(i);
            }
        }

        boolean[] deleted = new boolean[coords.size()];
        deleted[0] = false;
        deleted[coords.size() - 1] = false;

        while (!queue.isEmpty()) {
            int currPointIdx = queue.poll();
            if (deleted[currPointIdx]) {
                continue;
            }

            if (area[currPointIdx] >= areaThreshold) {
                break;
            }

            deleted[currPointIdx] = true;
            int left = findPrevActive(deleted, currPointIdx);
            int right = findNextActive(deleted, currPointIdx);

            if (left > 0) {
                double newArea = calcTriangleArea(coords.get(findPrevActive(deleted, left)), coords.get(left), coords.get(right));
                area[left] = newArea;

                if (!protectedPoints[left]) {
                    queue.add(left);
                }
            }

            if (right < coords.size() - 1) {
                double newArea = calcTriangleArea(coords.get(left), coords.get(right), coords.get(findNextActive(deleted, right)));
                area[right] = newArea;

                if (!protectedPoints[right]) {
                    queue.add(right);
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

    private boolean isLocalMaxOrMin(List<Coordinate> coords, int i) {
        double y = coords.get(i).y;
        double prev = coords.get(i - 1).y;
        double next = coords.get(i + 1).y;
        return (y > prev && y > next) || (y < prev && y < next);
    }

    private boolean isDirectionChange(List<Coordinate> coords, int i) {
        double x = coords.get(i).x;
        double prevX = coords.get(i - 1).x;
        double nextX = coords.get(i + 1).x;
        boolean xChange = (x >= prevX && x >= nextX) || (x <= prevX && x <= nextX);

        double y = coords.get(i).y;
        double prevY = coords.get(i - 1).y;
        double nextY = coords.get(i + 1).y;
        boolean yChange = (y >= prevY && y >= nextY) || (y <= prevY && y <= nextY);

        return xChange || yChange;
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
}
