package com.redsystem.agendaonline.Perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.redsystem.agendaonline.R;

public class Editar_imagen_perfil extends AppCompatActivity {

    ImageView ImagenPerfilActulizar;
    Button BtnElegirImagenDe, BtnActulizarImagen;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_imagen_perfil);

        ImagenPerfilActulizar = findViewById(R.id.ImagenPerfilActulizar);
        BtnElegirImagenDe = findViewById(R.id.BtnElegirImagenDe);
        BtnActulizarImagen = findViewById(R.id.BtnActulizarImagen);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        BtnElegirImagenDe.setOnClickListener(v -> {
            Toast.makeText(this, "Elegir imagen de", Toast.LENGTH_SHORT).show();
        });

        BtnActulizarImagen.setOnClickListener(v -> {
            Toast.makeText(this, "Actualizar imagen", Toast.LENGTH_SHORT).show();
        });

        LecturaDeImagen();
    }

    private void LecturaDeImagen() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Obtener el dato imagen
                String imagen_perfil = ""+snapshot.child("imagen_perfil").getValue();

                Glide.with(getApplicationContext())
                        .load(imagen_perfil)
                        .placeholder(R.drawable.imagen_perfil_usuario)
                        .into(ImagenPerfilActulizar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}