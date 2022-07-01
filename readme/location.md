

<h1> 获取定位 </h1>


```java

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                requestRunPermission(new PermissionUtils.FullCallback() {
                 @Override
                  public void onGranted(List<String> permissionsGranted) {
        
                }
        
                @Override
          public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
        
                }
                },"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION");
                return;
                }
                LocationUtils.register(mContext, 10000, 1000, new LocationUtils.OnLocationChangeListener() {@Override
        public void getLastKnownLocation(Location location) {
                showToast(location.getLatitude()+"");
                }
        
        @Override
        public void onLocationChanged(Location location) {
        
                }
        
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        
                }
                });

```