package com.example.linux1.appcohol;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PerfilActivity extends AppCompatActivity {

    /* Elementos del layout */
    private TextView tv_usuario;
    private Button bt_cockteles;
    private Button bt_favoritos;
    private ListView lv_lista;

    /* Variables */
    private ParseUser user;
    private List<ParseObject> lista_objetos;
    private AdaptadorListaCockteles adaptador;
    private List<Cocktel> lista_cockteles = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        /* Relacionar los elementos */
        tv_usuario = (TextView) findViewById(R.id.tv_perfil_usuario);
        bt_cockteles = (Button) findViewById(R.id.bt_perfil_cockteles);
        bt_favoritos = (Button) findViewById(R.id.bt_perfil_favoritos);
        lv_lista = (ListView) findViewById(R.id.lv_perfil_lista);

        /* Mostrar mis cockteles en la lista */
        bt_cockteles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CargarCocktelesLista().execute();
            }
        });

        /* Mostrar mis cocketeles favoritos en la lista */
        bt_favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CargarCocktelesFavoritosLista().execute();
            }
        });

        /* Mostrar el nombre de usuario */
        user = ParseUser.getCurrentUser();
        tv_usuario.setText(user.getUsername());

        /* Por defecto mostramos mis cockteles creados */
        new CargarCocktelesLista().execute();
    }

    private class CargarCocktelesLista extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            /* Creamos el array de cockteles */
            lista_cockteles = new ArrayList<Cocktel>();
            try {
                /* Elegimos la tabla en la que queremos hacer la consulta */
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "cockteles");
                /* Por defecto la ordenaremos por orden descendente de creacion*/
                query.orderByDescending("_created_at");
                query.whereEqualTo("creador", user.getObjectId());
                lista_objetos = query.find();
                for (ParseObject country : lista_objetos) {

                    Cocktel map = new Cocktel();
                    map.setNombre((String) country.get("nombre"));
                    map.setPersonas((String) country.get("personas"));
                    map.setPrecio((Integer) country.get("precio"));
                    map.setCalorias((Integer) country.get("calorias"));
                    map.setCalificacion((Integer) country.get("calificacion"));
                    map.setCreador((String) country.get("creador"));
                    lista_cockteles.add(map);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            /* Le pasamos los resultados obtenidos al adaptador */
            adaptador = new AdaptadorListaCockteles(PerfilActivity.this, lista_cockteles);
            /* Relacionamos el adaptador con la lista */
            lv_lista.setAdapter(adaptador);
        }
    }

    private class CargarCocktelesFavoritosLista extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
                /* En primer lugar vamos a obtener los nombres de los cockteles favoritos */
            ArrayList<String> nombres_cocktel;
            nombres_cocktel = new ArrayList<>();

            ParseQuery query = new ParseQuery("favoritos");
            query.whereContains("usuario", user.getObjectId());
            try {
                lista_objetos = query.find();
                for (ParseObject objeto : lista_objetos) {
                    nombres_cocktel.add(objeto.get("cocktel").toString());
                }
                System.out.println(nombres_cocktel);
                /* Creamos el array de cockteles */
                lista_cockteles = new ArrayList<Cocktel>();
                try {
                        /* Elegimos la tabla en la que queremos hacer la consulta */
                    query = new ParseQuery<ParseObject>(
                            "cockteles");
                    /* Por defecto la ordenaremos por orden descendente de creacion*/
                    query.orderByDescending("_created_at");
                    query.whereContainedIn("nombre", nombres_cocktel);
                    lista_objetos = query.find();
                    for (ParseObject objeto : lista_objetos) {

                        Cocktel map = new Cocktel();
                        map.setNombre((String) objeto.get("nombre"));
                        map.setPersonas((String) objeto.get("personas"));
                        map.setPrecio((Integer) objeto.get("precio"));
                        map.setCalorias((Integer) objeto.get("calorias"));
                        map.setCalificacion((Integer) objeto.get("calificacion"));
                        map.setCreador((String) objeto.get("creador"));
                        lista_cockteles.add(map);
                    }
                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            /* Le pasamos los resultados obtenidos al adaptador */
            adaptador = new AdaptadorListaCockteles(PerfilActivity.this, lista_cockteles);
            /* Relacionamos el adaptador con la lista */
            lv_lista.setAdapter(adaptador);
        }

    }
}

