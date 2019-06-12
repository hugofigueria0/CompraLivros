package pt.ipg.compralivros;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

public class BdTableLivros implements BaseColumns {
    public static final String NOME_TABELA = "livros";


    public static final String CAMPO_TITULO = "titulo";
    public static final String CAMPO_Data = "data";
    public static final String CAMPO_CATEGORIA = "categoria";


    public static final String[] TODAS_COLUNAS = new String[] { NOME_TABELA + "." + _ID, CAMPO_TITULO, CAMPO_Data, CAMPO_CATEGORIA};

    private SQLiteDatabase db;

    public BdTableLivros(SQLiteDatabase db) {
        this.db = db;
    }

    public void cria() {
        db.execSQL(
                "CREATE TABLE " + NOME_TABELA + "(" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        CAMPO_TITULO + " TEXT NOT NULL," +
                        CAMPO_Data + " TEXT NOT NULL," +
                        CAMPO_CATEGORIA + " TEXT NOT NULL" +
                        ")"
        );
    }

    public Cursor query (String[]columns, String selection, String[]selectionArg, String groupby, String having, String orderBy){
        return db.query(NOME_TABELA, columns, selection, selectionArg, groupby, having, orderBy);
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
