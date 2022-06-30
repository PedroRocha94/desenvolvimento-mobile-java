package com.ufc.UniversiChat.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ufc.UniversiChat.R;
import com.ufc.UniversiChat.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GrupoSelecionadoAdapter extends RecyclerView.Adapter<GrupoSelecionadoAdapter.MyViewHolder> {

    private List<User> contatosSelecionados;
    private Context context;

    public GrupoSelecionadoAdapter(List<User> listaContatos, Context c) {
        this.contatosSelecionados = listaContatos;
        this.context = c;
    }

    @NonNull
    @Override
    public GrupoSelecionadoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_grupo_selecionado, parent, false);

        return new GrupoSelecionadoAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull GrupoSelecionadoAdapter.MyViewHolder holder, int position) {

        User user = contatosSelecionados.get(position);

        holder.nome.setText(user.getNome());

        if(user.getFoto() != null){
            Uri uri = Uri.parse(user.getFoto());
            Glide.with(context).load(uri).into(holder.foto);
        } else{
            holder.foto.setImageResource(R.drawable.padrao);
        }

    }

    @Override
    public int getItemCount() {
        return contatosSelecionados.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView foto;
        TextView nome;

        public MyViewHolder(View itemView){
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewFotoMembroSelecionado);
            nome = itemView.findViewById(R.id.textNomeMembroSelecionado);

        }

    }
}
