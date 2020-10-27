# dianvo-native-bugly

基于Bugly封装的组件

## Installation

```sh
npm install dianvo-native-bugly
```

拉下依赖进行link之后,需要在原生下进行如下配置:

在应用主模块的build.gradle下(一般路径为app/build.gradle)增加如下代码
```gradle
apply plugin: 'bugly'
/**
 * 该字段自定义tecent bugly 的调试模式，并应用于整个应用，
 * 请在打release包的时候将此字段改为false
 */
def debugging = true
def buglyAppId = "4ecf369603"
def buglyAppKey = "e90839a4-8a6c-497d-ac19-577bf028cb7a"

bugly {
    appId = buglyAppId
    appKey = buglyAppKey
    debug = debugging
}

/**
 * for the fucking gradle String type of buildConfigField
 */
def CONFIG = { k -> "\"${k}\"" }
```

然后在应用Application入口处调用封装好的初始化函数
```java
import static com.dianvonativebugly.DianvoNativeBuglyModuleKt.initBuglyWithStrategy;
initBuglyWithStrategy(getApplicationContext(),appId,debugMode)
```
note: appId是在bugly控制台申请好的appId,debug模式是指定了bugly是否处于调试模式
，这两个值的传递可以通过两种方式进行

Method 1:
在build.gradle下使用已经声明过的变量构建编译常量，之后采用BuildConfig类传递
```gradle
    buildTypes {
        debug {
            buildConfigField "boolean", "DEBUGING", String.valueOf(debugging)
            buildConfigField "String", "BUGLY_APP_ID", CONFIG(String.valueOf(buglyAppId))
            buildConfigField "String", "BUGLY_APP_KEY", CONFIG(String.valueOf(buglyAppKey))
        }
        release {
            buildConfigField "boolean", "DEBUGING", String.valueOf(debugging)
            buildConfigField "String", "BUGLY_APP_ID", CONFIG(String.valueOf(buglyAppId))
            buildConfigField "String", "BUGLY_APP_KEY", CONFIG(String.valueOf(buglyAppKey))
          }
    }
    //在初始化的时候可以用生成的构建常量
    initBuglyWithStrategy(getApplicationContext(),BuildConfig.BUGLY_APP_ID,BuildConfig.DEBUGING)
```
Method 2:
直接在初始化的时候采用硬编码的形式
```java
    initBuglyWithStrategy(getApplicationContext(),"addhuashduyasvdfa",true)
```
## Usage

```
import {DianvoNativeBuglyType} from "dianvo-native-bugly";
 DianvoNativeBuglyType.getUpgradeInfo()
 .then((upgradeInfo) => {
     console.log(JSON.stringify(upgradeInfo));
     navigation.push('UpgradePage', {
         newFeature:upgradeInfo.newFeature,
         versionName:upgradeInfo.versionName,
         apkUrl:upgradeInfo.apkUrl,
         title:upgradeInfo.title,
         fileSize:upgradeInfo.fileSize
     } as UpgradeInfo);
 });
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
