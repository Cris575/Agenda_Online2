package com.redsystem.agendaonline.Contacto;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.redsystem.agendaonline.Objetos.Contacto;
import com.redsystem.agendaonline.R;
import com.redsystem.agendaonline.ViewHolder.ViewHolder_Contacto;

public class Listar_Contactos extends AppCompatActivity {

    RecyclerView recyclerViewContactos;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BD_Usuarios;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    FirebaseRecyclerAdapter<Contacto, ViewHolder_Contacto> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Contacto> firebaseRecyclerOptions;

    FloatingActionButton agregarContatco;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_contactos);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Mis contactos");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        agregarContatco = findViewById(R.id.AgregarContacto);
        recyclerViewContactos = findViewById(R.id.recyclerViewContactos);
        recyclerViewContactos.setHasFixedSize(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        BD_Usuarios = firebaseDatabase.getReference("Usuarios");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        dialog = new Dialog(Listar_Contactos.this);

        ListarContactos();
        agregarContatco.setOnClickListener(v -> {
            //Recuperanod el uid de la actividad anterior
            String Uid_Recuperado = getIntent().getStringExtra("Uid");
            Intent intent = new Intent(Listar_Contactos.this, Agregar_Contacto.class);
            //Envio del dato uid a la siguinte actividad
            intent.putExtra("Uid", Uid_Recuperado);
            startActivity(intent);
        });


    }

    private void ListarContactos() {
        Query query = BD_Usuarios.child(user.getUid()).child("Contactos").orderByChild("nombres");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Contacto>().setQuery(query, Contacto.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contacto, ViewHolder_Contacto>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Contacto viewHolderContacto, int position, @NonNull Contacto contacto) {
                viewHolderContacto.SetearDatosContacto(
                        getApplicationContext(),
                        contacto.getId_contacto(),
                        contacto.getUid_contacto(),
                        contacto.getNombres(),
                        contacto.getApellidos(),
                        contacto.getCorreo(),
                        contacto.getTelefono(),
                        contacto.getDireccion(),
                        contacto.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolder_Contacto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
                ViewHolder_Contacto viewHolderContacto = new ViewHolder_Contacto(view);
                viewHolderContacto.setOnClickListener(new ViewHolder_Contacto.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(Listar_Contactos.this, "On item click", Toast.LENGTH_SHORT).show();
                        //Obteniendo los datos del contacto seleccionado
                        String id_c = getItem(position).getId_contacto();
                        String uid_usuario = getItem(position).getUid_contacto();
                        String nombres_c = getItem(position).getNombres();
                        String apellidos_c = getItem(position).getApellidos();
                        String correo_c = getItem(position).getCorreo();
                        String telefono_c = getItem(position).getTelefono();
                        String direccion_c = getItem(position).getDireccion();
                        String imagen_c = getItem(position).getImagen();

                        //Enviar los datos a la siguinete acyividad
                        Intent intent = new Intent(Listar_Contactos.this, Detalle_contacto.class);
                        intent.putExtra("id_c", id_c);
                        intent.putExtra("uid_usuario", uid_usuario);
                        intent.putExtra("nombres_c", nombres_c);
                        intent.putExtra("apellidos_c", apellidos_c);
                        intent.putExtra("correo_c", correo_c);
                        intent.putExtra("telefono_c", telefono_c);
                        intent.putExtra("direccion_c", direccion_c);
                        intent.putExtra("imagen_c", imagen_c);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        String id_c = getItem(position).getId_contacto();
                        String uid_usuario = getItem(position).getUid_contacto();
                        String nombres_c = getItem(position).getNombres();
                        String apellidos_c = getItem(position).getApellidos();
                        String correo_c = getItem(position).getCorreo();
                        String telefono_c = getItem(position).getTelefono();
                        String direccion_c = getItem(position).getDireccion();
                        String imagen_c = getItem(position).getImagen();

                        //Toast.makeText(Listar_Contactos.this, "On item long click", Toast.LENGTH_SHORT).show();
                        Button Btn_Eliminar_C, Btn_Actualizar_C;
                        dialog.setContentView(R.layout.cuadro_dialogo_opciones_contacto);

                        Btn_Eliminar_C = dialog.findViewById(R.id.Btn_Eliminar_C);
                        Btn_Actualizar_C = dialog.findViewById(R.id.Btn_Actualizar_C);

                        Btn_Eliminar_C.setOnClickListener(v -> {
                            //Toast.makeText(Listar_Contactos.this, "Eliminar contacto", Toast.LENGTH_SHORT).show();
                            EliminarContacto(id_c);
                            dialog.dismiss();
                        });

                        Btn_Actualizar_C.setOnClickListener(v -> {
                            Intent intent = new Intent(Listar_Contactos.this, Actulizar_Contacto.class);
                            intent.putExtra("id_c", id_c);
                            intent.putExtra("uid_usuario", uid_usuario);
                            intent.putExtra("nombres_c", nombres_c);
                            intent.putExtra("apellidos_c", apellidos_c);
                            intent.putExtra("correo_c", correo_c);
                            intent.putExtra("telefono_c", telefono_c);
                            intent.putExtra("direccion_c", direccion_c);
                            intent.putExtra("imagen_c", imagen_c);
                            startActivity(intent);
                            dialog.dismiss();
                        });
                        dialog.show();
                    }
                });
                return viewHolderContacto;
            }
        };

        recyclerViewContactos.setLayoutManager(new GridLayoutManager(Listar_Contactos.this, 2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewContactos.setAdapter(firebaseRecyclerAdapter);
    }

    private void EliminarContacto(String id_c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Listar_Contactos.this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Desea eleminar este contacto?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query query = BD_Usuarios.child(user.getUid()).child("Contactos").orderByChild("id_contacto").equalTo(id_c);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(Listar_Contactos.this, "Los contactos seleccionados se han eliminado correctamente", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Listar_Contactos.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Listar_Contactos.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();

    }



    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}