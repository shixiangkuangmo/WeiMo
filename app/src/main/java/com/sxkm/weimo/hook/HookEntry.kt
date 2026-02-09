package com.sxkm.weimo.hook

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.sxkm.weimo.BuildConfig
import com.sxkm.weimo.hook.entity.WeChatHooker
import com.sxkm.weimo.hook.entity.ImageGalleryHookerDynamic

@InjectYukiHookWithXposed
class HookEntry : IYukiHookXposedInit {

    override fun onInit() = configs {
        isDebug = BuildConfig.DEBUG
    }

    override fun onHook() = encase {
        YLog.info("进入 onHook：")
        loadApp(name = "com.tencent.mm", hooker = WeChatHooker)
        loadApp(name = "com.tencent.mm", hooker = ImageGalleryHookerDynamic)
    }
}