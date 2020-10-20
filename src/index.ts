import { NativeModules } from 'react-native';

type DianvoNativeBuglyType = {
  initSDK(appKey: string): Function; //初始化SDK
};

const { DianvoNativeBugly } = NativeModules;

export default DianvoNativeBugly as DianvoNativeBuglyType;
