package mx.edu.ittepic.judamedranoba.recordatec;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class detallestarea extends AppCompatActivity {
    String _id,idalumno;
    php uris;
    String json_string;
    WServices hconexion;

    EditText materia,des,f_entrega,maestro,enlace,puntuacion;

    Button enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detallestarea);
        _id = getIntent().getStringExtra("id");
        idalumno = getIntent().getStringExtra("idalumno");
        Toast.makeText(getApplicationContext(), "id = "+_id+idalumno, Toast.LENGTH_SHORT).show();

        materia = (EditText) findViewById(R.id.editText_Materia);
        des = (EditText) findViewById(R.id.editText_Descripcion);
        f_entrega = (EditText) findViewById(R.id.editText_FechaE);
        maestro = (EditText) findViewById(R.id.editText_Maestro);
        enlace = (EditText) findViewById(R.id.editText_Ruta);
        puntuacion = (EditText) findViewById(R.id.editText_Puntuacion);
        enviar = (Button) findViewById(R.id.button_Enviar);

        uris = new php();
        hconexion = new WServices();
        hconexion.execute(uris.GET_TAREAS_POR_ID,"1");

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!enlace.getText().toString().isEmpty()) {
                    hconexion = new WServices();
                    hconexion.execute(uris.GET_TAREAS_POR_ID, "2");
                }
                else{Toast.makeText(getApplicationContext(), "Ingrese la ruta de su archivo", Toast.LENGTH_SHORT).show();}
            }
        });

    }

    public class WServices extends AsyncTask<String, Void, String> {

        URL url;
        @Override
        protected String doInBackground(String... params) {
            String cadena="";
            if (params[1] == "1") {
                try {
                    url = new URL(uris.GET_TAREAS_POR_ID+"?id="+_id);
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
                        JSONObject mate = jsonObj.getJSONObject("tareas");
                        materia.setText(mate.getString("materianombre"));
                        maestro.setText(mate.getString("maestronombre"));
                        f_entrega.setText(mate.getString("fecha_entrega"));
                        des.setText(mate.getString("descripcion"));
                    }
                    else{cadena = "";}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (params[1] == "2") {
                try {
                    url = new URL(uris.INSERT_TAREA);
                    HttpURLConnection connection = null; // Abrir conexion
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("idtarea", _id);
                    jsonParam.put("idalumno", idalumno);
                    jsonParam.put("ref", enlace.getText());
                    // Envio los parámetros post.
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();
                    writer.close();

                    int respuesta = connection.getResponseCode();


                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {

                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            result.append(line);
                            //response+=line;
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                        if (resultJSON == "1") {      // hay un alumno que mostrar
                            cadena = "Alumno insertado correctamente";

                        } else if (resultJSON == "2") {
                            cadena = "El alumno no pudo insertarse";
                        }
                    }
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
            enlace.setClickable(false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
