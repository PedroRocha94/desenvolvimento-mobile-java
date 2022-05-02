package dadm.quixada.ufc.basiccomponents;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class WinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        String pokemon1 = (String) getIntent().getExtras().get("pokemon1");
        String pokemon2 = (String) getIntent().getExtras().get("pokemon2");
        String result = (String) getIntent().getExtras().get("resultado");

        TextView textPokemon = findViewById(R.id.textViewWinner);


        int win = new Random().nextInt(3);

        TextView resultadoFinal = findViewById(R.id.textViewAposta);

        if(win == 0){
            textPokemon.setText("Empatou");
            if(result.equals("Empate")){
                resultadoFinal.setText("Você venceu!");
            }else{
                resultadoFinal.setText("Você perdeu!");
            }
        }else if(win == 1){
            textPokemon.setText(pokemon1);
            if(result.equals("Pokemon 1 vence")){
                resultadoFinal.setText("Você venceu!");
            }else{
                resultadoFinal.setText("Você perdeu!");
            }
        }else{
            textPokemon.setText(pokemon2);
            if(result.equals("Pokemon 2 vence")){
                resultadoFinal.setText("Você venceu!");
            }else{
                resultadoFinal.setText("Você perdeu!");
            }
        }

    }


}