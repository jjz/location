package com.pi.location;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationManagerActivity extends Activity {
	private LocationManager mLocationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLocationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
	}

	private String getProvider() {
		Criteria criteria = new Criteria();
		// ��ѯ���ȣ���
		criteria.setAccuracy(Criteria.ACCURACY_FINE);

		// �Ƿ��ѯ��������
		criteria.setAltitudeRequired(false);
		// ������ϢACCURACY_LOW, ACCURACY_MEDIUM, ACCURACY_HIGH or NO_REQUIREMENT.
		// criteria.setVerticalAccuracy(accuracy)

		// �Ƿ��ѯ��λ��:��
		criteria.setBearingRequired(false);
		// ���÷�λ�ǣ�ACCURACY_LOW, ACCURACY_HIGH, or NO_REQUIREMENT.
		// criteria.setBearingAccuracy(accuracy);

		// ����ˮƽ���Ҿ��ȣ�ACCURACY_HIGH �� ACCURACY_LOW �� ACCURACY_MEDIUM
		// criteria.setHorizontalAccuracy();s

		// �Ƿ������ѣ���
		criteria.setCostAllowed(false);

		// ����Ҫ�󣺵�
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		// �Ƿ��ṩ�ٶ���Ϣ
		criteria.setSpeedRequired(false);
		// ACCURACY_LOW, ACCURACY_HIGH, or NO_REQUIREMENT.
		// criteria.setSpeedAccuracy();

		// ��������ʵķ���������provider����2������Ϊtrue˵��,���ֻ��һ��provider����Ч��,�򷵻ص�ǰprovider
		return mLocationManager.getBestProvider(criteria, true);
	}

	private final LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub

		}
	};

	private Location getBestLocatio(Location newLocation, Location lastLocation) {
		return newLocation;

	}

	private static final int TWO_MINUTES = 1000 * 60 * 2;

	// http://developer.android.com/guide/topics/location/strategies.html
	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix * @param location The new Location that you want to evaluate
	 * * @param currentBestLocation The current Location fix, to which you want
	 * to compare the new one
	 */
	/** Checks whether two providers are the same */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}
		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;
		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}
		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;
		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());
		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

}
