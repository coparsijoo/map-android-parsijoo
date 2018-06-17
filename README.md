# map-android-parsijoo
<p dir="rtl">
نقشه پارسی جو در اپلیکیشن های اندروید
</p>

<p dir="rtl">
به منظور استفاده از ابزار نقشه پارسی جو در اپلیکیشن های اندروید قطعه کد زیر را در فایل gradle اپلیکیشن خود قرار دهید:
</p>
<div class="highlight highlight-source-groovy"><pre>repositories {
        maven { url 'https://jitpack.io' }
}
dependencies {
 	compile <span class="pl-s"><span class="pl-pds">'</span>com.github.coparsijoo:map-android-parsijoo:1.0<span class="pl-pds">'</span></span>
}</pre></div>

<p dir="rtl">
        کد زیر را در قسمت لایوت مربوط به اکتیویتی خود اضافه کنید:
</p>

<div class="highlight highlight-text-xml">
<pre>&lt;<span class="pl-ent">ir.parsijoo.map.android.Viewer</span>
    android:id="@+id/mapview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:api_key="*********"  /&gt;</pre></div>
    
    
<p dir="rtl">
      به جای ********* می بایست کلید api مربوط به نقشه پارسی جو را وارد نمایید. به منظور دریافت کلید api به لینک زیر مراجعه نمایید :
</p>
<p>
        http://developers.parsijoo.ir/dashboard/
</p>

<p dir="rtl">
    به منظورآموزش استفاده از تمامی توابع api نقشه پارسی جو به لینک زیر مراجعه نمایید:
</p>
<p>
        http://developers.parsijoo.ir/service/map
</p>
<p dir="rtl">
        دسترسی های مورد نیاز 
</p>

<div class="highlight highlight-text-xml"><pre>&lt;<span class="pl-ent">uses-permission</span> <span class="pl-e">android</span><span class="pl-e">:</span><span class="pl-e">name</span>=<span class="pl-s"><span class="pl-pds">"</span>android.permission.ACCESS_FINE_LOCATION<span class="pl-pds">"</span></span>/&gt;
&lt;<span class="pl-ent">uses-permission</span> <span class="pl-e">android</span><span class="pl-e">:</span><span class="pl-e">name</span>=<span class="pl-s"><span class="pl-pds">"</span>android.permission.INTERNET<span class="pl-pds">"</span></span> /&gt;
&lt;<span class="pl-ent">uses-permission</span> <span class="pl-e">android</span><span class="pl-e">:</span><span class="pl-e">name</span>=<span class="pl-s"><span class="pl-pds">"</span>android.permission.ACCESS_NETWORK_STATE<span class="pl-pds">"</span></span>  /&gt;
&lt;<span class="pl-ent">uses-permission</span> <span class="pl-e">android</span><span class="pl-e">:</span><span class="pl-e">name</span>=<span class="pl-s"><span class="pl-pds">"</span>android.permission.WRITE_EXTERNAL_STORAGE<span class="pl-pds">"</span></span> /&gt;</pre></div>
<p dir="rtl">
        برای اندروید 6 به بالا(API level 23)، می بایست مجوز ها به صورت داینامیک از کاربر دریافت گردد با استفاده از دستور زیر: <br/>
        
</p>
<p>
        <code dir="ltr">RequestPermission</code>
</p>

