package com.test.wllnon.sirenxinyi.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.event.DeviceStateMessageEvent;
import com.test.wllnon.sirenxinyi.event.MessageEventPool;
import com.test.wllnon.sirenxinyi.event.ReceivedDataMessageEvent;

import org.greenrobot.eventbus.EventBus;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class DeviceConnectService extends Service {
    public static final String TAG = "DeviceConnectService";

    private BluetoothSPP bluetoothSPP;

    private boolean isAutoConnect = false;

    private Binder binder = new Binder();

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothSPP = new BluetoothSPP(getApplicationContext());
        MessageEventPool.getInstance()
                .configStartSize(200)
                .configMaxSize(250)
                .initialize();

        bindListeners();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setupBluetooth();
        if (isAutoConnect) {
            bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    public boolean setupBluetooth() {
        boolean success = true;
        if (!bluetoothSPP.isBluetoothAvailable()) {
            Toast.makeText(this, "Bluetooth is not available.", Toast.LENGTH_LONG).show();
            stopSelf();
            success = false;
        }
        if (!bluetoothSPP.isBluetoothEnabled()) {
            bluetoothSPP.enable();
            success = false;
        }
        if (!bluetoothSPP.isServiceAvailable()) {
            bluetoothSPP.setupService();
            success = false;
        }

        return success;
    }

    private void bindListeners() {
        bluetoothSPP.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                ReceivedDataMessageEvent messageEvent = MessageEventPool.getInstance().getMessageEvent();
                messageEvent.receivedData = Integer.parseInt(message);
                EventBus.getDefault().post(messageEvent);
            }
        });

        bluetoothSPP.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            @Override
            public void onDeviceConnected(String name, String address) {

            }

            @Override
            public void onDeviceDisconnected() {
                if (isAutoConnect) {
                    autoConnect();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.device_disconnected),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onDeviceConnectionFailed() {
                if (isAutoConnect) {
                    autoConnect();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.device_connect_failed),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        bluetoothSPP.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
            @Override
            public void onServiceStateChanged(int state) {
                Constant.DeviceState deviceState;
                switch (state) {
                    case BluetoothState.STATE_CONNECTED:
                        deviceState = Constant.DeviceState.DEVICE_ON;
                        break;
                    case BluetoothState.STATE_CONNECTING:
                        deviceState = Constant.DeviceState.DEVICE_WAIT;
                        break;
                    default:
                        deviceState = Constant.DeviceState.DEVICE_OFF;
                }
                EventBus.getDefault().postSticky(new DeviceStateMessageEvent(bluetoothSPP.getConnectedDeviceAddress(), deviceState));
            }
        });
    }

    public boolean startConnection() {
        // TODO: 2016/3/31
        bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
        return true;
    }

    public boolean stopConnection() {
        // TODO: 2016/3/31
        bluetoothSPP.stopService();
        return true;
    }

    private void autoConnect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    wait(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean success = setupBluetooth();
                if (success) {
                    bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
                }
            }
        }.start();
    }

    public class Binder extends android.os.Binder {
        public DeviceConnectService getService() {
            return DeviceConnectService.this;
        }
    }

}
