package com.google.s2.polygon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.geometry.S2Cell;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2Loop;
import com.google.common.geometry.S2Point;
import com.google.gson.Gson;

/**
 * build s2loop from polygon and search s2point from all polygons
 *
 */
public class PolygonsToS2loopAndS2pointSearch {

	public static void main(String[] xx) {
		String polygon1 = "[ [ 77.381796762917745, 28.517792530100166 ], [ 77.384687720343848, 28.517636262131187 ], [ 77.386469175190214, 28.516823668692499 ], [ 77.387109873863025, 28.51557352494067 ], [ 77.381140437448039, 28.516558013145236 ], [ 77.381140437448039, 28.516558013145236 ], [ 77.381140437448039, 28.516558013145236 ], [ 77.381796762917745, 28.517792530100166 ] ]";
		S2Loop loop1 = new S2Loop(getS2PointsList(getLatLngFronGeojson(polygon1)));

		String polygon2 = "[ [ 77.38308963774162, 28.521585426818593 ], [ 77.38384169106034, 28.519667690855837 ], [ 77.379987417801857, 28.519592485523962 ], [ 77.380664265788724, 28.522243473472479 ], [ 77.38308963774162, 28.521585426818593 ] ]";
		S2Loop loop2 = new S2Loop(getS2PointsList(getLatLngFronGeojson(polygon2)));

		String polygon3 = "[ [ 77.38397366392654, 28.515772929001116 ], [ 77.384755801746351, 28.515626929941419 ], [ 77.384693230720771, 28.515215004022991 ], [ 77.38417701975969, 28.5150377194505 ], [ 77.383718165572077, 28.515428788360403 ], [ 77.38397366392654, 28.515772929001116 ] ]";
		S2Loop loop3 = new S2Loop(getS2PointsList(getLatLngFronGeojson(polygon3)));

		String polygon4 = "[ [ 77.385345232292892, 28.514885667291018 ], [ 77.386151428494315, 28.514184387033616 ], [ 77.386676008214422, 28.514675835402979 ], [ 77.386648398755469, 28.515156239988759 ], [ 77.385897421471952, 28.515366071876802 ], [ 77.385555064180934, 28.515404725119335 ], [ 77.385345232292892, 28.514885667291018 ] ]";
		S2Loop loop4 = new S2Loop(getS2PointsList(getLatLngFronGeojson(polygon4)));

		double userLat = 28.51577292;
		double userLng = 77.38534523;
		S2Point s2Point = S2LatLng.fromDegrees(userLat, userLng).toPoint();
		S2Cell s2Cell = new S2Cell(s2Point);
		System.out.println("l1 point intersects :" + loop1.mayIntersect(s2Cell));
		System.out.println("l2 point intersects :" + loop2.mayIntersect(s2Cell));
		System.out.println("l3 point intersects :" + loop3.mayIntersect(s2Cell));
		System.out.println("l4 point intersects :" + loop4.mayIntersect(s2Cell));
		System.out.println("loop intersects :" + loop1.intersects(loop2));
		System.out.println("loop containsOrCrosses :" + loop1.containsOrCrosses(loop2));

	}


	private static List<S2Point> getS2PointsList(Collection<?> list) {
		List<S2Point> s2Points = new ArrayList<>();
		for (Object ll : list) {
			List<?> latlng = (List<?>) ll;
			S2Point s2point = S2LatLng.fromDegrees(Double.parseDouble(latlng.get(0).toString()),
					Double.parseDouble(latlng.get(1).toString())).toPoint();
			s2Points.add(s2point);
		}
		System.out.println(s2Points);
		return s2Points;
	}

	private static Collection<?> getLatLngFronGeojson(String polygon) {
		Collection<?> data = new Gson().fromJson(polygon, Collection.class);
		System.out.println(data);
		return data;
	}

}
