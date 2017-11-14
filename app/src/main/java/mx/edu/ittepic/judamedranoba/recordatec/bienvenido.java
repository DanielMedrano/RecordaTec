package mx.edu.ittepic.judamedranoba.recordatec;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    String id,idalumno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenido);

        Toast.makeText(getApplicationContext(), "alumno: "+idalumno, Toast.LENGTH_SHORT).show();

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
                hconexion = new WServices();
                id = (i+3)+"";
                hconexion.execute(uris.GET_MATERIAS_POR_ID, "2",id);
                //Intent intent = new Intent(bienvenido.this, detallestarea.class);
                //startActivity(intent);
            }
        });

    }

    public void notificacion(){
        if(!datos.isEmpty()){
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY,23);
            calendar.set(Calendar.MINUTE,49);
            calendar.set(Calendar.SECOND,30);
            Intent intent = new Intent(getApplicationContext(),Notification_reciever.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
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
                }
            }
        });
    }

    public class WServices extends AsyncTask<String, Void, String>{

        URL url;
        @Override
        protected String doInBackground(String... params) {
            String cadena="";
            String ruta="";
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
                                    datos.add(new Lista_entrada(c.getString("materianombre"),c.getString("fecha_entrega"),c.getString("maestronombre")));
                        }
                    }
                    else{cadena = ""+respuesta;}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (params[1] == "2") {
                try {
                    url = new URL(uris.GET_MATERIAS_POR_ID+"?id="+id);
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
                        JSONObject alum = jsonObj.getJSONObject("alumno");
                        cadena +="";
                        }
                    else{cadena = ""+respuesta;}
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

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
