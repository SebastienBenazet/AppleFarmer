package com.beola.applefarmer.applefarmer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton appleButton;
    private Handler handler = new Handler();
    public final static String PREFS_NAME = "AppleFarmerPrefs";

    Player player = new Player();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // On restaures les données de l'utilisateur
        // On calcule la durée pendant laquelle le joueur a été absent
        SharedPreferences appleNumberPrefs = getSharedPreferences(PREFS_NAME, 0);
        final Long differenceDate = System.currentTimeMillis()/1000 - appleNumberPrefs.getLong("leavingDate", 0);
        player.setApplePerSecond(appleNumberPrefs.getFloat("applePerSecond", 0.0f));
        player.setAppleNumber(appleNumberPrefs.getFloat("appleNumber", 0.0f) + (player.getApplePerSecond() * 10) * differenceDate);
        player.setClickNumber(appleNumberPrefs.getInt("clickNumber", 0));
        player.setClickValue(appleNumberPrefs.getFloat("clickValue", 1.0f));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, player.getClickNumber() + " clics effectués\n" +
                        player.getApplePerSecond()*10 + " pommes par seconde", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                player.setApplePerSecond(0.1f);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //On affiche le nombre de pommes
        final TextView appleTextViewNumber = (TextView) findViewById(R.id.apple_number);

        //Permet de lancer l'ajout des pommes par secondes
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                player.setAppleNumber(player.getAppleNumber() + player.getApplePerSecond());
                                appleTextViewNumber.setText(String.format("%.1f", player.getAppleNumber()) + " pommes");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        appleButton = (ImageButton) findViewById(R.id.apple_button);
        appleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                player.setAppleNumber(player.getAppleNumber() + player.getClickValue());
                player.setClickNumber(player.getClickNumber() + 1);
                appleTextViewNumber.setText(String.format("%.1f", player.getAppleNumber()) + " pommes");
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_upgrade) {
            Intent intent = new Intent(MainActivity.this, UpgradeMain.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(), "Prochainement !", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(getApplicationContext(), "Prochainement !", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this, ManageMain.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Toast.makeText(getApplicationContext(), "Prochainement !", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_send) {
            SendMain s = new SendMain(this);
            String phoneNumber = "+33649925450";
            s.sendMain(phoneNumber, player.getAppleNumber(), player.getClickNumber());

            //sendSMSMessage(player.getAppleNumber(), player.getClickNumber());
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutMain.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences appleNumberPrefs = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = appleNumberPrefs.edit();
        editor.putFloat("appleNumber", player.getAppleNumber());
        editor.putInt("clickNumber", player.getClickNumber());
        editor.putFloat("applePerSecond", player.getApplePerSecond());
        editor.putFloat("clickValue", player.getClickValue());
        // On récupère la date en secondes
        editor.putLong("leavingDate", System.currentTimeMillis()/1000);

        // Commit the edits
        editor.commit();
    }
}
