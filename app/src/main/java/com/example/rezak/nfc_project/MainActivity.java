package com.example.rezak.nfc_project;

import android.app.PendingIntent;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener{

    public static final String TAG = MainActivity.class.getSimpleName();


    NfcTagManager mNfcIntentManager;

    TextView textView_content;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_content = findViewById(R.id.textView_content);
        mNfcAdapter=NfcAdapter.getDefaultAdapter(this);
        mNfcIntentManager=new NfcTagManager(this.getApplicationContext());


    }






    @Override
    protected void onResume() {
        super.onResume();
        PendingIntent mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNfcAdapter.enableForegroundDispatch(this,mNfcPendingIntent,null,null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }


    @Override
    void onAccuracyChanged (Sensor sensor,int accuracy){
        return;
    }


    @Override
    public void onNewIntent(Intent intent){
        String action=intent.getAction();
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)||
                NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            Log.v(TAG,"***"+intent.getAction());
            mNfcIntentManager.computeNfcIntent(intent);
        }
    }
}