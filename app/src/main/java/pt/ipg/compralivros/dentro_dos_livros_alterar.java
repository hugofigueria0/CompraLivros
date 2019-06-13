package pt.ipg.compralivros;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.Spinner;
import android.widget.Toast;

public class dentro_dos_livros_alterar extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int ID_CURSO_LOADER_CATEGORIAS = 0;

    private EditText editTextTitulo;
    private EditText editTextCategoria;
    private EditText editTextData;

    private Livro livro = null;



    private Uri enderecoLivroEditar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentro_dos_livros_alterar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextTitulo = (EditText) findViewById(R.id.editTextTitulo);
        editTextCategoria = (EditText) findViewById(R.id.editTextCategoria);
        editTextData = (EditText) findViewById(R.id.editTextData);

        getSupportLoaderManager().initLoader(ID_CURSO_LOADER_CATEGORIAS, null, this);

        Intent intent = getIntent();

        long idLivro = intent.getLongExtra(DentroDosLivros.ID_LIVRO, -1);

        if (idLivro == -1) {
            Toast.makeText(this, "Erro: não foi possível ler o livro", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        enderecoLivroEditar = Uri.withAppendedPath(LivrosContentProvider.ENDERECO_LIVROS, String.valueOf(idLivro));

        Cursor cursor = getContentResolver().query(enderecoLivroEditar, BdTableLivros.TODAS_COLUNAS, null, null, null);

        if (!cursor.moveToNext()) {
            Toast.makeText(this, "Erro: não foi possível ler o livro", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        livro = Livro.fromCursor(cursor);

        editTextTitulo.setText(livro.getTitulo());
        editTextCategoria.setText(livro.getCategoria());
        editTextData.setText(String.valueOf(livro.getData()));



    }

    @Override
    protected void onResume() {
        getSupportLoaderManager().restartLoader(ID_CURSO_LOADER_CATEGORIAS, null, this);

        super.onResume();
    }

    public void Cancelar(View view){
        Toast.makeText(this, "Cancelar", Toast.LENGTH_LONG).show();
        finish();
    }

    public void Alterar(View view){

        ValidarEscrita();
        Toast.makeText(this, "Foi alterado", Toast.LENGTH_LONG).show();

    }

    public void ValidarEscrita(){

        String titulo = editTextTitulo.getText().toString();

        if (titulo.trim().isEmpty()) {
            editTextTitulo.setError("Preencha o titulo");
            return;
        }

        String data = editTextData.getText().toString();

        if (data.length() != 4) {
            editTextData.setError("Preencha o titulo");
            return;
        }

        String Categoria = editTextCategoria.getText().toString();

        if (Categoria.trim().isEmpty()) {
            editTextCategoria.setError("Preencha o titulo");
            return;
        }


        livro.setTitulo(titulo);
        livro.setCategoria(Categoria);
        livro.setData(data);

        try {
            getContentResolver().update(enderecoLivroEditar, livro.getContentValues(), null, null);

            Toast.makeText(this, "livro alterado", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Snackbar.make(
                    editTextTitulo,
                    "Erro ao alterar",
                    Snackbar.LENGTH_LONG)
                    .show();

            e.printStackTrace();
        }

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
