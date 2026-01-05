package ta_src.tests;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import ta_src.algorithms.AngleAlgorithm;
import ta_src.algorithms.CircleAlgorithm;
import ta_src.algorithms.DouglasPeucker;
import ta_src.algorithms.LangSimplification;
import ta_src.algorithms.LiOpenshaw;
import ta_src.algorithms.ProgressiveAlgorithm;
import ta_src.algorithms.ReumannWitkam;
import ta_src.algorithms.SleeveFitting;
import ta_src.algorithms.VisvalingamWhyatt;
import ta_src.data.Polyline;

public class AlgorithmTester {
    public static void main(String[] args) {
        List<Coordinate> coords = List.of(
            new Coordinate(0, 0),
            new Coordinate(2, 6),
            new Coordinate(4, -4),
            new Coordinate(8, 8),
            new Coordinate(12, 0),
            new Coordinate(16, -3),
            new Coordinate(20, 10)
        );

        Polyline input = new Polyline(coords);
        DouglasPeucker dp = new DouglasPeucker(7);
        ProgressiveAlgorithm pa = new ProgressiveAlgorithm(1000);
        AngleAlgorithm angAlg = new AngleAlgorithm(60);
        CircleAlgorithm ca = new CircleAlgorithm(10);
        ReumannWitkam rw = new ReumannWitkam(7);
        LiOpenshaw lo = new LiOpenshaw(5);
        LangSimplification ls = new LangSimplification(2, 4);
        SleeveFitting sf = new SleeveFitting(6);
        VisvalingamWhyatt vw = new VisvalingamWhyatt(10);
        Polyline result = lo.simplify(input);

        System.out.print("Original: " + coords.size() + " points " + "-> ");
        for (Coordinate c : input.getCoordinates()) {
            System.out.print("(" + c.x + ", " + c.y + ")");
            System.out.print(", ");
        }

        System.out.println();

        System.out.print("Simplified: " + result.getCoordinates().size() + " points " + "-> ");
        for (Coordinate c : result.getCoordinates()) {
            System.out.print("(" + c.x + ", " + c.y + ")");
            System.out.print(", ");
        }
    }
}
