package mx.edu.ittepic.judamedranoba.recordatec;

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

public class bienvenido extends AppCompatActivity {

    ListView Lv;
    ArrayList<Lista_entrada> datos;
    FloatingActionButton NuevaTarea;
    int Posicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenido);

        NuevaTarea = (FloatingActionButton) findViewById(R.id.fab_agregar);

        datos = new ArrayList<Lista_entrada>();
        final DBAdapter db = new DBAdapter(this);

        ExplorandoBD();
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
                Intent intent = new Intent(bienvenido.this, detallestarea.class);
                startActivity(intent);
            }
        });


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

    public void ExplorandoBD(){
        final DBAdapter db = new DBAdapter(this);
        db.open();
        // Explorar el cursor
        Cursor c = db.getAllContacts();
        if (c.moveToFirst()) {
            do {
                datos.add(new Lista_entrada(c.getString(0),c.getString(1),c.getString(2)));
            } while (c.moveToNext());
        }
        db.close();
    }


}
