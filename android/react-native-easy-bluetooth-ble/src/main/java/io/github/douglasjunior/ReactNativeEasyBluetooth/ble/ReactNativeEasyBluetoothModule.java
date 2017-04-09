package io.github.douglasjunior.ReactNativeEasyBluetooth.ble;

import com.facebook.react.bridge.ReactApplicationContext;
import com.github.douglasjunior.bluetoothlowenergylibrary.BluetoothLeService;

import io.github.douglasjunior.ReactNativeEasyBluetooth.CoreModule;

public class ReactNativeEasyBluetoothModule extends CoreModule {

    public ReactNativeEasyBluetoothModule(ReactApplicationContext reactContext) {
        super(reactContext, BluetoothLeService.class);
    }
}