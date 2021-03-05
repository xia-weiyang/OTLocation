# OTLocation

Android 位置选择

For [旧时光](http://jiushig.com)

## Screenshot 
<img src="./img/S80409-211119.jpg" width = "200"/>  <img src="./img/S80409-211114.jpg" width = "200"/>  <img src="./img/S80409-211106.jpg" width = "200"/> 


## Setup

To use this library your `minSdkVersion` must be >= 19

Add it in your root build.gradle at the end of repositories

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://www.jitpack.io' }
    }
}
```
Add the dependency
```gradle
dependencies {
        compile 'com.github.otjiushig:OTLocation:1.1'
}
```

## Usage

注意：
- 目前仅集成了高德地图。

添加依赖后，请前往高德地图开放平台自行申请定位key，并按照高德官方教程添加key。

#### 定位

```java
new LocationBuilder(this)
        .setmaxErrorNum(3)
        .setType(LocationBuilder.TYPE.GAO_DE)
        .setCallbackSuccess(locationInfo -> Toast.makeText(MainActivity.this, locationInfo.toString(), Toast.LENGTH_LONG).show())
        .setCallbackFail(msg -> Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show())
        .start()
```

#### 打开地图

```java
findViewById(R.id.button1).setOnClickListener(view -> {
            MapActivity.start(this, 30.63, 106.07);
        });
```

#### 位置选择

```java
findViewById(R.id.button2).setOnClickListener(view -> {
            SelectActivity.start(this,"不选择位置");
        });
```
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == SelectActivity.REQUEST_CODE) {
        if (resultCode == RESULT_OK) {
            Location location = (Location) data.getSerializableExtra("info");
            if (location != null)
                Toast.makeText(MainActivity.this, location.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
```

Look at the example app for more.

