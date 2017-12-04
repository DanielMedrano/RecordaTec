package mx.edu.ittepic.judamedranoba.recordatec;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class agregartarea extends AppCompatActivity {
    php uris;
    WServices hconexion;
    String json_string;
    ArrayList<Lista_entrada2> datos;
    ListView Lv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregartarea);
        uris = new php();
        hconexion = new WServices();
        hconexion.execute(uris.GET_MATERIAS, "1");

        datos = new ArrayList<Lista_entrada2>();

        //ExplorandoBD();
        Lv = (ListView) findViewById(R.id.lv_mat);
        LlenandoLista();
    }

    public void  LlenandoLista(){
        Lv.setAdapter(new Lista_adaptador(this, R.layout.entradados, datos){
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView materia = (TextView) view.findViewById(R.id.tv_materia);
                    if (materia != null)
                        materia.setText(((Lista_entrada2) entrada).getTextoMateria());

                    TextView fechaentrega = (TextView) view.findViewById(R.id.tv_fechaentrega);
                    if (fechaentrega != null)
                        fechaentrega.setText(((Lista_entrada2) entrada).getTextoFechaEntrega());

                    TextView maestro = (TextView) view.findViewById(R.id.tv_maestro);
                    if (maestro != null)
                        maestro.setText(((Lista_entrada2) entrada).getTextoMaestro());
                }
            }
        });
    }

    public class WServices extends AsyncTask<String, Void, String> {

        URL url;
        @Override
        protected String doInBackground(String... params) {
            String cadena="";
            String ruta="";
            if (params[1] == "1") {
                try {
                    url = new URL(uris.GET_MATERIAS);
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
                            datos.add(new Lista_entrada2(c.getString("nombre"),c.getString("clave"),c.getString("maestron")));
                        }
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
