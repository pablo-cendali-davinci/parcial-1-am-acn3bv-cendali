package ar.edu.davinci.aplicacin_seguimientos_de_gastos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    Button btn_ReturnLogin;

    Button btn_register;

    TextView btnGoLogin;
    EditText email, usuario, password;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        usuario = findViewById(R.id.etUsuario);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etContrasena);
        btn_register = findViewById(R.id.btnRegister);
        btnGoLogin = findViewById(R.id.btn_GoLogin);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String nameUSer = usuario.getText().toString().trim();
              String emailUser = email.getText().toString().trim();
              String passUser = password.getText().toString().trim();


                if (nameUSer.isEmpty() && emailUser.isEmpty() && passUser.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Complete los campos solicitados", Toast.LENGTH_SHORT).show();
                }else{
                    registerUser(nameUSer,emailUser,passUser);
                }
            }
        });

        btnGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser(String nameUser, String emailUser, String passUser){
        mAuth.createUserWithEmailAndPassword(emailUser,passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> map= new HashMap<>();
                map.put("id", id);
                map.put("name", nameUser);
                map.put("password",passUser);
                map.put("email",passUser);

                mFirestore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error al enviar el email de verificación", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error al registrar, por favor intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

