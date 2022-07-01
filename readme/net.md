

<h1> 网络请求使用 </h1>

<h3> Step 1. 继承BaseApp  </h3>

```java

        /**
        * @return 请求总地址
        */
        @Override
        public String setBaseUrl() {
        return "https://github.com/AnglePengCoding/xmvp";
        }

        /**
        * 请求成功code，默认200，实际按后台为准
        *
        * @return
        */
        @Override
        public String setResultCodeOperationSuccessful() {
        return "200";
        }

```

<h3> Step 2. 新建接口类  </h3>

```java

    public interface Api {
   
    //Bean 后台返回的bean, retrofit网络请求，不会请百度，不做解释
    @GET("xx/xx")
    Observable<BaseResponse<Bean>> sweepCode(@Query("xx") String eid);

    }
```
<h3> Step 3. Contract层  </h3>

```java

    public interface MainContract {
    interface Model extends BaseModel {
        Observable<BaseResponse<bean>> sweepCode(String eid);
    }

    interface View extends BaseView {
        void requestNetCallback();
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void sweepCode(String eid);
    }
}

```

<h3> Step 4. Model层 ，务必调用.compose(RxUtil.rxSchedulerHelper()，切换子线程请求 </h3>

```java

    public class MainModel implements MainContract.Model {
    @Override
    public Observable<BaseResponse<QualifiedEntCertInfo2>> sweepCode(String eid) {
        return RxService.createApi(Api.class).sweepCode(eid).compose(RxUtil.rxSchedulerHelper());
    }
    }

```

<h3> Step 5. Presenter层  </h3>

```java

    public class MainPresenter extends MainContract.Presenter {
    @Override
    public void onStart() {
    }

    @Override
    void sweepCode(String eid) {
        mModel.sweepCode(eid).subscribe(new BaseObserver<BaseResponse
                <bean>>(mView) {
            @Override
            public void onSuccess(BaseResponse<bean> response) {
                mView.requestNetCallback();
            }
        });
    }
}

```

<h3> Step 6. 发起网络请求，务必都写在initPresenter函数中,统一处理网络失败，重新请求  </h3>

```java

        @Override
        public void initPresenter() {
        mPresenter.sweepCode("1");
        }

```

<h3> Step 7. 处理数据  </h3>

```java


    public class MainActivity extends BaseActivity<ActivityMainBinding, MainPresenter, MainModel> implements MainContract.View {

    @Override
    public void initPresenter() {
        mPresenter.sweepCode("1");
    }


    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void requestNetCallback() {
        // TODO: 2022/2/22 请求收到的数据
    }
    }


```