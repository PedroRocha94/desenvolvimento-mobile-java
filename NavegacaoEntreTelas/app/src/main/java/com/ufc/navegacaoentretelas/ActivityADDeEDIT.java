package com.ufc.navegacaoentretelas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import java.util.HashMap;
import java.util.Map;


public class ActivityADDeEDIT extends AppCompatActivity {

    EditText editNome;
    EditText editTipo;
    EditText editAtaque;
    EditText editDefesa;


    boolean edit;
    int idPokemonEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        editNome = findViewById(R.id.nome);
        editTipo = findViewById(R.id.tipo);
        editAtaque = findViewById(R.id.ataque);
        editDefesa = findViewById(R.id.defesa);

        edit = false;

        if(getIntent().getExtras() != null){
            String nome = (String) getIntent().getExtras().get("nome");
            String tipo = (String) getIntent().getExtras().get("tipo");
            String ataque = (String) getIntent().getExtras().get("ataque");
            String defesa = (String) getIntent().getExtras().get("defesa");
            idPokemonEditar = (int) getIntent().getExtras().get("id");

            editNome.setText(nome);
            editTipo.setText(tipo);
            editAtaque.setText(ataque);
            editDefesa.setText(defesa);

            edit = true;
        }

    }

    public void cancelar( View view ){
        setResult( Constants.RESULT_CANCEL );
        finish();
    }

    public void salvar( View view ){

        Intent intent = new Intent();

        String nome = editNome.getText().toString();
        String tipo = editTipo.getText().toString();
        String ataque = editAtaque.getText().toString();
        String defesa = editDefesa.getText().toString();


        Map<String, Object> pokemons = new HashMap<>();

        pokemons.put("nome", nome );
        pokemons.put("tipo", tipo );
        pokemons.put( "ataque", ataque );
        pokemons.put( "defesa", defesa );


        intent.putExtra( "nome", nome );
        intent.putExtra( "tipo", tipo );
        intent.putExtra( "ataque", ataque );
        intent.putExtra( "defesa", defesa );

        if( edit ) intent.putExtra( "id", idPokemonEditar );

        Log.d("teste","salvar: " + intent.getExtras().toString());

        setResult( Constants.RESULT_ADD, intent );
        finish();
    }

}
