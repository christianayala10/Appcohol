package com.example.linux1.appcohol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MostrarCocktelActivity extends AppCompatActivity {

    /* Elementos del layout */
    private TextView tv_nombre;
    private TextView tv_autor;
    private TextView tv_fecha;
    private ListView lv_componentes;
    private TextView tv_personas;
    private TextView tv_precio;
    private TextView tv_calorias;
    private TextView tv_calificacion;
    private Button bt_mapa;
    private Button bt_favoritos;

    /* Variables */
    private String cocktel;
    private String precio;
    private String personas;
    private String calorias;
    private String calificacion;
    private String autor;
    private String fecha;
    private List<ParseObject> lista_objetos;
    private AdaptadorListaComponentesMostrar adaptador;
    private List<Componente> lista_componentes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_cocktel);

        /* Relacionar los elementos */
        tv_nombre = (TextView) findViewById(R.id.tv_mostrarcocktel_nombrecocktel);
        tv_autor = (TextView) findViewById(R.id.tv_mostrarcocktel_autor);
        tv_fecha = (TextView) findViewById(R.id.tv_mostrarcocktel_fecha);
        lv_componentes = (ListView) findViewById(R.id.lv_mostrarcocktel_listacomponentes);
        tv_personas = (TextView) findViewById(R.id.tv_mostrarcocktel_personas);
        tv_precio = (TextView) findViewById(R.id.tv_mostrarcocktel_precio);
        tv_calorias = (TextView) findViewById(R.id.tv_mostrarcocktel_calorias);
        tv_calificacion = (TextView) findViewById(R.id.tv_mostrarcocktel_calificacion);
        bt_mapa = (Button) findViewById(R.id.bt_mostrarcocktel_mapa);
        bt_favoritos = (Button) findViewById(R.id.bt_mostrarcocktel_favoritos);

        /* Obtener la informacion del cocktel a mostrar */
        cocktel = getIntent().getStringExtra("Cocktel");
        precio = getIntent().getStringExtra("Precio");
        calorias = getIntent().getStringExtra("Calorias");
        calificacion = getIntent().getStringExtra("Calificacion");
        personas = getIntent().getStringExtra("Personas");

        lista_componentes = new ArrayList<Componente>();
        /* Obtenemos la lista haciendo una query */
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("componente_cocktel");
        query.whereEqualTo("cocktel", cocktel);
        try {
            lista_objetos = query.find();
            for (ParseObject objeto : lista_objetos) {

                Componente componente = new Componente();
                componente.setNombre( (String) objeto.get("componente") );
                componente.setCantidad( (Integer) objeto.get("cantidad"));
                lista_componentes.add(componente);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /* Busqueda para obtener el autor y la fecha */
        query = new ParseQuery<ParseObject>("cockteles");
        query.whereContains("nombre", cocktel);
        try {
            lista_objetos = query.find();

            autor = lista_objetos.get(0).get("creador_nombre").toString();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            fecha = (formatter.format(lista_objetos.get(0).getCreatedAt()));

        } catch (ParseException e) {
            e.printStackTrace();
        }


        /* Mostrar la informacion */
        tv_nombre.setText(cocktel);
        tv_personas.setText(personas);
        tv_precio.setText(precio);
        tv_calorias.setText(calorias);
        tv_calificacion.setText(calificacion);

        tv_autor.setText(autor);
        tv_fecha.setText(fecha);

        /* Mostramos la lista*/
        adaptador = new AdaptadorListaComponentesMostrar( MostrarCocktelActivity.this, lista_componentes);
        lv_componentes.setAdapter(adaptador);

        /* Añadir cocktel a favoritos */
        bt_favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* Comprobamos si el usuario ya le ha dado a favorito antes */
                ParseQuery query = new ParseQuery("favoritos");
                query.whereContains("usuario",ParseUser.getCurrentUser().getObjectId());
                query.whereContains("cocktel", cocktel);
                try {
                    List<ParseObject> fav = query.find();
                    if ( fav.isEmpty() ) {
                        ParseObject favorito = new ParseObject("favoritos");
                        favorito.put("usuario", ParseUser.getCurrentUser().getObjectId());
                        favorito.put("cocktel", cocktel );
                        favorito.saveInBackground();
                        Toast.makeText(MostrarCocktelActivity.this, "Cocktel añadido a favoritos", Toast.LENGTH_SHORT).show();
                    } else {
                        ParseObject.deleteAll(fav);
                        Toast.makeText(MostrarCocktelActivity.this, "Cocktel eliminado de favoritos", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        /* Mostrar el mapa */
        bt_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MostrarCocktelActivity.this, MapaActivity.class );
                startActivity( intent );
            }
        });

    }
}