package pt.ipg.compralivros;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DentroDosLivros extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_CURSO_LOADER_LIVROS = 0;
    public static final String ID_LIVRO = "ID_LIVRO";

    private RecyclerView recyclerViewLivros;
    private AdaptadorLivros adaptadorLivros;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentro_dos_livros);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportLoaderManager().initLoader(ID_CURSO_LOADER_LIVROS, null, this);

        recyclerViewLivros = (RecyclerView) findViewById(R.id.recyclerViewLivros);
        adaptadorLivros = new AdaptadorLivros(this);
        recyclerViewLivros.setAdapter(adaptadorLivros);
        recyclerViewLivros.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_livros, menu);

        this.menu = menu;

        return true;
    }

    @Override
    protected void onResume() {
        getSupportLoaderManager().restartLoader(ID_CURSO_LOADER_LIVROS, null, this);

        super.onResume();
    }


    public void atualizaOpcoesMenu() {
        Livro livro = adaptadorLivros.getLivroSelecionado();

        boolean mostraAlterarEliminar = (livro != null);

        menu.findItem(R.id.action_alterar).setVisible(mostraAlterarEliminar);
        menu.findItem(R.id.action_eliminar).setVisible(mostraAlterarEliminar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_inserir) {
            Intent intent = new Intent( this, dentro_dos_livros_inserir.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_alterar) {
            Intent intent = new Intent( this, dentro_dos_livros_alterar.class);
            intent.putExtra(ID_LIVRO, adaptadorLivros.getLivroSelecionado().getId());
            startActivity(intent);
            return true;
        } else if (id == R.id.action_eliminar) {
            Intent intent = new Intent( this, dentro_dos_livros_eliminar.class);
            intent.putExtra(ID_LIVRO, adaptadorLivros.getLivroSelecionado().getId());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this, LivrosContentProvider.ENDERECO_LIVROS, BdTableLivros.TODAS_COLUNAS, null, null, BdTableLivros.CAMPO_TITULO
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        adaptadorLivros.setCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adaptadorLivros.setCursor(null);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
