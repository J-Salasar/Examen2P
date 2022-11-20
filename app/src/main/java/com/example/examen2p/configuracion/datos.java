package com.example.examen2p.configuracion;
public class datos {
    private String id,nombre,numero,latitud,longitud,foto;
    public datos(){
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public datos(String id, String nombre, String numero, String latitud, String longitud, String foto) {
        this.id = id;
        this.nombre=nombre;
        this.numero=numero;
        this.latitud=latitud;
        this.longitud=longitud;
        this.foto=foto;
    }
}
