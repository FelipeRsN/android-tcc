package com.example.talit.projetotcc.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talit.projetotcc.R;
import com.example.talit.projetotcc.adapters.BuscaAdapter;
import com.example.talit.projetotcc.connectionAPI.BuscaFullText;
import com.example.talit.projetotcc.fragments.Buscas;
import com.example.talit.projetotcc.fragments.ListarEstabBuscas;
import com.example.talit.projetotcc.fragments.TabDestaques;
import com.example.talit.projetotcc.logicalView.Busca;
import com.example.talit.projetotcc.sqlight.DbConn;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by talit on 12/10/2017.
 */

public class SearchViewPaginaInicial extends AppCompatActivity {

    public static SearchView searchView;
    public static Activity act;
    public static Context context;
    public static FrameLayout frameEstab;
    private DbConn dbconn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_searchview);
        act = this;
        context = this;
        frameEstab = (FrameLayout) findViewById(R.id.listar_estabelecimentos_busca);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(2);
        dbconn = new DbConn(SearchViewPaginaInicial.this);
        replaceFragment(new Buscas());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchview_produtos, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint(getResources().getString(R.string.busca));
        searchMenuItem.expandActionView();

        int id = searchMenuItem.getItemId();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.length() != 0) {
                    insereHistorico(query);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        View closeSearch = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SearchViewPaginaInicial.this, "fechou", Toast.LENGTH_SHORT).show();
                searchView.setQuery("",false);
                replaceFragment(new Buscas());
            }
        });

        MenuItem searchVoice = menu.findItem(R.id.search_voice_btn);
        searchVoice.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                promptSpeechInput();
                return false;
            }
        });
        return true;
    }

    public void promptSpeechInput() {

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, getResources().getString(R.string.txt_diga_algo));

        try {
            startActivityForResult(i, 100);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(SearchViewPaginaInicial.this, getResources().getString(R.string.txt_sem_permissao), Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> voiceText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String voice = voiceText.get(0);
                    searchView.setQuery(voice, true);

                    //Toast.makeText(SearchViewPaginaInicial.this, voice, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void insereHistorico(String busca) {

        dbconn.insertSearchView(busca);
        BuscaFullText conn = new BuscaFullText(null);
        conn.execute(busca);
        replaceFragment(new ListarEstabBuscas());

    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.listar_estabelecimentos_busca, fragment);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchViewPaginaInicial.this, PaginalnicialConsumidor.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }
}
