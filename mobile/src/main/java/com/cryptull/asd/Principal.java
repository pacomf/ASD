package com.cryptull.asd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import java.sql.SQLOutput;
import java.util.Set;


public class Principal extends Activity {

    EditText nodos, segmentos, mensaje;
    Button limpiar, generar, pak;
    TextView consola;
    int nnodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        /*System.out.println("---- EMPIEZA -----");
        //Graph.setG();

        Graph.generateGraph(10);
        final long startTime = System.currentTimeMillis();

        String p = Utilities.getPackage(2, "Paco");

        final long duration1 = System.currentTimeMillis() - startTime;

        System.out.println("P:"+p);
        System.out.println("["+duration1+"]"+p.getBytes().length);

        final long startTime1 = System.currentTimeMillis();

        String des = Utilities.proccessPackage(p);

        final long duration2 = System.currentTimeMillis() - startTime1;

        System.out.println("["+duration2+"]"+des);

        System.out.println("---- TERMINA -----");*/

        nnodos=-1;

        nodos = (EditText) findViewById(R.id.editText);
        segmentos = (EditText) findViewById(R.id.editText2);
        mensaje = (EditText) findViewById(R.id.editText3);

        limpiar = (Button) findViewById(R.id.button);
        generar = (Button) findViewById(R.id.button2);
        pak = (Button) findViewById(R.id.buttonPAK);

        consola = (TextView) findViewById(R.id.textView4);
        consola.setMovementMethod(new ScrollingMovementMethod());

        /*Utilities.dim=3;
        boolean[][] m = {{false, true, false},
                         {true, true, false},
                         {true, false, true}};

        consola.append(Utilities.graph2Bin(m, consola)+"\n");
        consola.append("**************--******************");
        String st = Utilities.graph2Bin2(m, consola);
        consola.append(st+"\n");

        boolean[][] gr = null;

        try {
            gr = Utilities.readBooleans(st);
        } catch (Exception e){
            e.printStackTrace();
            consola.append("Fallo: "+e.getMessage());
        }

        Utilities.printGraph(gr);*/


        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    consola.setText("Console");
            }
        });

        generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("- En Proceso... -");
                if ((nnodos == -1) || (nnodos != Integer.parseInt(nodos.getText().toString()))){
                    nnodos = Integer.parseInt(nodos.getText().toString());
                    //consola.append("\nGenerando...\n");
                    Graph.generateGraph(Integer.parseInt(nodos.getText().toString()));
                    //consola.append("---> ¿El Grafo es No Planar?: "+Graph.isNoPlanar(Utilities.dim, Graph.edges)+"\n");
                }
                final long startTime = System.currentTimeMillis();
                //final long startTime = System.currentTimeMillis();
                String p = Utilities.getPackage(Integer.parseInt(segmentos.getText().toString()), mensaje.getText().toString(), consola);
                //final long duration1 = System.currentTimeMillis() - startTime;
                //consola.append("*********************************************\n");
                //consola.append("*** Tamaño del paquete: "+p.getBytes().length+" Bytes \n");
                //consola.append("*** Tiempo en Generar Paquete: "+duration1+" ms. \n");
                //consola.append("*** Paquete: "+p+"\n");
                //final long startTime1 = System.currentTimeMillis();
                String des = Utilities.proccessPackage(p, consola);
                //final long duration2 = System.currentTimeMillis() - startTime1;
                //consola.append("*** Tiempo en Procesar Paquete: "+duration2+" ms. \n");
                final long duration = System.currentTimeMillis() - startTime;
                consola.append("\nMensaje: "+des+"\nTiempo Requerido: "+duration+" ms. \n");
                //consola.append("*** Secreto: "+des+"\n");
                //consola.append("*********************************************\n");
                //System.out.println("- Fin -");
            }
        });

        pak.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (Utils.serverSocket != null) {
                   try {
                       Utils.serverSocket.close();
                   } catch (IOException e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
                   }
               }
               Intent i = new Intent(Principal.this, PAKActivity.class);
               Principal.this.startActivity(i);
           }
       });

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

    /*public void usePAK (){
        final long startTime = System.currentTimeMillis();
        String idA = "A", idB = "B", password = "password";
        PAK pakdh = new PAK(password);

        // 1. A calculates X
        BigInteger gRa = pakdh.generategRa();
        BigInteger X = pakdh.calculateX(idA, idB, gRa);

        // 2. A sends X to B

        // 3. B calculates Y and S1
        BigInteger gRb = pakdh.generategRb();
        BigInteger Xab = pakdh.calculateXab(idA, idB, X);
        BigInteger S1 = pakdh.calculateS1(idA, idB, Xab, gRb);
        BigInteger Y = pakdh.calculateY(idA, idB, gRb);

        // 4. B sends S1 and Y to A

        // 5. A calculates S1' and verifies
        BigInteger Yba = pakdh.calculateYba(idA, idB, Y);
        BigInteger S1p = pakdh.calculateS1(idA, idB, gRa, Yba);

        if (!S1p.equals(S1)){
            System.out.println("No coinciden S1 de A y S1 de B: "+S1.toString(16)+" | "+S1p.toString(16));
            return;
        }

        // 6. A calculates Ka and S2
        BigInteger Ka = pakdh.calculateK(idA, idB, gRa, Yba);
        BigInteger S2 = pakdh.calculateS2(idA, idB, gRa, Yba);

        // 7. A sends S2 to B

        // 8. B calculates S2' and verifies
        BigInteger S2p = pakdh.calculateS2(idA, idB, Xab, gRb);

        if (!S2p.equals(S2)){
            System.out.println("No coinciden S2 de A y S2 de B: "+S2.toString(16)+" | "+S2p.toString(16));
            return;
        }

        // 9. B calculates Kb
        BigInteger Kb = pakdh.calculateK(idA, idB, Xab, gRb);
        final long duration = System.currentTimeMillis() - startTime;

        System.out.println("Tiempo requerido: "+duration+" ms.");

        if (Ka.equals(Kb)){
            System.out.println("EXITO: "+Ka);
        } else {
            System.out.println("FRACASO!!!!!!");
            System.out.println("Ka: "+Ka);
            System.out.println("Kb: "+Kb);
        }
    }*/
}
