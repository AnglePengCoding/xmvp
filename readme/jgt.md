

<h1> 结构体重构 </h1>

<h3> Step 1. BaseResponse重写  </h3>

```java

        public static final String RESULT_CODE_OPERATION_SUCCESSFUL = "200";//操作成功
        public static final String RESULT_CODE_OPERATION_FAILED = "500";//操作失败
        public static final String RESULT_CODE_PARAMETER_EXCEPTION = "2001";//参数异常
        public static final String RESULT_CODE_SERVER_BUSY = "2003";//服务器正忙，请稍后重试
        public static final String RESULT_CODE_LOGIN_TIME_OUT = "3001";//登录超时,请重新登录
        public static final String RESULT_CODE_NOT_LOGGED = "3002";//您还未登录，请先登录
        public static final String RESULT_CODE_LOGIN_EXPIRED = "301";//登录过期，请重新登录
        public static final String RESULT_CODE_ILLEGAL_REQUEST = "4001";//非法请求
        public static final String RESULT_CODE_NOT_PERMISSION = "4002";//您还没该权限
        
        /**
         * 通用返回值属性   //按照服务器返回字段改写
         */
        private String status;
        /**
         * 通用返回信息。//按照服务器返回字段改写
         */
        private String message;
        /**
         * 具体的内容。//按照服务器返回字段改写
         */
        private DataType data;
        
        public String getCode() {
                return status;
                }
        
        public void setCode(String code) {
                this.status = code;
                }
        
        public String getMsg() {
                return message;
                }
        
        public void setMsg(String msg) {
                this.message = msg;
                }
        
        public DataType getData() {
                return data;
                }
        
        public void setData(DataType data) {
                this.data = data;
                }
        
        @Override
        public String toString() { //status  message data 按照上边字段一样
                return "BaseResponse{" +
                "code='" + status + '\'' +
                ", msg='" + message + '\'' +
                ", result=" + data +
                '}';
                }

```
<h3> Step 2. ResultException失败接收体重写  </h3>


```java

        public class ResultException extends IOException {
            private String status;
            private String message;
        
        
            public ResultException(String code, String msg) {
                this.status = code;
                this.message = msg;
            }
        
            public String getCode() {
                return status;
            }
        
            public void setCode(String code) {
                this.status = code;
            }
        
            public String getMsg() {
                return message;
            }
        
            public void setMsg(String msg) {
                this.message = msg;
            }
        }
```

<h3> Step 3.重写 BaseObserver 导入上边自定义的BaseResponse/ResultException </h3>


```java
    public abstract class BaseObserver <E extends BaseResponse> implements Observer<E> {
        protected final String LOG_TAG = getClass().getSimpleName();
        private final BaseUiInterface mUiInterface;
    
        public BaseObserver(BaseUiInterface baseUiInterface) {
            mUiInterface = baseUiInterface;
        }
    
        @Override
        public void onSubscribe(Disposable d) {
            if (!isNetConnect()) {
                d.dispose();
                mUiInterface.showNoNetworkConnection(1);
                return;
            }
            showLoadingDialog();
        }
    
        private boolean isNetConnect() {
            return NetworkUtils.isConnected();
        }
    
        private void showLoadingDialog() {
            ProgressDialogUtils.$().showProgress(mUiInterface.getAActivity());
        }
    
        @Override
        public void onComplete() {
            ProgressDialogUtils.$().dismissProgress();
        }
    
        @Override
        public void onNext(@androidx.annotation.NonNull E response) {
            switch (response.getCode()) {
                case BaseResponse.RESULT_CODE_OPERATION_SUCCESSFUL:
                    mUiInterface.showNoNetworkConnection(0);
                    onSuccess(response);
                    break;
            }
        }
    
    
        public abstract void onSuccess(E response);
    
        @Override
        public void onError(@NonNull Throwable throwable) {
            handleError(throwable, mUiInterface, LOG_TAG);
            ProgressDialogUtils.$().dismissProgress();
        }
    
    
        public void handleError(Throwable throwable, BaseUiInterface mUiInterface, String LOG_TAG) {
            if (throwable == null) {
                mUiInterface.showUnknownException();
                return;
            }
            //分为以下几类问题：网络连接，数据解析，客户端出错【空指针等】，服务器内部错误
            if (throwable instanceof SocketTimeoutException || throwable
                    instanceof ConnectException || throwable instanceof UnknownHostException) {
                mUiInterface.showNoNetworkConnection(1);
            } else if ((throwable instanceof JsonSyntaxException) || (throwable instanceof
                    NumberFormatException) || (throwable instanceof MalformedJsonException)) {
                mUiInterface.showDataException("数据解析出错");
            } else if ((throwable instanceof HttpException)) {
                mUiInterface.showDataException("服务器错误(" + ((HttpException) throwable).code() + ")");
            } else if (throwable instanceof NullPointerException) {
                mUiInterface.showDataException(BaseUiInterface.ERROR_CODE);
            } else if (throwable instanceof ResultException) {
                onServerErrorShow(((ResultException) throwable));
            } else {
                Logg.e("throwable:00-" + throwable.getMessage());
                mUiInterface.showDataException("客户端开小差了，攻城狮正在修复中...");
            }
        }
    
        private void onServerErrorShow(ResultException throwable) {
            switch (throwable.getCode()) {
                case BaseResponse.RESULT_CODE_OPERATION_FAILED:
                case BaseResponse.RESULT_CODE_NOT_PERMISSION:
                case BaseResponse.RESULT_CODE_ILLEGAL_REQUEST:
                case BaseResponse.RESULT_CODE_NOT_LOGGED:
                case BaseResponse.RESULT_CODE_LOGIN_TIME_OUT:
                case BaseResponse.RESULT_CODE_SERVER_BUSY:
                case BaseResponse.RESULT_CODE_PARAMETER_EXCEPTION:
                default:
                    mUiInterface.showDataException(throwable.getMsg());
            }
        }
    }


```