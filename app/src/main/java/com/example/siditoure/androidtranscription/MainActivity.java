package com.example.siditoure.androidtranscription;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    static User user = new User();
    static MyDatabase mydatabase;
    public Button connecter;
    public EditText login;
    public EditText pass;
    public TextView Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connecter = (Button) this.findViewById(R.id.button2);
        login = (EditText) this.findViewById(R.id.editText);
        pass = (EditText) this.findViewById(R.id.editText2);
        Text = (TextView) this.findViewById(R.id.textView2);
        mydatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "user_bd").allowMainThreadQueries().build();

        connecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Myclick(v);
            }
        });
        Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Myclick(v);

            }
        });



    }

        private void Myclick (View v){
            user.login = login.getText().toString();
            user.pass = pass.getText().toString();
            if (!(login.getText().toString()).matches("")) {
                List<User> usr = MainActivity.mydatabase.mydao().getUser(login.getText().toString());
                switch (v.getId()) {
                    case R.id.textView2:
                        if (usr.size() != 0) {
                            Toast.makeText(getApplicationContext(), "Impossible de créer le compte: login existant",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            MainActivity.mydatabase.mydao().adduser(user);
                            Toast.makeText(getApplicationContext(), "Parfait le compte est créé",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        }
                    case R.id.button2:
                        if (usr.size() != 0) {
                            if (usr.get(0).pass.contentEquals(pass.getText().toString())) {
                                Intent intent = new Intent(this, Maintranscription.class);
                                startActivity(intent);
                                break;
                            } else {
                                Toast.makeText(getApplicationContext(), "Mot de passe incorrect",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Compte non existant", Toast.LENGTH_SHORT).show();
                            break;
                        }
                }
            }
        }
    }


