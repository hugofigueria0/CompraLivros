package pt.ipg.compralivros;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class DentroDoComprarAlterar extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int ID_CURSO_LOADER_LIVROS = 0;

    private EditText editTextInserirPrecoAlterar;
    private Spinner spinnerLivroEditar;

    private Compras compras = null;

    private boolean livroCarregar = false;
    private boolean livroAtualizados = false;

    private Uri enderecoComprasEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentro_do_comprar_alterar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextInserirPrecoAlterar = (EditText) findViewById(R.id.InserirPrecoEditar);
        spinnerLivroEditar = (Spinner) findViewById(R.id.spinnerLivroEditar);

        getSupportLoaderManager().initLoader(ID_CURSO_LOADER_LIVROS, null, this);

        Intent intent = getIntent();

        long idCompras = intent.getLongExtra(DentroDoComprar.ID_COMPRAS, -1);

        if (idCompras == -1) {
            Toast.makeText(this, "Erro: não foi possível ler o livro", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        enderecoComprasEditar = Uri.withAppendedPath(LivrosContentProvider.ENDERECO_COMPRAS, String.valueOf(idCompras));

        Cursor cursor = getContentResolver().query(enderecoComprasEditar, BdTableCompras.TODAS_COLUNAS, null, null, null);

        if (!cursor.moveToNext()) {
            Toast.makeText(this, "Erro: não foi possível ler o livro", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        compras = Compras.fromCursor(cursor);

        editTextInserirPrecoAlterar.setText(String.valueOf(compras.getPreco()));


        actualizaComprasSelecionada();

    }

    private void actualizaComprasSelecionada() {

        if (!livroCarregar) return;
        if (livroAtualizados) return;

        for (int i = 0; i < spinnerLivroEditar.getCount(); i++) {
            if (spinnerLivroEditar.getItemIdAtPosition(i) == compras.getLivro()) {
                spinnerLivroEditar.setSelection(i);
                break;
            }
        }

        livroAtualizados = true;
    }


    @Override
    protected void onResume() {
        getSupportLoaderManager().restartLoader(ID_CURSO_LOADER_LIVROS, null, this);

        super.onResume();
    }

    private void mostraLivroSpinner(Cursor cursorLivros) {
        SimpleCursorAdapter adaptadorLivros = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursorLivros,
                new String[]{BdTableLivros.CAMPO_TITULO},
                new int[]{android.R.id.text1}
        );
        spinnerLivroEditar.setAdapter(adaptadorLivros);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comprar, menu);
        return true;
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


        double valor = 0;
        String conteudoPreco = editTextInserirPrecoAlterar.getText().toString();
        valor = Double.parseDouble(conteudoPreco);

        if (conteudoPreco.trim().length() == 0){
            editTextInserirPrecoAlterar.setError("Inserir o Preço");
            editTextInserirPrecoAlterar.requestFocus();
            return;
        } else  if (valor == 0) {
            editTextInserirPrecoAlterar.setError("Valor maior que 0");
            editTextInserirPrecoAlterar.requestFocus();
            return;
        }


        long idLivro = spinnerLivroEditar.getSelectedItemId();

        compras.setLivro(idLivro);
        compras.setPreco(valor);


        try {
            getContentResolver().update(enderecoComprasEditar, compras.getContentValues(), null, null);

            Toast.makeText(this, "Alterado com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Snackbar.make(
                    editTextInserirPrecoAlterar,
                    "ERROU",
                    Snackbar.LENGTH_LONG)
                    .show();

            e.printStackTrace();
        }
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * <p>This will always be called from the process's main thread.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        androidx.loader.content.CursorLoader cursorLoader = new androidx.loader.content.CursorLoader(this, LivrosContentProvider.ENDERECO_LIVROS, BdTableLivros.TODAS_COLUNAS, null, null, BdTableLivros.CAMPO_TITULO
        );

        return cursorLoader;
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     *
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     *
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context,
     * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * <p>This will always be called from the process's main thread.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        mostraLivroSpinner(data);
        livroCarregar = true;
        actualizaComprasSelecionada();
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * <p>This will always be called from the process's main thread.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        livroCarregar = false;
        livroAtualizados = false;
        mostraLivroSpinner(null);

    }
}
