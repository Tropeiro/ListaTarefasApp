package com.unir.listatarefasapp.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unir.listatarefasapp.R;
import com.unir.listatarefasapp.adpater.TarefaAdapter;
import com.unir.listatarefasapp.databinding.ActivityMainBinding;
import com.unir.listatarefasapp.helper.DbHelper;
import com.unir.listatarefasapp.helper.RecyclerItemClickListener;
import com.unir.listatarefasapp.helper.TarefaDAO;
import com.unir.listatarefasapp.model.Tarefa;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> tarefaList = new ArrayList<Tarefa>();
    private Tarefa tarefaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Tarefa tarefaSelecionada = tarefaList.get(position);
                        Intent intent = new Intent(MainActivity.this, TarefaActivity.class);
                        intent.putExtra("tarefaSelecionada", tarefaSelecionada);
                        Toast.makeText(getApplicationContext(), "Editando tarefa", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        tarefaSelecionada = tarefaList.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Confirmar exclusão.");
                        builder.setMessage("Deseja excluir a tarefa: " + tarefaSelecionada.getNomeTarefa() + "?");
                        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                                if(tarefaDAO.deletar(tarefaSelecionada)){ // chamando método para deletar a tarefa
                                    carregarTarefas();
                                    Toast.makeText(getApplicationContext(), "Tarefa excluída com sucesso!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Erro ao excluir tarefa!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("Não", null);
                        builder.create().show();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TarefaActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void carregarTarefas(){

        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
        tarefaList = tarefaDAO.listar(); // pega da lista do banco de dados e coloca em outra lista.

        tarefaAdapter = new TarefaAdapter(tarefaList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(tarefaAdapter);

    }

    @Override
    protected void onStart() {
        carregarTarefas();
        super.onStart();
    }
}