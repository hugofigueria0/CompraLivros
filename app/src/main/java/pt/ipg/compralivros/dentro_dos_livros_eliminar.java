package pt.ipg.compralivros;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class dentro_dos_livros_eliminar extends AppCompatActivity {

    private Uri enderecoLivroApagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentro_dos_livros_eliminar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TextView textViewTitulo = (TextView) findViewById(R.id.textViewTitulo);
        TextView textViewCategoria = (TextView) findViewById(R.id.textViewCategoria);
        TextView textViewData = (TextView) findViewById(R.id.textViewData);

        Intent intent = getIntent();

        long idLivro = intent.getLongExtra(DentroDosLivros.ID_LIVRO, -1);

        if (idLivro == -1) {
            Toast.makeText(this, "Erro: não foi possível apagar o livro", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        enderecoLivroApagar = Uri.withAppendedPath(LivrosContentProvider.ENDERECO_LIVROS, String.valueOf(idLivro));

        Cursor cursor = getContentResolver().query(enderecoLivroApagar, BdTableLivros.TODAS_COLUNAS, null, null, null);

        if (!cursor.moveToNext()) {
            Toast.makeText(this, "Erro: não foi possível apagar o livro", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        Livro livro = Livro.fromCursor(cursor);

        textViewTitulo.setText(livro.getTitulo());
        textViewCategoria.setText(livro.getCategoria());
        textViewData.setText(String.valueOf(livro.getData()));

    }


    public void Cancelar(View view){
        Toast.makeText(this, "Cancelar", Toast.LENGTH_LONG).show();
        finish();
    }

    public void Eliminar(View view){

        int livrosApagados = getContentResolver().delete(enderecoLivroApagar, null, null);

        if (livrosApagados == 1) {
            Toast.makeText(this, "Livro eliminado com sucesso", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Erro: Não foi possível eliminar o livro", Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this, "Foi eliminado", Toast.LENGTH_LONG).show();
    }



}





