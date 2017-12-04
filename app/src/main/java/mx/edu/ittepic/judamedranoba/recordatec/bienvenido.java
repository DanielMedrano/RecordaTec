package mx.edu.ittepic.judamedranoba.recordatec;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class bienvenido extends AppCompatActivity {

    ListView Lv;
    ArrayList<Lista_entrada> datos;
    FloatingActionButton NuevaTarea;
    php uris;
    WServices hconexion;
    String json_string;
    String _id,idalumno;
    final long EXECUTION_TIME = 60000; // 1 minuto
/*
    @Override
    protected void onResume(){
        super.onResume();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override public void run() {

               notificacion();
                Toast.makeText(getApplicationContext(), "Refrescar", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, EXECUTION_TIME);
            }
        }, EXECUTION_TIME);
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenido);
        idalumno = getIntent().getStringExtra("idalumno");

        Toast.makeText(getApplicationContext(), "idalum = "+idalumno, Toast.LENGTH_SHORT).show();
        uris = new php();
        hconexion = new WServices();
        hconexion.execute(uris.GET_TAREAS, "1");
        NuevaTarea = (FloatingActionButton) findViewById(R.id.fab_agregar);

        datos = new ArrayList<Lista_entrada>();

        //ExplorandoBD();
        Lv = (ListView) findViewById(R.id.lv_tareas);
        LlenandoLista();

        NuevaTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(bienvenido.this, agregartarea.class);
                startActivity(intent);
            }
        });

        Lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                _id = datos.get(i).getTextoId();
                Toast.makeText(getApplicationContext(), "id ="+_id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(bienvenido.this, detallestarea.class);
                intent.putExtra("id",_id);
                intent.putExtra("idalumno",idalumno);
                startActivity(intent);
            }
        });

    }

    public void notificacion(){
        if(datos.isEmpty()){
            NotificationCompat.Builder mBuilder;
            NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

            int icono = R.mipmap.ic_launcher;
            Intent i=new Intent(this, bienvenido.class);
            i.putExtra("idalumno",idalumno);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);

            mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(icono)
                    .setContentTitle("Tareas pendientes")
                    .setContentText("No olvides enviar tus tareas")
                    .setPriority(1)
                    .setColor(98149252)
                    .setLights(0xff00ff00, 300, 100)
                    .setVibrate(new long[] {100, 250, 100, 500})
                    .setAutoCancel(true);



            mNotifyMgr.notify(1, mBuilder.build());
        }
        else {
            Toast.makeText(getApplicationContext(), "Esta vacia", Toast.LENGTH_SHORT).show();
        }
    }
    public void  LlenandoLista(){
        Lv.setAdapter(new Lista_adaptador(this, R.layout.entrada, datos){
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView materia = (TextView) view.findViewById(R.id.tv_materia);
                    if (materia != null)
                        materia.setText(((Lista_entrada) entrada).getTextoMateria());

                    TextView fechaentrega = (TextView) view.findViewById(R.id.tv_fechaentrega);
                    if (fechaentrega != null)
                        fechaentrega.setText(((Lista_entrada) entrada).getTextoFechaEntrega());

                    TextView maestro = (TextView) view.findViewById(R.id.tv_maestro);
                    if (maestro != null)
                        maestro.setText(((Lista_entrada) entrada).getTextoMaestro());

                    TextView id = (TextView) view.findViewById(R.id.tv_id);
                    if (id != null)
                        id.setText(((Lista_entrada) entrada).getTextoId());
                }
            }
        });
    }

    public class WServices extends AsyncTask<String, Void, String>{

        URL url;
        @Override
        protected String doInBackground(String... params) {
            String cadena="";
            if (params[1] == "1") {
                try {
                    url = new URL(uris.GET_TAREAS);
                    HttpURLConnection connection = null; // Abrir conexion
                    connection = (HttpURLConnection) url.openConnection();
                    int respuesta = 0;
                    respuesta = connection.getResponseCode();
                    InputStream inputStream = null;
                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        while ((json_string = bufferedReader.readLine()) != null) {
                            stringBuilder.append(json_string + "\n");
                        }
                        bufferedReader.close();
                        inputStream.close();
                        connection.disconnect();
                        String temporal = stringBuilder.toString();
                        JSONObject jsonObj = new JSONObject(temporal);
                        // Getting JSON Array node
                        JSONArray tareas = jsonObj.getJSONArray("tareas");

                        // looping through All Contacts
                        for (int i = 0; i < tareas.length(); i++) {
                            JSONObject c = tareas.getJSONObject(i);
                                cadena+=c.getString("materianombre");
                                    datos.add(new Lista_entrada(c.getString("materianombre"),c.getString("fecha_entrega"),c.getString("maestronombre"),c.getString("id")));
                        }
                    }
                    else{cadena = "";}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return cadena;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!s.isEmpty()){notificacion();}


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
