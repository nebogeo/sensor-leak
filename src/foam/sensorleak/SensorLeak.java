// Sensorleak Copyright (C) 2013 Dave Griffiths
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package foam.sensorleak;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Toast;
import android.net.wifi.WifiManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SensorLeak extends Activity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    static String URI;
    static AssetManager assetManager;

    UDPBroadcaster broadcast;


    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);

        assetManager = getAssets();
        broadcast = new UDPBroadcaster();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //get the accelerometer sensor
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        ((SeekBar) findViewById(R.id.volume_uri)).setOnSeekBarChangeListener(
            new OnSeekBarChangeListener() {
                int lastProgress = 100;
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    assert progress >= 0 && progress <= 100;
                    lastProgress = progress;
                }
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });


        ((SeekBar) findViewById(R.id.pan_uri)).setOnSeekBarChangeListener(
                new OnSeekBarChangeListener() {
            int lastProgress = 100;
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                assert progress >= 0 && progress <= 100;
                lastProgress = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        ((SeekBar) findViewById(R.id.mix)).setOnSeekBarChangeListener(
                new OnSeekBarChangeListener() {
            int lastProgress = 100;
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                assert progress >= 0 && progress <= 100;
                lastProgress = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }


    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event)
    {
        int x = (int)(event.values[0]*10);
        int y = (int)(event.values[1]*10);
        int z = (int)(event.values[2]*10);

        // clamp
        if (x<0) x=0;
        if (y<0) y=0;
        if (z<0) z=0;
        if (x>100) x=100;
        if (y>100) y=100;
        if (z>100) z=100;

        try {
            // todo: put url/port on interface
            // message format???
            broadcast.Send(InetAddress.getByName("10.255.195.233"),8888,"accel:"+x+":"+y+":"+z);
        }
        catch (UnknownHostException e)
        {
            Log.i("starwisp","error:"+e);
        }

        // visualise
        ((SeekBar) findViewById(R.id.volume_uri)).setProgress(x);
        ((SeekBar) findViewById(R.id.pan_uri)).setProgress(y);
        ((SeekBar) findViewById(R.id.mix)).setProgress(z);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /** Called when the activity is about to be destroyed. */
    @Override
    protected void onPause()
    {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    /** Called when the activity is about to be destroyed. */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
