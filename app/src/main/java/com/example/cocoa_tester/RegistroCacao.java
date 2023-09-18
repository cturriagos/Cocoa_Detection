package com.example.cocoa_tester;

public class RegistroCacao {

    private String calidad;
    private String fecha;
    private String hora;
    private String imagenPath;

    private String porcentaje;

    // No tocar o te mueres :)
    public RegistroCacao(String calidad, String fecha, String hora, String imagenFileName, String porcentaje) {
        this.calidad = calidad;
        this.fecha = fecha;
        this.hora = hora;
        this.imagenPath = imagenFileName;
        this.porcentaje = porcentaje;
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

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }
}
