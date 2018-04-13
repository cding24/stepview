package com.linghu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.linghu.view.stepview.R;

/**
 * Created by linghu on 2018/3/16.
 * 步骤指示控件
 *
 */
public class StepView extends View {
    private int mWidth;
    private int mHeight;
    private int padLeft;
    private int padRight;
    private int gapWidth = 3;
    private int stepNum = 1;

    private int defaultColor = 0xff4488;
    private int selectColor = 0xff0044;
    private int numColor = 0xffffff;
    private int tipTxtColor = 0x393939;
    private int tipSelectTxtColor = 0x1587DF;
    private int tipTxtSize = 16;
    private int raduis = 2;
    private int lineWidth = 1;
    private Paint defaultPaint;
    private Paint selectPaint;
    private Paint numPaint;
    private Paint txtPaint;
    private int txtSize = 14;
    private float numBaseLine = 0;
    private float txtBaseLine = 0;
    private String stepTxt1;
    private String stepTxt2;
    private String stepTxt3;

    public StepView(Context context) {
        super(context);
        initView(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs){
        setWillNotDraw(false);

        //代码中dp值转换成px
        DisplayMetrics dm = getResources().getDisplayMetrics();
        lineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, lineWidth, dm);
        raduis = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, raduis, dm);
        gapWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, gapWidth, dm);
        txtSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, txtSize, dm);
        tipTxtSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tipTxtSize, dm);

        // get custom attrs
        if(attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StepView);
            lineWidth = typedArray.getDimensionPixelSize(R.styleable.StepView_stepLineWidth, lineWidth);
            gapWidth = typedArray.getDimensionPixelSize(R.styleable.StepView_stepLineGap, gapWidth);
            defaultColor = typedArray.getColor(R.styleable.StepView_stepDefaultColor, defaultColor);
            selectColor = typedArray.getColor(R.styleable.StepView_stepSelectColor, selectColor);
            numColor = typedArray.getColor(R.styleable.StepView_stepTxtColor, numColor);
            txtSize = typedArray.getDimensionPixelSize(R.styleable.StepView_stepTxtSize, txtSize);
            tipTxtSize = typedArray.getDimensionPixelSize(R.styleable.StepView_stepTipTxtSize, tipTxtSize);
            tipTxtColor = typedArray.getColor(R.styleable.StepView_stepTipTxtColor, tipTxtColor);
            tipSelectTxtColor = typedArray.getColor(R.styleable.StepView_stepTipTxtSelectColor, tipSelectTxtColor);
            raduis = typedArray.getDimensionPixelSize(R.styleable.StepView_stepRadius, raduis);
            stepNum = typedArray.getInt(R.styleable.StepView_stepNum, 1);
            stepTxt1 = typedArray.getString(R.styleable.StepView_steptxt1);
            stepTxt2 = typedArray.getString(R.styleable.StepView_steptxt2);
            stepTxt3 = typedArray.getString(R.styleable.StepView_steptxt3);
            typedArray.recycle();
        }

        defaultPaint = new Paint();
        defaultPaint.setAntiAlias(true);
        defaultPaint.setStyle(Paint.Style.FILL);
        defaultPaint.setColor(defaultColor);
        defaultPaint.setStrokeWidth(lineWidth);
        selectPaint = new Paint();
        selectPaint.setAntiAlias(true);
        selectPaint.setStyle(Paint.Style.FILL);
        selectPaint.setColor(selectColor);
        selectPaint.setStrokeWidth(lineWidth);

        numPaint = new Paint();
        numPaint.setAntiAlias(true);
        numPaint.setStyle(Paint.Style.FILL);
        numPaint.setColor(numColor);
        numPaint.setTextSize(txtSize);
        numPaint.setTextAlign(Paint.Align.CENTER);

        txtPaint = new Paint();
        txtPaint.setAntiAlias(true);
        txtPaint.setStyle(Paint.Style.FILL);
        txtPaint.setColor(tipTxtColor);
        txtPaint.setTextSize(tipTxtSize);
        txtPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = right - left;
        mHeight = bottom - top;
        padLeft = getPaddingLeft();
        padRight = getPaddingRight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(numBaseLine == 0){
            Rect targetRect = new Rect(0, mHeight/4-raduis, mWidth, mHeight/4+raduis);
            Paint.FontMetrics txtFontMetrics = numPaint.getFontMetrics();
            numBaseLine = (targetRect.bottom + targetRect.top - txtFontMetrics.bottom - txtFontMetrics.top)/2.0f;

            targetRect = new Rect(0, mHeight*3/4-raduis, mWidth, mHeight*3/4+raduis);
            txtFontMetrics = txtPaint.getFontMetrics();
            txtBaseLine = (targetRect.bottom + targetRect.top - txtFontMetrics.bottom - txtFontMetrics.top)/2.0f;
        }

        canvas.drawCircle(padLeft+raduis, mHeight/4, raduis, selectPaint);               // 小圆
        canvas.drawText("1", padLeft+raduis, numBaseLine, numPaint);
        txtPaint.setColor(tipSelectTxtColor);

        canvas.drawText(TextUtils.isEmpty(stepTxt1) ? "普通会员": stepTxt1, padLeft+raduis, txtBaseLine, txtPaint);
        if(stepNum > 1){
            canvas.drawLine(padLeft+2*raduis+gapWidth, mHeight/4, mWidth/2-raduis-gapWidth, mHeight/4, selectPaint);
            canvas.drawCircle(mWidth/2, mHeight/4, raduis, selectPaint);                  // 小圆
            txtPaint.setColor(tipSelectTxtColor);
            canvas.drawText(TextUtils.isEmpty(stepTxt1) ? "普通会员": stepTxt1, mWidth/2, txtBaseLine, txtPaint);
        }else{
            canvas.drawLine(padLeft+2*raduis+gapWidth, mHeight/4, mWidth/2-raduis-gapWidth, mHeight/4, defaultPaint);
            canvas.drawCircle(mWidth/2, mHeight/4, raduis, defaultPaint);                 // 小圆
            txtPaint.setColor(tipTxtColor);
            canvas.drawText(TextUtils.isEmpty(stepTxt2) ? "黄金会员": stepTxt2, mWidth/2, txtBaseLine, txtPaint);
        }
        canvas.drawText("2", mWidth/2, numBaseLine, numPaint);
        if(stepNum > 2){
            canvas.drawLine(mWidth/2+raduis+gapWidth, mHeight/4, mWidth-padRight-2*raduis-gapWidth, mHeight/4, selectPaint);
            canvas.drawCircle(mWidth-padRight-raduis, mHeight/4, raduis, selectPaint);   // 小圆
            txtPaint.setColor(tipSelectTxtColor);
            canvas.drawText(TextUtils.isEmpty(stepTxt3) ? "钻石会员": stepTxt3, mWidth-padRight-raduis, txtBaseLine, txtPaint);
        }else{
            canvas.drawLine(mWidth/2+raduis+gapWidth, mHeight/4, mWidth-padRight-2*raduis-gapWidth, mHeight/4, defaultPaint);
            canvas.drawCircle(mWidth-padRight-raduis, mHeight/4, raduis, defaultPaint);  // 小圆
            txtPaint.setColor(tipTxtColor);
            canvas.drawText(TextUtils.isEmpty(stepTxt3) ? "钻石会员": stepTxt3, mWidth-padRight-raduis, txtBaseLine, txtPaint);
        }
        canvas.drawText("3", mWidth-padRight-raduis, numBaseLine, numPaint);
    }

}