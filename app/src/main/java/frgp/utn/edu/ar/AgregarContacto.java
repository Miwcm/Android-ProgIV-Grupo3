package frgp.utn.edu.ar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle item selection
        int id =item.getItemId();

        if (id== R.id.AgregarContacto){
            Intent intent = new Intent(this, AgregarContacto.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.ListarContacto){
            Intent intent = new Intent(this, ListarContacto.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}