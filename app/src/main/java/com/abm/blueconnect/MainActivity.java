package com.abm.blueconnect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    TextView StatusTv, PairedTv;
    ImageView blueIcon;
    Button onBtn, offBtn, discoverBtn, pairedBtn;

    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusTv = findViewById(R.id.BTstatus);
        PairedTv = findViewById(R.id.pairedList);
        blueIcon = findViewById(R.id.BTicon);
        onBtn = findViewById(R.id.onBtn);
        offBtn = findViewById(R.id.offBtn);
        discoverBtn = findViewById(R.id.discoverableBtn);
        pairedBtn = findViewById(R.id.pairedBtn);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            StatusTv.setText("Bluetooth is not available");
        }
        else {
            StatusTv.setText("Bluetooth is available");
        }

        if (bluetoothAdapter.isEnabled()) {
            blueIcon.setImageResource(R.drawable.ic_action_on);
        }
        else {
            blueIcon.setImageResource(R.drawable.ic_action_off);
        }

        onBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!bluetoothAdapter.isEnabled()) {
                    showToast("Turning On Bluetooth...");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                }
                else {
                    showToast("Bluetooth is Already Turned On!");
                }
            }
        });

        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.disable();
                    showToast("Turning Off Bluetooth...");
                    blueIcon.setImageResource(R.drawable.ic_action_off);
                }
                else {
                    showToast("Bluetooth is Already Turned Off!");
                }

            }
        });

        discoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!bluetoothAdapter.isDiscovering()){
                    showToast("Making Device Discoverable");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }
            }
        });

        pairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter.isEnabled()){
                    PairedTv.setText("Paired Devices:");
                    Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                    for (BluetoothDevice device: devices) {
                        PairedTv.append("\nDevice: " + device.getName() + "," + device);
                    }
                }
                else {
                    showToast("Turn On Bluetooth to Get The Paired Devices List!");
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    blueIcon.setImageResource(R.drawable.ic_action_on);
                    showToast("Bluetooth is On");
                }
                else {
                    showToast("Couldn't Turn On Bluetooth");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
