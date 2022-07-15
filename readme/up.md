


<h2> app更新下载 </h2>

```java
      new DownloadManager.Builder(HomeActivity.this)
        .apkName("贵州大屏.apk")
        .apkUrl(data.getUrl())
        .smallIcon(R.drawable.icon_gzdp)
        .build()
        .download();

```