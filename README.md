# DemoTest
自定义View控件
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.Cuiernao:DemoTest:1.0'
	}
	
实例旋转控件用法
	
	<com.cuiernao.rouletteview.RouletteView
        android:id="@+id/RouletteView"
        android:layout_width="300dp"
        android:layout_height="wrap_content"
        android:background="@color/Blue"
        app:proportionY="0.2"
        app:proportionX="0.8"
        app:globuleRadius="6dp"
        app:reflectionDiance="20dp"
        app:density="40"/>
