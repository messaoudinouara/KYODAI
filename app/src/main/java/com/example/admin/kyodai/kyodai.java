package com.example.admin.kyodai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toolbar;

/**
 * Created by admin on 10/04/2016.
 */
public class kyodai extends Activity {

   private kyodaiView mkyodaiView;
    public static boolean fin = false;



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // initialise notre activity avec le constructeur parent
        super.onCreate(savedInstanceState);
        // charge le fichier main.xml comme vue de l'activit�
        setContentView(R.layout.main);
       if (fin) {

            Intent secondeActivite = new Intent(kyodai.this, kyodaiView.class);

            startActivity(secondeActivite);
        }


        // recuperation de la vue une voie cree � partir de son id
        mkyodaiView = (kyodaiView)findViewById(R.id.kyodai_View);
        // rend visible la vue
       mkyodaiView.setVisibility(View.VISIBLE);


    }

    public void onResume(Bundle savedInstanceState) {
        super.onResume();

    }

    protected void onStop() {
        super.onStop();
    }

    protected void onStart() {

        super.onStart();
        mkyodaiView.setVisibility(View.VISIBLE);
    }
}