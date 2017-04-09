/**
 * An ReactNative wrapper for AndroidBluetoothLibrary <https://github.com/douglasjunior/AndroidBluetoothLibrary>
 * 
 * =======================
 *       FUNCTIONS
 * =======================
 * 
 * config(objectConfig) -> Promise
 * startScan() -> Promise<[objectDevice]>
 * connect(objectDevice) -> Promise
 * getStatus() -> Promise<String>
 * write(string) -> Promise
 * writeln(string) -> Promise
 * stopService() -> Promise
 * 
 * =======================
 *       EVENTS
 * =======================
 * 
 * EVENT_DEVICE_FOUND
 * EVENT_DATA_READ
 * EVENT_STATUS_CHANGE
 * EVENT_DEVICE_NAME
 * 
 */
import { NativeModules, DeviceEventEmitter } from 'react-native';

const ReactNativeEasyBluetooth = NativeModules.ReactNativeEasyBluetooth;

ReactNativeEasyBluetooth.addOnDeviceFoundListener = function(eventCallback) {
    DeviceEventEmitter.addListener(ReactNativeEasyBluetooth.EVENT_DEVICE_FOUND, eventCallback);
}

ReactNativeEasyBluetooth.addOnDataReadListener = function(eventCallback) {
    DeviceEventEmitter.addListener(ReactNativeEasyBluetooth.EVENT_DATA_READ, eventCallback);
}

ReactNativeEasyBluetooth.addOnStatusChangeListener = function(eventCallback) {
    DeviceEventEmitter.addListener(ReactNativeEasyBluetooth.EVENT_STATUS_CHANGE, eventCallback);
}

ReactNativeEasyBluetooth.addOnDeviceNameListener = function(eventCallback) {
    DeviceEventEmitter.addListener(ReactNativeEasyBluetooth.EVENT_DEVICE_NAME, eventCallback);
}

module.exports = ReactNativeEasyBluetooth;