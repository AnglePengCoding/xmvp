

<h1> 沉浸式使用 </h1>

```java

    protected StatusBar mBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mBar = StatusBar.with(this);
            mBar.navigationBarColor(android.R.color.white)
            .statusBarDarkFont(true, 0.2f)
            .fitsSystemWindows(true, android.R.color.white)
            .init();   //所有子类都将继承这些相同的属性
            getLifecycle().addObserver(this);
            }
            
```

<h3> 记得销毁，避免出现内存泄露 </h3>

```java
        public void onDestroy() {
        if (mBar != null) mBar.destroy();
        }
            
```