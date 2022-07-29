package com.unir.listatarefasapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.unir.listatarefasapp.R;
import com.unir.listatarefasapp.helper.TarefaDAO;
import com.unir.listatarefasapp.model.Tarefa;

public class TarefaActivity extends AppCompatActivity {
    private TextInputEditText editTarefa;
    private Tarefa tarefaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);
        editTarefa = findViewById(R.id.textTarefa);

        Intent intent = getIntent();
        tarefaAtual = (Tarefa) intent.getSerializableExtra("tarefaSelecionada");
        if (tarefaAtual != null) {
            editTarefa.setText(tarefaAtual.getNomeTarefa());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){ // Recuperar a id do item que foi selecionado no menu
            case R.id.itemSalvar:
                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                if(tarefaAtual != null){ // se for nulo, é uma tarefa nova. e então cadastra uma nova tarefa, senão significa que é uma tarefa a ser editada.
                    Tarefa tarefa = new Tarefa();
                    String nomeTarefa = editTarefa.getText().toString(); //pegou o texto
                    if(!nomeTarefa.isEmpty()){ //verifica se tá vazio
                        tarefa.setNomeTarefa(nomeTarefa);
                        tarefa.setId(tarefaAtual.getId()); // pega o id do objeto que foi enviado da activity anterior
                        if(tarefaDAO.atualizar(tarefa)){ // verifica se a atualização retornou true ou false em TarefaDAO
                            Toast.makeText(getApplicationContext(), "Tarefa atualizada!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else{
                            Toast.makeText(getApplicationContext(), "Erro ao atualizar tarefa!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Preencha o nome da tarefa!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    String nomeTarefa = editTarefa.getText().toString();
                    if(!nomeTarefa.isEmpty()){
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);
                        if(tarefaDAO.salvar(tarefa)){
                            Toast.makeText(getApplicationContext(), "Tarefa salva!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Erro ao salvar a tarefa!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Preencha o nome da tarefa!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}