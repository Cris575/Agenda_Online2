package com.redsystem.agendaonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class recuperar_contrasena extends AppCompatActivity {

    private EditText editTextCorreo;
    private Button btnCambiarContrasena;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        editTextCorreo = findViewById(R.id.editTextCorreo);
        btnCambiarContrasena = findViewById(R.id.btnCambiarContrasena);
        mAuth = FirebaseAuth.getInstance();
        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setTitle("Cambiar contraseña");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        btnCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = editTextCorreo.getText().toString().trim();
                cambiarContrasena(correo);
            }
        });
    }

    private void cambiarContrasena(String correo) {
        mAuth.sendPasswordResetEmail(correo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(recuperar_contrasena.this, "Correo de cambio de contraseña enviado.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(recuperar_contrasena.this, "Error al enviar el correo.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
