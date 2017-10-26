package mx.edu.ittepic.judamedranoba.recordatec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class login extends AppCompatActivity {

    Button Iniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Iniciar = (Button) findViewById(R.id.btn_iniciar);

        Iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, bienvenido.class);
                startActivity(intent);
            }
        });

    }
}
