package com.example.cocoa_tester;

public class RegistroCacao {

    private String calidad;
    private String fecha;
    private String hora;
    private String imagenPath;

    // No tocar o te mueres :)
    public RegistroCacao(String calidad, String fecha, String hora, String imagenFileName) {
        this.calidad = calidad;
        this.fecha = fecha;
        this.hora = hora;
        this.imagenPath = imagenFileName;
    }


    public String getCalidad() {
        return calidad;
    }

    public void setCalidad(String calidad) {
        this.calidad = calidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getImagenPath() {
        return imagenPath;
    }

    public void setImagenPath(String imagenPath) {
        this.imagenPath = imagenPath;
    }
}
