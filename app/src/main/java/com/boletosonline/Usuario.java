package com.boletosonline;

public class Usuario {

    private String nombre;
    private String cedula;
    private String telefono;
    private String contraseña;
    private String nombreTarjeta;
    private String numeroTarjeta;
    private String fechacaducidadtarjeta;
    private String codigocvctarjeta;

public Usuario(){

}

    public Usuario(String nombre, String cedula, String telefono, String contraseña, String nombreTarjeta, String numeroTarjeta, String fechacaducidadtarjeta, String codigocvctarjeta) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.contraseña = contraseña;
        this.nombreTarjeta = nombreTarjeta;
        this.numeroTarjeta = numeroTarjeta;
        this.fechacaducidadtarjeta = fechacaducidadtarjeta;
        this.codigocvctarjeta = codigocvctarjeta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getNombreTarjeta() {
        return nombreTarjeta;
    }

    public void setNombreTarjeta(String nombreTarjeta) {
        this.nombreTarjeta = nombreTarjeta;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getFechacaducidadtarjeta() {
        return fechacaducidadtarjeta;
    }

    public void setFechacaducidadtarjeta(String fechacaducidadtarjeta) {
        this.fechacaducidadtarjeta = fechacaducidadtarjeta;
    }

    public String getCodigocvctarjeta() {
        return codigocvctarjeta;
    }

    public void setCodigocvctarjeta(String codigocvctarjeta) {
        this.codigocvctarjeta = codigocvctarjeta;
    }
}
