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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import java.net.InetAddress;

public class UDPBroadcaster {
    private AsyncTask<Void, Void, Void> task;
    String m_message;
    InetAddress m_address;
    int m_port;

    public void Send(InetAddress address, int port, String msg) {
        // store the details so we can get to them later via async task
        m_message = msg;
        m_address = address;
        m_port = port;

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                DatagramSocket ds = null;
                try {
                    ds = new DatagramSocket();
                    DatagramPacket dp;
                    dp = new DatagramPacket(m_message.getBytes(),
                                            m_message.length(),
                                            m_address, m_port);
                    ds.setBroadcast(true);
                    ds.send(dp);
                } catch (Exception e) {
                        e.printStackTrace();
                } finally {
                    if (ds != null) {
                        ds.close();
                    }
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
            }
        };

        if (Build.VERSION.SDK_INT >= 11) task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else task.execute();
    }

}
