package com.fall.devs.chute;

/**
 * Created by Dev$ 2017.
 */
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.telephony.gsm.SmsManager;

import java.util.ArrayList;
import java.util.List;

public class Alarme extends AppCompatActivity implements View.OnClickListener, SensorEventListener{
    private float lastX, lastY, lastZ;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private TextView time;
    public String address;
    private ImageButton cancel;
    private CountDownTimer countDownTimer;
    final  private  int seconde=10;
    MediaPlayer a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarme);
        Context context = getApplicationContext();
        a = MediaPlayer.create(this, R.raw.a);
        cancel = (ImageButton) findViewById(R.id.cancel);
        time = (TextView) findViewById(R.id.time);
        time.setText(Integer.toString(seconde));
        cancel.setOnClickListener(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // fai! we dont have an accelerometer!
        }
    }
    //-------------------------------Accelerometre------------------------------------------//
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        int etq;
        // get the change of the x,y,z values of the accelerometer
        float x=event.values[0];
        float y=event.values[1];
        float z=event.values[2];
        deltaX = Math.abs(lastX - x);
        deltaY = Math.abs(lastY - y);
        deltaZ = Math.abs(lastZ - z);
        // if the change is below 2, it is just plain noise
        double val=Math.sqrt((deltaX*deltaX)+(deltaY*deltaY)+(deltaZ*deltaZ));
        if(val>=22)
            start(seconde);
        else etq=0;
    }
    //-------------------------------------------------------------------------------------//

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.cancel:
                cancel();
                return;
        }
    }


    private void start(int x) {
        time.setText(Integer.toString(x));
            countDownTimer=new CountDownTimer(x*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                String xx = recupNum();
                String msg=recupMessage();
                EnvoisSms(xx,msg);
                System.exit(0);
            }
        };
        countDownTimer.start();
        a.start();
    }

    private  String recupMessage(){
        BaseDeDonnees db=new BaseDeDonnees(this);
        Cursor c = db.getAllData();
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            buffer.append(c.getString(0)+" ");
            buffer.append(c.getString(1)+" ");
            buffer.append(c.getString(2)+" ");
            buffer.append(c.getString(5));
        }
        String[] commande=buffer.toString().split(" ");
        String Secu=commande[0];
        String Nom=commande[1];
        String Prenom=commande[2];
        String NumMedecin=commande[3];

        String Add="99 Avenue Jean bapiste Clément, 93430 Villetaneuse ";

        String res =String.format("%s,\nviens de faire une chute au: %s,\nNumero du medecin : %s \nNum Securité :%s",Nom.toUpperCase(),Add,NumMedecin,Secu);
        return res;
    }
    private  String recupNum(){
        BaseDeDonnees db=new BaseDeDonnees(this);
        Cursor c=db.getAllData();
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            buffer.append(c.getString(4));
        }
        String numeo=buffer.toString();
        return numeo;
    }

    private void cancel() {
        if(countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer= null;
            a.stop();
            System.exit(0);
        }
    }

    private void EnvoisSms(String numero, String Message){
            android.telephony.SmsManager sms= android.telephony.SmsManager.getDefault();
            sms.sendTextMessage(numero, null, Message, null, null);
            String rs=recupMessage();
            time.setText("Le message d'urgence est envoyé !!!");


    }

}