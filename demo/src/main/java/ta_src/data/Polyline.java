package ta_src.data;

import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Polyline {
    private List<Coordinate> points; 

    public Polyline(List<Coordinate> coords) {
        this.points = new ArrayList<>(coords);
    }

    public List<Coordinate> getCoordinates() {
        return this.points;
    }
}
