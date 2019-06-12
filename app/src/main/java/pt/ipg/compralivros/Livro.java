package pt.ipg.compralivros;

import android.content.ContentValues;
import android.database.Cursor;

public class Livro {

    private long id ;
    private String titulo;
    private String categoria;
    private String data;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getData(){return data;}

    public void setData(String data){ this.data = data; }






    public  ContentValues getContentValues() {
        ContentValues valores = new ContentValues();

        valores.put(BdTableLivros.CAMPO_TITULO, titulo);
        valores.put(BdTableLivros.CAMPO_Data, data);
        valores.put(BdTableLivros.CAMPO_CATEGORIA, categoria);

        return valores;
    }

    public static Livro fromCursor(Cursor cursor) {
        long id = cursor.getLong(
                cursor.getColumnIndex(BdTableLivros._ID)
        );

        String titulo = cursor.getString(
                cursor.getColumnIndex(BdTableLivros.CAMPO_TITULO)
        );

        String data = cursor.getString(
                cursor.getColumnIndex(BdTableLivros.CAMPO_Data)
        );

        String categoria = cursor.getString(
                cursor.getColumnIndex(BdTableLivros.CAMPO_CATEGORIA)
        );



        Livro livro = new Livro();

        livro.setId(id);
        livro.setTitulo(titulo);
        livro.setData(data);
        livro.setCategoria(categoria);

        return livro;
    }

}
