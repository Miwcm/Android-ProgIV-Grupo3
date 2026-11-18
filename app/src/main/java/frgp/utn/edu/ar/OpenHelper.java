package frgp.utn.edu.ar;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OpenHelper extends SQLiteOpenHelper {

    public static final String NOMBRE_BD = "BD_Contactos";
    public static final int VERSION_BD = 1;

    public static final String ContactosTable = "contactos";
    public static final String ContactosColumnaID = "_ID";
    public static final String ContactosColumnaNombre = "nombre";
    public static final String ContactosColumnaApellido = "apellido";
    public static final String ContactosColumnaTipoTelefono = "tipoTelefono";
    public static final String ContactosColumnaTelefono = "telefono";
    public static final String ContactosColumnaTipoEmail = "tipoEmail";
    public static final String ContactosColumnaEmail = "email";
    public static final String ContactosColumnaDireccion = "direccion";
    public static final String ContactosColumnaFechaNacimiento = "fechaNacimiento";
    public static final String ContactosColumnaNivelEstudios = "nivelEstudios";
    public static final String ContactosColumnaIntereses = "intereses";       // ej: "Deporte,Arte" separado con coma
    public static final String ContactosColumnaRecibirInfo = "recibirInfo";   // 1 = sí, 0 = no

    public static final String ContactosCreaciontable =
            "CREATE TABLE IF NOT EXISTS " + ContactosTable + "("
                    + ContactosColumnaID + " integer primary key autoincrement, "
                    + ContactosColumnaNombre + " text, "
                    + ContactosColumnaApellido + " text, "
                    + ContactosColumnaTipoTelefono + " text, "
                    + ContactosColumnaTelefono + " text, "
                    + ContactosColumnaTipoEmail + " text, "
                    + ContactosColumnaEmail + " text, "
                    + ContactosColumnaDireccion + " text, "
                    + ContactosColumnaFechaNacimiento + " text, "
                    + ContactosColumnaNivelEstudios + " text, "
                    + ContactosColumnaIntereses + " text, "
                    + ContactosColumnaRecibirInfo + " integer);";

    public OpenHelper(@Nullable Context context, @Nullable String name,
                      @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContactosCreaciontable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS " + ContactosTable);
        onCreate(db);
    }

    public void abrir() {
        this.getWritableDatabase();
    }

    public void cerrar() {
        this.close();
    }


}