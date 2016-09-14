package android_jamie.mypainter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private MyView myView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myView = (MyView)findViewById(R.id.myView);
//        myView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("jamie","Click from activity");
//            }
//        });

    }

    @Override
    public void finish() {
        myView.getTimer().cancel();
        super.finish();
    }
}
