package ar.edu.davinci.aplicacin_seguimientos_de_gastos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;
import java.util.List;

public class RevisarGastosActivity extends AppCompatActivity {
    private Spinner spinnerCategoria;
    private EditText editFecha;
    private Button btnFiltrar, btnLimpiarFiltros;
    private LinearLayout listaGastos;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisar_gastos);

        db = FirebaseFirestore.getInstance();

        spinnerCategoria = findViewById(R.id.spinner_categoria);
        editFecha = findViewById(R.id.edit_fecha);
        btnFiltrar = findViewById(R.id.btn_filtrar);
        btnLimpiarFiltros = findViewById(R.id.btn_limpiar_filtros);
        listaGastos = findViewById(R.id.lista_gastos);

        ImageButton btnRegreso = findViewById(R.id.btn_regreso);
        btnRegreso.setOnClickListener(v -> {
            Intent intent = new Intent(RevisarGastosActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        editFecha.setOnClickListener(v -> mostrarDatePickerDialog());

        btnFiltrar.setOnClickListener(v -> filtrarGastos());
        btnLimpiarFiltros.setOnClickListener(v -> limpiarFiltros());

        mostrarGastos();
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
                    editFecha.setText(fechaSeleccionada);
                },
                anio, mes, dia);
        datePickerDialog.show();
    }

    private void mostrarGastos() {
        listaGastos.removeAllViews();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user != null ? user.getUid(): null;

        if (userId == null) {
            Toast.makeText(this, "Error al obtener el ID del usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("gastos")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Gasto gasto = document.toObject(Gasto.class);
                            View itemGasto = getLayoutInflater().inflate(R.layout.item_gasto, null);
                            TextView tvCantidad = itemGasto.findViewById(R.id.tv_cantidad);
                            TextView tvCategoria = itemGasto.findViewById(R.id.tv_categoria);
                            TextView tvFecha = itemGasto.findViewById(R.id.tv_fecha);
                            TextView tvNota = itemGasto.findViewById(R.id.tv_nota);
                            Button btnEliminar = itemGasto.findViewById(R.id.btn_eliminar);

                            tvCantidad.setText(String.valueOf(gasto.getCantidad()));
                            tvCategoria.setText(gasto.getCategoria());
                            tvFecha.setText(gasto.getFecha());
                            tvNota.setText(gasto.getNota());

                            btnEliminar.setOnClickListener(v -> {
                                db.collection("gastos").document(document.getId()).delete()
                                        .addOnSuccessListener(aVoid -> mostrarGastos())
                                        .addOnFailureListener(e -> Toast.makeText(RevisarGastosActivity.this, "Error al eliminar el gasto.", Toast.LENGTH_SHORT).show());
                            });

                            listaGastos.addView(itemGasto);
                        }
                    } else {
                        Toast.makeText(RevisarGastosActivity.this, "Error al cargar los gastos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void agregarGastoALaVista(Gasto gasto) {
        View itemGasto = getLayoutInflater().inflate(R.layout.item_gasto, null);
        TextView tvCantidad = itemGasto.findViewById(R.id.tv_cantidad);
        TextView tvCategoria = itemGasto.findViewById(R.id.tv_categoria);
        TextView tvFecha = itemGasto.findViewById(R.id.tv_fecha);
        TextView tvNota = itemGasto.findViewById(R.id.tv_nota);
        Button btnEliminar = itemGasto.findViewById(R.id.btn_eliminar);

        tvCantidad.setText(String.valueOf(gasto.getCantidad()));
        tvCategoria.setText(gasto.getCategoria());
        tvFecha.setText(gasto.getFecha());
        tvNota.setText(gasto.getNota());

        btnEliminar.setOnClickListener(v -> {
            db.collection("gastos").document(gasto.getId()).delete().addOnSuccessListener(aVoid -> mostrarGastos());
        });

        listaGastos.addView(itemGasto);
    }

    private void filtrarGastos() {
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String fecha = editFecha.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user != null ? user.getUid(): null;

        if (userId == null) {
            Toast.makeText(this, "Error al obtener el ID del usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("gastos")
                .whereEqualTo("userId", userId)
                .whereEqualTo("categoria", categoria)
                .whereEqualTo("fecha", fecha)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaGastos.removeAllViews();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Gasto gasto = document.toObject(Gasto.class);
                            View itemGasto = getLayoutInflater().inflate(R.layout.item_gasto, null);
                            TextView tvCantidad = itemGasto.findViewById(R.id.tv_cantidad);
                            TextView tvCategoria = itemGasto.findViewById(R.id.tv_categoria);
                            TextView tvFecha = itemGasto.findViewById(R.id.tv_fecha);
                            TextView tvNota = itemGasto.findViewById(R.id.tv_nota);
                            Button btnEliminar = itemGasto.findViewById(R.id.btn_eliminar);

                            tvCantidad.setText(String.valueOf(gasto.getCantidad()));
                            tvCategoria.setText(gasto.getCategoria());
                            tvFecha.setText(gasto.getFecha());
                            tvNota.setText(gasto.getNota());

                            btnEliminar.setOnClickListener(v -> {
                                db.collection("gastos").document(document.getId()).delete()
                                        .addOnSuccessListener(aVoid -> mostrarGastos())
                                        .addOnFailureListener(e -> Toast.makeText(RevisarGastosActivity.this, "Error al eliminar el gasto.", Toast.LENGTH_SHORT).show());
                            });

                            listaGastos.addView(itemGasto);
                        }
                    } else {
                        Toast.makeText(RevisarGastosActivity.this, "Error al cargar los gastos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void limpiarFiltros() {
        spinnerCategoria.setSelection(0);
        editFecha.setText("");
        mostrarGastos();
    }
}
