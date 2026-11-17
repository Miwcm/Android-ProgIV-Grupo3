package frgp.utn.edu.ar;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MasDatosContacto extends AppCompatActivity {
    public static final String EXTRA_DATOS_CONTACTO = "frgp.utn.edu.ar.DATOS_CONTACTO";

    private Bundle datosContacto;
    private SwitchCompat switchInfo;

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
    }
    private void configurarSwitchInfo() {
        switchInfo = findViewById(R.id.switchInfo);
        switchInfo.setOnCheckedChangeListener((boton, activado) ->
                switchInfo.setText(activado ? R.string.switch_info_si : R.string.switch_info_no));
    }

    /** true si el usuario activó el Switch. Para usar desde la validación y el guardado en SQLite. */
    public boolean getRecibirInformacion() {
        return switchInfo.isChecked();
    }
}
