package frgp.utn.edu.ar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListarContacto extends AppCompatActivity {
    private ListView listViewContactos;
    private ArrayAdapter<Contacto> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_listar_contacto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listViewContactos = findViewById(R.id.listViewContactos);

        adaptador = new ArrayAdapter<>(
                this,
                R.layout.item_contacto,
                R.id.txtDatosContacto,
                new ArrayList<>()
        );

        listViewContactos.setAdapter(adaptador);


        listViewContactos.setEmptyView(findViewById(R.id.txtSinContactos));

        configurarSeleccionContacto();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recargarContactos();
    }

    private void recargarContactos() {
        OpenHelper bd = new OpenHelper(this, OpenHelper.NOMBRE_BD, null, OpenHelper.VERSION_BD);
        try {
            List<Contacto> contactos = bd.obtenerContactos();
            adaptador.setNotifyOnChange(false);
            adaptador.clear();
            adaptador.addAll(contactos);
            adaptador.notifyDataSetChanged();
        } finally {
            bd.cerrar();
        }
    }

    private void configurarSeleccionContacto() {
        listViewContactos.setOnItemClickListener((parent, view, position, id) -> {
            Object contactoSeleccionado = parent.getItemAtPosition(position);

            // Placeholder para Aporte 9 (apertura de detalle)
            Toast.makeText(this, "Contacto seleccionado en posición: " + position, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.AgregarContacto) {
            Intent intent = new Intent(this, AgregarContacto.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            return true;
        } else if (id == R.id.ListarContacto) {
            // Ya estamos en esta pantalla, no hacemos nada
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}