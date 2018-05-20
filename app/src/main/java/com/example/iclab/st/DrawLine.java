package com.example.iclab.st;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class DrawLine extends View
{

    PorterDuffXfermode clear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    private Paint paint = null;     // 그리기 조건(색상, 두께 등)
    private Bitmap bitmap = null;   // 도화지
    private Canvas canvas = null;   // 붓
    private Path path;  // 경로
    private float   oldX;   // 손가락 X 좌표
    private float   oldY;   // 손가락 Y 좌표

    public DrawLine(Context context, Rect rect)
    {
        this(context);

        //그리기를 할 bitmap 객체 생성
        bitmap = Bitmap.createBitmap(rect.width(), rect.height(),
                Bitmap.Config.ARGB_8888);

        //그리기 bitmap에서 canvas를 받아옴
        canvas = new Canvas(bitmap);

        //경로 초기화
        path = new Path();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        //앱 종료시 초기화
        if(bitmap!= null) bitmap.recycle();
        bitmap = null;

        super.onDetachedFromWindow();
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        if(bitmap != null)
        {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
            {
                //최초 마우스를 눌렀을때(손가락을 댓을때) 경로를 초기화
                path.reset();

                //현재경로로 경로를 이동
                path.moveTo(x, y);

                //포인터 위치값을 기억
                oldX = x;
                oldY = y;

                //계속 이벤트 처리를 함
                return true;
            }
            case MotionEvent.ACTION_MOVE:
            {
                //포인트가 이동될때 마다 두 좌표(이전에눌렀던 좌표와 현재 이동한 좌료)간의 간격을 구함
                float dx = Math.abs(x - oldX);
                float dy = Math.abs(y - oldY);

                //두 좌표간의 간격이 4px이상이면 (가로든, 세로든) 선을 그림
                if (dx >= 4 || dy >= 4)
                {
                    //path에 좌표의 이동 상황을 넣음(이전좌표 -> 신규좌표)
                    path.quadTo(oldX, oldY, x, y);


                    //포인터의 마지막 위치값을 기억
                    oldX = x;
                    oldY = y;

                    //그리기 bitmap에 path를 따라서 선을 그림
                    canvas.drawPath(path, paint);
                }

                //화면을 갱신시킴(onDraw메소드 호출)
                invalidate();

                //계속 이벤트 처리를 함
                return true;
            }
        }

        //더이상 이벤트 처리를 하지 않음
        return false;
    }

    // 펜 색상 및 두께 지정
    public void setLineColor(int color)
    {
        paint = new Paint();
        paint.setColor(color);

        paint.setAlpha(255);
        paint.setDither(true);
        paint.setStrokeWidth(20);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);

        // 지우개 모드이면
        if(color == Color.WHITE) {
            paint.setXfermode(clear);
            paint.setStrokeWidth(100);
        }
    }

    public DrawLine(Context context)
    {
        super(context);
    }
}
