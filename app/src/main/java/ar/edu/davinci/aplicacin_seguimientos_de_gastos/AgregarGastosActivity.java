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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class AgregarGastosActivity extends AppCompatActivity {
    private Button btnConfirmarTransaccion;
    private Spinner spinnerCategorias;
    private ImageButton btnRegreso;
    private EditText cant1, fecha1, nota;
    private TextView tituloTrans, seleccionCat, fechaTrans, notaLabel, ingresarCantidad;
    private FirebaseFirestore db;

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

        db = FirebaseFirestore.getInstance();

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

        btnConfirmarTransaccion = findViewById(R.id.btn_confirmarTransaccion);
        btnConfirmarTransaccion.setOnClickListener(view -> agregarGasto());

        fecha1.setOnClickListener(v -> mostrarDatePickerDialog());

        btnRegreso = findViewById(R.id.btn_regreso);
        btnRegreso.setOnClickListener(v -> {
            Intent intent = new Intent(AgregarGastosActivity.this, MenuActivity.class);
            startActivity(intent);
        });
    }

    private void mostrarDatePickerDialog() {
        final Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String fechaSeleccionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    fecha1.setText(fechaSeleccionada);
                },
                anio, mes, dia);
        datePickerDialog.show();
    }

    private void agregarGasto() {
        String cantidadStr = cant1.getText().toString();
        String categoria = spinnerCategorias.getSelectedItem().toString();
        String fecha = fecha1.getText().toString();
        String notaText = nota.getText().toString();

        if (cantidadStr.isEmpty() || fecha.isEmpty() || categoria.equals("Seleccionar categoría")) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        double cantidad = Double.parseDouble(cantidadStr);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userId = user != null ? user.getUid() : null;

        if (userId == null) {
            Toast.makeText(this, "Error al obtener el ID del usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        Gasto gasto = new Gasto(null, cantidad, categoria, fecha, notaText, userId);

        db.collection("gastos").add(gasto).addOnSuccessListener(documentReference -> {
            Toast.makeText(this, "Gasto agregado exitosamente.", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al agregar el gasto.", Toast.LENGTH_SHORT).show();
        });
    }
    private void limpiarCampos() {
        cant1.setText("");
        fecha1.setText("");
        nota.setText("");
        spinnerCategorias.setSelection(0);
    }
}
