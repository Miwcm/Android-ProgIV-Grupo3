package frgp.utn.edu.ar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class AgregarContacto extends AppCompatActivity {
    //Solo letras (con tildes y ñ). Permite nombres compuestos
    private static final String REGEX_NOMBRE = "^\\p{L}+([ '\\-]\\p{L}+)*$";

    private EditText editTextNombre;
    private EditText editTextApellido;
    private Spinner spinnerTelefono;
    private Spinner spinnerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_agregar_contacto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        configurarValidacionNombreApellido();
        spinnerTelefono = findViewById(R.id.spinnerTelefono);
        spinnerEmail = findViewById(R.id.spinnerEmail);
        configurarSpinners();
    }

    private void configurarValidacionNombreApellido() {
        // 1) Bloquea números y símbolos mientras se escribe (también al pegar texto)
        editTextNombre.setFilters(new InputFilter[]{crearFiltroSoloLetras(editTextNombre)});
        editTextApellido.setFilters(new InputFilter[]{crearFiltroSoloLetras(editTextApellido)});

        // 2) Al salir del campo, valida el contenido completo
        editTextNombre.setOnFocusChangeListener((v, tieneFoco) -> {
            if (!tieneFoco && !editTextNombre.getText().toString().trim().isEmpty()) {
                validarCampoNombre(editTextNombre, getString(R.string.campo_nombre));
            }
        });
        editTextApellido.setOnFocusChangeListener((v, tieneFoco) -> {
            if (!tieneFoco && !editTextApellido.getText().toString().trim().isEmpty()) {
                validarCampoNombre(editTextApellido, getString(R.string.campo_apellido));
            }
        });
    }
    private InputFilter crearFiltroSoloLetras(EditText campo) {
        return (source, start, end, dest, dstart, dend) -> {
            StringBuilder permitido = new StringBuilder();
            boolean huboRechazo = false;
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (Character.isLetter(c) || c == ' ' || c == '\'' || c == '-') {
                    permitido.append(c);
                } else {
                    huboRechazo = true;
                }
            }
            if (huboRechazo) {
                campo.setError(getString(R.string.error_solo_letras));
                return permitido.toString();
            }
            return null; // sin cambios
        };
    }

    private boolean validarCampoNombre(EditText campo, String nombreCampo) {
        String texto = campo.getText().toString().trim();
        if (texto.isEmpty()) {
            campo.setError(getString(R.string.error_campo_vacio, nombreCampo));
            return false;
        }
        if (!texto.matches(REGEX_NOMBRE)) {
            campo.setError(getString(R.string.error_solo_letras));
            return false;
        }
        campo.setError(null);
        return true;
    }

    /** Devuelve true si Nombre y Apellido son válidos. Pensado para llamarse desde el botón Continuar. */
    public boolean validarNombreYApellido() {
        boolean nombreOk = validarCampoNombre(editTextNombre, getString(R.string.campo_nombre));
        boolean apellidoOk = validarCampoNombre(editTextApellido, getString(R.string.campo_apellido));
        return nombreOk && apellidoOk;
    }
    private void configurarSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tipos_contacto, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTelefono.setAdapter(adapter);
        spinnerEmail.setAdapter(adapter);
    }

    public String getTipoTelefono() { return spinnerTelefono.getSelectedItem().toString(); }
    public String getTipoEmail()    { return spinnerEmail.getSelectedItem().toString(); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.AgregarContacto) {
            // Ya estamos en esta pantalla, no hacemos nada
            return true;
        } else if (id == R.id.ListarContacto) {
            Intent intent = new Intent(this, ListarContacto.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}