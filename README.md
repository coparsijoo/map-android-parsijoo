# map-android-parsijoo
نقشه پارسی جو در اپلیکیشن های اندروید


به منظور استفاده از ابزار نقشه خود در اپلیکیشن های اندروید به صورت زیر عمل نمایید:

1) در فایل gradle مربوط به پروژه کد زیر را در قسمت مشخص شده وارد نمایید:
allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
  
  2) در فایل gradle مربوط به ماجول app در قسمت dependencies کد زیر را وارد نمایید:
  
  dependencies {
	        compile 'com.github.coparsijoo:map-android-parsijoo:1.0'
	}
