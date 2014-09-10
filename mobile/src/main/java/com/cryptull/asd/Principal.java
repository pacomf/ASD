package com.cryptull.asd;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Set;


public class Principal extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);



        System.out.println("---- EMPIEZA -----");
        //Graph.setG();

        Graph.generateGraph(5);
        final long startTime = System.currentTimeMillis();

        String p = Utilities.getPackage(2, "Paco");

        final long duration1 = System.currentTimeMillis() - startTime;

        System.out.println("P:"+p);
        System.out.println("["+duration1+"]"+p.getBytes().length);

        final long startTime1 = System.currentTimeMillis();

        String des = Utilities.proccessPackage(p);

        final long duration2 = System.currentTimeMillis() - startTime1;

        System.out.println("["+duration2+"]"+des);

        System.out.println("---- TERMINA -----");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
