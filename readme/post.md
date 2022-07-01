

<h1> 多组件间通信 </h1>


<h3> 第一种使用方式,使用presenter调用，可以发送任意值和对象，接收值时强转（注意点：使用Presenter,必须发送和接受的类必须持有Presenter和model实例，否则会报空指针异常） </h3>

```java

    binding.mTvTest.setOnClickListener(view -> {
        // mPresenter.mRxManager.post("tag", "多组件间通信");
        });



```

<h3> presenter 接收值，强转支持任意对象</h3>

```java

        mPresenter.mRxManager.on("tag", o -> {
        String msg = (String) o;

        });


```

<h3> 第二种使用方式  RxBus发送 </h3>

```java

    RxBus.$().post("tag", "多组件间通信");


```

<h3> 第二种使用方式  RxBus接收 </h3>

```java

        new RxManager().on("tag", o -> {
        String msg = (String) o;
        });


```