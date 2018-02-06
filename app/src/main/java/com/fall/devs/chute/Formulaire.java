package com.fall.devs.chute;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fall.devs.chute.BaseDeDonnees;


public class Formulaire extends ActionBarActivity{

    BaseDeDonnees myDb;
    EditText editnom,editprenom,editdatenaissance,editnumsec,edittel,editnummed;
    Button idbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDb = new BaseDeDonnees(this);
        Cursor res= myDb.getAllData();
        if(res.getCount()==0){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            }

            setContentView(R.layout.formulaire);
            editnom= (EditText)findViewById(R.id.editnom);
            editprenom= (EditText)findViewById(R.id.editprenom);
            editdatenaissance=(EditText)findViewById(R.id.editdatenaissance);
            editnumsec= (EditText)findViewById(R.id.editnumsec);
            edittel= (EditText)findViewById(R.id.edittel);
            editnummed= (EditText)findViewById(R.id.editnummed);
            idbutton= (Button)findViewById(R.id.save);
            AddData();
        }
        else{
            passer();

        }

    }

    public  void AddData() {
        idbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(editnumsec.getText().toString(),
                                editprenom.getText().toString(),
                                editdatenaissance.getText().toString(),
                                editnom.getText().toString(),
                                edittel.getText().toString(),
                                editnummed.getText().toString());
                        if(isInserted == true) {
                            Toast.makeText(Formulaire.this, "Vos données sont insérées !!", Toast.LENGTH_LONG).show();
                            passer();
                        }
                        else
                            Toast.makeText(Formulaire.this,"Utilisateur déja existant !!!",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    public  void passer(){
        View v = new View(this);
        Intent intent=new Intent(v.getContext(),Alarme.class);
        startActivityForResult(intent,0);

    }
}
