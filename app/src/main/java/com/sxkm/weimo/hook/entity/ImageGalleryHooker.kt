package com.sxkm.weimo.hook.entity

import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.YLog
import com.sxkm.weimo.data.FeatureManager
import java.util.LinkedList

/**
 * 图片查看器Hook类 - 动态类版本
 */
object ImageGalleryHookerDynamic : YukiBaseHooker() {

    override fun onHook() {
        loadApp(name = "com.tencent.mm") {
            // 使用Kavaref动态查找和Hook类
            // 查找所有包含"ImageGallery"的Activity
            findAndHookImageGallery()
        }
    }

    /**
     * 动态查找图片查看Activity并Hook
     */
    private fun findAndHookImageGallery() {
        try {
            // 获取所有已加载的类
            val classLoader = appClassLoader

            // 尝试查找已知的图片查看类
            val targetClasses = listOf(
                "com.tencent.mm.ui.chatting.gallery.ImageGalleryUI",
            )

            for (className in targetClasses) {
                try {
                    val targetClass = className.toClass(classLoader)
                    YLog.info("找到目标类: $className")

                    // Hook onCreate方法
                    targetClass.resolve().firstMethod {
                        name = "onCreate"
                    }.hook {
                        after {
                            val activity = instance as Activity
                            YLog.info("$className 被创建")
                            // 延迟执行
                            activity.window?.decorView?.postDelayed({
                                processImageActivity(activity)
                            }, 1500)
                        }
                    }

                    // 也Hook onResume
                    targetClass.resolve().firstMethod {
                        name = "onResume"
                    }.hook {
                        after {
                            val activity = instance as Activity
                            YLog.info("$className 恢复显示")
                            activity.window?.decorView?.postDelayed({
                                processImageActivity(activity)
                            }, 1000)
                        }
                    }

                } catch (e: ClassNotFoundException) {
                    YLog.debug("类不存在: $className")
                } catch (e: Exception) {
                    YLog.error("Hook $className 失败: ${e.message}")
                }
            }

            // 通用Hook：监听所有Activity
            hookAllActivities()

        } catch (e: Exception) {
            YLog.error("动态查找失败: ${e.message}")
        }
    }

    /**
     * 通用Hook：监听所有Activity
     */
    private fun hookAllActivities() {
        try {
            "android.app.Activity".toClass().resolve().firstMethod {
                name = "onCreate"
            }.hook {
                after {
                    val activity = instance as Activity
                    val className = activity.javaClass.name

                    // 检查是否是图片相关Activity
                    if (isImageGalleryActivity(className)) {
                        YLog.info("检测到可能的图片Activity: $className")

                        // 稍后处理
                        activity.window?.decorView?.postDelayed({
                            processImageActivity(activity)
                        }, 2000)
                    }
                }
            }
        } catch (e: Exception) {
            YLog.error("Hook所有Activity失败: ${e.message}")
        }
    }

    /**
     * 判断是否是图片查看Activity
     */
    private fun isImageGalleryActivity(className: String): Boolean {
        val galleryKeywords = listOf(
            "gallery", "Gallery", "preview", "Preview",
            "image", "Image", "photo", "Photo", "browse", "Browse"
        )

        return galleryKeywords.any { keyword ->
            className.contains(keyword, ignoreCase = true)
        }
    }

    /**
     * 处理图片Activity
     */
    private fun processImageActivity(activity: Activity) {
        try {
            // 检查功能是否开启
            val featureManager = FeatureManager.getInstance(activity)
            if (!featureManager.isViewOrgImgEnabled()) {
                return
            }

            YLog.info("开始处理图片Activity: ${activity.javaClass.simpleName}")

            // 查找查看原图按钮
            val found = findViewOriginalButton(activity)

            if (found) {
                YLog.info("成功点击查看原图按钮")
            } else {
                YLog.debug("未找到查看原图按钮，将重试")

                // 延迟重试
                activity.window?.decorView?.postDelayed({
                    YLog.info("重试查找查看原图按钮")
                    findViewOriginalButton(activity)
                }, 3000)
            }

        } catch (e: Exception) {
            YLog.error("处理图片Activity失败: ${e.message}")
        }
    }

    /**
     * 查找查看原图按钮
     */
    private fun findViewOriginalButton(activity: Activity): Boolean {
        return try {
            val rootView = activity.window?.decorView
            if (rootView == null) {
                YLog.error("根视图为空")
                return false
            }

            // 使用BFS搜索
            val queue = LinkedList<View>()
            queue.add(rootView)

            var found = false

            while (queue.isNotEmpty()) {
                val view = queue.poll()

                // 检查当前视图
                if (view is Button || view is TextView) {
                    val text = view.text?.toString()?.trim() ?: ""
                    if (text == "查看原图") {
                        view.visibility = View.GONE;
                        YLog.info("找到查看原图按钮: $text")
                        YLog.info("Visibility: ${when (view.visibility) {
                            View.VISIBLE -> "VISIBLE"
                            View.INVISIBLE -> "INVISIBLE"
                            View.GONE -> "GONE"
                            else -> "UNKNOWN(${view.visibility})"
                        }}")
                        YLog.info("=======================")
                        view.performClick();
                        found = true
                        view.visibility = View.GONE;
                        break;
                    }
                }

                // 如果是ViewGroup，添加子视图
                if (view is android.view.ViewGroup) {
                    for (i in 0 until view.childCount) {
                        queue.add(view.getChildAt(i))
                    }
                }
            }

            found
        } catch (e: Exception) {
            YLog.error("查找按钮失败: ${e.message}")
            false
        }
    }
}