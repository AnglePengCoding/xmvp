
<h1> 相机使用 </h1>

<h3> Step 1. 调用相机  </h3>

```java

    binding.mTvTest.setOnClickListener(view -> {
        YDialogChooseImage dialog = new YDialogChooseImage(this);
        dialog.show();
        });

```

<h3> Step 2. onActivityResult 接收  </h3>

```java

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
        switch (requestCode) {
        case YPhotoUtils.GET_IMAGE_FROM_PHONE://选择相册之后的处理
        Logg.e("选择相册后回调");
        YUtils.getInstance().initUCrop(this, data.getData());

        break;
        case YPhotoUtils.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
        YUtils.getInstance().initUCrop(this, YPhotoUtils.imageUriFromCamera);

        break;
        case YPhotoUtils.CROP_IMAGE://普通裁剪后的处理
        Glide.with(mContext).
        load(YPhotoUtils.cropImageUri).
        diskCacheStrategy(DiskCacheStrategy.RESOURCE).
        transform(new GlideCircleTransform()).
        thumbnail(0.5f).
        priority(Priority.LOW).
        dontAnimate().
        into(binding.mIvHead);
        break;

        case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
        Uri resultUri = UCrop.getOutput(data);
        File my_avatar = new File(YPhotoUtils.getImageAbsolutePath(this, resultUri));
        Glide.with(this).load(my_avatar).transform(new GlideCircleTransform()).into(binding.mIvHead);
        break;
        case UCrop.RESULT_ERROR://UCrop裁剪错误之后的处理
        UCrop.getError(data);
        break;
        default:
        break;
        }
   }
        super.onActivityResult(requestCode, resultCode, data);
    }
        
```