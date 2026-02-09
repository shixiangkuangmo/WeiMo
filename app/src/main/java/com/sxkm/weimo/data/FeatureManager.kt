package com.sxkm.weimo.data

import android.content.Context
import android.content.SharedPreferences

/**
 * 功能管理器
 * 负责管理各个功能的开关状态和持久化存储
 */
class FeatureManager private constructor(context: Context) {

    companion object {
        private const val PREF_NAME = "weimo_features"
        private const val KEY_VIEWORGIMMG = "veiworgimg"
        private const val KEY_ANTIRECALL = "antirecall"
        private const val KEY_FORWARD_ENHANCE = "forward_enhance"
        private const val KEY_CHAT_EXPORT = "chat_export"
        private const val KEY_AUTO_REPLY = "auto_reply"
        private const val KEY_AUTO_VIEW_ORIGINAL = "auto_view_original"
        
        @Volatile
        private var INSTANCE: FeatureManager? = null
        
        fun getInstance(context: Context): FeatureManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FeatureManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * 获取自动查看原图功能状态
     */
    fun isViewOrgImgEnabled(): Boolean {
        return prefs.getBoolean(KEY_VIEWORGIMMG, false)
    }

    /**
     * 设置自动查看原图功能状态
     */
    fun setViewOrgImgEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_VIEWORGIMMG, enabled).apply()
    }
    /**
     * 获取防撤回功能状态
     */
    fun isAntiRecallEnabled(): Boolean {
        return prefs.getBoolean(KEY_ANTIRECALL, false)
    }

    /**
     * 设置防撤回功能状态
     */
    fun setAntiRecallEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_ANTIRECALL, enabled).apply()
    }

    /**
     * 获取消息转发增强功能状态
     */
    fun isForwardEnhanceEnabled(): Boolean {
        return prefs.getBoolean(KEY_FORWARD_ENHANCE, false)
    }

    /**
     * 设置消息转发增强功能状态
     */
    fun setForwardEnhanceEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_FORWARD_ENHANCE, enabled).apply()
    }

    /**
     * 获取聊天记录导出功能状态
     */
    fun isChatExportEnabled(): Boolean {
        return prefs.getBoolean(KEY_CHAT_EXPORT, false)
    }

    /**
     * 设置聊天记录导出功能状态
     */
    fun setChatExportEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_CHAT_EXPORT, enabled).apply()
    }

    /**
     * 获取自动回复功能状态
     */
    fun isAutoReplyEnabled(): Boolean {
        return prefs.getBoolean(KEY_AUTO_REPLY, false)
    }

    /**
     * 设置自动回复功能状态
     */
    fun setAutoReplyEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_REPLY, enabled).apply()
    }

    /**
     * 获取自动查看原图功能状态
     */
    fun isAutoViewOriginalEnabled(): Boolean {
        return prefs.getBoolean(KEY_AUTO_VIEW_ORIGINAL, false)
    }

    /**
     * 设置自动查看原图功能状态
     */
    fun setAutoViewOriginalEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_VIEW_ORIGINAL, enabled).apply()
    }
}