package com.redsystem.agendaonline.Contacto;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.paging.UiReceiver;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.redsystem.agendaonline.R;

import java.util.HashMap;
import java.util.Objects;

public class Actulizar_Contacto extends AppCompatActivity {

    ImageView Imagen_C_A,Actualizar_imagen_C_A;
    TextView Id_C_A , Uid_C_A;
    EditText Nombres_C_A, Apellidos_C_A, Correo_C_A,Telefono_C_A,Direccion_C_A;
    Button Btn_Actualizar_C_A;

    String id_c, uid_usuario_c, nombres_c, apellidos_c, correo_c, telefono_c, direccion_c;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Uri imagenUri = null;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actulizar_contacto);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Actualizar contacto");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        InicializarVistas();
        RecuperarDatos();
        SetearDatosRecuperados();
        ObtenerImagen();

    }

    private void InicializarVistas(){
        Id_C_A = findViewById(R.id.Id_C_A);
        Uid_C_A = findViewById(R.id.Uid_C_A);
        Telefono_C_A = findViewById(R.id.Telefono_C_A);
        Nombres_C_A = findViewById(R.id.Nombres_C_A);
        Apellidos_C_A = findViewById(R.id.Apellidos_C_A);
        Correo_C_A = findViewById(R.id.Correo_C_A);
        Direccion_C_A = findViewById(R.id.Direccion_C_A);
        Imagen_C_A = findViewById(R.id.Imagen_C_A);
        Actualizar_imagen_C_A = findViewById(R.id.Actualizar_imagen_C_A);
        Btn_Actualizar_C_A = findViewById(R.id.Btn_Actualizar_C_A);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Btn_Actualizar_C_A.setOnClickListener(v ->{
            ActualizarInfmacionContacto();
        });

        Actualizar_imagen_C_A.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(Actulizar_Contacto.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                SeleccionarImagenGaleria();

            }else {
                SolicitarPermisoGaleria.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });

        progressDialog = new ProgressDialog(Actulizar_Contacto.this);
        progressDialog.setTitle("Espere por Favor");
        progressDialog.setCanceledOnTouchOutside(false);
    }



    private void RecuperarDatos(){
        Bundle bundle = getIntent().getExtras();
        id_c = bundle.getString("id_c");
        uid_usuario_c = bundle.getString("uid_usuario");
        nombres_c = bundle.getString("nombres_c");
        apellidos_c = bundle.getString("apellidos_c");
        correo_c = bundle.getString("correo_c");
        telefono_c = bundle.getString("telefono_c");
        direccion_c = bundle.getString("direccion_c");

    }

    private void SetearDatosRecuperados(){
            Id_C_A.setText(id_c);
            Uid_C_A.setText(uid_usuario_c);
            Nombres_C_A.setText(nombres_c);
            Apellidos_C_A.setText(apellidos_c);
            Correo_C_A.setText(correo_c);
            Telefono_C_A.setText(telefono_c);
            Direccion_C_A.setText(direccion_c);
    }

    private void ObtenerImagen(){
        String imagen_c = getIntent().getStringExtra("imagen_c");

        try {

            Glide.with(getApplicationContext()).load(imagen_c).placeholder(R.drawable.imagen_contacto).into(Imagen_C_A);

        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ActualizarInfmacionContacto(){
        String NombresActaulizar = Nombres_C_A.getText().toString().trim();
        String ApellidosActualizar = Apellidos_C_A.getText().toString().trim();
        String CorreoActualizar = Correo_C_A.getText().toString().trim();
        String TelefonoActualizar = Telefono_C_A.getText().toString().trim();
        String DireccionActualizar = Direccion_C_A.getText().toString().trim();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Usuarios");

        Query query = databaseReference.child(user.getUid()).child("Contactos").orderByChild("id_contacto").equalTo(id_c);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().child("nombres").setValue(NombresActaulizar);
                    ds.getRef().child("apellidos").setValue(ApellidosActualizar);
                    ds.getRef().child("correo").setValue(CorreoActualizar);
                    ds.getRef().child("telefono").setValue(TelefonoActualizar);
                    ds.getRef().child("direccion").setValue(DireccionActualizar);
                }
                Toast.makeText(Actulizar_Contacto.this, "Informacion Actulizada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void subirImagenStorage(){
        progressDialog.setMessage("Subiendo imagen");
        progressDialog.show();
        String id_c =  getIntent().getStringExtra("id_c");

        String carpetaImagenesContactos = "ImagenesPerfilContactos/";
        String NombreImagen = carpetaImagenesContactos+id_c;
        StorageReference reference = FirebaseStorage.getInstance().getReference(NombreImagen);
        reference.putFile(imagenUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String UriIMAGEN = ""+uriTask.getResult();
                        ActualizarImagenBD(UriIMAGEN);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Actulizar_Contacto.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void ActualizarImagenBD(String uriIMAGEN) {
        progressDialog.setMessage("Actualizando la imagen");
        progressDialog.dismiss();

        String id_c = getIntent().getStringExtra("id_c");

        HashMap<String, Object> hashMap = new HashMap<>();
        if (imagenUri != null){
            hashMap.put("imagen", ""+uriIMAGEN);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(user.getUid()).child("Contactos").child(id_c)
                .updateChildren(hashMap)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Actulizar_Contacto.this, "Imagen actulizada con exito", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Actulizar_Contacto.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void SeleccionarImagenGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galeriActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galeriActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imagenUri = data.getData();
                        Imagen_C_A.setImageURI(imagenUri);
                        subirImagenStorage();
                    }else{
                        Toast.makeText(Actulizar_Contacto.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<String> SolicitarPermisoGaleria = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(isGranted){
                    SeleccionarImagenGaleria();
                }else{
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}