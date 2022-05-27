package com.ufc.navegacaoentretelas;

import android.content.Intent;
import android.os.Bundle;

import com.ufc.navegacaoentretelas.model.Pokemon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Pokemon> listaPokemons;
    //ArrayAdapter adapter;
    ExpandableListAdapter adapter;
    ExpandableListView expandableListView;
    int selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selected = -1;

        listaPokemons = new ArrayList<Pokemon>();

        adapter = new ExpandableListAdapter(this, listaPokemons);

        expandableListView = (ExpandableListView) findViewById(R.id.listPokemons);
        expandableListView.setAdapter(adapter);
        expandableListView.setSelector(android.R.color.holo_green_dark);

//        listViewCarros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, "" + listaCarros.get(position).toString(), Toast.LENGTH_SHORT).show();
//                selected = position;
//            }
//        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                selected = groupPosition;
                return false;
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    public void adicionar(View view){
        Intent intent = new Intent(this, ActivityADDeEDIT.class);
        startActivityForResult(intent, Constants.REQUEST_ADD);
    }

    public void editar(View view){
        if( listaPokemons.size() > 0 && selected >= 0) {
            Intent intent = new Intent(this, ActivityADDeEDIT.class);

            Pokemon pokemon = listaPokemons.get(selected);

            intent.putExtra("id", pokemon.getId());
            intent.putExtra("nome", pokemon.getNome());
            intent.putExtra("tipo", pokemon.getTipo());
            intent.putExtra("ataque", pokemon.getAtaque());
            intent.putExtra("defesa", pokemon.getDefesa());

            startActivityForResult(intent, Constants.REQUEST_EDIT);
        }
        else {
            selected = -1;
            Toast.makeText(this, "Selecione um item!", Toast.LENGTH_SHORT).show();
        }

    }

    public void apagarItem(View view){
        if( listaPokemons.size() > 0){
            listaPokemons.remove(selected);
            adapter.notifyDataSetChanged();
        }
        else {
            selected = -1;
            Toast.makeText(this, "Selecione um item!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.REQUEST_ADD && resultCode == Constants.RESULT_ADD){

            String nome = (String) data.getExtras().get("nome");
            String marca = (String) data.getExtras().get("marca");
            String placa = (String) data.getExtras().get("placa");
            String ano = (String) data.getExtras().get("ano");

            Pokemon pokemon = new Pokemon(nome, marca, placa, ano);
            Log.d("Main", pokemon.getNome());
            listaPokemons.add(pokemon);
            adapter.notifyDataSetChanged();
        }
        else if(requestCode == Constants.REQUEST_EDIT && resultCode == Constants.RESULT_ADD){

            String nome = (String) data.getExtras().get("nome");
            String tipo = (String) data.getExtras().get("tipo");
            String ataque = (String) data.getExtras().get("ataque");
            String defesa = (String) data.getExtras().get("defesa");
            int idEditar = (int) data.getExtras().get("id");

            for(Pokemon pokemon : listaPokemons){
                if(pokemon.getId() == idEditar){
                    pokemon.setNome(nome);
                    pokemon.setTipo(tipo);
                    pokemon.setAtaque(ataque);
                    pokemon.setDefesa(defesa);
                }
            }
            adapter.notifyDataSetChanged();
        }
        else if(resultCode == Constants.RESULT_CANCEL){
            Toast.makeText(this, "Cancelado!", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }

    }

}