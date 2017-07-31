# Easy Bluetooth Classic

[![Licence MIT](https://img.shields.io/badge/licence-MIT-blue.svg)](https://github.com/douglasjunior/react-native-easybluetooth-classic/blob/master/LICENSE)
[![npm version](https://img.shields.io/npm/v/easy-bluetooth-classic.svg)](https://www.npmjs.com/package/easy-bluetooth-classic)
[![npm downloads](https://img.shields.io/npm/dt/easy-bluetooth-classic.svg)](#install)

A Library for easy implementation of Serial Bluetooth Classic on React Native. 💙

_Looking for **Bluetooth Low Energy**? [Look here](https://github.com/douglasjunior/react-native-easybluetooth-le)._

## Requirements

- Android 4.1 (API 16)
- React Native >= 0.40.0

## Use

### Configuration

```javascript
import EasyBluetooth from 'easy-bluetooth-classic';

...

    var config = {
      "uuid": "00001101-0000-1000-8000-00805f9b34fb",
      "deviceName": "Bluetooth Example Project",
      "bufferSize": 1024,
      "characterDelimiter": "\n"
    }

    EasyBluetooth.init(config)
      .then(function (config) {
        console.log("config done!");
      })
      .catch(function (ex) {
        console.warn(ex);
      });
```

### Scanning

```javascript
    EasyBluetooth.startScan()
      .then(function (devices) {
        console.log("all devices found:");
        console.log(devices);
      })
      .catch(function (ex) {
        console.warn(ex);
      });
```

### Connecting

```javascript
    EasyBluetooth.connect(device)
      .then(() => {
        console.log("Connected!");
      })
      .catch((ex) => {
        console.warn(ex);
      })
```

### Writing

```javascript
    EasyBluetooth.writeln("Works in React Native!")
      .then(() => {
        console.log("Writing...")
      })
      .catch((ex) => {
        console.warn(ex);
      })
```

### Events

```javascript
componentWillMount() {
    this.onDeviceFoundEvent = EasyBluetooth.addOnDeviceFoundListener(this.onDeviceFound.bind(this));
    this.onStatusChangeEvent = EasyBluetooth.addOnStatusChangeListener(this.onStatusChange.bind(this));
    this.onDataReadEvent = EasyBluetooth.addOnDataReadListener(this.onDataRead.bind(this));
    this.onDeviceNameEvent = EasyBluetooth.addOnDeviceNameListener(this.onDeviceName.bind(this));
}
...
  onDeviceFound(device) {
    console.log("onDeviceFound");
    console.log(device);
  }

  onStatusChange(status) {
    console.log("onStatusChange");
    console.log(status);
  }

  onDataRead(data) {
    console.log("onDataRead");
    console.log(data);
  }
  
  onDeviceName(name) {
    console.log("onDeviceName");
    console.log(name);
  }
...
componentWillUnmount() {
    this.onDeviceFoundEvent.remove();
    this.onStatusChangeEvent.remove();
    this.onDataReadEvent.remove();
    this.onDeviceNameEvent.remove();
}
```

## Install 

1. Run in console:
   ```bash
   npm i -S easy-bluetooth-classic
   ```

2. Link:
   ```bash
   react-native link easy-bluetooth-classic
   ```
    
2. Add jitpack repository in `android/build.gradle`:
   ```gradle
   allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
   }

   ```
 
## Known issues

- Location needs to be enabled for Bluetooth Low Energy Scanning on Android 6.0 http://stackoverflow.com/a/33045489/2826279
 
## Contribute

New features, bug fixes and improvements are welcome! For questions and suggestions use the [issues](https://github.com/douglasjunior/react-native-easybluetooth-le/issues).

<a href="https://www.patreon.com/douglasjunior"><img src="http://i.imgur.com/xEO164Z.png" alt="Become a Patron!" width="200" /></a>
[![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=E32BUP77SVBA2)

## Licence

```
The MIT License (MIT)

Copyright (c) 2017 Douglas Nassif Roma Junior
```

See the full [licence file](https://github.com/douglasjunior/react-native-easybluetooth-classic/blob/master/LICENSE).
