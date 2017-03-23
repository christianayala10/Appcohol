package com.example.linux1.appcohol;

/**
 * Created by Christian on 21/03/2017.
 */

public class ComponenteDatos {

    String nombre;
    int precio;
    int calorias;
    String supermercado;

    public ComponenteDatos(String nombre, int precio, int calorias, String supermercado) {
        this.nombre = nombre;
        this.precio = precio;
        this.calorias = calorias;
        this.supermercado = supermercado;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public  int getCalorias() {
        return calorias;
    }

    public String getSupermercado() {
        return supermercado;
    }
}
