package pt.ipg.compralivros;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DentroDoComprarEliminar extends AppCompatActivity {

    private Uri enderecoComprasApagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentro_do_comprar_eliminar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textViewLivroNomeApagar = (TextView) findViewById(R.id.textViewLivroNomeApaga);
        TextView textViewPrecoApaga = (TextView) findViewById(R.id.textViewPrecoApaga);


        Intent intent = getIntent();
        long idCompras = intent.getLongExtra(DentroDoComprar.ID_COMPRAS, -1);
        if (idCompras == -1) {
            Toast.makeText(this, "Erro: não foi possível apagar o livro", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        enderecoComprasApagar = Uri.withAppendedPath(LivrosContentProvider.ENDERECO_COMPRAS, String.valueOf(idCompras));

        Cursor cursor = getContentResolver().query(enderecoComprasApagar, BdTableCompras.TODAS_COLUNAS, null, null, null);

        if (!cursor.moveToNext()) {
            Toast.makeText(this, "Erro: não foi possível apagar o livro", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Compras compras = Compras.fromCursor(cursor);

        textViewLivroNomeApagar.setText(compras.getNomeLivro());
        textViewPrecoApaga.setText(String.valueOf(compras.getPreco()));
    }

    public void Cancelar(View view){
        Toast.makeText(this, "Cancelar", Toast.LENGTH_LONG).show();
        finish();
    }

    public void Eliminar(View view){

        int ComprasApagados = getContentResolver().delete(enderecoComprasApagar, null, null);

        if (ComprasApagados == 1) {
            Toast.makeText(this, "Compras eliminado com sucesso", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Erro: Não foi possível eliminar a compra", Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this, "Foi eliminado", Toast.LENGTH_LONG).show();
    }


}
