package chunying.mypainter_v2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private MyView myView;
    private Button clear, undo, redo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myView = (MyView) findViewById(R.id.myView);
        clear = (Button)findViewById(R.id.clear);
        undo = (Button)findViewById(R.id.undo);
        redo = (Button)findViewById(R.id.redo);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               myView.doClear();
            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.doUndo();
            }
        });
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.doRedo();
            }
        });
    }

    @Override
    public void finish() {
        myView.getTime().cancel();
        super.finish();
    }
}
