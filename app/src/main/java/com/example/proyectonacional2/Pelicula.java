package com.example.proyectonacional2;

public class Pelicula {
    private static String nombre;
    private String resena;

    public Pelicula() {
        this.nombre = "";
        this.resena = "";
    }

    public Pelicula(String nombre, String resena) {
        this.nombre = nombre;
        this.nombre = nombre;
    }

    public static String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getResena() {
        return resena;
    }

    public void setResena(String resena) {
        this.resena = resena;
    }

    @Override
    public String toString() {
        return "Reseña = " + resena;
    }
}
