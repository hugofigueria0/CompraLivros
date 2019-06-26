package pt.ipg.compralivros;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LivrosContentProvider extends ContentProvider {
    public static final String AUTHORITY = "pt.ipg.compralivros";
    public static final String CATEGORIAS = "categorias";
    public static final String LIVROS = "livros";
    public static final String COMPRAS = "compras";

    private static final Uri ENDERECO_BASE = Uri.parse("content://" + AUTHORITY);
    public static final Uri ENDERECO_CATEGORIAS = Uri.withAppendedPath(ENDERECO_BASE, CATEGORIAS);
    public static final Uri ENDERECO_LIVROS = Uri.withAppendedPath(ENDERECO_BASE, LIVROS);
    public static final Uri ENDERECO_COMPRAS = Uri.withAppendedPath(ENDERECO_BASE, COMPRAS);

    public static final int URI_CATEGORIAS = 100;
    public static final int URI_CATEGORIA_ESPECIFICA = 101;
    public static final int URI_LIVROS = 200;
    public static final int URI_LIVRO_ESPECIFICO = 201;
    public static final int URI_COMPRAS = 300;
    public static final int URI_COMPRAS_ESPECIFICO = 301;

    public static final String UNICO_ITEM = "vnd.android.cursor.item/";
    public static final String MULTIPLOS_ITEMS = "vnd.android.cursor.dir/";

    private BdLivrosOpenHelper bdLivrosOpenHelper;

    private UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, CATEGORIAS, URI_CATEGORIAS);
        uriMatcher.addURI(AUTHORITY, CATEGORIAS + "/#", URI_CATEGORIA_ESPECIFICA);

        uriMatcher.addURI(AUTHORITY, LIVROS, URI_LIVROS);
        uriMatcher.addURI(AUTHORITY, LIVROS + "/#", URI_LIVRO_ESPECIFICO);

        uriMatcher.addURI(AUTHORITY, COMPRAS, URI_COMPRAS);
        uriMatcher.addURI(AUTHORITY, COMPRAS + "/#", URI_COMPRAS_ESPECIFICO);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        bdLivrosOpenHelper = new BdLivrosOpenHelper(getContext());

        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase bd = bdLivrosOpenHelper.getReadableDatabase();

        String id = uri.getLastPathSegment();

        switch (getUriMatcher().match(uri)) {
            case URI_CATEGORIAS:
                return new BdTableCategorias(bd).query(projection, selection, selectionArgs, null, null, sortOrder);

            case URI_CATEGORIA_ESPECIFICA:
                return new BdTableCategorias(bd).query(projection, BdTableCategorias._ID + "=?", new String[] { id }, null, null, null);

            case URI_LIVROS:
                return new BdTableLivros(bd).query(projection, selection, selectionArgs, null, null, sortOrder);

            case URI_LIVRO_ESPECIFICO:
                return  new BdTableLivros(bd).query(projection,  BdTableLivros.NOME_TABELA + "." + BdTableLivros._ID + "=?", new String[] { id }, null, null, null);

            case URI_COMPRAS:
                return new BdTableCompras(bd).query(projection, selection, selectionArgs, null, null, sortOrder);

            case URI_COMPRAS_ESPECIFICO:
                return  new BdTableCompras(bd).query(projection,  BdTableCompras.NOME_TABELA + "." + BdTableCompras._ID + "=?", new String[] { id }, null, null, null);

            default:
                throw new UnsupportedOperationException("URI inválida (QUERY): " + uri.toString());
        }
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (getUriMatcher().match(uri)) {
            case URI_CATEGORIAS:
                return MULTIPLOS_ITEMS + CATEGORIAS;
            case URI_CATEGORIA_ESPECIFICA:
                return UNICO_ITEM + CATEGORIAS;
            case URI_LIVROS:
                return MULTIPLOS_ITEMS + LIVROS;
            case URI_LIVRO_ESPECIFICO:
                return UNICO_ITEM + LIVROS;

            case URI_COMPRAS:
                return MULTIPLOS_ITEMS + COMPRAS;
            case URI_COMPRAS_ESPECIFICO:
                return UNICO_ITEM + COMPRAS;

            default:
                return null;
        }
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase bd = bdLivrosOpenHelper.getWritableDatabase();

        long id = -1;

        switch (getUriMatcher().match(uri)) {
            case URI_CATEGORIAS:
                id = new BdTableCategorias(bd).insert(values);
                break;

            case URI_LIVROS:
                id = new BdTableLivros(bd).insert(values);
                break;

            case URI_COMPRAS:
                id = new BdTableCompras(bd).insert(values);
                break;

            default:
                throw new UnsupportedOperationException("URI inválida (INSERT):" + uri.toString());
        }

        if (id == -1) {
            throw new SQLException("Não foi possível inserir o registo");
        }

        return Uri.withAppendedPath(uri, String.valueOf(id));
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase bd = bdLivrosOpenHelper.getWritableDatabase();

        String id = uri.getLastPathSegment();

        switch (getUriMatcher().match(uri)) {
            case URI_CATEGORIA_ESPECIFICA:
                return new BdTableCategorias(bd).delete( BdTableCategorias._ID + "=?", new String[] {id});
            case URI_LIVRO_ESPECIFICO:
                return new BdTableLivros(bd).delete(BdTableLivros._ID + "=?", new String[] {id});

            case URI_COMPRAS_ESPECIFICO:
                return new BdTableCompras(bd).delete(BdTableCompras._ID + "=?", new String[] {id});

            default:
                throw new UnsupportedOperationException("URI inválida (DELETE): " + uri.toString());
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase bd = bdLivrosOpenHelper.getWritableDatabase();

        String id = uri.getLastPathSegment();

        switch (getUriMatcher().match(uri)) {
            case URI_CATEGORIA_ESPECIFICA:
                return new BdTableCategorias(bd).update(values, BdTableCategorias._ID + "=?", new String[] {id});
            case URI_LIVRO_ESPECIFICO:
                return new BdTableLivros(bd).update(values, BdTableLivros._ID + "=?", new String[] {id});
            case URI_COMPRAS_ESPECIFICO:
                return new BdTableCompras(bd).update(values, BdTableCompras._ID + "=?", new String[] {id});

            default:
                throw new UnsupportedOperationException("URI inválida (UPDATE): " + uri.toString());
        }
    }
}
