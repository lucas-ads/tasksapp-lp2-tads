package br.edu.ifms.tasksapp.dao;

import java.util.List;

import br.edu.ifms.tasksapp.models.Tarefa;

public interface ITarefaDao {
	void cadastrar(Tarefa tarefa);
	void atualizar(Tarefa tarefa);
	Tarefa buscar(int id);
	List<Tarefa> buscarTodas();
	void excluir(int id);
	void excluir(Tarefa tarefa);
}
