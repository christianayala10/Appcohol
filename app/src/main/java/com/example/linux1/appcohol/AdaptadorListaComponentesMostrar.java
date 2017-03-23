package com.example.linux1.appcohol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 19/03/2017.
 */

public class AdaptadorListaComponentesMostrar extends BaseAdapter {

    /* Declaracion de variables de nuestro adaptador */
    Context context;
    LayoutInflater inflater;
    private List<Componente> lista_componentes = null;
    private ArrayList<Componente> arraylist;

    /* Constructor de nuestro adaptador */
    public AdaptadorListaComponentesMostrar(Context context, List<Componente> lista_componentes) {
        this.context = context;
        this.lista_componentes = lista_componentes;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<Componente>();
        this.arraylist.addAll(lista_componentes);
    }

    /* Elementos del item de nuestra lista */
    public class ViewHolder {
        TextView nombre;
        TextView cantidad;
    }

    /* Funciones para obtener el elemento de la lista necesario */
    @Override
    public int getCount() {
        return lista_componentes.size();
    }

    @Override
    public Object getItem(int position) {
        return lista_componentes.get(position);
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
            view = inflater.inflate(R.layout.item_componente_mostrar, null);

            /* Relacionamos los elementos del item declarados con el layout */
            holder.nombre = (TextView) view.findViewById(R.id.tv_mostrarcocktel_item_nombre);
            holder.cantidad = (TextView) view.findViewById(R.id.tv_mostrarcocktel_item_cantidad);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        /* Rellenamos esos elementos con los datos del cocktel */
        holder.nombre.setText(lista_componentes.get(position).getNombre());
        holder.cantidad.setText(lista_componentes.get(position).getCantidad()+"");

        return view;
    }

}
