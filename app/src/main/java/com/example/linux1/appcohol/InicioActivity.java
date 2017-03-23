package com.example.linux1.appcohol;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class InicioActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /* Elementos del layout */
    private TextView tv_menu_usuario;
    private View cabecera_menu;
    private ListView lista;

    /* Variables */
    private ParseUser user;
    private List<ParseObject> lista_objetos;
    private AdaptadorListaCockteles adaptador;
    private List<Cocktel> lista_cockteles = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //Barra de navegacion lateral
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl_inicio_menu);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navegacion_lateral);
        navigationView.setNavigationItemSelectedListener(this);

        /* Relacionar elementos del layout */
        cabecera_menu = navigationView.getHeaderView(0);
        tv_menu_usuario = (TextView) cabecera_menu.findViewById(R.id.tv_inicio_usuario);
        lista = (ListView) findViewById(R.id.lv_inicio_lista);

        /* Realizamos la carga de los datos de la lista */
        new CargarCocktelesListaNuevos().execute();

        //Boton flotante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        /* Abrir menu de navegacion lateral */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Abrir menu de navegacion lateral
                drawer.openDrawer(GravityCompat.START);
            }
        });

        /* Mostrar el nombre de usuario */

        user = ParseUser.getCurrentUser();
        tv_menu_usuario.setText(user.getUsername());
    }

    /* Abrir/Cerrar menu lateral al pulsar retroceder */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl_inicio_menu);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /* Gestionar las opciones del menu de navegacion lateral*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            /* Abrir ventana de perfil */
            Intent intent = new Intent( InicioActivity.this, PerfilActivity.class );
            startActivity( intent );

        } else if ( id == R.id.nav_eliminar ) {
            /* Eliminar la cuenta de usuario */
            Intent intent = new Intent( InicioActivity.this, PopupEliminarCuenta.class );
            startActivityForResult(intent, 1);

        } else if (id == R.id.nav_cocket) {
            /* Abrir ventana de añadir nuevo Cocktel */
            Intent intent = new Intent( InicioActivity.this, NuevoCocktelActivity.class );
            startActivity( intent );

        }else if (id == R.id.nav_random) {
            /* Abrir ventana de Cocktel aleatorio */
            Intent intent = new Intent( InicioActivity.this, RandomActivity.class );
            startActivity( intent );

        } else if (id == R.id.nav_calificacion) {
            /* Mostrar lista ordenada por calificacion */
            Ordenar("calificacion");

        } else if (id == R.id.nav_calorias) {
            /* Mostrar lista ordenada por calorias */
            Ordenar("calorias");

        } else if (id == R.id.nav_precio) {
            /* Mostrar lista ordenada por precio */
            Ordenar("precio");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl_inicio_menu);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
            case 1:
                finish();
            case 2:
                //
        }
    }

    public void Ordenar(String criterio){
        try{
            ParseQuery<ParseObject> query = ParseQuery.getQuery("cockteles");
            query.orderByDescending(criterio);
            lista_objetos = query.find();
            lista_cockteles.clear();
            for (ParseObject cock : lista_objetos) {
                Cocktel map = new Cocktel();
                map.setNombre((String) cock.get("nombre"));
                map.setPersonas((String) cock.get("personas"));
                map.setPrecio((Integer) cock.get("precio"));
                map.setCalorias((Integer) cock.get("calorias"));
                map.setCalificacion((Integer) cock.get("calificacion"));
                map.setCreador((String) cock.get("creador"));
                lista_cockteles.add(map);
            }
            adaptador.notifyDataSetChanged();
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    /* CargarDatosLista */
    private class CargarCocktelesListaNuevos extends AsyncTask<Void, Void, Void> {

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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            /* Le pasamos los resultados obtenidos al adaptador */
            adaptador = new AdaptadorListaCockteles(InicioActivity.this, lista_cockteles);
            /* Relacionamos el adaptador con la lista */
            lista.setAdapter(adaptador);
        }
    }
}
