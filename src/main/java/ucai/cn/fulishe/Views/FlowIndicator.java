package ucai.cn.fulishe.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import ucai.cn.fulishe.R;

/**
 * Created by Administrator on 2016/10/18.
 * 显示下方圆圈
 */
public  class FlowIndicator extends View{

    int mCount;//指示器中实心圆数量
    int mRadius;//实心圆半径
    int mSpace;//实心圆之前的距离
    int mFocus;//当前实心圆的颜色
    int mNormalColor;//非焦点实心圆的颜色
    int mFocusColor;//焦点实心圆的颜色
     Paint paint;

    public FlowIndicator(Context context) {
        super(context);
    }

    public int getmRadius() {
        return mRadius;
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }
    public int getmFocus() {
        return mFocus;
    }

    public void setmFocus(int mFocus) {
        this.mFocus = mFocus;
    }

    public FlowIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowIndicator);
        mNormalColor = array.getColor(R.styleable.FlowIndicator_normal_color,
                0xffffff);
        mFocusColor = array.getColor(R.styleable.FlowIndicator_focus_color,
                0xffff07);
        mSpace = array.getDimensionPixelOffset(R.styleable.FlowIndicator_space,
                getCount()-1);
        mRadius = array.getDimensionPixelOffset(
                R.styleable.FlowIndicator_r, 9);
        mCount=array.getInt(R.styleable.FlowIndicator_count,getCount());
        mFocus=array.getInt(R.styleable.FlowIndicator_focus,0);
        array.recycle();
        paint=new Paint();
    }

    /**
     * 返回指示器实心圆数量
     * @return
     */
    public int getCount(){
        return this.mCount;
    }

    /**
     * 设置指示器实心圆数量
     */
    public  void setCount( int count){
        this.mCount=count;
        requestLayout();
        invalidate();
    }

    /**
     * 设置当前焦点的下标
     * @param focus
     */
    public void setFocus(int focus){
        this.mFocus=focus;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * 测量宽度
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec){
        int result=0;
        int mode= MeasureSpec.getMode(widthMeasureSpec);
        int spaceSize= MeasureSpec.getSize(widthMeasureSpec);
        if(mode== MeasureSpec.EXACTLY){
            result=spaceSize;
        }else{
            result=(int)(getLeftPaddingOffset()+getRightFadingEdgeStrength())
                    +2*mRadius*mCount+(mCount-1)*mSpace;
            result= Math.min(result, spaceSize);
        }
        return result;
    }

    /**
     * 测量高度
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec){
        int result=0;
        int mode= MeasureSpec.getMode(heightMeasureSpec);
        int spaceSize= MeasureSpec.getSize(heightMeasureSpec);
        if(mode== MeasureSpec.EXACTLY){
            result=spaceSize;
        }else{
            result=(int)(getTopPaddingOffset()+getBottomFadingEdgeStrength())+2*mRadius;
            result= Math.min(result, spaceSize);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);//全部填充
        int leftSpace=(getWidth()-(mSpace+mRadius)*2*mCount-(mCount-1)*mSpace)/2;
        for(int i=0;i<mCount;i++){
            int x=getLeftPaddingOffset()+leftSpace+i*(2*mRadius+mSpace)+mRadius;
            int color=mFocus==i?mFocusColor:mNormalColor;
            paint.setColor(color);
            canvas.drawCircle(x,mRadius,mRadius,paint);
        }
    }
}
