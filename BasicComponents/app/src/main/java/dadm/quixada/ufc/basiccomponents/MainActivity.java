package dadm.quixada.ufc.basiccomponents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    Spinner spnr, spnr2;

    RadioButton result;

    RadioGroup radioG;

    int itemRGselecionado;

    String aposta;

    MediaPlayer som;

    public String [] pokedex = {
            "",
            "Pikachu",
            "Squirtle",
            "Charmander",
            "Torterra",
            "Nidoking",
            "Ratata",
            "Mankey",
            "Primeape",
            "Machoke",
            "Scisor",
            "Ledian",
            "Cartepie",
            "Nidoran M",
            "Meowth",
            "Seal",
            "Chinchar",
            "Parasect",
            "Ponita",
            "Scyter",
            "Trapinch"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        som = MediaPlayer.create(this, R.raw.aberturapokemon);
        som.start();


        spnr = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, pokedex);

        spnr.setAdapter(adapter);
        spnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spnr.getSelectedItemPosition();
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                }
        );

        spnr2 = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, pokedex);

        spnr2.setAdapter(adapter2);
        spnr2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spnr2.getSelectedItemPosition();
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                }
        );
    }

    public void jogar(View view){

        radioG = (RadioGroup)findViewById(R.id.radioGroup);

        itemRGselecionado = radioG.getCheckedRadioButtonId();
        result = findViewById(itemRGselecionado);
        aposta = result.getText().toString();

        Intent intent = new Intent(this, WinnerActivity.class);
        intent.putExtra("pokemon1", pokedex[spnr.getSelectedItemPosition()]);
        intent.putExtra("pokemon2", pokedex[spnr2.getSelectedItemPosition()]);
        intent.putExtra("resultado", aposta);
        //som.stop();
        startActivity(intent);
    }


    public void playSom (View view){
        if(som.isPlaying()){
            som.pause();
        }
        else{
            som.start();
        }
    }


}