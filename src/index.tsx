import { NativeModules } from 'react-native';

type DianvoNativeBuglyType = {
  multiply(a: number, b: number): Promise<number>;
};

const { DianvoNativeBugly } = NativeModules;

export default DianvoNativeBugly as DianvoNativeBuglyType;
