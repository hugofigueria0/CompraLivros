package pt.ipg.compralivros;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Before
    public void apagaBaseDados() {
        getAppContext().deleteDatabase(BdLivrosOpenHelper.NOME_BASE_DADOS);
    }

    @Test
    public void criaBdLivros() {
        Context appContext = getAppContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);

        SQLiteDatabase db = openHelper.getReadableDatabase();

        assertTrue(db.isOpen());
    }

    private Context getAppContext() {
        return InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testCRUD() {

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(getAppContext());
        SQLiteDatabase db = openHelper.getWritableDatabase();

        BdTableLivros tabelaLivros = new BdTableLivros(db);


        // Teste read livros (cRud)
        Cursor cursorLivros = getLivros(tabelaLivros);
        assertEquals(0, cursorLivros.getCount());

        //Teste create/read livros(CRud)
        String titulo = "Pablo Escobar";
        String categoria = "Terror";
        String data = "1996";

        long id = criaLivro(tabelaLivros, titulo, data, categoria);

        cursorLivros = getLivros(tabelaLivros);
        assertEquals(1, cursorLivros.getCount());

        Livro livro = getLivroComID(cursorLivros, id);

        assertEquals(titulo, livro.getTitulo());
        assertEquals(data, livro.getData());
        assertEquals(categoria,livro.getCategoria());


        titulo = "Algoritmos";
        data = "1996";
        categoria = "Terror";
        id = criaLivro(tabelaLivros, titulo, data, categoria);
        cursorLivros = getLivros(tabelaLivros);
        assertEquals(2, cursorLivros.getCount());

        livro = getLivroComID(cursorLivros, id);
        assertEquals(titulo, livro.getTitulo());
        assertEquals(data, livro.getData());
        assertEquals(categoria, livro.getCategoria());

        id = criaLivro(tabelaLivros, "Algoritmos", "1996", "Terror");
        cursorLivros = getLivros(tabelaLivros);
        assertEquals(3, cursorLivros.getCount());


        livro = getLivroComID(cursorLivros, id);
        titulo = "Alemanha";
        categoria= "Romantico";
        data = "1998";

        livro.setTitulo(titulo);
        livro.setData(data);
        livro.setCategoria(categoria);

        tabelaLivros.update(livro.getContentValues(), BdTableLivros._ID + "=?", new String[]{String.valueOf(id)});

        cursorLivros = getLivros(tabelaLivros);

        livro = getLivroComID(cursorLivros, id);
        assertEquals(titulo, livro.getTitulo());
        assertEquals(data, livro.getData());
        assertEquals(categoria, livro.getCategoria());

        tabelaLivros.delete(BdTableLivros._ID + "=?", new String[]{String.valueOf(id)});
        cursorLivros = getLivros(tabelaLivros);
        assertEquals(2, cursorLivros.getCount());
    }

    private Cursor getLivros(BdTableLivros tabelaLivros) {
        return tabelaLivros.query(BdTableLivros.TODAS_COLUNAS, null, null, null, null, null);
    }

    private long criaLivro(BdTableLivros tabelaLivros, String titulo, String data, String categoria) {
        Livro livro = new Livro();

        livro.setTitulo(titulo);
        livro.setData(data);
        livro.setCategoria(categoria);

        long id = tabelaLivros.insert(livro.getContentValues());
        assertNotEquals(-1, id);

        return id;
    }

    private Livro getLivroComID(Cursor cursor, long id) {
        Livro livro = null;

        while (cursor.moveToNext()) {
            livro = Livro.fromCursor(cursor);

            if (livro.getId() == id) {
                break;
            }
        }

        assertNotNull(livro);

        return livro;
    }




}
