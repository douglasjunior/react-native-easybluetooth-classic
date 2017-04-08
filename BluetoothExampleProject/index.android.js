/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';

import ReactNativeEasyBluetooth from 'react-native-easy-bluetooth';

export default class BluetoothExampleProject extends Component {

  constructor(props) {
    super(props);
    console.log("aqui " + new Date());
    console.log(ReactNativeEasyBluetooth);

    var config = {
      "uuid" : "35111C00001101-0000-1000-8000-00805F9B34FB",
      "deviceName": "Teste React Native",
      "bufferSize": 1024,
      "characterDelimiter": "\n"
    }

    ReactNativeEasyBluetooth.config(config);

    ReactNativeEasyBluetooth.startScan(this.onScanCallback);
  }

  onScanCallback(action, address, name, index) {
    console.log("onScanCallback " + action + ": " + address + " - " + name);
  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          To get started, edit index.android.js
        </Text>
        <Text style={styles.instructions}>
          Double tap R on your keyboard to reload,{'\n'}
          Shake or press menu button for dev menu
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('BluetoothExampleProject', () => BluetoothExampleProject);
