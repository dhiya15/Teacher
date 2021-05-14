package com.app.teacher.ui.attendance;


import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.teacher.R;
import com.app.teacher.ui.marks.MarksItem;

import java.util.ArrayList;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.interfaces.DeviceCallback;
import me.aflak.bluetooth.interfaces.DiscoveryCallback;


public class AttendanceActivity extends AppCompatActivity {

    Bluetooth bluetooth;
    private final static int REQUEST_ENABLE_BT = 1111;
    ArrayList<BluetoothDevice> btDevices;
    ListView blueListView;
    BlueAdapter adapter;

    public static final int BLUE_CONNECTED = 0;
    public static final int BLUE_CONNECTED_ERR = 1;

    TextView connectionStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_activity);

        bluetooth = new Bluetooth(this);
        btDevices = new ArrayList<>();
        blueListView = findViewById(R.id.blueListView);
        connectionStatus = findViewById(R.id.connectionStatus);

        adapter = new BlueAdapter(btDevices);
        blueListView.setAdapter(adapter);

        bluetooth.setDiscoveryCallback(new DiscoveryCallback() {
            @Override public void onDiscoveryStarted() {
                Log.d("Start", "Start");
                btDevices.clear();
                adapter.notifyDataSetChanged();
            }
            @Override public void onDiscoveryFinished() {
                Log.d("Finished", "Finished");
                adapter = new BlueAdapter(btDevices);
                blueListView.setAdapter(adapter);
            }
            @Override public void onDeviceFound(BluetoothDevice device) {
                Log.d("Found", device.getName());
                if(! btDevices.contains(device)){
                    btDevices.add(device);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override public void onDevicePaired(BluetoothDevice device) {}
            @Override public void onDeviceUnpaired(BluetoothDevice device) {}
            @Override public void onError(int errorCode) {}
        });

        blueListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bluetooth.stopScanning();
                bluetooth.connectToDevice(btDevices.get(i));
            }
        });

        bluetooth.setDeviceCallback(new DeviceCallback() {
            @Override public void onDeviceConnected(BluetoothDevice device) {
                Message message2 = Message.obtain();
                message2.what = BLUE_CONNECTED;
                handler.sendMessage(message2);
            }
            @Override public void onDeviceDisconnected(BluetoothDevice device, String message) {}
            @Override public void onMessage(byte[] message) {
                String s = new String(message);
            }
            @Override public void onError(int errorCode) {
                Message message2 = Message.obtain();
                message2.what = BLUE_CONNECTED_ERR;
                handler.sendMessage(message2);
            }
            @Override public void onConnectError(BluetoothDevice device, String message) {}
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bluetooth.onStart();
        if(bluetooth.isEnabled()){
            bluetooth.startScanning();
        } else {
            bluetooth.showEnableDialog(AttendanceActivity.this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetooth.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    bluetooth.startScanning();
                }
                break;
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case BLUE_CONNECTED:
                    connectionStatus.setText("Connected.");
                    connectionStatus.setTextColor(Color.GREEN);
                    break;
                case BLUE_CONNECTED_ERR:
                    connectionStatus.setText("Faild.");
                    connectionStatus.setTextColor(Color.RED);
                    break;
            }
            return true;
        }
    });

    public class BlueAdapter extends BaseAdapter {

        ArrayList<BluetoothDevice> list;

        public BlueAdapter(ArrayList<BluetoothDevice> list){
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.blue_item, null);

            ImageView blueImage = view.findViewById(R.id.imageBlue);
            TextView blueName = view.findViewById(R.id.blueName);
            TextView blueAddrr = view.findViewById(R.id.blueAddrr);

            blueImage.setImageResource(R.drawable.download);
            blueName.setText(list.get(position).getName());
            blueAddrr.setText(list.get(position).getAddress());

            return view;
        }

    }

}
