package ar.edu.davinci.aplicacin_seguimientos_de_gastos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MenuActivity extends AppCompatActivity {

    private Button btnLoginMenu;
    private Button btnAddTransactionMenu;
    private Button btnReviewExpensesMenu;

    private Button btnReturnLogin;

    private FirebaseAuth mAuth;

    private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnAddTransactionMenu = findViewById(R.id.btnAddTransactionMenu);
        btnReviewExpensesMenu = findViewById(R.id.btnRevisarGastosMenu);
        btnReturnLogin = findViewById(R.id.btnReturnLogin);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

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
        btnReturnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            }
        });
    }
}