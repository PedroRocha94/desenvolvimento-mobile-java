package com.ufc.UniversiChat.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.ufc.UniversiChat.config.ConfiguracaoFirebase;
import com.ufc.UniversiChat.helper.UserFirebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String foto;

    public User() { }

    public void salvar(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference user = databaseReference.child("users").child(getId());

        user.setValue(this);
    }

    public void atualizar(){
        String identificadorUser = UserFirebase.getIdenficadorUser();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();

        DatabaseReference usersRef = database.child("users")
                .child(identificadorUser);

        Map<String, Object> valoresUser = converterParaMap();

        usersRef.updateChildren(valoresUser);
    }

    @Exclude
    public Map<String, Object> converterParaMap(){
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("email", getEmail());
        userMap.put("nome", getNome());
        userMap.put("foto", getFoto());

        return userMap;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() { return foto; }

    public void setFoto(String foto) { this.foto = foto; }
}
