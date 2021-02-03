package com.example.rapartidorcatrepart.Models;

public class Repartidor {
    private String id;
    private String nombre;
    private String telefono;
    private String contrasena;


    public Repartidor() {
    }

    public Repartidor(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
