package com.cryptull.asd;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Set;


public class Principal extends Activity {

    EditText nodos, segmentos, mensaje;
    Button limpiar, generar;
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
                consola.setText("Consola");
            }
        });

        generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("- En Proceso... -");
                if ((nnodos == -1) || (nnodos != Integer.parseInt(nodos.getText().toString()))){
                    nnodos = Integer.parseInt(nodos.getText().toString());
                    consola.append("\nGenerando...\n");
                    Graph.generateGraph(Integer.parseInt(nodos.getText().toString()));
                }
                final long startTime = System.currentTimeMillis();
                String p = Utilities.getPackage(Integer.parseInt(segmentos.getText().toString()), mensaje.getText().toString(), consola);
                final long duration1 = System.currentTimeMillis() - startTime;
                consola.append("*********************************************\n");
                consola.append("*** Tamaño del paquete: "+p.getBytes().length+" Bytes \n");
                consola.append("*** Tiempo en Generar Paquete: "+duration1+" ms. \n");
                //consola.append("*** Paquete: "+p+"\n");
                final long startTime1 = System.currentTimeMillis();
                String des = Utilities.proccessPackage(p, consola);
                final long duration2 = System.currentTimeMillis() - startTime1;
                consola.append("*** Tiempo en Procesar Paquete: "+duration2+" ms. \n");
                consola.append("*** Secreto: "+des+"\n");
                consola.append("*********************************************\n");
                System.out.println("- Fin -");
            }
        });



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
