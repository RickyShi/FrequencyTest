package edu.missouri.ricky.frequencytest;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	private static final double nbElements = 100;
	private SensorManager sensorManager;
	private Sensor accelerometer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		boolean accelSupported = sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);
		accelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (!accelSupported) {
			sensorManager.unregisterListener(this, accelerometer);
			((TextView) findViewById(R.id.frequncy))
					.setText("Accelerometer not detected");
		}

	}

	/**
	 * Always make sure to disable sensors you don't need, especially when your
	 * activity is paused. Failing to do so can drain the battery in just a few
	 * hours. Note that the system will not disable sensors automatically when
	 * the screen turns off.
	 **/
	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Nothing to do
	}

	long now = 0;
	long time = 0;
	int temp = 0;

	@Override
	public void onSensorChanged(SensorEvent event) {
		long tS;
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float x, y, z;
			x = event.values[0];
			y = event.values[1];
			z = event.values[2];

			// Get timestamp of the event
			tS = event.timestamp;

			((TextView) this.findViewById(R.id.axex)).setText(String.format(
					getString(R.string.x_axis), x));
			((TextView) this.findViewById(R.id.axey)).setText(String.format(
					getString(R.string.y_axis), y));
			((TextView) this.findViewById(R.id.axez)).setText(String.format(
					getString(R.string.z_axis), z));

			// Get the mean frequency for "nbElements" (=100) elements
			if (now != 0) {
				temp++;
				if (temp == nbElements) {
					time = tS - now;
					double result = (nbElements * 1000000000 / time);
					((TextView) findViewById(R.id.frequncy)).setText(String
							.format(getString(R.string.frequncy), result)
							);
					temp = 0;
				}
			}
			// To set up now on the first event and do not change it while we do
			// not have "nbElements" events
			if (temp == 0) {
				now = tS;
			}
		}
	}

	public void onClickQuit(View v) {
		finish();
	}
}
