package io.github.douglasjunior.ReactNativeEasyBluetooth;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothClassicService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;

import java.util.UUID;

public class ReactNativeEasyBluetoothModule extends ReactContextBaseJavaModule {

    private static final String TAG = "ReactNativeJS";
    private BluetoothService mService;

    public ReactNativeEasyBluetoothModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ReactNativeEasyBluetooth";
    }

    @ReactMethod
    public void config(ReadableMap config) {

        BluetoothService.BluetoothConfiguration bluetoothConfig = new BluetoothService.BluetoothConfiguration();
        bluetoothConfig.context = getReactApplicationContext();
        bluetoothConfig.bluetoothServiceClass = BluetoothClassicService.class;
        bluetoothConfig.deviceName = config.getString("deviceName");
        bluetoothConfig.characterDelimiter = config.getString("characterDelimiter").charAt(0);
        bluetoothConfig.bufferSize = config.getInt("bufferSize");
        bluetoothConfig.uuid = UUID.fromString(config.getString("uuid"));
        bluetoothConfig.callListenersInMainThread = false;

        BluetoothService.setDefaultConfiguration(bluetoothConfig);
        mService = BluetoothService.getDefaultInstance();
    }

    @ReactMethod
    public void startScan(final Promise devicesDiscovered) {
        Log.d(TAG, "devicesDiscovered: " + devicesDiscovered);

        mService.setOnScanCallback(new BluetoothService.OnBluetoothScanCallback() {

            WritableNativeArray devices = new WritableNativeArray();

            @Override
            public void onDeviceDiscovered(BluetoothDevice bluetoothDevice, int i) {
                Log.d(TAG, "onDeviceDiscovered: " + bluetoothDevice.getAddress() + " - " + bluetoothDevice.getName());
                WritableNativeMap device = new WritableNativeMap();
                device.putString("address", bluetoothDevice.getAddress());
                device.putString("name", bluetoothDevice.getName());
                devices.pushMap(device);
            }

            @Override
            public void onStartScan() {
                Log.d(TAG, "onStartScan");
            }

            @Override
            public void onStopScan() {
                Log.d(TAG, "onStopScan");
                devicesDiscovered.resolve(devices);
            }
        });
        mService.startScan();
    }


}