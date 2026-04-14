package com.horsa.shadowlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CssShadowLayout extends FrameLayout {

    private Paint shadowPaint;
    private RectF shadowRect;

    private int shadowColor;
    private float shadowBlur;
    private float shadowOffsetX;
    private float shadowOffsetY;
    private float shadowSpread;
    private float shadowCornerRadius;
    private int shadowFillColor;

    public CssShadowLayout(Context context) {
        super(context);
        init(null);
    }

    public CssShadowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CssShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setWillNotDraw(false);

        // ADVANCED PERFORMANCE: Only disable hardware acceleration on older APIs.
        // API 28 (Pie) and above support setShadowLayer natively on the GPU.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowRect = new RectF();

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CssShadowLayout);
            shadowColor = a.getColor(R.styleable.CssShadowLayout_shadowColor, Color.parseColor("#33000000"));
            shadowBlur = a.getDimension(R.styleable.CssShadowLayout_shadowBlur, 10f);
            shadowOffsetX = a.getDimension(R.styleable.CssShadowLayout_shadowOffsetX, 0f);
            shadowOffsetY = a.getDimension(R.styleable.CssShadowLayout_shadowOffsetY, 5f);
            shadowSpread = a.getDimension(R.styleable.CssShadowLayout_shadowSpread, 0f);
            shadowCornerRadius = a.getDimension(R.styleable.CssShadowLayout_shadowCornerRadius, 0f);
            shadowFillColor = a.getColor(R.styleable.CssShadowLayout_shadowFillColor, Color.WHITE);
            a.recycle();
        }

        updateShadowPaint();
    }

    /**
     * Applies the current attributes to the Paint object.
     */
    private void updateShadowPaint() {
        shadowPaint.setColor(shadowFillColor);
        shadowPaint.setShadowLayer(shadowBlur, shadowOffsetX, shadowOffsetY, shadowColor);
        invalidate(); // Forces the view to redraw when a value changes
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float left = getPaddingLeft() - shadowSpread;
        float top = getPaddingTop() - shadowSpread;
        float right = getWidth() - getPaddingRight() + shadowSpread;
        float bottom = getHeight() - getPaddingBottom() + shadowSpread;

        shadowRect.set(left, top, right, bottom);

        // PRECISE CSS SPREAD MATH:
        // In CSS, positive spread physically increases the corner radius of the shadow.
        // Negative spread decreases it. We replicate that exact browser behavior here.
        float shadowActualRadius = shadowCornerRadius;
        if (shadowSpread > 0) {
            shadowActualRadius += shadowSpread;
        } else if (shadowSpread < 0) {
            shadowActualRadius = Math.max(0, shadowActualRadius + shadowSpread);
        }

        // Draw the background with the mathematically precise shadow
        canvas.drawRoundRect(shadowRect, shadowActualRadius, shadowActualRadius, shadowPaint);
    }

    // =========================================================================
    // DYNAMIC SETTERS (For programmatic changes and animations)
    // =========================================================================

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
        updateShadowPaint();
    }

    public void setShadowBlur(float shadowBlur) {
        this.shadowBlur = shadowBlur;
        updateShadowPaint();
    }

    public void setShadowOffsetX(float shadowOffsetX) {
        this.shadowOffsetX = shadowOffsetX;
        updateShadowPaint();
    }

    public void setShadowOffsetY(float shadowOffsetY) {
        this.shadowOffsetY = shadowOffsetY;
        updateShadowPaint();
    }

    public void setShadowSpread(float shadowSpread) {
        this.shadowSpread = shadowSpread;
        updateShadowPaint(); // Spread changes bounds, onDraw will handle it via invalidate()
    }

    public void setShadowCornerRadius(float shadowCornerRadius) {
        this.shadowCornerRadius = shadowCornerRadius;
        invalidate();
    }

    public void setShadowFillColor(int shadowFillColor) {
        this.shadowFillColor = shadowFillColor;
        updateShadowPaint();
    }
}
