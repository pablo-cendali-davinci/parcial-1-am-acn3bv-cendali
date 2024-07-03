package ar.edu.davinci.aplicacin_seguimientos_de_gastos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button btnLoginMenu;
    private Button btnAddTransactionMenu;
    private Button btnReviewExpensesMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnAddTransactionMenu = findViewById(R.id.btnAddTransactionMenu);
        btnReviewExpensesMenu = findViewById(R.id.btnRevisarGastosMenu);

        btnAddTransactionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnReviewExpensesMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RevisarGastosActivity.class);
                startActivity(intent);
            }
        });
    }
}