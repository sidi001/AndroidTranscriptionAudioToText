package com.example.siditoure.androidtranscription;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Apropos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apropos);
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
            System.exit(0);
            break;
    }
        return super.onOptionsItemSelected(item); }
}
