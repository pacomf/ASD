package com.cryptull.asd;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cryptull.pak.PAK;
import com.cryptull.sockets.Utils;

import java.io.IOException;
import java.math.BigInteger;


public class PAKActivity extends Activity {

    Button clear, server, client;
    TextView consola, myip;
    EditText ip, port, msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pak);

        clear = (Button) findViewById(R.id.clear);
        server = (Button) findViewById(R.id.server);
        client = (Button) findViewById(R.id.client);

        consola = (TextView) findViewById(R.id.consola);
        consola.setMovementMethod(new ScrollingMovementMethod());
        myip = (TextView) findViewById(R.id.myipTV);

        consola.setText("\nConsole\n");

        ip = (EditText) findViewById(R.id.ipET);
        port = (EditText) findViewById(R.id.portET);
        msg = (EditText) findViewById(R.id.msgET);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consola.setText("\nConsole\n");
            }
        });

        server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.RunServer(PAKActivity.this, Integer.parseInt(port.getText().toString()), consola);
            }
        });

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.startTime = System.currentTimeMillis();
                Utils.pak = new PAK("password");
                BigInteger[] bi = Utils.pak.processPackage(1, null, null, "idA", "idB", "password", consola, PAKActivity.this);
                Utils.ConnectClientToServer(ip.getText().toString(), Integer.parseInt(port.getText().toString()), Utils.createPackage(1, bi[0], bi[1]), Utils.password, 1, consola, PAKActivity.this, msg.getText().toString());
            }
        });

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ipstr = String.format("%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
        myip.setText("Mi IP es: "+ipstr);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (Utils.serverSocket != null) {
            try {
                Utils.serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pak, menu);
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
}
