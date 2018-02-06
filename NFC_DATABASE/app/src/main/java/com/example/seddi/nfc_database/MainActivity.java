package com.example.seddi.nfc_database;
        import android.app.PendingIntent;
        import android.content.Intent;
        import android.content.IntentFilter;

        import android.nfc.NfcAdapter;
        import android.nfc.Tag;
        import android.nfc.tech.Ndef;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Listener{

    public static final String TAG = MainActivity.class.getSimpleName();

    TextView  mTvMessage;
    private NFC_Read mNfcReadFragment;
    DataBaseNFC db;

    private boolean isDialogDisplayed = false ;

    private NfcAdapter mNfcAdapter;
    ListView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNFC();
        db=new DataBaseNFC(this,null);
       // mTvMessage=findViewById(R.id.mTvMessage);

        this.show();
    }
    public void show(){
        String[] data=db.showItems().split("/");

        l=(ListView)findViewById(R.id.list_item);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.single_row,R.id.textView,data);
        l.setAdapter(adapter);
    }


    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    public void onDialogDisplayed() {

        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {

        isDialogDisplayed = false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);
            this.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    public String getSerialNumber(Tag tag){
        String serial_nbr = new String();

        for(int i = 0; i < tag.getId().length; i++){
            String x = Integer.toHexString(((int) tag.getId()[i] & 0xff));
            if(x.length() == 1){
                x = '0' + x;
            }
            serial_nbr += x + ' ';
        }

        return serial_nbr;
    }

    @Override
    protected void onNewIntent(Intent intent) {

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        //Base de donées

       if (tag != null) {//si on a reussi a lire la carte:
           Ndef ndef = Ndef.get(tag);
            if(!db.showItemByNFC(getSerialNumber(tag).substring(0,(getSerialNumber(tag).length()-1))).equals("")){
                Toast.makeText(this,getSerialNumber(tag).substring(0,(getSerialNumber(tag).length()-1)),Toast.LENGTH_SHORT).show();

               // mTvMessage.setText(db.showItemByNFC(getSerialNumber(tag).substring(0,(getSerialNumber(tag).length()-1))));
               // mTvMessage.setTextSize(30);
              //  mTvMessage.setText(db.showItems());

            }else{
                Intent i=new Intent(this,AjouterEtudiant.class);
                i.putExtra("Id",getSerialNumber(tag).substring(0,(getSerialNumber(tag).length()-1)));
                startActivity(i);
               // db.insertItem("01255420",123L,"ks,dsq","kjdcj");
            }
            if (isDialogDisplayed) {
                mNfcReadFragment = (NFC_Read) getFragmentManager().findFragmentByTag(NFC_Read.TAG);
                mNfcReadFragment.onNfcDetected(ndef);
            }
        }
    }

}