package com.example.admin.kyodai;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

public class Main_kyodai extends Activity {

    private Button jouer = null;
    private Button Apropos=null;
    private Button quitter=null;
    private Button Aide=null;
    private Button Score = null;
    private RadioButton actson;
    MediaPlayer Son;
    boolean sel;
    boolean clic;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_kyodai);

        jouer = (Button) findViewById(R.id.button);
        quitter = (Button) findViewById(R.id.button4);
        Apropos = (Button) findViewById(R.id.button3);
        actson = (RadioButton) findViewById(R.id.radiobutton);
        Aide = (Button) findViewById(R.id.button5);
        Score = (Button) findViewById(R.id.button2);
        Son = MediaPlayer.create(getBaseContext(), R.raw.son);
        clic = false;

        actson.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (actson.isChecked()) {
                    sel = true;
                    Son.start();

                } else {
                    Son.pause();
                    sel = false;
                }
            }
        });


        jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                clic = true;
                Intent Activite2 = new Intent(Main_kyodai.this, kyodai.class);
                startActivity(Activite2);

            }

            ;


        });


        Apropos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                clic = true;
                Intent Activite2 = new Intent(Main_kyodai.this, aproposactivity.class);
                startActivity(Activite2);

            }

            ;


        });


        quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Son.pause();
                finish();
            }
        });


        Aide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                clic = true;
                Intent Activite2 = new Intent(Main_kyodai.this, aideactivity.class);
                startActivity(Activite2);

            }

            ;


        });






    }



            public void onResume(Bundle savedInstanceState) {
                super.onResume();

            }

            protected void onStop() {
                super.onStop();
                if (!clic)
                    Son.pause();

            }


            protected void onStart() {

                super.onStart();

                clic = false;
                if (sel)
                    Son.start();
            }
        }




