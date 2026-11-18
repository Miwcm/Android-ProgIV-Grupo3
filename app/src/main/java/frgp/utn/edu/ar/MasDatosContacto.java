package frgp.utn.edu.ar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MasDatosContacto extends AppCompatActivity {
    public static final String EXTRA_DATOS_CONTACTO = "frgp.utn.edu.ar.DATOS_CONTACTO";

    private Bundle datosContacto;
    // Datos recibidos del formulario Agregar Contacto (Aporte 5)
    private String nombre, apellido, tipoTelefono, telefono, tipoEmail, email, direccion, fechaNacimiento;
    private SwitchCompat switchInfo;

    private RadioGroup radioGroup;
    private CheckBox chkDeporte, chkMusica, chkArte, chkTecnologia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_mas_datos_contacto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        datosContacto = getIntent().getBundleExtra(EXTRA_DATOS_CONTACTO);
        if (datosContacto == null) {
            finish();
            return;
        }

        recibirDatosContacto();

       // TextView resumen = findViewById(R.id.txtViewDescripcion);
       // resumen.setText(getString(R.string.resumen_contacto,
             //   datosContacto.getString("nombre", ""),
             //   datosContacto.getString("apellido", ""),
             //   datosContacto.getString("tipoTelefono", ""),
             //   datosContacto.getString("telefono", ""),
             //   datosContacto.getString("tipoEmail", ""),
             //   datosContacto.getString("email", ""),
             //   datosContacto.getString("direccion", ""),
            //    datosContacto.getString("fechaNacimiento", "")));
        configurarSwitchInfo();
        radioGroup = findViewById(R.id.radioGroupEstudios);

        chkDeporte = findViewById(R.id.chkDeporte);
        chkMusica = findViewById(R.id.chkMusica);
        chkArte = findViewById(R.id.chkArte);
        chkTecnologia = findViewById(R.id.chkTecnologia);
    }
    private void configurarSwitchInfo() {
        switchInfo = findViewById(R.id.switchInfo);
        switchInfo.setOnCheckedChangeListener((boton, activado) ->
                switchInfo.setText(activado ? R.string.switch_info_si : R.string.switch_info_no));

    }

    private void recibirDatosContacto() {
        nombre = datosContacto.getString("nombre", "");
        apellido = datosContacto.getString("apellido", "");
        tipoTelefono = datosContacto.getString("tipoTelefono", "");
        telefono = datosContacto.getString("telefono", "");
        tipoEmail = datosContacto.getString("tipoEmail", "");
        email = datosContacto.getString("email", "");
        direccion = datosContacto.getString("direccion", "");
        fechaNacimiento = datosContacto.getString("fechaNacimiento", "");

        // Para verificar la recepción en Logcat
        Log.d("MasDatosContacto", "Recibido: " + nombre + " | " + apellido
                + " | " + tipoTelefono + " " + telefono
                + " | " + tipoEmail + " " + email
                + " | " + direccion + " | " + fechaNacimiento);
    }

    private boolean validarSeleccionRadio() {

        int idSeleccionado = radioGroup.getCheckedRadioButtonId();
        boolean estado =true;

        if(idSeleccionado == -1){
            Toast.makeText(this, "Debe seleccionar un nivel de estudio", Toast.LENGTH_SHORT).show();
            return false;
        }
        return estado;
    }

    private boolean validarIntereses() {
        boolean haySeleccion = chkDeporte.isChecked() ||
                chkMusica.isChecked() ||
                chkArte.isChecked() ||
                chkTecnologia.isChecked();

        if (!haySeleccion) {
            Toast.makeText(this, "Debe seleccionar al menos un interés", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void eventoBoton(View view) {

        if (validarSeleccionRadio()== false) {
            return;
        }

        if (validarIntereses() == false) {
            return;
        }
    }

    public String getNombre()          { return nombre; }
    public String getApellido()        { return apellido; }
    public String getTipoTelefono()    { return tipoTelefono; }
    public String getTelefono()        { return telefono; }
    public String getTipoEmail()       { return tipoEmail; }
    public String getEmail()           { return email; }
    public String getDireccion()       { return direccion; }
    public String getFechaNacimiento() { return fechaNacimiento; }

    /** true si el usuario activó el Switch. Para usar desde la validación y el guardado en SQLite. */
    public boolean getRecibirInformacion() {
        return switchInfo.isChecked();
    }
}
