import { NativeModules } from 'react-native';

/**
 * 发布类型
 */
enum publishType {
  //测试
  test = 0,
  //正式
  production = 1
}

/**
 * 升级策略
 */
enum upgradeType {
  //建议
  suggest = 1,
  //强制
  mandotary = 2,
  //手工
  manual = 3
}


type DianvoNativeBuglyType = {
  initBuglyWithStrategy(appId: string, debug: boolean): Function; //初始化SDK

  /**
   * android only
   * 获取本地已有升级策略（非实时，可用于界面红点展示）
   */
  getUpgradeInfo(): Promise<{
    //包md5值
    apkMd5: string,
    //APK的CDN外网下载地址
    apkUrl: string,
    //唯一标识
    id: string,
    //图片url
    imageUrl: string,
    //升级特性描述
    newFeature: string,
    //升级提示标题
    title: string,
    versionName: string,
    //APK文件的大小
    fileSize: number,
    //提醒间隔
    popInterval: number,
    //提醒次数
    popTimes: number,
    //升级类型 0测试 1正式
    publishType: publishType,
    updateType: number,
    //升级策略 1建议 2强制 3手工
    upgradeType: upgradeType,
    versionCode: number,
    //升级发布时间,ms
    publishTime: number,
  }>;


  /**
   * @param isManual  用户手动点击检查，非用户点击操作请传false
   * @param isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
   */
  checkAppUpgrade(params: { isManual?: boolean, isSilence?: boolean }): void;

};

const { DianvoNativeBugly } = NativeModules;

export default DianvoNativeBugly as DianvoNativeBuglyType;
