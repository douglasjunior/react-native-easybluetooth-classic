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

const EasyBluetooth = NativeModules.EasyBluetooth;

EasyBluetooth.addOnDeviceFoundListener = function (eventCallback) {
    return DeviceEventEmitter.addListener(EasyBluetooth.EVENT_DEVICE_FOUND, eventCallback);
}

EasyBluetooth.addOnDataReadListener = function (eventCallback) {
    return DeviceEventEmitter.addListener(EasyBluetooth.EVENT_DATA_READ, eventCallback);
}

EasyBluetooth.addOnStatusChangeListener = function (eventCallback) {
    return DeviceEventEmitter.addListener(EasyBluetooth.EVENT_STATUS_CHANGE, eventCallback);
}

EasyBluetooth.addOnDeviceNameListener = function (eventCallback) {
    return DeviceEventEmitter.addListener(EasyBluetooth.EVENT_DEVICE_NAME, eventCallback);
}

module.exports = EasyBluetooth;