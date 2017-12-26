package com.newapplol;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.newapplol.adapter.MyAdapter;
import com.newapplol.request.ApiRequest;

import java.util.ArrayList;
import java.util.List;

public class HistoriqueActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String playerName;
    private Long playerId;
    private RecyclerView recyclerViewMatchHistory;
    private MyAdapter myAdapter;
    private ApiRequest request;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        queue = MySingleton.getInstance(this).getRequestQueue();
        request = new ApiRequest(queue,this);

        String json = request.getJsonFile(this,"match-example.json");
        Log.d("APP : ",json);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras.getString("NAME") != null && extras.getLong("ID") > 0){

            playerName = extras.getString("NAME");
            playerId = extras.getLong("ID");
            setTitle(playerName);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //RecyclerView
        /*
        List<String> data =  new ArrayList<>();
        data.add("12/9/13");


        recyclerViewMatchHistory = (RecyclerView) findViewById(R.id.rv_match);
        recyclerViewMatchHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMatchHistory.setHasFixedSize(true);
        myAdapter = new MyAdapter(this,data);
        recyclerViewMatchHistory.setAdapter(myAdapter);
        */

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

        switch (id){
            case R.id.nav_search:
                Intent intSea = new Intent(HistoriqueActivity.this,SearchActivity.class);
                startActivity(intSea);
                break;
            case R.id.nav_patch:
                Intent intPat = new Intent(HistoriqueActivity.this,PatchActivity.class);
                startActivity(intPat);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
