
<h1> Activity使用 </h1>

```java

public class MainActivity extends BaseActivity<ActivityMainBinding, MainPresenter, MainModel> implements MainContract.View {

    @Override
    public void initView() {
        baseBinding.mRlTitleLayout.setVisibility(View.VISIBLE);
        baseBinding.mTitleText.setText("");
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

}

```

<h3> 开启ViewBinding，自动生成布局,不了解的具体去查</h3>

```java

android{
        compileSdk 32
        defaultConfig{
        /* 加入 viewBinding enabled为true*/
        viewBinding{
        enabled=true
        }
       

```


