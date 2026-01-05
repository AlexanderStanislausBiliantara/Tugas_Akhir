package ta_src.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

import ta_src.projection.CoordinateProjector;

public class DatasetLoader {
    public static List<Polyline> loadData(String path) {
        File dataset = new File(path);
        List<Polyline> polylines = new ArrayList<>();

        try (GeoJSONReader reader = new GeoJSONReader(new FileInputStream(dataset))) {
            SimpleFeatureCollection features = reader.getFeatures();

            try (SimpleFeatureIterator it = features.features()) {
                while (it.hasNext()) {
                    SimpleFeature currFeature = it.next();
                    Geometry geom = (Geometry) currFeature.getDefaultGeometry();

                    if (geom instanceof LineString) {
                        // CoordinateProjector.reprojectToUTM((LineString) geom);
                        polylines.add(new Polyline(Arrays.asList(geom.getCoordinates())));
                    } else if (geom instanceof MultiLineString) {
                        for (int i = 0;i < geom.getNumGeometries();++i) {
                            LineString ls = (LineString) geom.getGeometryN(i);
                            // CoordinateProjector.reprojectToUTM((LineString) ls);
                            polylines.add(new Polyline(Arrays.asList(ls.getCoordinates())));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return polylines;
    }
}
