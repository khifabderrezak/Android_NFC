package com.example.rezak.nfc_project;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;
import android.nfc.NdefMessage;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by rezak on 25/01/18.
 */

public class NfcTagManager {
    final String TAG_NFC = "sensor";
    Context mContext;


    public NfcTagManager(Context context){
        mContext = context;
    }
    public String computeNfcIntent(Intent intent){
        Parcelable[] rawMsgs = null;
        String action = intent.getAction();
        Log.v(TAG_NFC,"compute = "+action);
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        }else if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)){
            rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_TAG);
        }else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
            rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_TAG);
        }
        //for(String tech : TagFromIntent.getT)TagFromInten
        if(rawMsgs != null && rawMsgs.length > 0){
            return NdefComputeData(rawMsgs);
        }else{
            Toast.makeText(mContext,"NFC has not Tag ",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    protected String NdefComputeData(Parcelable[] rawMsgs){
        NdefMessage msgs[] = new NdefMessage[rawMsgs.length];
        for(int i=0;i<rawMsgs.length;i++){
            msgs[i] = (NdefMessage) rawMsgs[i];
            NdefRecord[] records = msgs[i].getRecords();
            for(NdefRecord ndefRecord: records){
                if(ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(),NdefRecord.RTD_TEXT)){
                    try {
                        //nfcInfo(ndefRecord);
                        return computeNdefRecord(ndefRecord);
                    }catch (UnsupportedEncodingException e ){
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
    private String computeNdefRecord(NdefRecord record)throws UnsupportedEncodingException{
        byte[] payload = record.getPayload();
        String textEncoding = ((payload[0] & 128) == 0)? "UTF-8":"UTF-16";
        int languageCodeLength = payload[0] & 0063;
        byte[] tagInfo = record.getType();

        if(tagInfo[0] == NdefRecord.RTD_TEXT[0]){
            Log.v(TAG_NFC,"Type = RTD_URI");
        }else if (tagInfo[0] == NdefRecord.RTD_SMART_POSTER[0] && tagInfo[1] == NdefRecord.RTD_SMART_POSTER[1]){
            Log.v(TAG_NFC,"Type = RTD_SMART_POSTER");
        }
        //Log.v(TAG_NFC,"MimeType = "+record.toMimeType());
        byte[] id= record.getId();
        if(id.length>0){
            for(byte b : id){
                Log.i(TAG_NFC,"ID = "+String.format("0x%20x",b));
            }
        }else{
            Log.v(TAG_NFC,"Id = Not Id defined");
        }
        Log.v(TAG_NFC,"INF"+ record.getTnf());
        int len = payload.length - languageCodeLength -1;
        Log.v(TAG_NFC,"TEXT LENGTH"+len);
        String s = new String(payload,1,languageCodeLength,"US-ASCII");
        Log.v(TAG_NFC,s);
        return s;
    }

}
