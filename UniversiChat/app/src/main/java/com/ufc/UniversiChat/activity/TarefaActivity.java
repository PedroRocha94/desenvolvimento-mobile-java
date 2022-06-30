package com.ufc.UniversiChat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ufc.UniversiChat.R;
import com.ufc.UniversiChat.adapter.ContatosAdapter;
import com.ufc.UniversiChat.adapter.GrupoSelecionadoAdapter;
import com.ufc.UniversiChat.config.ConfiguracaoFirebase;
import com.ufc.UniversiChat.helper.RecyclerItemClickListener;
import com.ufc.UniversiChat.helper.UserFirebase;
import com.ufc.UniversiChat.model.User;

import java.util.ArrayList;

public class TarefaActivity extends AppCompatActivity {

    private RecyclerView recyclerMembrosSelecionados, recyclerMembros;
    private ContatosAdapter contatosAdapter;
    private GrupoSelecionadoAdapter grupoSelecionadoAdapter;
    private ArrayList<User> listaMembros = new ArrayList<>();
    private ArrayList<User> listaMembrosSelecionados = new ArrayList<>();
    private ValueEventListener valueEventListenerMembros;
    private DatabaseReference usersRef;
    private FirebaseUser userAtual;
    private Toolbar toolbar;
    private FloatingActionButton fabAvancarCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);
        toolbar = findViewById(R.id.toolbar);
        setTitle("Nova conversa em grupo");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //configuraçoes iniciais
        recyclerMembros = findViewById(R.id.recyclerMembros);
        recyclerMembrosSelecionados = findViewById(R.id.recyclerMembrosSelecionados);
        fabAvancarCadastro = findViewById(R.id.fabAvancarCadastro);

        usersRef = ConfiguracaoFirebase.getFirebaseDatabase().child("users");
        userAtual = UserFirebase.getUserAtual();

        //configurar adapter
        contatosAdapter = new ContatosAdapter(listaMembros, getApplicationContext());
        grupoSelecionadoAdapter = new GrupoSelecionadoAdapter(listaMembrosSelecionados, getApplicationContext());

        //configurar recyclerviewMembros (todos os contatos)
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMembros.setLayoutManager(layoutManager);
        recyclerMembros.setHasFixedSize(true);
        recyclerMembros.setAdapter(contatosAdapter);

        recyclerMembros.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerMembros,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                User userSelecionado = listaMembros.get(position);

                                //remover user selecionado da lista geral
                                listaMembros.remove(userSelecionado);
                                contatosAdapter.notifyDataSetChanged();

                                //adiciona user na nova lista de selecionados
                                listaMembrosSelecionados.add(userSelecionado);
                                grupoSelecionadoAdapter.notifyDataSetChanged();

                                atualizarMembrosToolbar();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

        //configurar recyclerMembrosSelecionados (membros selecionados para o grupo)

        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );

        recyclerMembrosSelecionados.setLayoutManager(layoutManagerHorizontal);
        recyclerMembrosSelecionados.setHasFixedSize(true);
        recyclerMembrosSelecionados.setAdapter(grupoSelecionadoAdapter);

        recyclerMembrosSelecionados.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerMembrosSelecionados,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                User userSelecionado = listaMembrosSelecionados.get(position);

                                //remover da lista de membros selecionados
                                listaMembrosSelecionados.remove(userSelecionado);
                                grupoSelecionadoAdapter.notifyDataSetChanged();

                                //adicionar a lista geral
                                listaMembros.add(userSelecionado);
                                contatosAdapter.notifyDataSetChanged();

                                atualizarMembrosToolbar();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

        //configurar o floating action button
        fabAvancarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TarefaActivity.this, CadastroTarefaActivity.class);

                i.putExtra("membros", listaMembrosSelecionados);

                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMembros();

    }

    @Override
    public void onStop() {
        super.onStop();
        usersRef.removeEventListener(valueEventListenerMembros);
    }

    public void recuperarMembros(){
        valueEventListenerMembros = usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dados : snapshot.getChildren()){
                    User user = dados.getValue(User.class);

                    String emailUserAtual = userAtual.getEmail();
                    if(!emailUserAtual.equals(user.getEmail())){
                        listaMembros.add(user);
                    }
                }
                contatosAdapter.notifyDataSetChanged();
                atualizarMembrosToolbar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarMembrosToolbar(){

        int totalSelecionados = listaMembrosSelecionados.size();
        int total = listaMembros.size() + totalSelecionados;

        toolbar.setSubtitle(totalSelecionados + " de " + total + " selecionados");

    }

}