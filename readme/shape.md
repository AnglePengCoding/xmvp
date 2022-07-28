


<h2> 无需再写shape.xml </h2>

```java
        //边框+背景+圆角
        <TextView
            android:layout_width="130dp"
            android:layout_width="130dp"
            android:layout_height="36dp"
            android:gravity="center"
            android:text="TextView"
            android:textColor="#8c6822"
            android:textSize="20sp"
            app:bl_corners_radius="4dp"
            app:bl_solid_color="#E3B666"
            app:bl_stroke_color="#8c6822"
            app:bl_stroke_width="2dp" />
                    
        //渐变
        <Button
            android:id="@+id/btn"
            android:layout_width="130dp"
            android:layout_height="36dp"
            android:layout_marginTop="5dp"
            android:gravity="center"
            android:padding="0dp"
            android:text="跳转到列表"
            android:textColor="#4F94CD"
            android:textSize="20sp"
            app:bl_corners_radius="2dp"
            app:bl_gradient_angle="0"
            app:bl_gradient_endColor="#4F94CD"
            app:bl_gradient_startColor="#63B8FF" />

```