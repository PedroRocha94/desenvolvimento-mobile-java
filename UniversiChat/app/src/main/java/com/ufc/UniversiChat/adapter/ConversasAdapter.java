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
import com.ufc.UniversiChat.model.Conversa;
import com.ufc.UniversiChat.model.Tarefa;
import com.ufc.UniversiChat.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {

    private List<Conversa> conversas;
    private Context context;

    public ConversasAdapter(List<Conversa> lista, Context c) {
        this.conversas = lista;
        this.context = c;
    }

    public List<Conversa> getConversas(){
        return this.conversas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Conversa conversa = conversas.get(position);

        holder.ultimaMsg.setText(conversa.getUltimamensagem());

        if(conversa.getIsTarefa().equals("true")){
            Tarefa tarefa = conversa.getTarefa();
            holder.nome.setText(tarefa.getNome());

            if(tarefa.getFoto() != null){
                Uri uri = Uri.parse(tarefa.getFoto());
                Glide.with(context).load(uri).into(holder.foto);
            } else{
                holder.foto.setImageResource(R.drawable.padrao);
            }

        } else{
            User user = conversa.getUserExibicao();

            if(user != null){
                holder.nome.setText(user.getNome());

                if(user.getFoto() != null){
                    Uri uri = Uri.parse(user.getFoto());
                    Glide.with(context).load(uri).into(holder.foto);
                } else{
                    holder.foto.setImageResource(R.drawable.padrao);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView foto;
        TextView nome, ultimaMsg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imageViewFotoContato);
            nome = itemView.findViewById(R.id.textNomeContato);
            ultimaMsg = itemView.findViewById(R.id.textEmailContato);
        }
    }

}
