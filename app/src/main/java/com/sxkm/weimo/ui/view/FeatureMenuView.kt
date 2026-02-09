package com.sxkm.weimo.ui.view

import android.app.Activity
import android.app.AlertDialog
import android.widget.Toast
import android.widget.LinearLayout
import android.widget.Switch
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sxkm.weimo.data.FeatureManager

/**
 * 功能菜单视图类
 * 负责显示和管理微魔模块的功能菜单
 */
class FeatureMenuView(private val activity: Activity) {

    /**
     * 显示功能菜单对话框
     */
    fun show() {
        val featureManager = FeatureManager.getInstance(activity)
        
        // 创建包含开关的布局
        val layout = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        // 自动查看原图功能
        val viewOrgImg = Switch(activity).apply {
            text = "自动查看原图"
            isChecked = featureManager.isViewOrgImgEnabled()
            textSize = 18f
            setPadding(16, 16, 16, 16)
            setOnCheckedChangeListener { _, isChecked ->
                featureManager.setViewOrgImgEnabled(isChecked)
                showToast(if (isChecked) "自动查看原图已开启" else "自动查看原图已关闭")
            }
        }
        layout.addView(viewOrgImg)

        // 防撤回功能开关
        val antiRecallSwitch = Switch(activity).apply {
            text = "防撤回功能"
            isChecked = featureManager.isAntiRecallEnabled()
            textSize = 18f
            setPadding(16, 16, 16, 16)
            setOnCheckedChangeListener { _, isChecked ->
                featureManager.setAntiRecallEnabled(isChecked)
                showToast(if (isChecked) "防撤回功能已开启" else "防撤回功能已关闭")
            }
        }
        layout.addView(antiRecallSwitch)
        
        // 消息转发增强开关
        val forwardEnhanceSwitch = Switch(activity).apply {
            text = "消息转发增强"
            isChecked = featureManager.isForwardEnhanceEnabled()
            textSize = 18f
            setPadding(16, 16, 16, 16)
            setOnCheckedChangeListener { _, isChecked ->
                featureManager.setForwardEnhanceEnabled(isChecked)
                showToast(if (isChecked) "消息转发增强已开启" else "消息转发增强已关闭")
            }
        }
        layout.addView(forwardEnhanceSwitch)
        
        // 聊天记录导出开关
        val chatExportSwitch = Switch(activity).apply {
            text = "聊天记录导出"
            isChecked = featureManager.isChatExportEnabled()
            textSize = 18f
            setPadding(16, 16, 16, 16)
            setOnCheckedChangeListener { _, isChecked ->
                featureManager.setChatExportEnabled(isChecked)
                showToast(if (isChecked) "聊天记录导出已开启" else "聊天记录导出已关闭")
            }
        }
        layout.addView(chatExportSwitch)
        
        // 自动回复设置开关
        val autoReplySwitch = Switch(activity).apply {
            text = "自动回复设置"
            isChecked = featureManager.isAutoReplyEnabled()
            textSize = 18f
            setPadding(16, 16, 16, 16)
            setOnCheckedChangeListener { _, isChecked ->
                featureManager.setAutoReplyEnabled(isChecked)
                showToast(if (isChecked) "自动回复设置已开启" else "自动回复设置已关闭")
            }
        }
        layout.addView(autoReplySwitch)
        

        
        AlertDialog.Builder(activity)
            .setTitle("微魔功能菜单")
            .setView(layout)
            .setPositiveButton("确定", null)
            .show()
    }



    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}