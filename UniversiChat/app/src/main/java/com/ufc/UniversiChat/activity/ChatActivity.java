package com.ufc.UniversiChat.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ufc.UniversiChat.R;
import com.ufc.UniversiChat.adapter.MensagensAdapter;
import com.ufc.UniversiChat.config.ConfiguracaoFirebase;
import com.ufc.UniversiChat.helper.Base64Custom;
import com.ufc.UniversiChat.helper.UserFirebase;
import com.ufc.UniversiChat.model.Conversa;
import com.ufc.UniversiChat.model.Mensagem;
import com.ufc.UniversiChat.model.Tarefa;
import com.ufc.UniversiChat.model.User;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;

    private CircleImageView circleImageViewFoto;
    private EditText editMensagem;
    private ImageView imageCamera;
    private ImageView imageGaleria;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private User userDestinatario;
    User userRemetente;
    private StorageReference storageReference;
    private DatabaseReference database;
    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;

    //identificador users remetente e destinatario
    private String idUserRemetente;
    private String idUserDestinatario;
    private Tarefa tarefa;

    private RecyclerView recyclerMensagens;
    private MensagensAdapter adapter;
    private List<Mensagem> listaMensagens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //configurações iniciais
        textViewNome = findViewById(R.id.textViewNomeChat);

        circleImageViewFoto = findViewById(R.id.circleImageFotoChat);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);
        imageCamera = findViewById(R.id.imageCamera);
        imageGaleria = findViewById(R.id.imageGaleria);

        //recuperar dados do usuario remetente
        idUserRemetente = UserFirebase.getIdenficadorUser();
        userRemetente = UserFirebase.getDadosUserLogado();

        //Recuperar dados do usuário destinatário
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){

            if(bundle.containsKey("chatTarefa")){

                tarefa = (Tarefa) bundle.getSerializable("chatTarefa");
                idUserDestinatario = tarefa.getId();
                textViewNome.setText(tarefa.getNome());


                String foto = tarefa.getFoto();
                if(foto != null){
                    Uri url = Uri.parse(foto);
                    Glide.with(ChatActivity.this)
                            .load(url)
                            .into(circleImageViewFoto);
                } else{
                    circleImageViewFoto.setImageResource(R.drawable.padrao);
                }


            } else{
                userDestinatario = (User) bundle.getSerializable("chatContato");
                textViewNome.setText(userDestinatario.getNome());

                String foto = userDestinatario.getFoto();

                if(foto != null){
                    Uri url = Uri.parse(userDestinatario.getFoto());
                    Glide.with(ChatActivity.this)
                            .load(url)
                            .into(circleImageViewFoto);
                } else{
                    circleImageViewFoto.setImageResource(R.drawable.padrao);
                }

                idUserDestinatario = Base64Custom.codificarBase64(userDestinatario.getEmail());
            }
        }

        //Configurar adapter
        adapter = new MensagensAdapter(listaMensagens, getApplicationContext());

        //configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager(layoutManager);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setAdapter(adapter);

        database = ConfiguracaoFirebase.getFirebaseDatabase();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        mensagensRef = database.child("mensagens")
                .child(idUserRemetente)
                .child(idUserDestinatario);

        //evento de clique na camera
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });

        imageGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        if(userDestinatario == null){
            inflater.inflate(R.menu.menu_chat, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuInfo:
                informacoes();
                break;
        }

        return super.onContextItemSelected(item);
    }

    public void informacoes(){
        Intent intent = new Intent(this, InformacoesActivity.class);
        intent.putExtra("infoTarefa", (Serializable) tarefa);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {

                switch (requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localimagem = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localimagem);
                        break;
                }

                if(imagem != null){
                    //recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    String nomeImagem = UUID.randomUUID().toString();

                    //Salvar imagem no firebase
                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("chat")
                            .child(idUserRemetente)
                            .child(nomeImagem + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Erro", "Erro ao fazer upload!");
                            Toast.makeText(ChatActivity.this,
                                    "Erro ao fazer upload da imagem!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String url = task.getResult().toString();

                                    if(userDestinatario != null){ //conversa normal

                                        Mensagem mensagem = new Mensagem();
                                        mensagem.setIdUser(idUserRemetente);
                                        mensagem.setTextoMensagem("imagem.jpeg");
                                        mensagem.setImagem(url);

                                        //salvando para o remetente
                                        salvarMensagem(idUserRemetente, idUserDestinatario, mensagem);

                                        //salvando para o destinatario
                                        salvarMensagem(idUserDestinatario, idUserRemetente, mensagem);

                                    } else{ //conversa em grupo

                                        for(User membro: tarefa.getMembros()){

                                            String idRementendeTarefa = Base64Custom.codificarBase64(membro.getEmail());
                                            String idUserLogadoTarefa = UserFirebase.getIdenficadorUser();

                                            Mensagem mensagem = new Mensagem();
                                            mensagem.setIdUser(idUserLogadoTarefa);
                                            mensagem.setTextoMensagem("imagem.jpeg");
                                            mensagem.setNomeUser(userRemetente.getNome());
                                            mensagem.setImagem(url);

                                            //salvar mensagem para o membro
                                            salvarMensagem(idRementendeTarefa, idUserDestinatario, mensagem);

                                            //salvar conversa remetente
                                            salvarConversa(idRementendeTarefa, idUserDestinatario, userDestinatario, mensagem, true);

                                        }

                                    }

                                    Toast.makeText(ChatActivity.this,
                                            "Imagem enviada!",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    });

                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void enviarMensagem(View view){

        String textoMensagem = editMensagem.getText().toString();

        if(!textoMensagem.isEmpty()){ //conversa normal

            if(userDestinatario != null){

                Mensagem msg = new Mensagem();
                msg.setIdUser(idUserRemetente);
                msg.setTextoMensagem(textoMensagem);

                //salvar msg para o remetente
                salvarMensagem(idUserRemetente, idUserDestinatario, msg);

                //salvar msg para o destinatario
                salvarMensagem(idUserDestinatario, idUserRemetente, msg);

                //salvar conversa remetente
                salvarConversa(idUserRemetente, idUserDestinatario, userDestinatario,msg, false);

                //salvar conversa destinatario
                userRemetente = UserFirebase.getDadosUserLogado();
                salvarConversa(idUserDestinatario, idUserRemetente, userRemetente,msg, false);

            } else{ //conversa em grupo

                for(User membro: tarefa.getMembros()){

                    String idRementendeTarefa = Base64Custom.codificarBase64(membro.getEmail());
                    String idUserLogadoTarefa = UserFirebase.getIdenficadorUser();

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUser(idUserLogadoTarefa);
                    mensagem.setTextoMensagem(textoMensagem);
                    mensagem.setNomeUser(userRemetente.getNome());

                    //salvar mensagem para o membro
                    salvarMensagem(idRementendeTarefa, idUserDestinatario, mensagem);

                    //salvar conversa remetente
                    salvarConversa(idRementendeTarefa, idUserDestinatario, userDestinatario, mensagem, true);

                }

            }

        } else{

            Toast.makeText(ChatActivity.this,
                    "Digite uma mensagem para ser enviada!", Toast.LENGTH_SHORT).show();

        }

    }

    private void salvarConversa(String idRemetente, String idDestinatario, User userExibicao, Mensagem msg, boolean isTarefa){

        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente(idRemetente);
        conversaRemetente.setIdDestinatario(idDestinatario);
        conversaRemetente.setUltimamensagem(msg.getTextoMensagem());

        if(isTarefa){ //conversa na tarefa em grupo
            conversaRemetente.setIsTarefa("true");
            conversaRemetente.setTarefa(tarefa);
        } else{ //conversa normal
            conversaRemetente.setUserExibicao(userExibicao);
            conversaRemetente.setIsTarefa("false");
        }
        conversaRemetente.salvar();

    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference mensagemRef = database.child("mensagens");
        mensagemRef.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(mensagem);

        //limpar editText
        editMensagem.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListenerMensagens);
    }

    private void recuperarMensagens(){

        listaMensagens.clear();

        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensagem mensagem = snapshot.getValue(Mensagem.class);
                listaMensagens.add(mensagem);
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