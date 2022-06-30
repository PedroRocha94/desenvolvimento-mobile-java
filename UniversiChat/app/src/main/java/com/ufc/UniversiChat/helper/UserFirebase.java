package com.ufc.UniversiChat.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.ufc.UniversiChat.config.ConfiguracaoFirebase;
import com.ufc.UniversiChat.model.User;

public class UserFirebase {

    public static String getIdenficadorUser(){
        FirebaseAuth user = ConfiguracaoFirebase.getAuth();
        String email = user.getCurrentUser().getEmail();
        String identificadorUser = Base64Custom.codificarBase64(email);

        return identificadorUser;
    }

    public static FirebaseUser getUserAtual(){
        FirebaseAuth user = ConfiguracaoFirebase.getAuth();
        return user.getCurrentUser();
    }

    public static boolean atualizarNomeUser(String nome){

        try{

            FirebaseUser user = getUserAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil.");
                    }
                }
            });
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }

    public static boolean atualizarFotoUser(Uri url){

        try{

            FirebaseUser user = getUserAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar foto de perfil.");
                    }
                }
            });
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public static User getDadosUserLogado(){

        FirebaseUser firebaseUser = getUserAtual();

        User user = new User();
        user.setEmail(firebaseUser.getEmail());
        user.setNome(firebaseUser.getDisplayName());

        if(firebaseUser.getPhotoUrl() == null){
            user.setFoto("");
        } else{
            user.setFoto(firebaseUser.getPhotoUrl().toString());
        }
        return user;
    }


}
