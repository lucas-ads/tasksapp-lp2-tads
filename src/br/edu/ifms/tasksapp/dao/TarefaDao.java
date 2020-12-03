package br.edu.ifms.tasksapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifms.tasksapp.models.Tarefa;

public class TarefaDao {

	public void cadastrar(Tarefa tarefa) throws Exception {
		if (tarefa != null && tarefa.isValid() && tarefa.getId() == 0) {
			Connection conexao = ConnectionFactory.getConnection();

			String sql = "insert into tarefas (tarefa, prioridade) values (?,?)";
			PreparedStatement prepStatement = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			prepStatement.setString(1, tarefa.getDescricao());
			prepStatement.setInt(2, tarefa.getPrioridade());

			prepStatement.executeUpdate();

			ResultSet result = prepStatement.getGeneratedKeys();

			if (result.next()) {
				tarefa.setId(result.getInt(1));
			}

			result.close();
			prepStatement.close();
			conexao.close();
		}
	}

	public Tarefa buscar(int id) throws Exception {
		if (id > 0) {
			Connection conexao = ConnectionFactory.getConnection();

			String sql = "select * from tarefas where id = ?";
			PreparedStatement prepStatement = conexao.prepareStatement(sql);
			prepStatement.setInt(1, id);

			ResultSet result = prepStatement.executeQuery();

			if (result.next()) {
				Tarefa tarefa = new Tarefa();
				tarefa.setId(result.getInt("id"));
				tarefa.setDescricao(result.getString("tarefa"));
				tarefa.setPrioridade(result.getInt("prioridade"));
				return tarefa;
			}

			result.close();
			prepStatement.close();
			conexao.close();
		}
		return null;
	}

	public List<Tarefa> buscarTodas() throws Exception {
		Connection conexao = ConnectionFactory.getConnection();

		String sql = "select * from tarefas";
		PreparedStatement prepStatement = conexao.prepareStatement(sql);

		ResultSet result = prepStatement.executeQuery();

		List<Tarefa> tarefas = new ArrayList<Tarefa>();

		while (result.next()) {
			Tarefa tarefa = new Tarefa();
			tarefa.setId(result.getInt("id"));
			tarefa.setDescricao(result.getString("tarefa"));
			tarefa.setPrioridade(result.getInt("prioridade"));
			tarefas.add(tarefa);
		}

		result.close();
		prepStatement.close();
		conexao.close();

		return tarefas;
	}

	public void atualizar(Tarefa tarefa) throws SQLException {
		if (tarefa != null && tarefa.isValid() && tarefa.getId() > 0) {
			Connection conexao = ConnectionFactory.getConnection();

			String sqlUpdate = "update tarefas set tarefa=?, prioridade=? where id = ?";

			PreparedStatement prepStatement = conexao.prepareStatement(sqlUpdate);

			prepStatement.setString(1, tarefa.getDescricao());
			prepStatement.setInt(2, tarefa.getPrioridade());
			prepStatement.setInt(3, tarefa.getId());

			prepStatement.executeUpdate();

			prepStatement.close();
			conexao.close();
		}
	}

	public void excluir(int id) throws SQLException {
		if (id > 0) {
			Connection conexao = ConnectionFactory.getConnection();

			String sql = "delete from tarefas where id = ?";

			PreparedStatement prepStatement = conexao.prepareStatement(sql);
			prepStatement.setInt(1, id);
			
			prepStatement.executeUpdate();
			
			prepStatement.close();
			conexao.close();
		}
	}
	
	public void excluir(Tarefa tarefa) throws SQLException {
		if(tarefa != null && tarefa.getId()>0) {
			this.excluir(tarefa.getId());
		}
	}

}
