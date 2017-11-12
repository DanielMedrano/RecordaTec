package mx.edu.ittepic.judamedranoba.recordatec;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.AsyncTask;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class login extends AppCompatActivity {


    Button Iniciar;
    EditText ncon,nip;

    php uris;

    /*
    String IP = "http://172.20.1.28/php";
    String GET_BY_ID = IP + "/obtener_alumnos_por_id.php";
*/
    WServices hconexion;

    String json_string,id,lnip,ip;
    boolean valida = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uris = new php();

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        Toast.makeText(getApplicationContext(), "ip: "+uris.RUTA, Toast.LENGTH_SHORT).show();
        ncon = (EditText) (findViewById(R.id.edt_numcontrol));
        nip = (EditText) (findViewById(R.id.edt_password));
        Iniciar = (Button) findViewById(R.id.btn_iniciar);

        Iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = ncon.getText().toString();
                lnip = nip.getText().toString();
                if(id.isEmpty()){ Toast.makeText(getApplicationContext(), "LLENE EL ID", Toast.LENGTH_SHORT).show(); return;}
                hconexion = new WServices();
                hconexion.execute(uris.GET_ALUMNO_BY_ID, "1",id,lnip);
                if(valida) {
                    Intent intent = new Intent(login.this, bienvenido.class);
                    startActivity(intent);
                }
                else{Toast.makeText(getApplicationContext(), "Usuario y/o contraseña incorrecto", Toast.LENGTH_SHORT).show();}
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
                    url = new URL(uris.GET_ALUMNO_BY_ID+"?id="+id+"&nip="+lnip);
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
                        cadena += "ID: " + alum.getString("id")+ "nip: "+alum.getString("nip");
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
            if(s.isEmpty()){
                valida = false;
            }
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            valida = true;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
