package com.horsa.testporject;

import android.animation.ValueAnimator;
import com.horsa.shadowlib.CssShadowLayout; 

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CssShadowLayout shadowLayout = findViewById(R.id.my_shadow_layout);

        // Example: Animate the blur radius from 10px to 40px
        ValueAnimator animator = ValueAnimator.ofFloat(10f, 40f);
        animator.setDuration(500); // 500 milliseconds
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                shadowLayout.setShadowBlur(animatedValue);
                // You can also animate Offset Y at the same time to make it look like it's lifting!
                shadowLayout.setShadowOffsetY(animatedValue / 2);
            }
        });
        
        animator.start();
    }
}
