package io.github.douglasjunior.ReactNativeEasyBluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.ParcelUuid;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothConfiguration;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothStatus;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothWriter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.github.douglasjunior.ReactNativeEasyBluetooth.CoreConstants.EVENT_DATA_READ;
import static io.github.douglasjunior.ReactNativeEasyBluetooth.CoreConstants.EVENT_DEVICE_FOUND;
import static io.github.douglasjunior.ReactNativeEasyBluetooth.CoreConstants.EVENT_DEVICE_NAME;
import static io.github.douglasjunior.ReactNativeEasyBluetooth.CoreConstants.EVENT_STATUS_CHANGE;

public class CoreModule extends ReactContextBaseJavaModule implements BluetoothService.OnBluetoothEventCallback, BluetoothService.OnBluetoothScanCallback {

    private static final String TAG = "ReactNativeJS";

    private final BluetoothManager mBluetoothManager;
    private final BluetoothAdapter mBluetoothAdapter;
    private final Class<? extends BluetoothService> mBluetoothServiceClass;

    private BluetoothService mService;
    private BluetoothWriter mWriter;

    public CoreModule(ReactApplicationContext reactContext, Class<? extends BluetoothService> bluetoothServiceClass) {
        super(reactContext);
        this.mBluetoothServiceClass = bluetoothServiceClass;

        mBluetoothManager = (BluetoothManager) getReactApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        } else {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
    }

    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> constants = new HashMap<>();
        constants.put("EVENT_DATA_READ", EVENT_DATA_READ);
        constants.put("EVENT_DEVICE_FOUND", EVENT_DEVICE_FOUND);
        constants.put("EVENT_DEVICE_NAME", EVENT_DEVICE_NAME);
        constants.put("EVENT_STATUS_CHANGE", EVENT_STATUS_CHANGE);
        return constants;
    }

    @Override
    public String getName() {
        return "ReactNativeEasyBluetooth";
    }

    /* ====================================
                 REACT METHODS
    ==================================== */

    public void init(ReadableMap config, Promise promise) {
        Log.d(TAG, "config: " + config);
        if (!validateBluetoothAdapter(promise)) return;

        BluetoothConfiguration bluetoothConfig = new BluetoothConfiguration();
        bluetoothConfig.context = getReactApplicationContext();
        bluetoothConfig.bluetoothServiceClass = mBluetoothServiceClass;
        bluetoothConfig.deviceName = config.getString("deviceName");
        bluetoothConfig.characterDelimiter = config.getString("characterDelimiter").charAt(0);
        bluetoothConfig.bufferSize = config.getInt("bufferSize");
        if (config.hasKey("uuid"))
            bluetoothConfig.uuid = UUID.fromString(config.getString("uuid"));
        if (config.hasKey("uuidService"))
            bluetoothConfig.uuidService = UUID.fromString(config.getString("uuidService"));
        if (config.hasKey("uuidCharacteristic"))
            bluetoothConfig.uuidCharacteristic = UUID.fromString(config.getString("uuidCharacteristic"));
        if (config.hasKey("transport"))
            bluetoothConfig.transport = config.getInt("transport");
        bluetoothConfig.callListenersInMainThread = false;

        BluetoothService.init(bluetoothConfig);
        mService = BluetoothService.getDefaultInstance();
        mService.setOnScanCallback(this);
        mService.setOnEventCallback(this);

        mWriter = new BluetoothWriter(mService);

        WritableNativeMap returnConfig = new WritableNativeMap();
        returnConfig.merge(config);

        promise.resolve(returnConfig);
    }

    public void startScan(final Promise promise) {
        Log.d(TAG, "startScan: " + promise);
        if (!validateServiceConfig(promise)) return;

        mStartScanPromise = promise;
        mService.startScan();
    }

    public void stopScan(Promise promise) throws InterruptedException {
        Log.d(TAG, "stopScan: " + promise);
        if (!validateServiceConfig(promise)) return;

        mService.stopScan();

        Thread.sleep(1000);

        promise.resolve(null);
    }

    public void connect(ReadableMap device, final Promise promise) throws InterruptedException {
        Log.d(TAG, "connect: " + device);

        if (!validateServiceConfig(promise)) return;

        String address = device.getString("address");
        String name = device.getString("name");

        if (!mBluetoothAdapter.checkBluetoothAddress(address)) {
            promise.reject(new IllegalArgumentException("Invalid device address: " + address));
            return;
        }

        BluetoothDevice btDevice = mBluetoothAdapter.getRemoteDevice(address);

        mService.connect(btDevice);

        Thread.sleep(2000);
        while (mService.getStatus() == BluetoothStatus.CONNECTING) {
            Thread.yield();
        }

        if (mService.getStatus() == BluetoothStatus.CONNECTED) {
            promise.resolve(wrapDevice(btDevice, 0));
        } else {
            promise.reject(new IllegalStateException("Unable to connect to: " + name + " [" + address + "]"));
        }
    }

    public void getStatus(final Promise promise) {
        Log.d(TAG, "getStatus: " + promise);
        if (!validateServiceConfig(promise)) return;

        promise.resolve(mService.getStatus().name());
    }

    public void write(String data, Promise promise) {
        Log.d(TAG, "write: " + data);
        if (!validateServiceConfig(promise)) return;

        mWriter.write(data);
        promise.resolve(null);
    }

    public void writeln(String data, Promise promise) {
        Log.d(TAG, "writeln: " + data);
        if (!validateServiceConfig(promise)) return;

        mWriter.writeln(data);
        promise.resolve(null);
    }

    public void writeIntArray(ReadableArray data, Promise promise) {
        Log.d(TAG, "writeIntArray: " + data);
        if (!validateServiceConfig(promise)) return;

        byte[] bytes = new byte[data.size()];

        for (int i = 0; i < data.size(); i++) {
            bytes[i] = (byte) data.getInt(i);
        }

        mService.write(bytes);
        promise.resolve(null);
    }

    public void stopService(final Promise promise) {
        Log.d(TAG, "stopService: " + promise);
        if (!validateServiceConfig(promise)) return;

        mService.stopService();
        mService = null;
        mWriter = null;
        promise.resolve(null);
    }

    /* ====================================
            ADAPTER - REACT METHODS
     ==================================== */

    public void isAdapterEnable(final Promise promise) {
        Log.d(TAG, "isAdapterEnable: " + promise);
        if (!validateBluetoothAdapter(promise)) return;

        promise.resolve(mBluetoothAdapter.isEnabled());
    }

    public void enable(final Promise promise) {
        Log.d(TAG, "enable: " + promise);
        if (!validateBluetoothAdapter(promise)) return;

        if (mBluetoothAdapter.enable()) {
            promise.resolve(null);
        } else {
            promise.reject(new IllegalAccessException("Could not enable bluetooth adapter."));
        }
    }

    public void getBoundedDevices(final Promise promise) {
        Log.d(TAG, "getBoundedDevices: " + promise);
        if (!validateBluetoothAdapter(promise)) return;

        WritableNativeArray devices = new WritableNativeArray();

        for (BluetoothDevice btDevice : mBluetoothAdapter.getBondedDevices()) {
            WritableNativeMap device = wrapDevice(btDevice, 0);
            devices.pushMap(device);
        }

        promise.resolve(devices);
    }

    /* ====================================
                    EVENTS
     ==================================== */

    private void sendEvent(String eventName, Object params) {
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    private void sendEventDeviceFound(WritableNativeMap device) {
        sendEvent(EVENT_DEVICE_FOUND, device);
    }

    private void sendEventDataRead(String data) {
        sendEvent(EVENT_DATA_READ, data);
    }

    private void sendEventStatusChange(BluetoothStatus status) {
        sendEvent(EVENT_STATUS_CHANGE, status.name());
    }

    private void sendEventDeviceName(String name) {
        sendEvent(EVENT_DEVICE_NAME, name);
    }

    /* ====================================
                    OTHERS
     ==================================== */

    private boolean validateBluetoothAdapter(Promise promise) {
        if (mBluetoothAdapter == null) {
            promise.reject(new IllegalStateException("Bluetooth Adapter not found."));
            return false;
        }
        return true;
    }

    private boolean validateServiceConfig(Promise promise) {
        if (mService == null) {
            promise.reject(new IllegalStateException("BluetoothService has not been configured. " +
                    "Call ReactNativeEasyBluetooth.init(config)."));
            return false;
        }
        return true;
    }

    private WritableNativeMap wrapDevice(BluetoothDevice bluetoothDevice, int RSSI) {
        WritableNativeMap device = new WritableNativeMap();
        device.putString("address", bluetoothDevice.getAddress());
        device.putString("name", bluetoothDevice.getName());
        device.putInt("rssi", RSSI);
        WritableArray uuids = new WritableNativeArray();
        if (bluetoothDevice.getUuids() != null) {
            for (ParcelUuid uuid : bluetoothDevice.getUuids()) {
                uuids.pushString(uuid.toString());
            }
        }
        device.putArray("uuids", uuids);
        return device;
    }

    @Override
    public void onDataRead(byte[] bytes, int length) {
        sendEventDataRead(new String(bytes, 0, length));
    }

    @Override
    public void onStatusChange(BluetoothStatus bluetoothStatus) {
        sendEventStatusChange(bluetoothStatus);
    }

    @Override
    public void onDeviceName(String name) {
        sendEventDeviceName(name);
    }

    @Override
    public void onToast(String s) {

    }

    @Override
    public void onDataWrite(byte[] bytes) {

    }

    private WritableNativeArray mDevicesFound;
    private Promise mStartScanPromise;

    @Override
    public void onDeviceDiscovered(BluetoothDevice bluetoothDevice, int RSSI) {
        if (mDevicesFound != null) {
            mDevicesFound.pushMap(wrapDevice(bluetoothDevice, RSSI));
        }
        sendEventDeviceFound(wrapDevice(bluetoothDevice, RSSI));
    }

    @Override
    public void onStartScan() {
        mDevicesFound = new WritableNativeArray();
    }

    @Override
    public void onStopScan() {
        if (mStartScanPromise != null) {
            mStartScanPromise.resolve(mDevicesFound);
            mStartScanPromise = null;
        }
        mDevicesFound = null;
    }
}