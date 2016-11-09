package com.example.admin.kyodai;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import java.util.Random;
/**
 * Created by admin on 10/04/2016.
 */
public class kyodaiView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private kyodai active;

    // déclaration des papillons
    private Bitmap pap1;
    private Bitmap pap2;
    private Bitmap pap3;
    private Bitmap pap4;
    private Bitmap pap5;
    private Bitmap pap6;
    private Bitmap pap7;
    private Bitmap pap8;
    private Bitmap pap9;
    private Bitmap pap10;
    private Bitmap vide;


    // Declaration des objets Ressources et Context permettant d'acc�der aux ressources de notre application et de les charger
    private Resources mRes;
    private Context mContext;


    // tableau modelisant la carte du jeu
    int[][] carte;
    int j1;
    int i1;

    // ancres pour pouvoir centrer la carte du jeu
    int carteTopAnchor;                   // coordonn�es en Y du point d'ancrage de notre carte
    int carteLeftAnchor;

    int score = 0;
    boolean clic = false; //teste la touche sur l'interface
    boolean xx;


    // taille de la carte
    static final int carteWidth = 8;
    static final int carteHeight = 8;
    static final int carteTileSize = 40;


    // constante modelisant les differentes types de cases

    static final int CST_capture10 = 10;
    static final int CST_capture9 = 9;
    static final int CST_capture8 = 8;
    static final int CST_capture7 = 7;
    static final int CST_capture6 = 6;
    static final int CST_capture5 = 5;
    static final int CST_capture4 = 4;
    static final int CST_capture3 = 3;
    static final int CST_capture2 = 2;
    static final int CST_capture1 = 1;
    static final int CST_cap = 0;





    // tableau de reference du terrain
    int[][] ref1 = {
            {CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap },
            {CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap},
            {CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap},
            {CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap},
            {CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap},
            {CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap},
            {CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap },
            {CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap, CST_cap},

    };


    int[][] refdiamants = {
            {2, 3},
            {2, 6},
            {6, 3},
            {6, 6}
    };

    // position de reference du joueur
    int refxPlayer = 4;
    int refyPlayer = 1;


    // position courante du joueur
    int xPlayer = 4;
    int yPlayer = 1;

    // thread utiliser pour animer les zones ou les cases
    private boolean in = true;
    private Thread cv_thread;
    SurfaceHolder holder;

    Paint paintcarte;

    public kyodaiView(Context context, AttributeSet attrs) {
        super(context, attrs);

        active = new kyodai();
        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
        holder = getHolder();
        holder.addCallback(this);

        // chargement des images
        mContext = context;
        mRes = mContext.getResources();
        vide = BitmapFactory.decodeResource(mRes, R.drawable.cap);
        pap1 = BitmapFactory.decodeResource(mRes, R.drawable.capture1);
        pap2 = BitmapFactory.decodeResource(mRes, R.drawable.capture2);
        pap3 = BitmapFactory.decodeResource(mRes, R.drawable.capture3);
        pap4 = BitmapFactory.decodeResource(mRes, R.drawable.capture4);
        pap5 = BitmapFactory.decodeResource(mRes, R.drawable.capture5);
        pap6 = BitmapFactory.decodeResource(mRes, R.drawable.capture6);
        pap7 = BitmapFactory.decodeResource(mRes, R.drawable.capture7);
        pap8 = BitmapFactory.decodeResource(mRes, R.drawable.capture8);
        pap9 = BitmapFactory.decodeResource(mRes, R.drawable.capture9);
        pap10 = BitmapFactory.decodeResource(mRes, R.drawable.capture10);

        // initialisation des paramétres du jeu
        initparameters();

        // creation du thread
        cv_thread = new Thread(this);
        // prise de focus pour gestion des touches
        setFocusable(true);

    }



    // chargement de notre matrice de départ
    private void loadlevel() {
        Random generateur = new Random();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int randomint = 0 + generateur.nextInt(7 - 0);
                carte[i][j] = randomint;

            }
        }

    }


    public void initparameters() {

        xx = true;
        if (score == 0) {
            carte = new int[carteHeight][carteWidth];
            loadlevel();

        }
        carteTopAnchor = (getHeight() - carteHeight * carteTileSize) / 2;
        carteLeftAnchor = (getWidth() - carteWidth * carteTileSize) / 2;
        xPlayer = refxPlayer;
        yPlayer = refyPlayer;

        if ((cv_thread != null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
    }


    // dessine une alerte
    private void showAbout() {
        final boolean[] reponse = new boolean[1];
        AlertDialog.Builder about = new AlertDialog.Builder(mContext);
        about.setTitle("Oops");
        TextView l_viewabout = new TextView(mContext);
        l_viewabout.setMovementMethod(LinkMovementMethod.getInstance());
        about.setView(l_viewabout);
        about.setPositiveButton("OUI", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                score = 0;
                initparameters();

            }

        });
        about.setNegativeButton("NON", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                active.finish();


            }

        });
        about.show();
    }



    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("-> FCT <-", "surfaceChanged " + width + " - " + height);
        initparameters();
    }



    // dessin de la carte du jeu
    private void paintcarte(Canvas canvas) {
        for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < carteWidth; j++) {
                switch (carte[i][j]) {
                    case CST_cap:
                        canvas.drawBitmap(vide, (j * carteTileSize) , (i * carteTileSize) , null);
                        break;
                    case CST_capture1:
                        canvas.drawBitmap(pap1, (j * carteTileSize) , (i * carteTileSize) , null);
                        break;
                    case CST_capture2:
                        canvas.drawBitmap(pap2, (j * carteTileSize) , (i * carteTileSize), null);
                        break;
                    case CST_capture3:
                        canvas.drawBitmap(pap3, (j * carteTileSize) , (i * carteTileSize) , null);
                        break;
                    case CST_capture4:
                        canvas.drawBitmap(pap4, (j * carteTileSize) , (i * carteTileSize), null);
                        break;
                    case CST_capture5:
                        canvas.drawBitmap(pap5, (j * carteTileSize) , (i * carteTileSize) , null);
                        break;
                    case CST_capture6:
                        canvas.drawBitmap(pap6, (j * carteTileSize), (i * carteTileSize) , null);
                        break;
                    case CST_capture7:
                        canvas.drawBitmap(pap7, (j * carteTileSize) , (i * carteTileSize) , null);
                        break;
                    case CST_capture8:
                        canvas.drawBitmap(pap8, (j * carteTileSize) , (i * carteTileSize) , null);
                        break;
                    case CST_capture9:
                        canvas.drawBitmap(pap9, (j * carteTileSize) , (i * carteTileSize) , null);
                        break;
                    case CST_capture10:
                        canvas.drawBitmap(pap10, (j * carteTileSize) , (i * carteTileSize), null);
                        break;

                }
            }
        }
    }



    // dessin du jeu
    private void nDraw(Canvas canvas) {

        canvas.drawRGB(48, 48, 48);

        paintcarte(canvas);

    }
    // callback sur le cycle de vie de la surfaceview
    public void surfaceCreated(SurfaceHolder arg0) {
        Log.i("-> FCT <-", "surfaceCreated");
    }


    public void surfaceDestroyed(SurfaceHolder arg0) {

        Log.i("-> FCT <-", "surfaceDestroyed");
    }


     // run (run du thread cr��)on endort le thread, on modifie le compteur d'animation, on prend la main pour dessiner et on dessine puis on lib�re le canvas

    public void run() {
        Canvas c = null;
        while (in) {
            try {
                cv_thread.sleep(40);

                try {
                    c = holder.lockCanvas(null);
                    nDraw(c);
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            } catch (Exception e) {
                Log.e("-> RUN <-", "PB DANS RUN");
            }
        }
    }

    // fonction permettant de recuperer les evenements tactiles

    public boolean onTouchEvent(MotionEvent event) {
        final int y = (int) event.getY();
        final int x = (int) event.getX();


        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.i("-> FCT <-", "onTouchEvent: ");

                clic = true;


        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {


        }


        if (event.getAction() == MotionEvent.ACTION_UP) {
            if ((clic)/*&& (cpt==1)*/) {

                int i = (int) (x / 40);
                int j = (int) (y / 40);
                j1 = j;
                i1 = i;

                // on supprime les mm images sue l'extrémité de la matrice
                if ((j1 == 0) || (j1 == 7 )) {
                    for (i = 0; i < 8; i++) {
                        if ((carte[j1][i1] == carte[j1][i]) &&(i!= i1 )) {
                            carte[j1][i1] = 0;
                            carte[j1][i] = 0;
                        }
                    }
                }
                else {
                    if ((i1 == 0) || (i1 == 7)) {
                        for (j = 0; j < 8; j++) {
                            if ((carte[j1][i1] == carte[j][i1]) && (j!=j1)) {
                                carte[j1][i1] = 0;
                                carte[j][i1] = 0;
                            }
                        }
                    }
                }


                // on supprime deux images contigues sur la mm colone
                for (i = 0; i < 8; i++) {
                    if ((carte[j1][i1] == carte[j1][i]) && ((i == i1 + 1) || (i1 == i + 1))) {
                        carte[j1][i1] = 0;
                        carte[j1][i] = 0;
                    }

                }
               // on supprime deux images contigues sur la mm ligne
                for (j = 0; j < 8; j++) {
                    if ((carte[j1][i1] == carte[j][i1]) && ((j == j1 + 1) || (j1 == j + 1))) {
                        carte[j1][i1] = 0;
                        carte[j][i1] = 0;
                    }
                }
              //on supprime les deux papillons qui sont sur la méme ligne mais séparer avec le vide
                for (j = 0; j < 8; j++) {
                    if ((carte[j1][i1 + 1] == 0) && (carte[j1][i1] == carte[j][i1 + 2])) {
                        carte[j1][i1] = 0;
                        carte[j][i1 + 2] = 0;
                    }
                }



                //on supprime les deux papillons qui sont sur la méme colone et séparer par un vide
                for (i = 0; i < 8; i++) {
                    if ((carte[j1 + 1][i1] == 0) && (carte[j1][i1] == carte[j1 + 2][i])) {
                        carte[j1][i1] = 0;
                        carte[j1 + 2][i] = 0;
                    }
                }

                // on supprime les deux cases qui sont en diagonale mais avec un vide entre eux dans tout les sens
                if ((carte[j1][i1 + 1] == 0) && (carte[j1][i1] == carte[j1 + 1][i1 + 1])) {
                    carte[j1][i1] = 0;
                    carte[j1 + 1][i1 + 1] = 0;
                } else {
                    if ((i1 >= 1) && (carte[j1][i1 - 1] == 0) && (carte[j1][i1] == carte[j1 + 1][i1-1])) {
                        carte[j1][i1] = 0;
                        carte[j1 + 1][i1-1] = 0;
                    } else {
                        if ((carte[j1 + 1][i1] == 0) && (carte[j1][i1] == carte[j1 + 1][i1 + 1])) {
                            carte[j1][i1] = 0;
                            carte[j1 + 1][i1 + 1] = 0;
                        } else {
                            if ((i1 >= 1) && (carte[j1 + 1][i1] == 0) && (carte[j1][i1] == carte[j1 + 1][i1 - 1])) {
                                carte[j1][i1] = 0;
                                carte[j1 + 1][i1 - 1] = 0;
                            }
                        }
                    }
                }

            }



        }

        return true;


    }
}
