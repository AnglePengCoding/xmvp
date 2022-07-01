

<h1> 权限申请 </h1>

<h3> 调用activity/fragment  </h3>

```java

        requestRunPermission(new PermissionUtils.FullCallback() {
        @Override
        public void onGranted(List<String> permissionsGranted) {
        
        }
        
        @Override
        public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
        
           }
     },"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION");

```