package android_jamie.mypainter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 2016/9/13.
 */
public class MyView extends View {
    private LinkedList<LinkedList<HashMap<String, Integer>>> lines;
    private Resources res;
    private boolean isInit;
    private int viewW, viewH;
    private Bitmap bmp;
    private Matrix matrix;
    private Timer timer;
    private int ballX, ballY, bW, bH, dx, dy;
    private GestureDetector gd;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.MAGENTA);
//        setOnClickListener(new MyClickListener());

        lines = new LinkedList<>();
        res = context.getResources();
        matrix = new Matrix();
        timer = new Timer();
        gd = new GestureDetector(context, new MyGDListener());
    }


    private class MyGDListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("jamie", "onDown");
            return true; //super.onDown(e);
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("jamie", "onFling:" + velocityX + "x" + velocityY);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

    }
    Timer getTimer(){return timer;}
    private void init(){
        viewW = getWidth(); viewH = getHeight();
        bW = viewW/5 ; bH = bW;

        bmp = BitmapFactory.decodeResource(res, R.drawable.b2);
        bmp = resizeBitmap(bmp, bW, bH);

        dx = dy = 10;

        timer.schedule(new RefreshView(), 0, 40);
        timer.schedule(new BallTask(), 1000, 100);

        isInit = true;
    }

    private Bitmap resizeBitmap(Bitmap src, float newW, float newH){
        matrix.reset();
        matrix.postScale(newW/src.getWidth(), newH/src.getHeight());
        bmp = Bitmap.createBitmap(src,0,0,src.getWidth(),src.getHeight(),matrix, false);
        return bmp;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isInit) init();

//        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.b2);
//        int bW = viewW/2 , bH = bW;
//        Matrix matrix = new Matrix();
//        matrix.postScale(bW/bmp.getWidth(), bH/bmp.getHeight());
//        bmp = Bitmap.createBitmap(bmp, 0,0,bmp.getWidth(),bmp.getHeight(),matrix,false);

        canvas.drawBitmap(bmp, ballX, ballY, null);

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStrokeWidth(4);
        for(LinkedList<HashMap<String,Integer>> line:lines) {
            for (int i = 1; i < line.size(); i++) {
                canvas.drawLine(line.get(i - 1).get("x"), line.get(i - 1).get("y"), line.get(i).get("x"), line.get(i).get("y"), p);
            }
        }
    }
    //    private class MyClickListener implements OnClickListener{
//        @Override
//        public void onClick(View view) {
//            Log.d("jamie","onClick");
//        }
//
//    }
    private class RefreshView extends TimerTask {
        @Override
        public void run() {
            //invalidate();
            postInvalidate();
        }
    }
    private class BallTask extends TimerTask{
        @Override
        public void run() {
            if (ballX<0 || ballX + bW > viewW) dx *= -1;
            if (ballY<0 || ballY + bH > viewH) dy *= -1;
            ballX += dx; ballY += dy;
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int ex = (int)event.getX(), ey =(int)event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN){
//            Log.d("jamie", "Down:"+ex+"x"+ey);//偵測到往下的移動,之後會跳到Move
            doTouchDown(ex,ey);
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
//            Log.d("jamie", "Move:"+ex+"x"+ey);
            doTouchMove(ex, ey);
        }

//        return super.onTouchEvent(event);         會觸發mainactivity的myView.setOnClickListener
//        return true;//true會抓到touch的路徑
        return gd.onTouchEvent(event);
    }

    //    @Override
//    public void setOnClickListener(OnClickListener l) {
//        super.setOnClickListener(l);
//    }
//    ==MyClickListener
    private void doTouchDown(int x, int y){
        LinkedList<HashMap<String, Integer>> line = new LinkedList<>();
        lines.add(line);
        addPoint(x,y);

    }
    private void doTouchMove(int x, int y){
        addPoint(x,y);
    }
    private void addPoint(int x, int y){
        HashMap<String, Integer> point = new HashMap<>();
        point.put("x", x); point.put("y", y);
        lines.getLast().add(point);
        invalidate();
    }
//    void clear(){
//        lines.remove();
//        invalidate();
//    }
}