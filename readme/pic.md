

<h1> 图片加载使用 </h1>

<h3> Step 1.  在app build.gradle 添加 </h3>

```java

 annotationProcessor 'com.github.bumptech.glide:compiler:4.13.0'

```
<h3> 加载头像 </h3>

```java

    Glide.with(mContext).
        load(YPhotoUtils.cropImageUri).
        diskCacheStrategy(DiskCacheStrategy.RESOURCE).
        thumbnail(0.5f).
        priority(Priority.LOW).
        dontAnimate().
        into(binding.mIvHead);

```
<h3> 加载圆头像 </h3>

```java

    Glide.with(this)
        .load(my_avatar)
        .transform(new GlideCircleTransform())
        .into(binding.mIvHead);

```

<h3> glide </h3>

<p dir="auto"><a href="https://github.com/bumptech/glide">glide具体使用详情</a></p>


