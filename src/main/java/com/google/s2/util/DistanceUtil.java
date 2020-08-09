package com.google.s2.util;

import java.math.BigDecimal;

import com.google.common.geometry.S1Angle;
import com.google.common.geometry.S2LatLng;

public class DistanceUtil {

	private DistanceUtil() {
	}

	public static void main(String[] args) {
		double lat1 = 28.51454331;
		double lng1 = 77.38510503;
		double lat2 = 28.518657621503635;
		double lng2 = 77.384119715594346;
		distanceV1(lat1, lng1, lat2, lng2, Unit.KILOMETERS);
		distanceV2(lat1, lng1, lat2, lng2, Unit.KILOMETERS);
	}

	public enum Unit {
		MILES, KILOMETERS, NAUTICAL_MILES;
	}

	public static Double distanceV1(Double latitude1, Double longitude1, Double latitude2, Double longitude2,
			Unit unit) {
		S2LatLng latLng1 = new S2LatLng(S1Angle.degrees(truncateupto8(new BigDecimal(latitude1)).doubleValue()),
				S1Angle.degrees(truncateupto8(new BigDecimal(longitude1)).doubleValue()));
		S2LatLng latLng2 = new S2LatLng(S1Angle.degrees(truncateupto8(new BigDecimal(latitude2)).doubleValue()),
				S1Angle.degrees(truncateupto8(new BigDecimal(longitude2)).doubleValue()));
		double distance = toUnit(unit, latLng1.getDistance(latLng2).degrees());
		System.out.println("distance " + distance);
		return distance;
	}

	public static double distanceV2(Double latitude1, Double longitude1, Double latitude2, Double longitude2,
			Unit unit) {
		if ((latitude1 == latitude2) && (longitude1 == longitude2)) {
			return 0;
		} else {
			double theta = longitude1 - longitude2;
			double dist = Math.sin(Math.toRadians(latitude1)) * Math.sin(Math.toRadians(latitude2))
					+ Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
							* Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			double distance = toUnit(unit, dist);
			System.out.println("distance " + distance);
			return distance;
		}
	}

	private static BigDecimal truncateupto8(BigDecimal value) {
		String[] xx = value.toString().split("\\.", 2);
		if (xx[1].length() > 8) {
			xx[1] = xx[1].substring(0, 8);
		}
		return new BigDecimal(xx[0] + "." + xx[1]);
	}

	private static double toUnit(Unit unit, double degree) {
		// default Miles
		degree = degree * 60 * 1.1515;
		switch (unit) {
		case KILOMETERS:
			degree = degree * 1.609344;
			break;
		case NAUTICAL_MILES:
			degree = degree * 0.8684;
			break;
		default:
			break;
		}
		return degree;
	}

}
