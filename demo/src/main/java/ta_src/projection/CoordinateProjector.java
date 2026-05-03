package ta_src.projection;

import javax.xml.crypto.dsig.TransformException;

import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

public class CoordinateProjector {
    public static int getUTMZone(double lon) {
        return (int) Math.floor((lon + 180.0) / 6.0) + 1;
    }

    public static LineString reprojectToUTM(LineString src) throws NoSuchAuthorityCodeException, FactoryException, TransformException, MismatchedDimensionException, org.geotools.api.referencing.operation.TransformException {
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");
        Coordinate startCoord = src.getCoordinateN(0);
        int utmZone = getUTMZone(startCoord.x);
        String hemisphere = "";

        if (startCoord.y >= 0) {
            hemisphere = "326";
        } else {
            hemisphere = "327";
        }

        String targetEPSG = "EPSG:" + hemisphere + String.format("%02d", utmZone);
        CoordinateReferenceSystem targetCRS = CRS.decode(targetEPSG);

        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, false);
        LineString target = (LineString) JTS.transform(src, transform);

        return target;
    }

    public static LineString reprojectFromUTM(LineString utmLine) throws NoSuchAuthorityCodeException, FactoryException, TransformException, MismatchedDimensionException, org.geotools.api.referencing.operation.TransformException {
        Coordinate startCoord = utmLine.getCoordinateN(0);
        int utmZone = (int) Math.floor((startCoord.x + 180.0) / 6.0) + 1;
        String hemisphere = "";

        if (startCoord.y >= 0) {
            hemisphere = "326";    
        } else {
            hemisphere = "327";
        }

        String targetEPSG = "EPSG:" + hemisphere + String.format("%02d", utmZone);

        CoordinateReferenceSystem sourceCRS = CRS.decode(targetEPSG);
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4236");

        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, false);

        return (LineString) JTS.transform(utmLine, transform);
    }
}
