package pt.ipg.compralivros;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;


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


public class DentroDoComprarInserir extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static  final int ID_CURSOR_LOADER_LIVRO= 0;

    private Spinner spinnerLivro;
    private EditText editTextInserirPreco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentro_do_comprar_inserir);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportLoaderManager().initLoader(ID_CURSOR_LOADER_LIVRO, null, this);


        spinnerLivro = (Spinner) findViewById(R.id.spinnerLivro);
        editTextInserirPreco = (EditText) findViewById(R.id.InserirPreco);



    }

    @Override
    protected void onResume() {

        getSupportLoaderManager().restartLoader(ID_CURSOR_LOADER_LIVRO, null, this);


        super.onResume();
    }

    private void showLivroSpinner(Cursor cursorLivro) {
        SimpleCursorAdapter adaptadorLivro = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursorLivro,
                new String[]{BdTableLivros.CAMPO_TITULO},
                new int[]{android.R.id.text1}
        );
        spinnerLivro.setAdapter(adaptadorLivro);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comprar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_inserir) {
            save();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void save() {

        double valor = 0;
        String conteudoPreco = editTextInserirPreco.getText().toString();
        valor = Double.parseDouble(conteudoPreco);

        if (conteudoPreco.trim().length() == 0){
            editTextInserirPreco.setError("Inserir o Preço");
            editTextInserirPreco.requestFocus();
            return;
        } else  if (valor == 0) {
            editTextInserirPreco.setError("Valor maior que 0");
            editTextInserirPreco.requestFocus();
            return;
        }


        long idLivro = spinnerLivro.getSelectedItemId();


        Compras compras = new Compras();

        compras.setLivro(idLivro);
        compras.setPreco(valor);


        try {
            getContentResolver().insert(LivrosContentProvider.ENDERECO_COMPRAS, compras.getContentValues());

            Toast.makeText(this, "Guardar com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Snackbar.make(editTextInserirPreco, "Error while saving!", Snackbar.LENGTH_LONG).show();
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


            androidx.loader.content.CursorLoader cursorLoaderLivro = new androidx.loader.content.CursorLoader(this, LivrosContentProvider.ENDERECO_LIVROS, BdTableLivros.TODAS_COLUNAS, null, null, BdTableLivros.CAMPO_TITULO
            );
            return cursorLoaderLivro;


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
        showLivroSpinner(data);
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

        showLivroSpinner(null);

    }
}
