package pt.ipg.compralivros;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorLivros extends RecyclerView.Adapter<AdaptadorLivros.ViewHolderLivro> {
    private Cursor cursor;
    private Context context;

    public AdaptadorLivros(Context context) {
        this.context = context;
    }

    public void setCursor(Cursor cursor) {
        if (this.cursor != cursor) {
            this.cursor = cursor;
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public ViewHolderLivro onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLivro = LayoutInflater.from(context).inflate(R.layout.item_livro, parent, false);

        return new ViewHolderLivro(itemLivro);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolderLivro holder, int position) {
        cursor.moveToPosition(position);
        Livro livro = Livro.fromCursor(cursor);
        holder.setLivro(livro);
    }


    @Override
    public int getItemCount() {
        if (cursor == null) return 0;

        return cursor.getCount();
    }

    public Livro getLivroSelecionado() {
        if (viewHolderLivroSelecionado == null) return null;

        return viewHolderLivroSelecionado.livro;
    }

    private static ViewHolderLivro viewHolderLivroSelecionado = null;

    public class ViewHolderLivro extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewTitulo;
        private TextView textViewCategoria;
        private TextView textViewPagina;

        private Livro livro;

        public ViewHolderLivro(@NonNull View itemView) {
            super(itemView);

            textViewTitulo = (TextView) itemView.findViewById(R.id.textViewTitulo);
            textViewCategoria = (TextView) itemView.findViewById(R.id.textViewCategoria);
            textViewPagina = (TextView) itemView.findViewById(R.id.textViewData);

            itemView.setOnClickListener(this);
        }

        public void setLivro(Livro livro) {
            this.livro = livro;

            textViewTitulo.setText(livro.getTitulo());
            textViewPagina.setText(livro.getData());
            textViewCategoria.setText(livro.getCategoria());
        }

        @Override
        public void onClick(View v) {
            if (viewHolderLivroSelecionado != null) {
                viewHolderLivroSelecionado.desSeleciona();
            }

            viewHolderLivroSelecionado = this;

            ((DentroDosLivros) context).atualizaOpcoesMenu();

            seleciona();
        }

        private void desSeleciona() {
            itemView.setBackgroundResource(android.R.color.white);
        }

        private void seleciona() {
            itemView.setBackgroundResource(R.color.colorItemSelecionado);
        }
    }
}