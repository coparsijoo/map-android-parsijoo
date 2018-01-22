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
 	compile <span class="pl-s"><span class="pl-pds">'</span>com.github.coparsijoo:map-android-parsijoo:1.0;<span class="pl-pds">'</span></span>
}</pre></div>

<p dir="rtl">
        <strong >طریقه افزودن نقشه در لایوت برنامه</strong><br/><br/>
        کد زیر را در قسمت لایوت مربوط به اکتیویتی خود اضافه کنید:
</p>

<div class="highlight highlight-text-xml">
<pre>&lt;<span class="pl-ent">ir.parsijoo.map.android.Viewer</span>
    android:id="@+id/mapview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:api_key="@string/api_key"  /&gt;</pre></div>
