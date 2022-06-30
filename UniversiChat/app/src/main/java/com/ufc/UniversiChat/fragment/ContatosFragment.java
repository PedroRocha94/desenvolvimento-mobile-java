package com.ufc.UniversiChat.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ufc.UniversiChat.R;
import com.ufc.UniversiChat.activity.ChatActivity;
import com.ufc.UniversiChat.activity.TarefaActivity;
import com.ufc.UniversiChat.adapter.ContatosAdapter;
import com.ufc.UniversiChat.config.ConfiguracaoFirebase;
import com.ufc.UniversiChat.helper.RecyclerItemClickListener;
import com.ufc.UniversiChat.helper.UserFirebase;
import com.ufc.UniversiChat.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContatosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewListaContatos;
    private ContatosAdapter adapter;
    private ArrayList<User> listaContatos = new ArrayList<>();
    private DatabaseReference usersRef;
    private ValueEventListener valueEventListenerContatos;
    private FirebaseUser userAtual;

    public ContatosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContatosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContatosFragment newInstance(String param1, String param2) {
        ContatosFragment fragment = new ContatosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        recyclerViewListaContatos = view.findViewById(R.id.recyclerViewListaContatos);
        usersRef = ConfiguracaoFirebase.getFirebaseDatabase().child("users");
        userAtual = UserFirebase.getUserAtual();

        //configurar adapter
        adapter = new ContatosAdapter(listaContatos, getActivity());

        //configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListaContatos.setLayoutManager(layoutManager);
        recyclerViewListaContatos.setHasFixedSize(true);
        recyclerViewListaContatos.setAdapter(adapter);

        //Configurar evento de click no recyclerview
        recyclerViewListaContatos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewListaContatos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                List<User> listaUsersAtualizada = adapter.getContatos();

                                User userSelecionado = listaUsersAtualizada.get(position);
                                boolean cabecalho = userSelecionado.getEmail().isEmpty();

                                if(cabecalho){

                                    Intent i = new Intent(getActivity(), TarefaActivity.class);
                                    startActivity(i);

                                } else{
                                    Intent i = new Intent(getActivity(), ChatActivity.class);
                                    i.putExtra("chatContato", userSelecionado);
                                    startActivity(i);
                                }

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

        adicionaMenuNovatarefa();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        usersRef.removeEventListener(valueEventListenerContatos);
    }

    public void pesquisarContatos(String texto){

        List<User> listaContatosBusca = new ArrayList<>();

        for(User user : listaContatos){

            String nome = user.getNome().toLowerCase();
            if(nome.contains(texto)){
                listaContatosBusca.add(user);
            }

        }

        adapter = new ContatosAdapter(listaContatosBusca, getActivity());
        recyclerViewListaContatos.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void recarregarContatos(){
        adapter = new ContatosAdapter(listaContatos, getActivity());
        recyclerViewListaContatos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recuperarContatos(){
        valueEventListenerContatos = usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ajustarListaContatos();

                for(DataSnapshot dados : snapshot.getChildren()){
                    User user = dados.getValue(User.class);

                    String emailUserAtual = userAtual.getEmail();
                    if(!emailUserAtual.equals(user.getEmail())){
                        listaContatos.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ajustarListaContatos(){

        listaContatos.clear();
        adicionaMenuNovatarefa();

    }

    public void adicionaMenuNovatarefa(){
        //Define um usuário com email vazio para ser utilizado como cabeçalho
        User itemTarefa = new User();
        itemTarefa.setNome("Nova conversa em grupo");
        itemTarefa.setEmail("");

        listaContatos.add(itemTarefa);
        recuperarContatos();
    }

}