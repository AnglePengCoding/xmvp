


<h2> 标题栏使用,默认不显示 </h2>

```java

        baseBinding.mRlTitleLayout.setVisibility(View.VISIBLE);//标题栏显示
        baseBinding.mTitleText.setText("标题");//设置标题
        baseBinding.mRightText.setText("右边文字");//设置右边文字
        //设置右边图标,不需要VISIBLE，设置显示，不设置不会显示，
        baseBinding.rightImageView.setImageResource(R.drawable.bg_black);

```