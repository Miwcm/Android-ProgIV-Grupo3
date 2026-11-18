package frgp.utn.edu.ar;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

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

    public List<Contacto> obtenerContactos() {
        List<Contacto> contactos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String orden = ContactosColumnaApellido + " COLLATE NOCASE ASC, "
                + ContactosColumnaNombre + " COLLATE NOCASE ASC";

        try (Cursor cursor = db.query(ContactosTable, null, null, null, null, null, orden)) {
            int colId = cursor.getColumnIndexOrThrow(ContactosColumnaID);
            int colNombre = cursor.getColumnIndexOrThrow(ContactosColumnaNombre);
            int colApellido = cursor.getColumnIndexOrThrow(ContactosColumnaApellido);
            int colTipoTelefono = cursor.getColumnIndexOrThrow(ContactosColumnaTipoTelefono);
            int colTelefono = cursor.getColumnIndexOrThrow(ContactosColumnaTelefono);
            int colTipoEmail = cursor.getColumnIndexOrThrow(ContactosColumnaTipoEmail);
            int colEmail = cursor.getColumnIndexOrThrow(ContactosColumnaEmail);
            int colDireccion = cursor.getColumnIndexOrThrow(ContactosColumnaDireccion);
            int colFecha = cursor.getColumnIndexOrThrow(ContactosColumnaFechaNacimiento);
            int colEstudios = cursor.getColumnIndexOrThrow(ContactosColumnaNivelEstudios);
            int colIntereses = cursor.getColumnIndexOrThrow(ContactosColumnaIntereses);
            int colRecibirInfo = cursor.getColumnIndexOrThrow(ContactosColumnaRecibirInfo);

            while (cursor.moveToNext()) {
                Contacto c = new Contacto();
                c.setId(cursor.getInt(colId));
                c.setNombre(cursor.getString(colNombre));
                c.setApellido(cursor.getString(colApellido));
                c.setTipoTelefono(cursor.getString(colTipoTelefono));
                c.setTelefono(cursor.getString(colTelefono));
                c.setTipoEmail(cursor.getString(colTipoEmail));
                c.setEmail(cursor.getString(colEmail));
                c.setDireccion(cursor.getString(colDireccion));
                c.setFechaNacimiento(cursor.getString(colFecha));
                c.setNivelEstudios(cursor.getString(colEstudios));
                c.setIntereses(cursor.getString(colIntereses));
                c.setRecibirInfo(cursor.getInt(colRecibirInfo) == 1);
                contactos.add(c);
            }
        }
        return contactos;
    }

    public void abrir() {
        this.getWritableDatabase();
    }

    public void cerrar() {
        this.close();
    }


}