package com.example.linux1.appcohol;

/**
 * Created by Christian on 19/03/2017.
 */

public class Cocktel {

    private String nombre;
    private String creador;
    private String personas;
    private int precio;
    private int calorias;
    private int calificacion;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public void setPersonas(String personas) {
        this.personas = personas;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCreador() {
        return creador;
    }

    public String getPersonas() {
        return personas;
    }

    public int getPrecio() {
        return precio;
    }

    public int getCalorias() {
        return calorias;
    }

    public int getCalificacion() {
        return calificacion;
    }
}
