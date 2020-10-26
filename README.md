# dianvo-native-bugly

基于Bugly封装的组件

## Installation

```sh
npm install dianvo-native-bugly
```

 implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
  //    implementation 'com.tencent.bugly:crashreport:latest.release'
  //其中latest.release指代最新Bugly SDK版本号，也可以指定明确的版本号，例如2.1.9
  api 'com.tencent.bugly:crashreport_upgrade:latest.release'
//其中latest.release指代最新版本号，也可以指定明确的版本号，例如1.2.0
  api 'com.tencent.bugly:nativecrashreport:latest.release'

## Usage

```js
import DianvoNativeBugly from "dianvo-native-bugly";

// ...

const result = await DianvoNativeBugly.multiply(3, 7);
```
```
import SimpleRnBugly from './simple-rn-bugly/src/index'
  <Button title={"Get Update information"} onPress={async () => {
            let updateInfo = await SimpleRnBugly.getUpgradeInfo();
            console.log("update info : "+updateInfo.versionCode);
  }}/>
  <Button title={"app Update"} onPress={async () => {
            SimpleRnBugly.checkAppUpgrade({isManual:true,
                isSilence:false});
  }}/>
```

## Note
if you use android 9.0 and above ,please set following statement in your < application > tag
```css
android:usesCleartextTraffic="true"
```
or use custom network security config


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
