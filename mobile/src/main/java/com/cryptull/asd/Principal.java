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
        final long startTime = System.currentTimeMillis();

        Utilities.setG();
        Utilities.generateGi();

        final long duration1 = System.currentTimeMillis() - startTime;

        String msg = String.valueOf(Utilities.graph2Long(Utilities.G))+":"+Utilities.list2Bytes(Utilities.isomorfismo);

        System.out.println(msg.getBytes().length);

        System.out.println(Utilities.bytes2List(Utilities.list2Bytes(Utilities.isomorfismo)));



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
