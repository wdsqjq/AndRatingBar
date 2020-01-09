# AndRatingBar
A RatingBar library for android, you can customize size ,color and image easily!\
安卓平台的RatingBar，可以自定义大小，颜色，图片样式等


**Customize Size**

![Sample1](screenshot/capture1.gif)

**Customize Color & Image**

![Sample2](screenshot/capture2.gif)

**In RecyclerView**

![Sample3](screenshot/capture3.gif)

Implementation
----

Via Gradle:

```xml
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.giswangsj:AndRatingBar:v1.0.0'
}
```

Usage
----

In your layout file:

```xml
<per.wsj.library.AndRatingBar
        android:layout_width="wrap_content"
        android:layout_height="48dp"
        app:starDrawable="@drawable/star_blue"
        app:bgDrawable="@drawable/star_gray"
        android:rating="2.5"/>
```

Configuration
----
AndRatingBar Inherit from Android RatingBar,so all the configuration of RatingBar can be \
configured to AndRatingBar,for example:

* `android:rating="1"` set the rating of RatingBar
* `android:numStars="8"` set the number of star
* `android:layout_height="36dp"` set the size of star
* `android:stepSize="0.5"` set the step size of RatingBar
* `android:stepSize="0.5"` set the step size of RatingBar
* `android:isIndicator="true"` set if as indicator

AndRatingBar has it's own configuration as follows:

* `app:starDrawable=""` set the drawable of star 
* `app:bgDrawable=""` set the background drawable of star
* `app:starColor=""` set the color of star
* `app:subStarColor=""` set the subColor of star
* `app:bgColor=""` set the background color of star
