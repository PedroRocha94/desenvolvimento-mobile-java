package com.ufc.UniversiChat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.ufc.UniversiChat.R;
import com.ufc.UniversiChat.config.ConfiguracaoFirebase;
import com.ufc.UniversiChat.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText email, senha;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = ConfiguracaoFirebase.getAuth();

        email = findViewById(R.id.editLoginEmail);
        senha = findViewById(R.id.editLoginSenha);

    }

    public void logar(User user){

        auth.signInWithEmailAndPassword(
                user.getEmail(), user.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    telaInicial();
                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                        excecao = "Usuário não está cadastrado!";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "E-mail e/ou senha não correspondem a um usuário cadastrado.";
                    } catch (Exception e) {
                        excecao = "Erro ao logar: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this,
                            excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //verificar se os campos de login estão preenchidos
    public void validarAuthUser(View view){
        String textoEmail = email.getText().toString();
        String textoSenha = senha.getText().toString();

        if(!textoEmail.isEmpty()){
            if(!textoSenha.isEmpty()){
                User user = new User();
                user.setEmail(textoEmail);
                user.setSenha(textoSenha);

                logar(user);

            }
            else {
                Toast.makeText(LoginActivity.this,
                        "Preencha a senha!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(LoginActivity.this,
                    "Preencha o e-mail!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser userAtual = auth.getCurrentUser();
        if(userAtual != null){
            telaInicial();
        }
    }

    public void cadastro(View view){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void telaInicial(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}