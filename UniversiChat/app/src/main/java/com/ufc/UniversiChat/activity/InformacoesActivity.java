package com.ufc.UniversiChat.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ufc.UniversiChat.R;
import com.ufc.UniversiChat.model.Tarefa;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

public class InformacoesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Tarefa tarefa;
    private String idTarefa;
    private TextView textNomeTarefa;
    private TextView textDescricao;
    private CircleImageView circleImageViewTarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes);
        textNomeTarefa = findViewById(R.id.textNomeTarefa);
        textDescricao = findViewById(R.id.textDescricao);
        circleImageViewTarefa = findViewById(R.id.circleImageViewFotoTarefa);
        circleImageViewTarefa = findViewById(R.id.circleImageViewFotoTarefa);
        toolbar = findViewById(R.id.toolbarPrincipal);
        setTitle("Informações");

        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        tarefa = (Tarefa) bundle.getSerializable("infoTarefa");

        textNomeTarefa.setText(tarefa.getNome());
        textDescricao.setText(tarefa.getDescricao());

        Uri url = Uri.parse(tarefa.getFoto());

        if(url != null){
            Glide.with(InformacoesActivity.this)
                    .load(url)
                    .into(circleImageViewTarefa);
        } else{
            circleImageViewTarefa.setImageResource(R.drawable.padrao);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_info, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.cancelar:
                cancelar();
                break;
        }

        return super.onContextItemSelected(item);
    }

    public void cancelar(){
        finish();
    }

}
