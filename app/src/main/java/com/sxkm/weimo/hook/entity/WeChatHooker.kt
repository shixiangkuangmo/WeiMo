@file:Suppress("ConstPropertyName", "unused")

package com.sxkm.weimo.hook.entity

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.sxkm.weimo.ui.view.FeatureMenuView
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.type.android.BundleClass

object WeChatHooker : YukiBaseHooker() {

    override fun onHook() {
        loadApp(name = "com.tencent.mm") {
            // Hook所有Activity的onCreate方法
            "android.app.Activity".toClass().resolve().firstMethod {
                name = "onCreate"
                parameters(BundleClass)
            }.hook {
                after {
                    val activity = instance as Activity
                    val className = activity::class.java.name

                    // 只处理目标Activity
                    if (className == "com.tencent.mm.plugin.setting.ui.setting_new.MainSettingsUI") {
                        activity.runOnUiThread {
                            activity.window?.decorView?.postDelayed({
                                handleMainSettingsUI(activity)
                            }, 200)
                        }
                    }
                }
            }
        }
    }

    private fun handleMainSettingsUI(activity: Activity) {
        try {
            Toast.makeText(
                activity,
                "WeiMo加载成功",
                Toast.LENGTH_SHORT
            ).show();
            addWeiMoEntry(activity)

        } catch (e: Exception) {
            YLog.error("处理设置页面时出错: ${e.message}")
        }
    }

    private fun addWeiMoEntry(activity: Activity) {
        try {
            val rootView = activity.window?.decorView as? FrameLayout ?: return
            if (rootView.findViewWithTag<View>("weimo_entry") != null) {
                return
            }

            TextView(activity).apply {
                tag = "weimo_entry"
                text = "微魔"
                setTextColor(Color.BLACK)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                setTypeface(typeface, Typeface.BOLD)
                val drawable = android.graphics.drawable.GradientDrawable().apply {
                    setColor(Color.TRANSPARENT)
                    setStroke(2.dpToPx(activity), Color.BLACK)
                }
                background = drawable

                // 内边距
                setPadding(
                    15.dpToPx(activity), 8.dpToPx(activity),
                    15.dpToPx(activity), 8.dpToPx(activity)
                )

                // 布局参数：向上移动（减小 topMargin）
                val params = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.TOP or Gravity.END
                    topMargin = 50.dpToPx(activity)
                    marginEnd = 15.dpToPx(activity)
                }
                layoutParams = params

                // 点击效果
                setOnClickListener {
                    FeatureMenuView(activity).show()
                }

                // 添加到界面
                rootView.addView(this)
            }

        } catch (e: Exception) {
            YLog.error("添加微魔入口失败: ${e.message}")
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}