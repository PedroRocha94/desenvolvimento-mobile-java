package com.ufc.navegacaoentretelas;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.ufc.navegacaoentretelas.model.Pokemon;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    Context context;
    List<Pokemon> listaPokemons;

    public ExpandableListAdapter(Context context, List<Pokemon> listaPokemons) {
        this.context = context;
        this.listaPokemons = listaPokemons;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Pokemon pokemon = (Pokemon) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        TextView nomePokemon = convertView.findViewById(R.id.listHeader);
        nomePokemon.setTypeface(null, Typeface.BOLD);
        nomePokemon.setText(pokemon.getNome());

        return convertView;
    }

    public Object getGroup(int groupPosition) {
        return listaPokemons.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listaPokemons.get(groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        Pokemon pokemon = (Pokemon) getChild(groupPosition, childPosition);

        if(convertView == null){
            LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflaInflater.inflate(R.layout.row_layout,null);
        }

        TextView tipo = convertView.findViewById(R.id.tipo);
        TextView ataque = convertView.findViewById(R.id.ataque);
        TextView defesa = convertView.findViewById(R.id.defesa);

        tipo.setText(pokemon.getTipo());
        ataque.setText(pokemon.getAtaque());
        defesa.setText(pokemon.getDefesa());

        return convertView;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return listaPokemons.get( groupPosition ).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return listaPokemons.get(groupPosition).getId();
    }

    @Override
    public int getGroupCount() {
        return listaPokemons.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
