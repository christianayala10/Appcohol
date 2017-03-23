package com.example.linux1.appcohol;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 19/03/2017.
 */

public class AdaptadorListaCockteles extends BaseAdapter {

    /* Declaracion de variables de nuestro adaptador */
    Context context;
    LayoutInflater inflater;
    private List<Cocktel> lista_cockteles = null;
    private ArrayList<Cocktel> arraylist;

    /* Constructor de nuestro adaptador */
    public AdaptadorListaCockteles(Context context, List<Cocktel> lista_cockteles) {
        this.context = context;
        this.lista_cockteles = lista_cockteles;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<Cocktel>();
        this.arraylist.addAll(lista_cockteles);
    }

    /* Elementos del item de nuestra lista */
    public class ViewHolder {
        TextView nombre;
        TextView personas;
        TextView precio;
        TextView calorias;
        TextView calificacion;
    }

    /* Funciones para obtener el elemento de la lista necesario */
    @Override
    public int getCount() {
        return lista_cockteles.size();
    }

    @Override
    public Object getItem(int position) {
        return lista_cockteles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /* Generar el item de la lista */
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_cocktel, null);

            /* Relacionamos los elementos del item declarados con el layout */
            holder.nombre = (TextView) view.findViewById(R.id.tv_inicio_item_nombre);
            holder.personas = (TextView) view.findViewById(R.id.tv_inicio_item_personas);
            holder.precio = (TextView) view.findViewById(R.id.tv_inicio_item_precio);
            holder.calorias = (TextView) view.findViewById(R.id.tv_inicio_item_calorias);
            holder.calificacion = (TextView) view.findViewById(R.id.tv_inicio_item_calificacion);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        /* Rellenamos esos elementos con los datos del cocktel */
        holder.nombre.setText(lista_cockteles.get(position).getNombre());
        holder.personas.setText(lista_cockteles.get(position).getPersonas());
        holder.precio.setText(Integer.toString(lista_cockteles.get(position).getPrecio()));
        holder.calorias.setText(Integer.toString(lista_cockteles.get(position).getCalorias()));
        holder.calificacion.setText(Integer.toString(lista_cockteles.get(position).getCalificacion()));

        /* Abrir la ventana de cocktel detallado */
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent( context, MostrarCocktelActivity.class );
                intent.putExtra("Cocktel", lista_cockteles.get(position).getNombre() );
                intent.putExtra("Precio", Integer.toString(lista_cockteles.get(position).getPrecio()));
                intent.putExtra("Personas", lista_cockteles.get(0).getPersonas());
                intent.putExtra("Calorias", Integer.toString(lista_cockteles.get(position).getCalorias()));
                intent.putExtra("Calificacion", Integer.toString(lista_cockteles.get(position).getCalificacion()));
                context.startActivity(intent);
            }
        });

        return view;
    }

}
