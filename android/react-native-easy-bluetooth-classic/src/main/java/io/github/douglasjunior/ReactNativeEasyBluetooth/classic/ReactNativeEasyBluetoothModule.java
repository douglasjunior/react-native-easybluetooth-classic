package io.github.douglasjunior.ReactNativeEasyBluetooth.classic;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothClassicService;

import io.github.douglasjunior.ReactNativeEasyBluetooth.CoreModule;


public class ReactNativeEasyBluetoothModule extends CoreModule {

    public ReactNativeEasyBluetoothModule(ReactApplicationContext reactContext) {
        super(reactContext, BluetoothClassicService.class);
    }

    @ReactMethod
    @Override
    public void init(ReadableMap config, Promise promise) {
        super.init(config, promise);
    }

    @ReactMethod
    @Override
    public void startScan(Promise promise) {
        super.startScan(promise);
    }

    @ReactMethod
    @Override
    public void stopScan(Promise promise) throws InterruptedException {
        super.stopScan(promise);
    }

    @ReactMethod
    @Override
    public void getStatus(Promise promise) {
        super.getStatus(promise);
    }

    @ReactMethod
    @Override
    public void connect(ReadableMap device, Promise promise) throws InterruptedException {
        super.connect(device, promise);
    }

    @ReactMethod
    @Override
    public void write(String data, Promise promise) {
        super.write(data, promise);
    }

    @ReactMethod
    @Override
    public void writeln(String data, Promise promise) {
        super.writeln(data, promise);
    }

    @ReactMethod
    @Override
    public void writeIntArray(ReadableArray data, Promise promise) {
        super.writeIntArray(data, promise);
    }

    @ReactMethod
    @Override
    public void stopService(Promise promise) {
        super.stopService(promise);
    }

    @ReactMethod
    @Override
    public void isAdapterEnable(Promise promise) {
        super.isAdapterEnable(promise);
    }

    @ReactMethod
    @Override
    public void enable(Promise promise) {
        super.enable(promise);
    }

    @ReactMethod
    @Override
    public void getBoundedDevices(Promise promise) {
        super.getBoundedDevices(promise);
    }
}