package com.redsystem.agendaonline.Contacto;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.redsystem.agendaonline.Objetos.Contacto;
import com.redsystem.agendaonline.R;

public class Agregar_Contacto extends AppCompatActivity {

    TextView Uid_Usuario_C;
    EditText Nombres_C, Apellidos_C, Correo_C, Telefono_C, Edad_C,Direccion_C;
    Button Btn_Guardar_Contacto;
    DatabaseReference BD_Usuarios;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Agregar contacto");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        InicializarVariables();
        ObtenerUidUsuario();

        Btn_Guardar_Contacto.setOnClickListener(v -> {
            AgregarContacto();
        });
    }

    private void InicializarVariables(){
        Uid_Usuario_C = findViewById(R.id.Uid_Usuario_C);
        Nombres_C = findViewById(R.id.Nombres_C);
        Apellidos_C = findViewById(R.id.Apellidos_C);
        Correo_C = findViewById(R.id.Correo_C);
        Telefono_C = findViewById(R.id.Telefono_C);
        Direccion_C = findViewById(R.id.Direccion_C);
        Btn_Guardar_Contacto = findViewById(R.id.Btn_Guardar_Contacto);

        BD_Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        dialog = new Dialog(Agregar_Contacto.this);

    }

    private void ObtenerUidUsuario(){
         String UidRecuperado = getIntent().getStringExtra("Uid");
         Uid_Usuario_C.setText(UidRecuperado);
    }

    private void AgregarContacto(){
        //Obtener los datos
        String uid = Uid_Usuario_C.getText().toString();
        String nombre = Nombres_C.getText().toString();
        String apellidos = Apellidos_C.getText().toString();
        String correo = Correo_C.getText().toString();
        String telefono = Telefono_C.getText().toString();
        String direccion = Direccion_C.getText().toString();

        //Creacion de cadena unica
        String id_contacto = BD_Usuarios.push().getKey();

        //validar los datos
        if (!uid.equals("") && !nombre.equals("")){

            Contacto contacto = new Contacto(
                id_contacto,
                    uid,
                    nombre,
                    apellidos,
                    correo,
                    telefono,
                    direccion,
                    "");

            //Establecer el nombre de la bd
            String Nombre_BD = "Contactos";
            assert id_contacto != null;
            BD_Usuarios.child(user.getUid()).child(Nombre_BD).child(id_contacto).setValue(contacto);
            Toast.makeText(this, "Contatco agregado", Toast.LENGTH_SHORT).show();
            onBackPressed();

        }else{
            //Toast.makeText(this, "Por favor ingrese el nombre del contacto", Toast.LENGTH_SHORT).show();
            ValidarRegistroContacto();
        }

    }

    private void ValidarRegistroContacto(){
        Button Btn_Validar_Registro_C;

        dialog.setContentView(R.layout.cuadro_dialogo_validar_registro_contacto);

        Btn_Validar_Registro_C = dialog.findViewById(R.id.Btn_Validar_registro_C);

        Btn_Validar_Registro_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}