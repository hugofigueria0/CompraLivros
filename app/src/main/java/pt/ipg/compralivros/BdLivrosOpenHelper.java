package pt.ipg.compralivros;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BdLivrosOpenHelper extends SQLiteOpenHelper {

    public static final String NOME_BASE_DADOS = "compralivros.db";
    private static final int VERSAO_BASE_DADOS = 1;

    public BdLivrosOpenHelper(@Nullable Context context) {
        super(context, NOME_BASE_DADOS, null, VERSAO_BASE_DADOS);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        new BdTableLivros(db).cria();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
