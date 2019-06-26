package pt.ipg.compralivros;

import android.content.ContentValues;
import android.database.Cursor;

public class Compras {

    private long id;
    private double preco;
    private long livro;
    private String nomeLivro; //CAMPO "Externo"

    public String getNomeLivro() {
        return nomeLivro;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public long getLivro() {
        return livro;
    }

    public void setLivro(long livro) {
        this.livro = livro;
    }

    public ContentValues getContentValues() {

        ContentValues valores = new ContentValues();

        valores.put(BdTableCompras.CAMPO_PRECO, preco);
        valores.put(BdTableCompras.CAMPO_LIVRO, livro);

        return valores;
    }

    public static Compras fromCursor(Cursor cursor) {

        long id = cursor.getLong(cursor.getColumnIndex(BdTableCompras._ID));

        Double preco = cursor.getDouble(cursor.getColumnIndex(BdTableCompras.CAMPO_PRECO));

        long livro = cursor.getLong(cursor.getColumnIndex(BdTableCompras.CAMPO_LIVRO));

        String nomeLivro = cursor.getString(cursor.getColumnIndex(BdTableCompras.ALIAS_NOME_LIVRO));

        Compras compras = new Compras();

        compras.setId(id);
        compras.setPreco(preco);
        compras.setLivro(livro);
        compras.nomeLivro = nomeLivro;

        return compras;
    }

}
