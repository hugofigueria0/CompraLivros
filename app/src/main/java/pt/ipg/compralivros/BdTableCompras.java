package pt.ipg.compralivros;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

public class BdTableCompras implements BaseColumns {

    public static  final String NOME_TABELA = "Compras";

    public static  final String ALIAS_NOME_LIVRO = "nome_livro";

    public static final String CAMPO_PRECO = "preco";
    public static final String CAMPO_LIVRO = "livro";

    public  static  final String CAMPO_NOME_LIVRO = BdTableLivros.NOME_TABELA + "." + BdTableLivros.CAMPO_TITULO + " AS " + ALIAS_NOME_LIVRO;//Só leitura

    public static final String[] TODAS_COLUNAS = new String[] { NOME_TABELA + "." + _ID, CAMPO_PRECO, CAMPO_LIVRO, CAMPO_NOME_LIVRO};

    private SQLiteDatabase db;

    public BdTableCompras (SQLiteDatabase db){

        this.db = db;
    }

    public void cria () {

        db.execSQL(
                "CREATE TABLE " + NOME_TABELA + "(" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        CAMPO_PRECO + " DOUBLE NOT NULL," +
                        CAMPO_LIVRO + " INTEGER NOT NULL," +
                        "FOREIGN KEY (" + CAMPO_LIVRO + ") REFERENCES " + BdTableLivros.NOME_TABELA + "(" + BdTableLivros._ID + ")" +
                        ")"

        );
    }

        public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        String colunasSelect = TextUtils.join(",", columns);

        String sql = "SELECT " + colunasSelect + " FROM " +
                NOME_TABELA + " INNER JOIN " + BdTableLivros.NOME_TABELA + " WHERE " + NOME_TABELA + "." + CAMPO_LIVRO + "=" + BdTableLivros.NOME_TABELA + "." + BdTableLivros._ID
                ;

        if (selection != null) {
            sql += " AND " + selection;
        }

        Log.d("Tabela Livros", "query: " + sql);

        return db.rawQuery(sql, selectionArgs);
        }

        public long insert(ContentValues values) {
            return db.insert(NOME_TABELA, null, values);
        }

        public int update(ContentValues values, String whereClause, String [] whereArgs) {
            return db.update(NOME_TABELA, values, whereClause, whereArgs);
        }

        public int delete(String whereClause, String[] whereArgs) {
            return db.delete(NOME_TABELA, whereClause, whereArgs);
        }


}
