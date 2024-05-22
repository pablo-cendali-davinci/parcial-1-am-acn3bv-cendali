package ar.edu.davinci.aplicacin_seguimientos_de_gastos;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private Button btnConfirmarTransaccion;
    private Spinner spinnerCategorias;
    private ImageButton btnRegreso;
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

        spinnerCategorias= findViewById(R.id.spinner_categorias);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.categorias, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategorias.setAdapter(adapter);

        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccionarCategoria = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //accion al no seleccionar nada
            }
        });

        btnRegreso = findViewById(R.id.btn_regreso);
        btnRegreso.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Volviendo al login", Toast.LENGTH_SHORT).show();
        });

        btnConfirmarTransaccion = findViewById(R.id.btn_confirmarTransaccion);
        btnConfirmarTransaccion.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this,"La transacción se guardo correctamente", Toast.LENGTH_SHORT).show();
        });


    }
}