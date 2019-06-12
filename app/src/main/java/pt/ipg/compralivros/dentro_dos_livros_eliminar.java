package pt.ipg.compralivros;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class dentro_dos_livros_eliminar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentro_dos_livros_eliminar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void Cancelar(View view){
        Toast.makeText(this, "Cancelar", Toast.LENGTH_LONG).show();
        finish();
    }

    public void Eliminar(View view){
        ValidarEscrita();
        Toast.makeText(this, "Foi eliminado", Toast.LENGTH_LONG).show();

    }

    public void ValidarEscrita(){

        EditText editeditTextTitulo = (EditText) findViewById(R.id.editTextTitulo);
        String Livro = editeditTextTitulo.getText().toString();

        EditText editeditTextCategoria = (EditText) findViewById(R.id.editTextCategoria);
        String Genero = editeditTextCategoria.getText().toString();

        EditText editeditTextData = (EditText) findViewById(R.id.editTextData);
        String Data = editeditTextData.getText().toString();

        if(Livro.trim().length() == 0){

            editeditTextTitulo.setError("Titulo Invalido");
            editeditTextTitulo.requestFocus();
            return;


        }
        if(Genero.trim().length() == 0){

            editeditTextCategoria.setError("Categoria Invalida");
            editeditTextCategoria.requestFocus();
            return;

        }
        if(Data.trim().length() == 0){

            editeditTextData.setError("Data Invalida");
            editeditTextData.requestFocus();
            return;

        }


        finish();

    }


}
