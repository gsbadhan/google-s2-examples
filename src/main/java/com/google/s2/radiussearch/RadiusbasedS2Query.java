package com.google.s2.radiussearch;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.javatuples.Pair;

import com.google.common.geometry.S2;
import com.google.common.geometry.S2Cap;
import com.google.common.geometry.S2Cell;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2CellUnion;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2Point;
import com.google.common.geometry.S2RegionCoverer;

public class RadiusbasedS2Query {

	static HashMap<String, S2Cell> locationsDB = new HashMap<String, S2Cell>();
	static Set<String> searchResults = new HashSet<String>();
	static {
		locationsDB.put("hdfc bank", getS2Cell(28.51454331, 77.38510503, 13));
		locationsDB.put("tyagi dairy", getS2Cell(28.51569693, 77.38408678, 13));
		locationsDB.put("parsvnath prestige", getS2Cell(28.51709518, 77.38449693, 13));
		locationsDB.put("ATS village", getS2Cell(28.51856799, 77.38315462, 13));
		locationsDB.put("Grand omaxe", getS2Cell(28.51153949, 77.39152544, 13));
		locationsDB.put("supertech emerald court", getS2Cell(28.52060011, 77.38199874, 13));
		locationsDB.put("shiv mandir", getS2Cell(28.50422003, 77.38642473, 13));
		locationsDB.put("jaypee green", getS2Cell(28.51936532, 77.36270293, 13));
	}
	
	static double kEarthCircumferenceMeters = 1000 * 40075.017;

	static double earthMetersToRadians(double meters) {
		return (2 * S2.M_PI) * (meters / kEarthCircumferenceMeters);
	}

	private static S2Cell getS2Cell(double lat, double lng, int uptoLevel) {
		if (uptoLevel >= 0 && uptoLevel <= 30) {
			return new S2Cell(new S2Cell(S2LatLng.fromDegrees(lat, lng).toPoint()).id().parent(uptoLevel));
		}
		return new S2Cell(S2LatLng.fromDegrees(lat, lng).toPoint());
	}

	static double radiusRadians(double meters) {
		double radiusRadians = earthMetersToRadians(meters);
		return (radiusRadians * radiusRadians) / 2;
	}

	static S2Point s2Point;

	public static void main(String arg[]) {
		double userLat = 28.51454331;
		double userLng = 77.38510503;
		s2Point = S2LatLng.fromDegrees(userLat, userLng).toPoint();
		System.out.println(new S2Cell(s2Point));
		S2Cap region = S2Cap.fromAxisArea(s2Point, radiusRadians(5000));
		S2RegionCoverer coverer = new S2RegionCoverer();
		coverer.setMinLevel(12);
		coverer.setMaxLevel(12);
		coverer.setMaxCells(70);
		ArrayList<S2CellId> coveringArea = new ArrayList<S2CellId>();
		coverer.getCovering(region, coveringArea);
		System.out.println("5kmradius cells count " + coveringArea.size());
		getLatLng("5kmradius12", coveringArea);

	}

	private static List<S2CellId> getNeighbour(int lvl, S2CellId cellId) {
		List<S2CellId> cellIds = new ArrayList<S2CellId>();
		cellId.getAllNeighbors(lvl, cellIds);
		return cellIds;
	}

	private static List<Pair<Double, Double>> getLatLng(String meta, List<S2CellId> cellIds) {
		List<Pair<Double, Double>> latLngs = new ArrayList<>();
		// QGIS file format
		StringBuilder data = new StringBuilder("\"UTM_X,N,19,11\",\"UTM_Y,N,19,11\"");
		data.append("\n");
		StringBuilder cellTokens = new StringBuilder();
		StringBuilder cellNumbers = new StringBuilder();
		cellIds.forEach(cell -> {
			cellTokens.append(cell.toToken()).append(",");
			cellNumbers.append(cell.id()).append(",");
			intersection(cell);
			latLngs.add(Pair.with(cell.toLatLng().latDegrees(), cell.toLatLng().lngDegrees()));
			data.append(cell.toLatLng().lngDegrees() + "," + cell.toLatLng().latDegrees() + "\n");
		});
		System.out.println(
				"total locations " + locationsDB.size() + " found " + searchResults.size() + " " + searchResults);
		// optional
		writeFile(meta, data.toString());
		System.out.println(cellTokens.toString());
		System.out.println(cellNumbers.toString());
		return latLngs;
	}

	private static void intersection(S2CellId cell) {
		locationsDB.forEach((k, v) -> {
			if (cell.intersects(v.id())) {
				searchResults.add(cell.toToken()+"/"+v.id().toToken() + " " + k);
			}
		});
	}

	private static void writeFile(String filename, String data) {
		try {
			FileOutputStream outputStream = new FileOutputStream(filename, false);
			outputStream.write(data.getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
