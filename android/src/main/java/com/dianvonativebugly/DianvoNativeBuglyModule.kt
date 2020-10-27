package com.dianvonativebugly

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.facebook.react.bridge.*
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException


/**
 * 使用了bugly的多进程策略初始化bugly
 * 避免了多进程的时候重复上报异常
 */
fun initBuglyWithStrategy(context: Context, appId: String, isDebug: Boolean) {
    // 获取当前包名
    val packageName = context.packageName
    // 获取当前进程名
    val processName: String? = getProcessName(Process.myPid())
    // 设置是否为上报进程
    val strategy = UserStrategy(context)
    strategy.isUploadProcess = processName == null || processName == packageName
    //upgrade 包中已经集成了crash report
    //不自动检查更新也不自动初始化，做成包提供给rn调用
    Beta.autoCheckUpgrade = false
    //不启用热更新
    Beta.canShowApkInfo = false
    Bugly.init(context, appId, isDebug)
}

/**
 * 获取进程号对应的进程名
 *
 * @param pid 进程号
 * @return 进程名
 */
fun getProcessName(pid: Int): String? {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
        var processName = reader.readLine()
        if (!TextUtils.isEmpty(processName)) {
            processName = processName.trim { it <= ' ' }
        }
        return processName
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    } finally {
        try {
            reader?.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }
    return null
}

class DianvoNativeBuglyModule(val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "DianvoNativeBugly"
    }


    @ReactMethod
    fun checkAppUpgrade(options: ReadableMap) {
        //用户手动点击检查，非用户点击操作请传false
        var isManual = true
        //是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
        var isSilence = false
        if (options.hasKey("isManual")) {
            isManual = options.getBoolean("isManual")
        }
        if (options.hasKey("isSilence")) {
            isSilence = options.getBoolean("isSilence")
        }
        Beta.checkAppUpgrade(isManual, isSilence)
    }


    @ReactMethod
    fun getUpgradeInfo(promise: Promise) {
        UiThreadUtil.runOnUiThread {
            val info = Beta.getUpgradeInfo()
            if (info != null) {
                val writableMap = Arguments.createMap()
                writableMap.putString("apkMd5", info.apkMd5)
                writableMap.putString("apkUrl", info.apkUrl)
                writableMap.putString("id", info.id)
                writableMap.putString("imageUrl", info.imageUrl)
                writableMap.putString("newFeature", info.newFeature)
                writableMap.putString("title", info.title)
                writableMap.putString("versionName", info.versionName)
                writableMap.putDouble("fileSize", info.fileSize.toDouble())
                //弹窗间隔（ms）
                writableMap.putDouble("popInterval", info.popInterval.toDouble())
                //弹窗次数:
                writableMap.putInt("popTimes", info.popTimes)
                //发布类型（0:测试 1:正式）:
                writableMap.putInt("publishType", info.publishType)
                writableMap.putInt("updateType", info.updateType)
                //弹窗类型（1:建议 2:强制 3:手工）:
                writableMap.putInt("upgradeType", info.upgradeType)
                writableMap.putInt("versionCode", info.versionCode)
                writableMap.putDouble("publishTime", info.publishTime.toDouble())
                promise.resolve(writableMap)
            }
            promise.resolve(null)
        }
    }




}
