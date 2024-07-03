package ar.edu.davinci.aplicacin_seguimientos_de_gastos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BaseDeDatos extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gastos.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_GASTOS = "gastos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CANTIDAD = "cantidad";
    private static final String COLUMN_CATEGORIA = "categoria";
    private static final String COLUMN_FECHA = "fecha";
    private static final String COLUMN_NOTA = "nota";

    public BaseDeDatos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_GASTOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CANTIDAD + " REAL, "
                + COLUMN_CATEGORIA + " TEXT, "
                + COLUMN_FECHA + " TEXT, "
                + COLUMN_NOTA + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GASTOS);
        onCreate(db);
    }

    public void addGasto(Gasto gasto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CANTIDAD, gasto.getCantidad());
        values.put(COLUMN_CATEGORIA, gasto.getCategoria());
        values.put(COLUMN_FECHA, gasto.getFecha());
        values.put(COLUMN_NOTA, gasto.getNota());
        db.insert(TABLE_GASTOS, null, values);
        db.close();
    }

    public List<Gasto> getAllGastos() {
        List<Gasto> gastos = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_GASTOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Gasto gasto = new Gasto(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORIA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTA))
                );
                gastos.add(gasto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return gastos;
    }

    public void deleteGasto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GASTOS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // metodo para obtener los gastos filtrados
    public List<Gasto> getGastosFiltrados(String categoria, String fecha) {
        List<Gasto> gastos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_GASTOS + " WHERE 1=1");

        List<String> argsList = new ArrayList<>();

        if (!categoria.equals("Todos")) {
            query.append(" AND ").append(COLUMN_CATEGORIA).append(" = ?");
            argsList.add(categoria);
        }

        if (!fecha.isEmpty()) {
            query.append(" AND ").append(COLUMN_FECHA).append(" = ?");
            argsList.add(fecha);
        }

        String[] args = argsList.toArray(new String[0]);
        Cursor cursor = db.rawQuery(query.toString(), args);

        if (cursor.moveToFirst()) {
            do {
                Gasto gasto = new Gasto(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORIA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTA))
                );
                gastos.add(gasto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return gastos;
    }
}

