package mx.edu.ittepic.recetacocina_u4_sinahiruizroodriguez;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class BaseDatos extends SQLiteOpenHelper {
    public BaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //CREATETABLE y en caso de ser necesario inserta datos predeterminados (los insert)
    //Se ejecuta cuando la aplicacin se incia en el celular
    //Sirve para construir en el SQLITE que esta CEL las tablas que la APP requiere para funcionar
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Funciona para insert, create table, delate, update
        sqLiteDatabase.execSQL("CREATE TABLE RECETAS(ID INTEGER PRIMARY KEY NOT NULL, NOMBRE VARCHAR(200), INGREDIENTES VARCHAR(1000), PREPARACION VARCHAR (1000), OBSERVACIONES VARCHAR(500))");
        //Si requieres otra tabla hacer lo mismo que la linea de arriba

        //Select, regresa un tipo cursor
        // sqLiteDatabase.rawQuery()

    }

    @Override
    //Se ejecuta cuando se va a modificar la estructura de la tabla
    //Alteracion de tablas y estructuras
    //Las versiones nunca va en descenso
    //Upgrate   ejecuta una actualizacion mayor, se modifican tablas no datos
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVesion, int newVersion) {

    }
}
