

<h2> 存储数据/登录用户token信息使用 </h2>

<h2> !!!!!!! 这里注意，需要继承BaseApp !!!!!!!</h2>

<h3> 保存登录用户token信息 </h3>


```java

    LocalDataManager.getInstance().saveLoginInfo(userBean);

```
<h3> 获取登录用户token信息 </h3>

```java

LocalDataManager.getInstance().getLoginInfo(UserBean.class)

```

<h2> 存储数据 </h2>

```java

        Prefs.set("string", "1");
        Prefs.set("int", 1);
        Prefs.set("float", 1.1);


        Prefs.get("string",String.class);
        Prefs.get("int",Integer.class);
        Prefs.get("float",Float.class);

```