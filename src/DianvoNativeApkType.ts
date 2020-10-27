import { NativeModules } from 'react-native';

type DianvoNativeApkType = {
  /**
   * 传入下载apk的路径，安装apk (android only)
   * @param apkDownloadedPath 需要安装的apk的路径
   * @param noPermissionTitle 没有权限提示的标题
   * @param noPermissionMessage 没有权限提示的信息
   * @param noPermissionCancel 没有权限取消文本提示的文字
   * @param noPermissionEnsure  没有权限确认文本提示的文字
   */
  installApplication(
    apkDownloadedPath: string,
    noPermissionTitle: string,
    noPermissionMessage: string,
    noPermissionCancel: string,
    noPermissionEnsure: string
  ): Promise<any>;
};
const { DianvoNativeApk } = NativeModules;
export default DianvoNativeApk as DianvoNativeApkType;
