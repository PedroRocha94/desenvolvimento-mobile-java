package com.ufc.UniversiChat.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.ufc.UniversiChat.R;
import com.ufc.UniversiChat.activity.ChatActivity;
import com.ufc.UniversiChat.adapter.ConversasAdapter;
import com.ufc.UniversiChat.config.ConfiguracaoFirebase;
import com.ufc.UniversiChat.helper.RecyclerItemClickListener;
import com.ufc.UniversiChat.helper.UserFirebase;
import com.ufc.UniversiChat.model.Conversa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConversasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewConversas;
    private List<Conversa> listaConversas = new ArrayList<>();
    private ConversasAdapter adapter;
    private DatabaseReference database;
    private DatabaseReference conversasRef;
    private ChildEventListener childEventListenerConversas;

    public ConversasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConversasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConversasFragment newInstance(String param1, String param2) {
        ConversasFragment fragment = new ConversasFragment();
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
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        recyclerViewConversas = view.findViewById(R.id.recyclerConversas);

        //configurar o adapter
        adapter = new ConversasAdapter(listaConversas, getActivity());

        //configurar o recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewConversas.setLayoutManager(layoutManager);
        recyclerViewConversas.setHasFixedSize(true);
        recyclerViewConversas.setAdapter(adapter);

        //configurar evento de clique
        recyclerViewConversas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewConversas,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                List<Conversa> listaConversasAtualizada = adapter.getConversas();
                                Conversa conversaSelecionada = listaConversasAtualizada.get(position);

                                if(conversaSelecionada.getIsTarefa().equals("true")){
                                    Intent i = new Intent(getActivity(), ChatActivity.class);
                                    i.putExtra("chatTarefa", (Serializable) conversaSelecionada.getTarefa());
                                    startActivity(i);

                                } else{

                                    Intent in = new Intent(getActivity(), ChatActivity.class);
                                    in.putExtra("chatContato", conversaSelecionada.getUserExibicao());
                                    startActivity(in);

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

        //configura conversasRef
        String identificadorUser = UserFirebase.getIdenficadorUser();
        database = ConfiguracaoFirebase.getFirebaseDatabase();
        conversasRef = database.child("conversas").child(identificadorUser);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConversas();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversasRef.removeEventListener(childEventListenerConversas);
    }

    public void pesquisarConversas(String texto){

        List<Conversa> listaConversasBusca = new ArrayList<>();

        for(Conversa conversa : listaConversas){

            if(conversa.getUserExibicao() != null){
                String nome = conversa.getUserExibicao().getNome().toLowerCase();
                String ultimaMsg = conversa.getUltimamensagem().toLowerCase();

                if(nome.contains(texto) || ultimaMsg.contains(texto)){
                    listaConversasBusca.add(conversa);
                }

            } else{
                String nome = conversa.getTarefa().getNome().toLowerCase();
                String ultimaMsg = conversa.getUltimamensagem().toLowerCase();

                if(nome.contains(texto) || ultimaMsg.contains(texto)){
                    listaConversasBusca.add(conversa);
                }
            }

        }

        adapter = new ConversasAdapter(listaConversasBusca, getActivity());
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void recarregarConversas(){
        adapter = new ConversasAdapter(listaConversas, getActivity());
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recuperarConversas(){

        listaConversas.clear();

        childEventListenerConversas = conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //recuperar conversas
                Conversa conversa = snapshot.getValue(Conversa.class);
                listaConversas.add(conversa);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}