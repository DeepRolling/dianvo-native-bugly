package com.dianvonativebugly

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.facebook.react.bridge.*
import java.io.File
import java.net.URI


class DianvoNativeApkModule (val reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext),ActivityEventListener  {

  override fun getName(): String {
    return "DianvoNativeApk"
  }

  init {
    reactContext.addActivityEventListener(this);
  }


  val INSTALL_PERMISSION_REQUEST_CODE = 2

  val INSTALL_ERROR_NOT_IN_ACTIVITY = "1"
  val INSTALL_NEED_PERMISSION_CANCEL = "2"
  val INSTALL_NEED_PERMISSION_NOT_OPEN = "3"
  val INSTALL_FILE_NOT_EXIST = "4"

  lateinit var currentApkPath:String
  lateinit var currentPromise:Promise


  /**
   * 传入下载apk的路径，安装apk (android only)
   * @param apkDownloadedPath 需要安装的apk的路径
   * @param noPermissionTitle 没有权限提示的标题
   * @param noPermissionMessage 没有权限提示的信息
   * @param noPermissionCancel 没有权限取消文本提示的文字
   * @param noPermissionEnsure  没有权限确认文本提示的文字
   */
  @ReactMethod
  fun installApplication(apkDownloadedPath: String,
                         noPermissionTitle: String,
                         noPermissionMessage: String,
                         noPermissionCancel: String,
                         noPermissionEnsure: String,
                         promise: Promise) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      //先获取是否有安装未知来源应用的权限
      val haveInstallPermission = reactContext.packageManager.canRequestPackageInstalls()
      if (!haveInstallPermission) { //没有权限
        val activity = reactContext.currentActivity
        if (activity == null) {
          promise.reject(INSTALL_ERROR_NOT_IN_ACTIVITY, "not in a activity")
        } else {
          AlertDialog.Builder(activity)
            .setTitle(noPermissionTitle)
            .setMessage(noPermissionMessage)
            .setNegativeButton(noPermissionCancel) { dialog, _ ->
              dialog.cancel()
              promise.reject(INSTALL_NEED_PERMISSION_CANCEL,
                "user cancel when need install permissions")
            }
            .setPositiveButton(noPermissionEnsure) { dialog, _ ->
              dialog.cancel()
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //after jump to setting page , save current promise and params
                currentApkPath = apkDownloadedPath
                currentPromise = promise
                //jump to install permission setting
                val packageURI: Uri = Uri.parse("package:" + activity.getPackageName())
                //注意这个是8.0新API
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
                activity.startActivityForResult(intent, INSTALL_PERMISSION_REQUEST_CODE)
              }
            }.show()
            .setCanceledOnTouchOutside(false)
        }
      } else {
        installApkWhenHasPermission(apkDownloadedPath, promise)
      }
    } else {
      installApkWhenHasPermission(apkDownloadedPath, promise)
    }
  }


  /**
   * 在有权限的时候安装apk
   * @param apkDownloadedPath 需要安装的apk的路径
   */
  private fun installApkWhenHasPermission(apkDownloadedPath: String,
                                          promise: Promise) {
    val file = File(URI(apkDownloadedPath))

    if (file.exists()) {
      val intent = Intent(Intent.ACTION_VIEW)
      // 由于没有在Activity环境下启动Activity,设置下面的标签
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
        // 参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致, 参数3  共享的文件
        val apkUri: Uri = FileProvider.getUriForFile(
          reactContext.applicationContext,
          reactContext.applicationInfo.packageName +".fileprovider",
          file)
        // 添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
      } else {
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
      }

      reactContext.startActivity(intent)
      promise.resolve("call install successful")
    } else {
      promise.reject(INSTALL_FILE_NOT_EXIST, "install apk not exist")
    }
  }

  override fun onNewIntent(intent: Intent?) {
  }


  /**
   * 在activity返回的时候监听，继续安装
   */
  override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == INSTALL_PERMISSION_REQUEST_CODE) {
      if (resultCode == Activity.RESULT_OK) {
        installApkWhenHasPermission(currentApkPath, currentPromise)
      }else{
        currentPromise.reject(INSTALL_NEED_PERMISSION_NOT_OPEN,
          "user not open permission when setting page")
      }
    }

  }


}
