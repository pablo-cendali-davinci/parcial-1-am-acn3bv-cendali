package ar.edu.davinci.aplicacin_seguimientos_de_gastos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Button btnConfirmarTransaccion;
    private Spinner spinnerCategorias;
    private ImageButton btnRegreso;
    private EditText cant1, fecha1, nota;
    private TextView tituloTrans, seleccionCat, fechaTrans, notaLabel, ingresarCantidad;
    private BaseDeDatos db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new BaseDeDatos(this);

        cant1 = findViewById(R.id.cant_1);
        fecha1 = findViewById(R.id.fecha_1);
        nota = findViewById(R.id.id_nota);
        ingresarCantidad = findViewById(R.id.textView7);
        tituloTrans = findViewById(R.id.titulo_trans);
        seleccionCat = findViewById(R.id.seleccion_cat);
        fechaTrans = findViewById(R.id.fecha_trans);
        notaLabel = findViewById(R.id.nota);

        spinnerCategorias = findViewById(R.id.spinner_categorias);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(adapter);
        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccionarCategoria = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // acción al no seleccionar nada
            }
        });

        fecha1.setOnClickListener(v -> mostrarDatePickerDialog());

        ImageButton btnRegreso = findViewById(R.id.btn_regreso);
        btnRegreso.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        btnConfirmarTransaccion = findViewById(R.id.btn_confirmarTransaccion);
        btnConfirmarTransaccion.setOnClickListener(v -> {
            if (validarCampos()) {
                guardarTransaccion();
                Toast.makeText(MainActivity.this, "La transacción se guardó correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatePickerDialog() {
        final Calendar calendiario = Calendar.getInstance();
        int anio = calendiario.get(Calendar.YEAR);
        int mes = calendiario.get(Calendar.MONTH);
        int dia = calendiario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Actualizar el EditText con la fecha seleccionada
                    String fechaSeleccionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    fecha1.setText(fechaSeleccionada);
                },
                anio, mes, dia);
        datePickerDialog.show();
    }

    private boolean validarCampos() {
        String cantidadStr = cant1.getText().toString();
        String fechaStr = fecha1.getText().toString();

        if (cantidadStr.isEmpty()) {
            cant1.setError("La cantidad es requerida");
            return false;
        }

        try {
            double cantidad = Double.parseDouble(cantidadStr);
            if (cantidad <= 0) {
                cant1.setError("La cantidad debe ser mayor que 0");
                return false;
            }
        } catch (NumberFormatException e) {
            cant1.setError("Ingrese un número válido");
            return false;
        }

        if (fechaStr.isEmpty()) {
            fecha1.setError("La fecha es requerida");
            return false;
        }

        return true;
    }

    private void guardarTransaccion() {
        double cantidad = Double.parseDouble(cant1.getText().toString());
        String categoria = spinnerCategorias.getSelectedItem().toString();
        String fecha = fecha1.getText().toString();
        String notaText = nota.getText().toString();

        Gasto gasto = new Gasto(0, cantidad, categoria, fecha, notaText);
        db.addGasto(gasto);
    }
}
