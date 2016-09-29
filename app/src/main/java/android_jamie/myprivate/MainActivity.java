package android_jamie.myprivate;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private TelephonyManager tmgr;
    private AccountManager amgr;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView) findViewById(R.id.img);
        //android 6.0+
        if (Build.VERSION.SDK_INT >= 23) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        10);
            }

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},
                        10);
            }

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.GET_ACCOUNTS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        10);

            }
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        10);
            }
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        10);
            }
        }


        tmgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        String linenum = tmgr.getLine1Number();
        String imei = tmgr.getDeviceId();
        String imsi = tmgr.getSubscriberId();
        if(linenum != null) Log.d("jamie",linenum);
        Log.d("jamie",imei);
        Log.d("jamie",imsi);

        tmgr.listen(new MyPhoneState(), PhoneStateListener.LISTEN_CALL_STATE);

        amgr = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] accounts = amgr.getAccounts();
        for(Account account:accounts){
            String accountName = account.name;
            String accountType = account.type;
            Log.d("jamie",accountName+":"+accountType);
        }



    }
    private class MyPhoneState extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if(state == TelephonyManager.CALL_STATE_IDLE){
                Log.d("jamie","off");
            }else if(state == TelephonyManager.CALL_STATE_RINGING){
                Log.d("jamie",incomingNumber);
            }else if(state == TelephonyManager.CALL_STATE_OFFHOOK){
                Log.d("jamie","talk");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void test1(View v){
        ContentResolver contentresolver = getContentResolver();

        String name = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        String num = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Cursor c = contentresolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                ,new String[]{name,num},null,null,name);

        while (c.moveToNext()){
            String dname = c.getString(c.getColumnIndex(name));
            String dnum = c.getString(c.getColumnIndex(num));
            Log.d("jamie",dname+":"+dnum);
        }

    }
    public void test2(View v){

        ContentResolver contentresolver = getContentResolver();

        String name = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        String num = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri sim = Uri.parse("content://icc/adn");

        Cursor c = contentresolver.query(sim,null,null,null,name);
        int count = c.getCount();
        Log.d("jamie", "Count:" +count);

        while (c.moveToNext()){
            String dname = c.getString(c.getColumnIndex("name"));
            String dnum = c.getString(c.getColumnIndex("number"));
            Log.d("jamie",dname+":"+dnum);
        }
    }
    public void test3(View v){
        ContentResolver contentresolver = getContentResolver();
        Cursor c = contentresolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
        c.moveToNext();
        String photo = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
        //顯示照片路徑
        Log.d("jamie",photo);
        //顯示照片
        img.setImageBitmap(BitmapFactory.decodeFile(photo));

    }
}
