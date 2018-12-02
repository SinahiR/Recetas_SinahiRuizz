package mx.edu.ittepic.recetacocina_u4_sinahiruizroodriguez;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText nombre, id, observaciones, ingredientes,preparacion;
    Button insertar,consultar,eliminar,actualizar;
    BaseDatos base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id=findViewById(R.id.id);
       nombre=findViewById(R.id.nombre);
       observaciones=findViewById(R.id.observaciones);
       ingredientes=findViewById(R.id.ingredientes);
       preparacion=findViewById(R.id.preparacion);

       insertar=findViewById(R.id.insertar);
       consultar=findViewById(R.id.consultar);
       eliminar=findViewById(R.id.eliminar);
       actualizar=findViewById(R.id.actualizar);

       base=new BaseDatos(this,"primera", null,1);

       insertar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               codigoInsertar();

           }
       });

       consultar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                pedirID(1);
           }
       });

       eliminar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               pedirID(3);
           }
       });

       actualizar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (actualizar.getText().toString().startsWith("CONFIRMAR MODIFICACION")){
                   invocarConfirmacionActualizacion();
               }else{
                   pedirID(2);
               }

           }

       });

    }

    //Primeramente se pide el ID a consultar
    private void pedirID(final int origen) {

        final EditText pidoID=new EditText(this);
        pidoID.setInputType(InputType.TYPE_CLASS_NUMBER);
        pidoID.setHint("Valor entero mayor de 0");
        String mensaje="Escriba el ID a buscar";

        AlertDialog.Builder alerta=new AlertDialog.Builder(this);

        if(origen==2)
        {
         mensaje="Escriba el ID que desee modificar";
        }

        if(origen==3)
        {
            mensaje="Escriba el ID que desea eliminar";
        }

        alerta.setTitle("ATENCION").setMessage(mensaje)
        .setView(pidoID)
                .setPositiveButton("Buscar ID", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(pidoID.getText().toString().isEmpty())
                        {
                            Toast.makeText(MainActivity.this, "Debes escribir un numero", Toast.LENGTH_LONG).show();
                        return;
                        }
                        buscarDato(pidoID.getText().toString(), origen);
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancelar", null).show();

    }

    private void buscarDato(String idBuscar, int origen) {
        try{

            SQLiteDatabase tabla = base.getReadableDatabase();

            String SQL = "SELECT * FROM RECETAS WHERE ID="+idBuscar;

            Cursor resultado = tabla.rawQuery(SQL,null);
            if(resultado.moveToFirst()){//mover le primer resultado obtenido de la consulta

                if(origen==3){
                    //se consulto para borrar

                    String dato = idBuscar+"&"+ resultado.getString(1)+"&"+resultado.getString(2)+
                            "&"+resultado.getString(3)+"&"+resultado.getString(4);
                    invocarConfirmacionEliminacion(dato);
                    return;
                }

                id.setText(resultado.getString(0));
                nombre.setText(resultado.getString(1));
                ingredientes.setText(resultado.getString(2));
                preparacion.setText(resultado.getString(3));
                observaciones.setText(resultado.getString(4));

                if(origen==2){
                    //modificar
                    insertar.setEnabled(false);
                    consultar.setEnabled(false);
                    eliminar.setEnabled(false);
                    actualizar.setText("CONFIRMAR MODIFICACION");
                    id.setEnabled(false);
                }
            }else {
                //no hay resultado!
                Toast.makeText(this,"No se ENCONTRO EL RESULTADO",Toast.LENGTH_LONG).show();
            }
            tabla.close();

        }catch (SQLiteException e){
            Toast.makeText(this,"No se pudo buscar",Toast.LENGTH_LONG).show();
        }
    }
    private void invocarConfirmacionActualizacion(){
        AlertDialog.Builder confir = new AlertDialog.Builder(this);
        confir.setTitle("ALERTA!!").setMessage("" +
                "Seguro que deseas actualizar?")
                .setPositiveButton("si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aplicarActualizar();
                        dialog.dismiss();
                    }
                }).setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        }).show();
    }
    private void aplicarActualizar(){
        try{
            SQLiteDatabase tabla = base.getWritableDatabase();

            String SQL= "UPDATE RECETAS SET NOMBRE='"+nombre.getText().toString()+"', INGREDIENTES='"
                    +ingredientes.getText().toString()+"', PREPARACION='"+preparacion.getText().toString()+"', OBSERVACIONES='"+
                    observaciones.getText().toString()+"' WHERE ID="+id.getText().toString();
            tabla.execSQL(SQL);
            tabla.close();
            Toast.makeText(this,"Se modificaron los datos ",Toast.LENGTH_LONG).show();

        }catch (SQLiteException e){
            Toast.makeText(this,"Erorr al modificar",Toast.LENGTH_LONG).show();
        }

        id.setText("");
        nombre.setText("");
        ingredientes.setText("");
        observaciones.setText("");
        preparacion.setText("");
        insertar.setEnabled(true);
        consultar.setEnabled(true);
        eliminar.setEnabled(true);
        actualizar.setText("MODIFICAR");
        id.setEnabled(true);

    }

    private void invocarConfirmacionEliminacion(final String dato) {
        String datos[]=dato.split("&");
        final String id=datos[0];
        final String nombre=datos[1];
        final String ingredientes=datos[2];
        final String preparacion=datos[3];
        final String observaciones=datos[4];

        AlertDialog.Builder alerta= new AlertDialog.Builder(this);
        alerta.setTitle("ATENCION").setMessage("Seguro que deseas eliminar: "+"\n"+
         "Id: "+id+"\n"+
        "Receta: " + nombre+  "\n"+
        "Ingredientes: " + ingredientes+ "\n"+
        "Preparacion: " + preparacion+ "\n"+
        "Observaciones: " + observaciones)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eliminarMetodo(id);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", null).show();

    }



    private void eliminarMetodo(String idEliminar) {
        try
        {
            SQLiteDatabase tabla=base.getReadableDatabase();
            String SQL= "DELETE FROM RECETAS WHERE ID="+idEliminar;
            tabla.execSQL(SQL);
            tabla.close();

            Toast.makeText(MainActivity.this,"Se elimino correctamente", Toast.LENGTH_LONG).show();

            id.setText("");
            nombre.setText("");
            ingredientes.setText("");
            preparacion.setText("");
            observaciones.setText("");


        }
        catch (SQLiteException e)
        {
            Toast.makeText(MainActivity.this, "Error al eliminar datos",Toast.LENGTH_LONG).show();
        }
}


    private void codigoInsertar() {
        try{

            SQLiteDatabase tabla=base.getWritableDatabase();
            String SQL= "INSERT INTO RECETAS VALUES("+id.getText().toString()+", '"+nombre.getText().toString()+"' ," +
                    " '"+ingredientes.getText().toString()+"' , '"+preparacion.getText().toString()+"', '"+observaciones.getText().toString()+"')";


            Toast.makeText(MainActivity.this, "Se inserto correctamente", Toast.LENGTH_LONG).show();

            tabla.execSQL(SQL); //PONERLA EN EL EXAMEN
            tabla.close();
            id.setText("");
            nombre.setText("");
            ingredientes.setText("");
            preparacion.setText("");
            observaciones.setText("");
        }
        catch (SQLiteException e)
        {
            Toast.makeText(this, "No se pudo", Toast.LENGTH_LONG).show();
        }
    }
}
