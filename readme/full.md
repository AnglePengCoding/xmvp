

<h1> 多图上传   </h1>

```java

    private GridImageAdapter adapter;
    private ArrayList<LocalMedia> mediaArrayList = new ArrayList<>();
    
    
    private void initPhotoRecyclerView() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        binding.mRecyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(mediaArrayList);
        adapter.setSelectMax(3);
        binding.mRecyclerView.setAdapter(adapter);
    }



    private final GridImageAdapter.onAddPicClickListener onAddPicClickListener = () -> {
            PictureSelector.create(FeedBackActivity.this)
            .openGallery(SelectMimeType.ofAll())
            .setSelectorUIStyle(new PictureSelectorStyle())
            .setImageEngine(new ImageLoadEngine())
            .setCompressEngine(new ImageCompressEngine())
            .setSelectLimitTipsListener(new MeOnSelectLimitTipsListener())
            .setLanguage(LanguageConfig.CHINESE)
            .isDisplayCamera(false)
            .isOpenClickSound(false)
            .isFastSlidingSelect(false)
            .isWithSelectVideoImage(true)
            .isPreviewFullScreenMode(true)
            .isPreviewZoomEffect(true)
            .isPreviewImage(true)
            .isPreviewVideo(true)
            .isPreviewAudio(true)
            .isMaxSelectEnabledMask(true)
            .isDirectReturnSingle(false)
            .setMaxSelectNum(3)
            .isGif(false)
            .setSelectedData(adapter.getData())
            .forResult(new OnResultCallbackListener<LocalMedia>() {
             @Override
              public void onResult(ArrayList<LocalMedia> result) {
            adapter.setList(result);
            adapter.notifyDataSetChanged();

              @Override
             public void onCancel() {

            }
    });
   };


```