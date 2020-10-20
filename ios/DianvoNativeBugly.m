/**
 * Copyright (c) Dianvo, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 * 基于腾讯Bugly封装
 */

#import "DianvoNativeBugly.h"
#import <Bugly/Bugly.h>

@implementation DianvoNativeBugly

//调用多线程
- (dispatch_queue_t)methodQueue{
    return  dispatch_get_main_queue();
}

RCT_EXPORT_MODULE()

//Bugly
- (void)configureBugly:(NSString *) appKey {
    BuglyConfig *config = [[BuglyConfig alloc] init];
    
    config.unexpectedTerminatingDetectionEnable = YES; //非正常退出事件记录开关，默认关闭
    config.reportLogLevel = BuglyLogLevelWarn; //报告级别
    config.deviceIdentifier = [UIDevice currentDevice].identifierForVendor.UUIDString; //设备标识
    config.blockMonitorEnable = YES; //开启卡顿监控
    config.blockMonitorTimeout = 5; //卡顿监控判断间隔，单位为秒
    config.applicationGroupIdentifier = @"";
//    config.delegate = self;
    
#if DEBUG
    config.debugMode = YES; //SDK Debug信息开关, 默认关闭
    config.channel = @"debug";
#else
    config.channel = @"release";
#endif
    
    [Bugly startWithAppId:appKey
#if DEBUG
    developmentDevice:YES
#endif
   config:config];
}

/**
 * 初始化SDK
*/
RCT_EXPORT_METHOD(initSDK:(NSString *)appKey)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        [self configureBugly:appKey];
    });
}

@end
