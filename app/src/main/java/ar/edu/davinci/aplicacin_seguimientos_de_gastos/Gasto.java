package ar.edu.davinci.aplicacin_seguimientos_de_gastos;

import com.google.firebase.firestore.Exclude;
import java.io.Serializable;

public class Gasto implements Serializable {
    private String id;
    private double cantidad;
    private String categoria;
    private String fecha;
    private String nota;

    private String userId;

    public Gasto() { //// Constructor vacío necesario para Firestore
    }

    public Gasto(String id, double cantidad, String categoria, String fecha, String nota, String userId) {
        this.id = id;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.fecha = fecha;
        this.nota = nota;
        this.userId = userId;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.id = userId; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }
}
