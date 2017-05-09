/**
 * Sample Bluetooth Example Project
 * https://github.com/douglasjunior/react-native-easy-bluetooth
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  ListView,
  TouchableHighlight
} from 'react-native';

import _ from 'lodash'

import ReactNativeEasyBluetooth from 'react-native-easy-bluetooth';

export default class BluetoothExampleProject extends Component {

  constructor(props) {
    super(props);

    console.log(ReactNativeEasyBluetooth);

    let ds = new ListView.DataSource({ rowHasChanged: (r1, r2) => r1 !== r2 });

    this.state = {
      devices: [],
      dataSource: ds
    }

    ReactNativeEasyBluetooth.addOnDeviceFoundListener(this.onDeviceFound.bind(this));
    ReactNativeEasyBluetooth.addOnStatusChangeListener(this.onStatusChange.bind(this));
    ReactNativeEasyBluetooth.addOnDataReadListener(this.onDataRead.bind(this));

    var config = {
      "uuid": "35111C00001101-0000-1000-8000-00805F9B34FB",
      "deviceName": "Bluetooth Example Project",
      "bufferSize": 1024,
      "characterDelimiter": "\n"
    }

    ReactNativeEasyBluetooth.config(config)
      .then(function (config) {
        console.log("config:");
        console.log(config);
        return ReactNativeEasyBluetooth.startScan();
      })
      .then(function (devices) {
        console.log("all devices found:");
        console.log(devices);
      })
      .catch(function (ex) {
        console.warn(ex);
      });

  }

  onDeviceFound(device) {
    console.log("onDeviceFound");
    console.log(device);
    let devices = this.state.devices;

    if (_.findIndex(devices, ['address', device.address]) < 0) {

      devices.push(device);
      let dataSource = this.state.dataSource.cloneWithRows(devices);

      console.log("dataSource: ");
      console.log(dataSource);

      this.setState({
        devices: devices,
        dataSource: dataSource
      })
    }
  }

  onStatusChange(status) {
    console.log("onStatusChange");
    console.log(status);
  }

  onDataRead(data) {
    console.log("onDataRead");
    console.log(data);
  }

  onDeviceClick(device, index) {
    ReactNativeEasyBluetooth.stopScan()
      .then(() => {
        return ReactNativeEasyBluetooth.connect(device);
      })
      .then(() => {
        console.log("Connecting to " + device);
        return ReactNativeEasyBluetooth.writeIntArray([0x1b, 0x61, 0x1]);
      })
      .then(() => {
        console.log("Escreveu os bytes");
        return ReactNativeEasyBluetooth.writeln("Foi!\n\n\n");
      })
      .then(() => {
        console.log("Escreveu os dados");
      })
      .catch((ex) => {
        console.warn(ex);
      })
  }

  onWriteClick() {
    ReactNativeEasyBluetooth.writeln("Works in React Native!\n\n")
    .then(() => {
      console.log("Writing...")
    })
    .catch((ex) => {
      console.warn(ex);
    })
  }

  renderListView() {
    return (
      <ListView
        dataSource={this.state.dataSource}
        renderRow={this.renderRow.bind(this)}
      />
    )
  }

  renderRow(device, sectionID, rowID, highlightRow) {
    return (
      <TouchableHighlight onPress={() => {
        this.onDeviceClick(device, rowID)
        highlightRow(sectionID, rowID);
      }}>
        <View>
          <Text>
            {device.name} - {device.address}
          </Text>
        </View>
      </TouchableHighlight>
    )
  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>React Native Easy Bluetooth</Text>

        <Text>Devices found:</Text>

        {this.renderListView()}
        <TouchableHighlight onPress={() => {
          this.onWriteClick()
        }}>
          <Text>Escrever</Text>
        </TouchableHighlight>
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
