package ar.edu.davinci.aplicacin_seguimientos_de_gastos;

public class Gasto {
    private int id;
    private double cantidad;
    private String categoria;
    private String fecha;
    private String nota;

    public Gasto(int id, double cantidad, String categoria, String fecha, String nota) {
        this.id = id;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.fecha = fecha;
        this.nota = nota;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }
}
