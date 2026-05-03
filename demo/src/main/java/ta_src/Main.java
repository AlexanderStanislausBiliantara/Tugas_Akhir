package ta_src;

import ta_src.algorithms.AngleAlgorithm;
import ta_src.algorithms.CircleAlgorithm;
import ta_src.algorithms.DouglasPeucker;
import ta_src.algorithms.LangSimplification;
import ta_src.algorithms.LiOpenshaw;
import ta_src.algorithms.ProgressiveAlgorithm;
import ta_src.algorithms.ReumannWitkam;
import ta_src.algorithms.SimplificationAlgorithm;
import ta_src.algorithms.SleeveFitting;
import ta_src.algorithms.VisvalingamWhyatt;
import ta_src.data.DatasetLoader;
import ta_src.data.Polyline;
import ta_src.projection.CoordinateProjector;

import java.util.ArrayList;
import java.util.List;

// import org.locationtech.jts.geom.Coordinate;


public class Main {
    static {
        System.setProperty("org.geotools.referencing.forceXY", "true");
    
        try {
            org.geotools.referencing.CRS.decode("EPSG:4326", true);
        } catch (Exception e) {
            System.exit(1);
        }
    }
    public static void main(String[] args) {
        System.setProperty("org.geotools.referencing.forceXY", "true");

        // String iptPath = "C:\\Users\\MyLaptop\\Documents\\Tugas Akhir\\kode\\Tugas_Akhir\\data\\NHD\\NHDFlowline.geojson";
        // String optPath = "C:\\Users\\MyLaptop\\Documents\\Tugas Akhir\\kode\\Tugas_Akhir\\output\\dataset_test_results\\NHD";

        if (args.length < 3) {
            System.err.println("err");
            System.exit(1);
        }

        String inputPath = args[0];
        String outputDir = args[1];
        double tolerance = Double.parseDouble(args[2]);

        List<Polyline> polylines = DatasetLoader.loadData(inputPath);
        String outputPath = outputDir + "/" + "reprojected_dataset" + ".geojson";
        Exporter.geoJSONExporter(polylines, outputPath);

        SimplificationAlgorithm[] algorithms = {
            new DouglasPeucker(tolerance),
            new VisvalingamWhyatt(tolerance * tolerance),
            new CircleAlgorithm(tolerance),
            new AngleAlgorithm(60),
            new ReumannWitkam(tolerance),
            new LangSimplification(tolerance, 4),
            new SleeveFitting(tolerance),
            new ProgressiveAlgorithm(tolerance * tolerance),
            new LiOpenshaw(tolerance / 2.0)
        };

        for (SimplificationAlgorithm alg : algorithms) {
            List<Polyline> simplifiedPolylines = new ArrayList<>();

            for (Polyline p : polylines) {
                Polyline simplified = alg.simplify(p);
                simplifiedPolylines.add(simplified);
            }

            String algName = alg.getClass().getSimpleName();
            outputPath = outputDir + "/" + algName + ".geojson";
            Exporter.geoJSONExporter(simplifiedPolylines, outputPath);
            System.out.println("Exported: " + algName);
        }

        // String iptPath = "C:\\Users\\MyLaptop\\Documents\\Tugas Akhir\\dataset\\natural earth\\geojson\\rivers-natural-earth.geojson";

        // List<Polyline> polylines = DatasetLoader.loadData(iptPath);
        // if (polylines.isEmpty()) {
        //     System.out.println("No polylines found.");
        //     return;
        // }

        // System.out.println(polylines.size());

        // Polyline original = polylines.get(25);
        // List<Coordinate> coords = List.of(
        //     new Coordinate(96, 784),
        //     new Coordinate(384, 784),
        //     new Coordinate(320, 704),
        //     new Coordinate(144, 736),
        //     new Coordinate(80, 640),
        //     new Coordinate(112, 512),
        //     new Coordinate(256, 656),
        //     new Coordinate(448, 592),
        //     new Coordinate(480, 736),
        //     new Coordinate(560, 688), 
        //     new Coordinate(560, 480),
        //     new Coordinate(336, 544),
        //     new Coordinate(336, 608),
        //     new Coordinate(240, 592),
        //     new Coordinate(352, 320),
        //     new Coordinate(160, 368),
        //     new Coordinate(48, 464),
        //     new Coordinate(32, 336),
        //     new Coordinate(176, 240),
        //     new Coordinate(384, 160), 
        //     new Coordinate(448, 304),
        //     new Coordinate(512, 144),
        //     new Coordinate(336, 80),
        //     new Coordinate(112, 144),
        //     new Coordinate(144, 64)
        // );

        // Polyline test = new Polyline(coords);
        // System.out.printf("Num of points in original : %d\n", test.getCoordinates().size());
        // System.out.println(Exporter.toSimpleIpeString(test));
        // System.out.println();
        // System.out.println("=================");
        // System.out.println();

        // for (SimplificationAlgorithm algo : algorithms) {
        //     Polyline simplified = algo.simplify(test);
        //     System.out.println(algo.getClass().getSimpleName());
        //     System.out.printf("Num of points in simplified : %d\n", simplified.getCoordinates().size());
        //     System.out.println(Exporter.toSimpleIpeString(simplified));
        //     System.out.println();
        // }
    }
}