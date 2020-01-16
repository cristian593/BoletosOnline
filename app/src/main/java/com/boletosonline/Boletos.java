package com.boletosonline;

public class Boletos {

        private String nombre, autobus, ruta, horario, numeroLugares;

        public Boletos(){

        }

    public Boletos(String nombre, String autobus, String ruta, String horario, String numeroLugares) {
        this.nombre = nombre;
        this.autobus = autobus;
        this.ruta = ruta;
        this.horario = horario;
        this.numeroLugares = numeroLugares;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutobus() {
        return autobus;
    }

    public void setAutobus(String autobus) {
        this.autobus = autobus;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getNumeroLugares() {
        return numeroLugares;
    }

    public void setNumeroLugares(String numeroLugares) {
        this.numeroLugares = numeroLugares;
    }
}
