import * as React from 'react';
import { StyleSheet, View, Text, StatusBar } from 'react-native';
import DianvoNativeBugly from 'dianvo-native-bugly';

export default class App extends React.Component {
  componentDidMount() {
    DianvoNativeBugly.initSDK('bb11655438');
  }

  public render() {
    return (
      <View style={styles.container}>
        <StatusBar barStyle={'dark-content'}></StatusBar>
        <View style={styles.line}>
          <Text style={styles.auth}>Common</Text>
        </View>
        <View style={styles.buttonView}>
          {/* <TouchableOpacity
            onPress={() => this.initSDK()}
            style={styles.touchButton}
          >
            <Text style={styles.text}>init SDK</Text>
          </TouchableOpacity> */}
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingTop: 50,
    // alignItems:'center'
  },
  buttonView: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    paddingTop: 15,
  },
  touchButton: {
    backgroundColor: '#409eff',
    width: 110,
    height: 50,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 15,
    borderRadius: 10,
    marginLeft: 10,
    shadowRadius: 6.68,
    shadowColor: 'rgba(51, 94, 255, 1)',
    shadowOffset: {
      width: 130,
      height: 80,
    },
    elevation: 11,
  },
  text: {
    color: '#FFF',
    fontSize: 18,
    alignSelf: 'center',
  },
  line: {
    width: '94%',
    alignSelf: 'center',
    backgroundColor: '#67c23a',
    paddingTop: 10,
    paddingBottom: 10,
  },
  auth: {
    color: '#FFF',
    fontSize: 22,
    fontWeight: 'bold',
    alignSelf: 'center',
  },
});
