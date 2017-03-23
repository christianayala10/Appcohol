package com.example.linux1.appcohol;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static com.example.linux1.appcohol.Constantes.COMPONENTES_NOMBRES;
import static com.example.linux1.appcohol.Constantes.COMPONENTES_DATOS;

public class NuevoCocktelActivity extends AppCompatActivity {

    /* Elementos del layout */
    private EditText et_cocktel;
    private ListView lv_componentes;
    private AutoCompleteTextView actv_componente;
    private Spinner sp_cantidad;
    private Button bt_addcomponente;
    private Spinner sp_personas;
    private Spinner sp_categoria;
    private Button bt_add;
    private Button bt_volver;

    /* Variables */
    private AdaptadorListaComponentesNuevo adaptador;
    private List<Componente> lista_componentes = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_cocktel);

        /* Relacionar los elementos */
        et_cocktel = (EditText) findViewById(R.id.et_nuevococktel_nombre);
        lv_componentes = (ListView) findViewById(R.id.lv_nuevococktel_lista);
        actv_componente = (AutoCompleteTextView) findViewById(R.id.actv_nuevococktel_componente);
        sp_cantidad = (Spinner) findViewById(R.id.sp_nuevococktel_cantidad);
        bt_addcomponente = (Button) findViewById(R.id.bt_nuevococktel_addcomponente);
        sp_personas = (Spinner) findViewById(R.id.sp_nuevococktel_personas);
        sp_categoria = (Spinner) findViewById(R.id.sp_nuevococktel_categoria);
        bt_add = (Button) findViewById(R.id.bt_nuevococktel_add);
        bt_volver = (Button) findViewById(R.id.bt_nuevococktel_volver);

        /* Establecer los campos permitidos en cada Spinner */
        ArrayAdapter<CharSequence> adaptador_cantidades = ArrayAdapter.createFromResource(this,
                R.array.spiner_cantidad, android.R.layout.simple_spinner_item);
        adaptador_cantidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_cantidad.setAdapter(adaptador_cantidades);

        ArrayAdapter<CharSequence> adaptador_personas = ArrayAdapter.createFromResource(this,
                R.array.spiner_personas, android.R.layout.simple_spinner_item);
        adaptador_personas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_personas.setAdapter(adaptador_personas);

        ArrayAdapter<CharSequence> adaptador_categorias = ArrayAdapter.createFromResource(this,
                R.array.spiner_categoria, android.R.layout.simple_spinner_item);
        adaptador_categorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_categoria.setAdapter(adaptador_categorias);

        /* Establecer los campos permitidos en el campo de componentes */

        ArrayAdapter<String> adaptador_componentes = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COMPONENTES_NOMBRES );
        actv_componente.setAdapter(adaptador_componentes);

        /* Cancelar y volver al inicio */
        bt_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /* Inicializar la lista de componentes */
        lista_componentes = new ArrayList<Componente>();

        /* Añadir componente */
        bt_addcomponente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = actv_componente.getText().toString();
                int cantidad = Integer.parseInt(sp_cantidad.getSelectedItem().toString());

                if ( !nombre.equalsIgnoreCase("")){
                    Componente nuevo_componente = new Componente();
                    nuevo_componente.setNombre(nombre);
                    nuevo_componente.setCantidad(cantidad);
                    lista_componentes.add(nuevo_componente);
                    refrescarLista();
                    actv_componente.setText("");
                }else{
                    Toast.makeText(NuevoCocktelActivity.this,"Debes introducir un componente", Toast.LENGTH_SHORT ).show();
                }
            }
        });

         /* Añadir cocktel */
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cocktel = et_cocktel.getText().toString();

                /* Comprobar que no haya ninguna otro cocktel con el mismo nombre */
                ParseQuery<ParseObject> query = new ParseQuery("cockteles");
                query.whereEqualTo("nombre", cocktel);
                try {
                    List<ParseObject> lista_objetos = query.find();
                    if(lista_objetos.isEmpty()){
                        String personas = sp_personas.getSelectedItem().toString();
                        String categoria = sp_categoria.getSelectedItem().toString();

                        if( !cocktel.equalsIgnoreCase("") && !lista_componentes.isEmpty() ){
                            ParseUser user = new ParseUser();
                            user = ParseUser.getCurrentUser();

                            ParseObject nuevo_cocktel = new ParseObject("cockteles");
                            nuevo_cocktel.put("nombre",cocktel);
                            nuevo_cocktel.put("creador",user.getObjectId());
                            nuevo_cocktel.put("creador_nombre", user.getUsername());
                            nuevo_cocktel.put("personas",personas);
                            nuevo_cocktel.put("categoria", categoria);


                        /* Vamos a recorrer los componentes introduciendolos en la bd a la vez que calculamos las
                        * calorias y el precio totales como suma de el total */
                            int precioTotal = 0;
                            int caloriasTotal = 0;
                            for (Componente componente : lista_componentes) {
                                ParseObject add_componente = new ParseObject("componente_cocktel");
                                add_componente.put("componente",componente.getNombre());
                                add_componente.put("cantidad", componente.getCantidad());
                                add_componente.put("cocktel", cocktel);
                                add_componente.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                    }
                                });

                                for( ComponenteDatos componenteDatos : COMPONENTES_DATOS ) {

                                    if( componenteDatos.getNombre().equalsIgnoreCase(componente.getNombre()) ){
                                        precioTotal = precioTotal + (componenteDatos.getPrecio() * componente.getCantidad() );
                                        caloriasTotal = caloriasTotal + (componenteDatos.getCalorias() * componente.getCantidad() );
                                    }
                                }
                            }

                            nuevo_cocktel.put("calificacion",2);
                            nuevo_cocktel.put("precio",precioTotal);
                            nuevo_cocktel.put("calorias",caloriasTotal);
                            nuevo_cocktel.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if( e == null ){
                                        Toast.makeText( NuevoCocktelActivity.this, "Cocktel creado", Toast.LENGTH_SHORT ).show();
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText( NuevoCocktelActivity.this, "Asegurate de introducir toda la informacion", Toast.LENGTH_SHORT ).show();
                        }
                    } else {
                        Toast.makeText( NuevoCocktelActivity.this, "Ya existe un cocktel con ese nombre", Toast.LENGTH_SHORT ).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }




            }
        });
    }




    /* Refresca la lista cuando es llamada */
    private void refrescarLista( ){

        /* Le pasamos los resultados obtenidos al adaptador */
        adaptador = new AdaptadorListaComponentesNuevo( NuevoCocktelActivity.this, lista_componentes);
            /* Relacionamos el adaptador con la lista */
        lv_componentes.setAdapter(adaptador);

    }
}