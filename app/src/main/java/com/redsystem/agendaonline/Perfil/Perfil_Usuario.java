package com.redsystem.agendaonline.Perfil;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.redsystem.agendaonline.R;

import java.util.HashMap;

public class Perfil_Usuario extends AppCompatActivity {

    ImageView Imagen_Perfil;
    TextView Correo_Perfil, Uid_Perfil;
    EditText Nombres_Perfil, Apellidos_Perfil, Edad_Perfil,
            Telefono_Perfil, Domicilio_Perfil, Universidad_Perfil,
            Profesion_Perfil;

    Button Guardar_Datos;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference Usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Perfil de usuario");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        InicializarVariables();

        Guardar_Datos.setOnClickListener(v -> {
            ActualizarDatos();
        });

    }

    private void InicializarVariables(){
        Imagen_Perfil = findViewById(R.id.Imagen_Perfil);
        Correo_Perfil = findViewById(R.id.Correo_Perfil);
        Uid_Perfil = findViewById(R.id.Uid_Perfil);
        Nombres_Perfil = findViewById(R.id.Nombres_Perfil);
        Apellidos_Perfil = findViewById(R.id.Apellidos_Perfil);
        Edad_Perfil = findViewById(R.id.Edad_Perfil);
        Telefono_Perfil = findViewById(R.id.Telefono_Perfil);
        Domicilio_Perfil = findViewById(R.id.Domicilio_Perfil);
        Universidad_Perfil = findViewById(R.id.Universidad_Perfil);
        Profesion_Perfil = findViewById(R.id.Profesion_Perfil);

        Guardar_Datos = findViewById(R.id.Guardar_Datos);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        LecturaDeDatos();
    }

    private void LecturaDeDatos(){
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //Obtener sus datos
                    String uid = ""+snapshot.child("uid").getValue();
                    String nombre = ""+snapshot.child("nombres").getValue();
                    String apellidos = ""+snapshot.child("apellidos").getValue();
                    String correo = ""+snapshot.child("correo").getValue();
                    String edad = ""+snapshot.child("edad").getValue();
                    String telefono = ""+snapshot.child("telefono").getValue();
                    String domicilio = ""+snapshot.child("domicilio").getValue();
                    String universidad = ""+snapshot.child("universidad").getValue();
                    String profesion = ""+snapshot.child("profesion").getValue();
                    String imagen_perfil = ""+snapshot.child("imagen_perfil").getValue();

                    //Seteo de datos
                    Uid_Perfil.setText(uid);
                    Nombres_Perfil.setText(nombre);
                    Apellidos_Perfil.setText(apellidos);
                    Correo_Perfil.setText(correo);
                    Edad_Perfil.setText(edad);
                    Telefono_Perfil.setText(telefono);
                    Domicilio_Perfil.setText(domicilio);
                    Universidad_Perfil.setText(universidad);
                    Profesion_Perfil.setText(profesion);

                    Cargar_Imagen(imagen_perfil);

                }
                else {
                    Toast.makeText(Perfil_Usuario.this, "Esperando datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Perfil_Usuario.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ActualizarDatos() {
        String A_Nombre = Nombres_Perfil.getText().toString().trim();
        String A_Apellidos = Apellidos_Perfil.getText().toString().trim();
        String A_Telefono = Telefono_Perfil.getText().toString().trim();
        String A_Domicilio = Domicilio_Perfil.getText().toString();

        HashMap<String, Object> Datos_Actualizar = new HashMap<>();

        Datos_Actualizar.put("nombres", A_Nombre);
        Datos_Actualizar.put("apellidos", A_Apellidos);
        Datos_Actualizar.put("telefono", A_Telefono);
        Datos_Actualizar.put("domicilio", A_Domicilio);

        Usuarios.child(user.getUid()).updateChildren(Datos_Actualizar)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Perfil_Usuario.this, "Actualizado Correctamente", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Perfil_Usuario.this, "" + e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void Cargar_Imagen(String imagenPerfil) {
        try {
            //Cuando la imagen ha sido traida exitosamente desde Firebase
            Glide.with(getApplicationContext()).load(imagenPerfil).placeholder(R.drawable.imagen_perfil_usuario).into(Imagen_Perfil);

        } catch (Exception e) {
            //Si la imagen no fue traida con éxito
            Glide.with(getApplicationContext()).load(R.drawable.imagen_perfil_usuario).into(Imagen_Perfil);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}