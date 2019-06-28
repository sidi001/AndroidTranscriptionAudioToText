package com.example.siditoure.androidtranscription;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Maintranscription extends AppCompatActivity {

    TextView txtOutput;
    Button bouton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintranscription);

        txtOutput = (TextView) findViewById(R.id.text);
        bouton = (Button) findViewById(R.id.button);

        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "click",
                        Toast.LENGTH_SHORT).show();
                startSpeechToText();
            }
        });

    }

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr");  //"fr" Locale.getDefault()
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,
                1);
        try {
            this.startActivityForResult(intent, 666);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Callback for speech recognition activity
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 666: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    String a="a";
//                    for(int i = 0; i < result.size(); i++){
//                        a=a+result.get(i);
//                    }
                    String text = result.get(0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            !=PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
                    }
                    saveTextAsFile("vocalFile",text);

                    txtOutput.setText(text);
                }
                break;
            }
        }
    }

    private  void saveTextAsFile(String filename,String content){
        String fileName=filename+".txt";

        //create file
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),fileName);

        //write to file

        try {
            FileOutputStream fos= new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
            Toast.makeText(this, "enregister!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "fichier introuvable !", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }catch (IOException e){
            Toast.makeText(this, "error enregistrement !", Toast.LENGTH_SHORT).show();

        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"Permission not granted",Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){ // TODO Auto-generated method stub
         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu); }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
// On peut créer le menu via le code
        case R.id.item1: Toast.makeText(this, "à propos", Toast.LENGTH_LONG).show();
            Intent intent0 = new Intent(this, Apropos.class);
            startActivity(intent0);
            break;
        case R.id.item2: Toast.makeText(this, "transcrire audio", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(this, Maintranscription.class);
            startActivity(intent1);
            break;
        case R.id.item3: Toast.makeText(this, "voir fichier.txt", Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent(this, ExplorateurActivity.class);
            startActivity(intent2);
            break;
        case R.id.item4: Toast.makeText(this, "vous avez quitter l'application", Toast.LENGTH_LONG).show();
            finish();
            moveTaskToBack(true);
            break;
    }
        return super.onOptionsItemSelected(item); }
}
