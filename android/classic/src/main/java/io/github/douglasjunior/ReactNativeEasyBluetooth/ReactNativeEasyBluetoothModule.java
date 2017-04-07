package io.github.douglasjunior.ReactNativeEasyBluetooth;

import android.bluetooth.BluetoothDevice;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
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
    public void config(ReadableMap config){

        BluetoothService.BluetoothConfiguration bluetoothConfig = new BluetoothService.BluetoothConfiguration();
        bluetoothConfig.context = getReactApplicationContext();
        bluetoothConfig.bluetoothServiceClass = BluetoothClassicService.class;
        bluetoothConfig.deviceName = config.getString("deviceName");
        bluetoothConfig.characterDelimiter = config.getString("characterDelimiter").charAt(0);
        bluetoothConfig.bufferSize = config.getInt("bufferSize");
        bluetoothConfig.uuid = UUID.fromString(config.getString("uuid"));

        BluetoothService.setDefaultConfiguration(bluetoothConfig);
        mService = BluetoothService.getDefaultInstance();
    }

    @ReactMethod
    public void startScan(final Callback callbackOnStart, final Callback callbackOnStop, final Callback callbackOnDeviceDiscovered){
        mService.setOnScanCallback(new BluetoothService.OnBluetoothScanCallback() {
            @Override
            public void onDeviceDiscovered(BluetoothDevice bluetoothDevice, int i) {
                callbackOnDeviceDiscovered.invoke(bluetoothDevice.getAddress(), bluetoothDevice.getName(), i);
            }

            @Override
            public void onStartScan() {
                callbackOnStart.invoke();
            }

            @Override
            public void onStopScan() {
                callbackOnStop.invoke();
            }
        });
        mService.startScan();
    }


}