package pt.ipg.compralivros;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Toast;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    public void Livros(View view){

        Toast.makeText(this, "Livros", Toast.LENGTH_LONG).show();
        Intent intent = new Intent( this, DentroDosLivros.class);
        startActivity(intent);

    }

    public void Comprar(View view){

        Toast.makeText(this, "Comprar", Toast.LENGTH_LONG).show();
        Intent intent = new Intent( this, DentroDoComprar.class);
        startActivity(intent);

    }

    public void BandaDesenhada(View view){

        Toast.makeText(this, "Livros", Toast.LENGTH_LONG).show();
        Intent intent = new Intent( this, DentroDaBandaDesenhada.class);
        startActivity(intent);

    }

}
