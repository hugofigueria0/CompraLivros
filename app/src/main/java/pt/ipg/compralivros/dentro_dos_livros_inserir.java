package pt.ipg.compralivros;

import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class dentro_dos_livros_inserir extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_CURSO_LOADER_LIVROS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentro_dos_livros_inserir);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportLoaderManager().initLoader(ID_CURSO_LOADER_LIVROS, null, this);
    }

    public void Cancelar(View view){
        Toast.makeText(this, "Cancelar", Toast.LENGTH_LONG).show();
        finish();
    }

    public void Guardar(View view){

        EditText editeditTextTitulo = (EditText) findViewById(R.id.editTextTitulo);
        String Livre = editeditTextTitulo.getText().toString();

        EditText editeditTextCategoria = (EditText) findViewById(R.id.editTextCategoria);
        String Genero = editeditTextCategoria.getText().toString();

        EditText editeditTextData = (EditText) findViewById(R.id.editTextData);
        String Data = editeditTextData.getText().toString();

        if(Livre.trim().length() == 0){

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

        Livro livro = new Livro();

        livro.setTitulo(Livre);
        livro.setCategoria(Genero);
        livro.setData(Data);


        try {
            getContentResolver().insert(LivrosContentProvider.ENDERECO_LIVROS,livro.getContentValues());

            Toast.makeText(this, "Livro Guardado Com Sucesso", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Snackbar.make(
                    editeditTextTitulo,
                    "Erro!!",
                    Snackbar.LENGTH_LONG)
                    .show();

            e.printStackTrace();
        }


    }


    @Override
    protected void onResume() {
        getSupportLoaderManager().restartLoader(ID_CURSO_LOADER_LIVROS, null, this);

        super.onResume();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        androidx.loader.content.CursorLoader cursorLoader = new androidx.loader.content.CursorLoader(this, LivrosContentProvider.ENDERECO_LIVROS, BdTableLivros.TODAS_COLUNAS, null, null, BdTableLivros.CAMPO_TITULO
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
