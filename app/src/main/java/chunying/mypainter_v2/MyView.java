package chunying.mypainter_v2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.BoringLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Juyn-Ying on 15/9/2016.
 */
public class MyView extends View {
    private LinkedList<LinkedList<HashMap<String, Float>>> lines, recycle;
    private Resources res;
    private Matrix matrix;
    private Bitmap bmpbg, bmppic;
    private Timer timer;
    private float picX, picY, dx,dy,picH,picW;
    private int viewH,viewW;
    private boolean isInit;
    private GestureDetector gd;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        lines = new LinkedList<>();
        recycle = new LinkedList<>();
        res = context.getResources();
        matrix = new Matrix();
        timer = new Timer();
        gd = new GestureDetector(new MygdListener());

    }
    private class MygdListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
//            Log.i("jamie","onDown");
            return true;//super.onDown(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
//            Log.i("jamie","onLongPress");
            super.onLongPress(e);
            Toast.makeText(getContext(),"Let me go",Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
        viewH = getHeight(); viewW = getWidth();

        bmpbg = BitmapFactory.decodeResource(res, R.drawable.cb);
        bmpbg = resizeBitmap(bmpbg,viewW,viewH);

        picH = viewH/8; picW = picH;
        bmppic = BitmapFactory.decodeResource(res,R.drawable.yolk);
        bmppic = resizeBitmap(bmppic,picW,picH);


        dx=dy=20;
        timer.schedule(new refreshTask(),0,40);
        timer.schedule(new picTask(),0,100);

        isInit = true;
    }
    Timer getTime(){return timer;}
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isInit)init();

        canvas.drawBitmap(bmpbg,0,0,null);
        canvas.drawBitmap(bmppic,picX,picY,null);
        //背景圖片
//        viewH = getHeight(); viewW = getWidth();
//
//        bmpbg = BitmapFactory.decodeResource(res, R.drawable.cb);
//        bmpbg = resizeBitmap(bmpbg,viewW,viewH);
//        canvas.drawBitmap(bmpbg,0,0,null);
//
//        picH = viewH/5; picW = picH;
//        bmppic = BitmapFactory.decodeResource(res,R.drawable.yolk);
//        bmppic = resizeBitmap(bmppic,picW,picH);
//        canvas.drawBitmap(bmppic,picX,picY,null);
//
//        dx=dy=10;
//        timer.schedule(new refreshTask(),0,40);
//        timer.schedule(new picTask(),1000,100);

        //自行畫線區
        Paint p = new Paint();
        p.setColor(Color.GRAY);
        p.setStrokeWidth(2);
        for(LinkedList<HashMap<String, Float>> line:lines) {
            for (int i = 1; i < line.size(); i++) {
                canvas.drawLine(line.get(i - 1).get("x"), line.get(i - 1).get("y"), line.get(i).get("x"), line.get(i).get("y"), p);
            }
        }
    }

    private Bitmap resizeBitmap(Bitmap src, float W, float H){
        matrix.reset();
        matrix.postScale(W/src.getWidth(),H/src.getHeight());
        src = Bitmap.createBitmap(src,0,0,src.getWidth(),src.getHeight(),matrix,false);
        return src;
    }
    private class refreshTask extends TimerTask{
        @Override
        public void run() {
            postInvalidate();
        }
    }
    private class picTask extends TimerTask {
        @Override
        public void run() {

            if( picX<0 || picX+picW>viewW) dx *= -1;
            if( picY<0 || picY+picH>viewH) dy *= -1;
            picX += dx; picY += dy;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float ex = event.getX(), ey = event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            doDown(ex,ey);
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            doMove(ex,ey);
        }
//        return true;//super.onTouchEvent(event);
        return gd.onTouchEvent(event);
    }
    private void doDown(float ex, float ey) {
        LinkedList<HashMap<String, Float>> line = new LinkedList<>();
        lines.add(line);
        invalidate();
    }
    private void doMove(float ex, float ey) {
        addPoint(ex,ey);
    }
    private void addPoint(float ex, float ey){
        HashMap<String,Float> point = new HashMap<>();
        point.put("x", ex); point.put("y", ey);
        lines.getLast().add(point);
        invalidate();
    }

    void doClear(){
        lines.clear();
        invalidate();
    }
    void doUndo(){
        if(lines.size()>0) {
            recycle.add(lines.removeLast());
            invalidate();
        }
    }
    void doRedo(){
        if(recycle.size()>0) {
            lines.add(recycle.removeLast());
            invalidate();
        }
    }
}
