package com.adoctor.adoctor;

import java.util.Calendar;

import com.adoctor.adoctor.pref.Preference;
import com.adoctor.adoctor.pref.PreferenceData;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


/**
* 오늘 스마트폰 사용량을 시각적으로 나타내는 커스텀 위젯(custom view)
* @author H.John Choi
* 배경 원 그리기 > 사용량 부채꼴 그리기 / 현재 시간 나타내기 & 사용 시간 나타내기
* github web에서 작성중인 관계로 나중에 문서화 하겠음.
*/

class Clock01 extends View {
       int mMax;
       int mPos;
       
 
       public Clock01(Context context, AttributeSet attrs, int defStyle) {
             super(context, attrs, defStyle);
             init();
       }
 
       public Clock01(Context context, AttributeSet attrs) {
             super(context, attrs);
             init();
       }
 
       public Clock01(Context context) {
             super(context);
             init();
       }
 
       
       void init() {
             //최대 크기
             mMax = 1440; //하루 = 1440분
             //현재 위치
             mPos = 0;
       }
 
 
       //최대값 반환
       int getMax() { return 1440; }
 
       //pos를 변경하는 메소드
       void setPos(int aPos) {
             if (aPos < 0 || aPos > mMax) {
                    return;
             }
             mPos = aPos;
             invalidate();//onDraw를 다시 부르는 메소드
       }
 
       int getPos() { return mPos; }
 
       protected void onDraw(Canvas canvas) {
             
             //원을 만듦
             RectF rt = new RectF();
             rt.left = getPaddingLeft() + 20;//왼쪽값
             rt.right = getWidth() - getPaddingRight() - 20;//오른쪽값
             rt.bottom = getHeight() - getPaddingTop() + 20;//아래쪽값
             rt.top = getPaddingTop() - 20;
             
 
             Paint fillpnt = new Paint();
             fillpnt.setColor(Color.RED);
             canvas.drawArc(rt, 0, 360, true, fillpnt);
 
            //여기서부터는 사용량 부채꼴을 위한 부분
             rt.left = getPaddingLeft();//왼쪽값
             rt.right = getWidth() - getPaddingRight();//오른쪽값
             rt.bottom = getHeight() - getPaddingTop();//아래쪽값
             rt.top = getPaddingTop();
        
             fillpnt.setColor(Color.BLACK);
      
             
             PreferenceData pref = Preference.getPref();
             
             float startAngle = 360 * pref.dstime / (24*3600*1000); //시작 각도 float startAngle = dayStartingTime;
             float sweepAngle = 360 * mPos / mMax;//부채꼴의 각도는 계속 변함 float sweepAngle = 360 * (현재누적시간 + mPos) / mMax;
      
             canvas.drawArc(rt, startAngle, sweepAngle, true, fillpnt);
             
             //시침 표시
             fillpnt.setColor(Color.BLUE);
             float timeAngle = 40; // 현재 시각
             
             canvas.drawArc(rt,  timeAngle, timeAngle + 1, true, fillpnt);
             
             
             //TODO 현재시간, 사용량 숫자로 시계 위에 표시
       }
 
       //이 메소드는 뷰의 크기를 결정하는 부분
       protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
             int Width = 200, Height = 200;
 
            
             switch (MeasureSpec.getMode(widthMeasureSpec)) {
             case MeasureSpec.AT_MOST://부모가 최대가 어떤값이라고 하면
                    //부모가 말한 값과 내가 원하는 값중 작은 값을 씀
                    Width = Math.min(MeasureSpec.getSize(widthMeasureSpec), Width);
                    break;
             case MeasureSpec.EXACTLY://부모가 정확이 어떤값을 쓰라고 하면
                    Width = MeasureSpec.getSize(widthMeasureSpec);//그냥 그값씀
                    break;
             }
 
             //위와 같음
             switch (MeasureSpec.getMode(heightMeasureSpec)) {
             case MeasureSpec.AT_MOST:
                    Height = Math.min(MeasureSpec.getSize(heightMeasureSpec), Height);
                    break;
             case MeasureSpec.EXACTLY:
                    Height = MeasureSpec.getSize(heightMeasureSpec);
                    break;
             }
 
             //뷰의 크기를 정해줌
             setMeasuredDimension(Width, Height);
       }
}
