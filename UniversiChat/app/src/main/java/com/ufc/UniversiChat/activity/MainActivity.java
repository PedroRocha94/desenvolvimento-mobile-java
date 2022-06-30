package com.ufc.UniversiChat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ufc.UniversiChat.R;
import com.ufc.UniversiChat.config.ConfiguracaoFirebase;
import com.ufc.UniversiChat.fragment.ContatosFragment;
import com.ufc.UniversiChat.fragment.ConversasFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = ConfiguracaoFirebase.getAuth();


        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("UniversiChat");
        setSupportActionBar(toolbar);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
          getSupportFragmentManager(), FragmentPagerItems.with(this)
          .add("Conversas", ConversasFragment.class)
          .add("Contatos", ContatosFragment.class)
          .create()
        );

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPageTab = findViewById(R.id.viewPagerTab);
        viewPageTab.setViewPager(viewPager);

        searchView = findViewById(R.id.materialSearchPrincipal);
        searchView.setVoiceSearch( true );

        //listener para o search view
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                ConversasFragment fragment = (ConversasFragment) adapter.getPage(0);
                fragment.recarregarConversas();

            }
        });

        //listener para caixa de texto
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                switch (viewPager.getCurrentItem()){

                    case 0:
                        ConversasFragment tarefasFragment = (ConversasFragment) adapter.getPage(0);

                        if(newText != null && !newText.isEmpty()){
                            tarefasFragment.pesquisarConversas(newText.toLowerCase());
                        } else{
                            tarefasFragment.recarregarConversas();
                        }
                        break;

                    case 1:
                        ContatosFragment contatosFragment = (ContatosFragment) adapter.getPage(1);

                        if(newText != null && !newText.isEmpty()){
                            contatosFragment.pesquisarContatos(newText.toLowerCase());
                        } else{
                            contatosFragment.recarregarContatos();
                        }
                        break;

                }
                return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //configurar botao de pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);

        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.menuLocalizacao:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);

                break;

            case R.id.menuSair:
                deslogar();
                finish();
                break;

            case R.id.menuConfiguracoes:
                configuracoes();
                break;
        }

        return super.onContextItemSelected(item);
    }

    public void deslogar(){
        try {
            auth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void configuracoes(){
        Intent intent = new Intent(this, ConfiguracoesActivity.class);
        startActivity(intent);
    }
}