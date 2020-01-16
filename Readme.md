### 基础框架
	Rxjava+retrofit+dagger+mvp的封装



	

#####  2020-1-14
    切换到AndroidX,升级所有依赖库的版本
    遇到的坑:
        1, 编译不通过,报错:  Invoke-customs are only supported starting with Android O
        解决办法: 在app目录的build.gradle目录下的
        android{
            ......
             compileOptions {
                    sourceCompatibility JavaVersion.VERSION_1_8
                    targetCompatibility JavaVersion.VERSION_1_8

                }
        }
        2. 修改自定义apk输出名字时候,提示: API 'variantOutput.getPackageApplication()' is obsolete and has been replaced with 'variant.getPackageApplicationProvider()'
        原因: AS 3.2.0之后,使用${defaultConfig.versionCode}会报warning提示,
        解决办法: ${defaultConfig.versionCode}-->${variant.versionCode}
        3. plugins刷新不出插件列表....
        这个比较懵....一直刷新不出来,网上搜了解决办法,说是setting-->update里面取消安全链接,重启...然后发现没卵用...
        然后我关闭了setting,重新build一下,神奇的是plugins居然刷出来了...本来我打算从官网(https://plugins.jetbrains.com/androidstudio)下载到本地安装的.....
        关闭后,再打开又刷新不出了,只好用梯子了....
	
	引入动画框架Android Lottie
	踩坑记录: 测试动画,从https://lottiefiles.com/下载的最新动画,加载失败,查询后得知,lottie 3.0(2.8+版本迁移android X)以上版本才能兼容
	Bodymovin Version: 5.5+版本,解决办法:  方案一,项目升级到android X,引入最新Lottie框架;   方案二: 下载Bodymovin Version: 5
	.5以前版本动画; 方案三: 让UI设计师导出json动画的时候,导出兼容老版本的动画.
        
     
         
     
