package com.example.assignment2.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class CircleImageView extends androidx.appcompat.widget.AppCompatImageView {
    private int mSize;
    private Paint mPaint;
    private Xfermode mPorterDuffXfermode;
    private boolean first;

    public CircleImageView(Context context) {
        this(context,null);
    }

    public CircleImageView(Context context,AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleImageView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init(){
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        first = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mSize = Math.min(width,height);
        setMeasuredDimension(mSize,mSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) return;
        Bitmap sourceBitmap = ((BitmapDrawable)getDrawable()).getBitmap();
        if (sourceBitmap != null){
            Bitmap bitmap = resizeBitmap(sourceBitmap,getWidth(),getHeight());
            drawCircleBitmapByShader(canvas,bitmap);
        }
    }

    private Bitmap resizeBitmap(Bitmap sourceBitmap,int dstWidth,int dstHeight){
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();
        float widthScale = ((float)dstWidth) / width;
        float heightScale = ((float)dstHeight) / height;
        float scale = Math.max(widthScale,heightScale);
        Matrix matrix = new Matrix();
        matrix.postScale(scale,scale);

        return Bitmap.createBitmap(sourceBitmap,0,0,width,height,matrix,true);
    }

    private void drawCircleBitmapByShader(Canvas canvas,Bitmap bitmap){
        BitmapShader shader = new BitmapShader(bitmap,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP);
        mPaint.setShader(shader);
        canvas.drawCircle(mSize / 2,mSize /2 ,mSize / 2,mPaint);
    }
}
