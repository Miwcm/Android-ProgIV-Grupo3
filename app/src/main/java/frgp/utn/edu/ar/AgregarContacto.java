package frgp.utn.edu.ar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import android.util.Log;
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
import android.util.Patterns;

public class AgregarContacto extends AppCompatActivity {
    //Solo letras (con tildes y ñ). Permite nombres compuestos
    private static final String REGEX_NOMBRE = "^\\p{L}+([ '\\-]\\p{L}+)*$";

    // Solo dígitos, con guiones únicamente entre grupos de dígitos (ej: xx-xxxx-xxxx)
    private static final String REGEX_TELEFONO = "^\\d+(-\\d+)*$";
    private static final int MIN_DIGITOS_TELEFONO = 10;
    private static final int MAX_DIGITOS_TELEFONO = 10;
    private static final int MAX_LARGO_TELEFONO = 20; // incluye guiones

    private EditText editTextNombre;
    private EditText editTextApellido;
    private Spinner spinnerTelefono;
    private Spinner spinnerEmail;
    private EditText editTextFechaNacimiento;
    private EditText editTextTelefono;
    private EditText editTextEmail;
    private EditText editTextDireccion;

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

        editTextTelefono = findViewById(R.id.editTextTelefono);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextDireccion = findViewById(R.id.editTextDireccion);
        configurarValidacionTelefonoEmail();

        editTextFechaNacimiento = findViewById(R.id.editTextFechaNacimiento);
        editTextFechaNacimiento.setFocusable(false);
        editTextFechaNacimiento.setOnClickListener(v -> mostrarSelectorFechaNacimiento());
        findViewById(R.id.buttonContinuar).setOnClickListener(v -> continuar());
    }

    private void continuar() {
        if (!validarFormulario()) {
            return;
        }

        Bundle datosContacto = new Bundle();
        datosContacto.putString("nombre", editTextNombre.getText().toString().trim());
        datosContacto.putString("apellido", editTextApellido.getText().toString().trim());
        datosContacto.putString("telefono", editTextTelefono.getText().toString().trim());
        datosContacto.putString("tipoTelefono", getTipoTelefono());
        datosContacto.putString("email", editTextEmail.getText().toString().trim());
        datosContacto.putString("tipoEmail", getTipoEmail());
        datosContacto.putString("direccion", editTextDireccion.getText().toString().trim());
        datosContacto.putString("fechaNacimiento", editTextFechaNacimiento.getText().toString().trim());

        Intent intent = new Intent(this, MasDatosContacto.class);
        intent.putExtra(MasDatosContacto.EXTRA_DATOS_CONTACTO, datosContacto);
        startActivity(intent);
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

    private void mostrarSelectorFechaNacimiento() {
        Calendar hoy = Calendar.getInstance();
        hoy.set(Calendar.HOUR_OF_DAY, 0);
        hoy.set(Calendar.MINUTE, 0);
        hoy.set(Calendar.SECOND, 0);
        hoy.set(Calendar.MILLISECOND, 0);

        Calendar fechaMinima = (Calendar) hoy.clone();
        fechaMinima.add(Calendar.YEAR, -120);
        Calendar seleccion = (Calendar) hoy.clone();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formato.setLenient(false);
        String fechaActual = editTextFechaNacimiento.getText().toString().trim();
        if (!fechaActual.isEmpty()) {
            try {
                Date fecha = formato.parse(fechaActual);
                if (fecha != null && !fecha.before(fechaMinima.getTime())
                        && !fecha.after(hoy.getTime())) {
                    seleccion.setTime(fecha);
                }
            } catch (ParseException e) {
                // si el contenido no es valido, el calendario comienza en la fecha actual.
            }
        }

        DatePickerDialog selector = new DatePickerDialog(this,
                (view, anio, mes, dia) -> {
                    Calendar elegida = Calendar.getInstance();
                    elegida.set(anio, mes, dia);
                    editTextFechaNacimiento.setText(formato.format(elegida.getTime()));
                    validacionFechaNac();
                },
                seleccion.get(Calendar.YEAR),
                seleccion.get(Calendar.MONTH),
                seleccion.get(Calendar.DAY_OF_MONTH));
        selector.getDatePicker().setMinDate(fechaMinima.getTimeInMillis());
        selector.getDatePicker().setMaxDate(hoy.getTimeInMillis());
        selector.show();
    }

    public boolean validacionFechaNac() {

        editTextFechaNacimiento.setError(null);

        String fecha = editTextFechaNacimiento.getText().toString().trim();
        String regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$";

        if (fecha.isEmpty()) {
            editTextFechaNacimiento.setError("Campo requerido");
            return false;
        }

        if (!Pattern.matches(regex, fecha)) {
            editTextFechaNacimiento.setError("Formato inválido. Use dd/MM/yyyy");
            return false;
        }

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formato.setLenient(false); // rechaza fechas inexistentes como 31/02

        try {
            Date fechaNacimiento = formato.parse(fecha);

            Calendar fechaMinima = Calendar.getInstance();
            fechaMinima.add(Calendar.YEAR, -120);
            fechaMinima.set(Calendar.HOUR_OF_DAY, 0);
            fechaMinima.set(Calendar.MINUTE, 0);
            fechaMinima.set(Calendar.SECOND, 0);
            fechaMinima.set(Calendar.MILLISECOND, 0);

            if (fechaNacimiento.after(new Date())) {
                editTextFechaNacimiento.setError("La fecha no puede ser futura");
                return false;
            }
            if (fechaNacimiento.before(fechaMinima.getTime())) {
                editTextFechaNacimiento.setError("La edad no puede superar los 120 años");
                return false;
            }
        } catch (ParseException e) {
            editTextFechaNacimiento.setError("La fecha ingresada no existe");
            return false;
        }

        return true;
    }

    // ===== Aporte 9: validaciones de Teléfono, Email y campos obligatorios =====

    private void configurarValidacionTelefonoEmail() {
        // Bloquea todo lo que no sea número o guion (también al pegar) y limita el largo
        editTextTelefono.setFilters(new InputFilter[]{
                crearFiltroTelefono(editTextTelefono),
                new InputFilter.LengthFilter(MAX_LARGO_TELEFONO)
        });

        // Al salir del campo, valida el contenido completo
        editTextTelefono.setOnFocusChangeListener((v, tieneFoco) -> {
            if (!tieneFoco && !editTextTelefono.getText().toString().trim().isEmpty()) {
                validarTelefono();
            }
        });
        editTextEmail.setOnFocusChangeListener((v, tieneFoco) -> {
            if (!tieneFoco && !editTextEmail.getText().toString().trim().isEmpty()) {
                validarEmail();
            }
        });
    }

    private InputFilter crearFiltroTelefono(EditText campo) {
        return (source, start, end, dest, dstart, dend) -> {
            StringBuilder permitido = new StringBuilder();
            boolean huboRechazo = false;
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if ((c >= '0' && c <= '9') || c == '-') {
                    permitido.append(c);
                } else {
                    huboRechazo = true;
                }
            }
            if (huboRechazo) {
                campo.setError(getString(R.string.error_solo_numeros));
                return permitido.toString();
            }
            return null; // sin cambios
        };
    }

    private boolean validarTelefono() {
        String texto = editTextTelefono.getText().toString().trim();
        if (texto.isEmpty()) {
            editTextTelefono.setError(getString(R.string.error_campo_vacio, getString(R.string.campo_telefono)));
            return false;
        }
        if (!texto.matches(REGEX_TELEFONO)) {
            editTextTelefono.setError(getString(R.string.error_telefono_formato));
            return false;
        }
        int digitos = texto.replace("-", "").length();
        if (digitos < MIN_DIGITOS_TELEFONO || digitos > MAX_DIGITOS_TELEFONO) {
            editTextTelefono.setError(getString(R.string.error_telefono_longitud));
            return false;
        }
        editTextTelefono.setError(null);
        return true;
    }

    private boolean validarEmail() {
        String texto = editTextEmail.getText().toString().trim();
        if (texto.isEmpty()) {
            editTextEmail.setError(getString(R.string.error_campo_vacio, getString(R.string.campo_email)));
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(texto).matches()) {
            editTextEmail.setError(getString(R.string.error_email_invalido));
            return false;
        }
        editTextEmail.setError(null);
        return true;
    }

    private boolean validarDireccion() {
        if (editTextDireccion.getText().toString().trim().isEmpty()) {
            editTextDireccion.setError(getString(R.string.error_campo_vacio, getString(R.string.campo_direccion)));
            return false;
        }
        editTextDireccion.setError(null);
        return true;
    }

    /** Devuelve true si todos los campos validados hasta ahora están completos y son válidos.
     *  Pensado para llamarse desde el botón Continuar. */
    public boolean validarFormulario() {
        boolean nombreApellidoOk = validarNombreYApellido();
        boolean telefonoOk = validarTelefono();
        boolean emailOk = validarEmail();
        boolean direccionOk = validarDireccion();
        boolean fechaOk = validacionFechaNac();
        return nombreApellidoOk && telefonoOk && emailOk && direccionOk && fechaOk;
    }


}