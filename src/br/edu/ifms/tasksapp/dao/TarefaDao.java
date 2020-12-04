package br.edu.ifms.tasksapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifms.tasksapp.exceptions.TarefaDaoException;
import br.edu.ifms.tasksapp.models.Tarefa;

public class TarefaDao implements ITarefaDao {

	@Override
	public void cadastrar(Tarefa tarefa) {
		if (tarefa != null && tarefa.isValid() && tarefa.getId() == 0) {

			String sql = "insert into tarefas (tarefa, prioridade) values (?,?)";

			// try-with-resources

			try (Connection conexao = ConnectionFactory.getConnection();
					PreparedStatement prepStatement = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

				prepStatement.setString(1, tarefa.getDescricao());
				prepStatement.setInt(2, tarefa.getPrioridade());

				prepStatement.executeUpdate();

				try (ResultSet result = prepStatement.getGeneratedKeys()) {
					if (result.next()) {
						tarefa.setId(result.getInt(1));
					}
				}

			} catch (Exception e) {
				throw new TarefaDaoException(e.getMessage());
			}
		}
	}

	@Override
	public Tarefa buscar(int id){
		if (id > 0) {
			
			String sql = "select * from tarefas where id = ?";
			
			try(Connection conexao = ConnectionFactory.getConnection();
					PreparedStatement prepStatement = conexao.prepareStatement(sql)){
				prepStatement.setInt(1, id);
	
				try(ResultSet result = prepStatement.executeQuery()){
					if (result.next()) {
						Tarefa tarefa = new Tarefa();
						tarefa.setId(result.getInt("id"));
						tarefa.setDescricao(result.getString("tarefa"));
						tarefa.setPrioridade(result.getInt("prioridade"));
						return tarefa;
					}
				}
			}catch(Exception e) {
				throw new TarefaDaoException(e.getMessage());
			}
		}else {
			throw new TarefaDaoException("O ID informado é inválido.");
		}
		return null;
	}

	@Override
	public List<Tarefa> buscarTodas() {
		String sql = "select * from tarefas";

		try(Connection conexao = ConnectionFactory.getConnection();
				PreparedStatement prepStatement = conexao.prepareStatement(sql);
				ResultSet result = prepStatement.executeQuery()){
			
			List<Tarefa> tarefas = new ArrayList<Tarefa>();
	
			while (result.next()) {
				Tarefa tarefa = new Tarefa();
				tarefa.setId(result.getInt("id"));
				tarefa.setDescricao(result.getString("tarefa"));
				tarefa.setPrioridade(result.getInt("prioridade"));
				tarefas.add(tarefa);
			}
			return tarefas;
		}catch(Exception e) {
			throw new TarefaDaoException(e.getMessage());
		}
	}

	@Override
	public void atualizar(Tarefa tarefa) {
		if (tarefa != null && tarefa.isValid() && tarefa.getId() > 0) {
			
			String sqlUpdate = "update tarefas set tarefa=?, prioridade=? where id = ?";
			
			try(Connection conexao = ConnectionFactory.getConnection();
					PreparedStatement prepStatement = conexao.prepareStatement(sqlUpdate)){

				prepStatement.setString(1, tarefa.getDescricao());
				prepStatement.setInt(2, tarefa.getPrioridade());
				prepStatement.setInt(3, tarefa.getId());
	
				prepStatement.executeUpdate();
			}catch(Exception e) {
				throw new TarefaDaoException(e.getMessage());
			}
		}
	}

	@Override
	public void excluir(int id){
		if (id > 0) {
			String sql = "delete from tarefas where id = ?";
			
			try(Connection conexao = ConnectionFactory.getConnection();
					PreparedStatement prepStatement = conexao.prepareStatement(sql)){
				prepStatement.setInt(1, id);
	
				if(prepStatement.executeUpdate()==0) {
					throw new TarefaDaoException("O ID informado não corresponde a nenhuma tarefa.");
				}
			}catch(Exception e) {
				throw new TarefaDaoException(e.getMessage());
			}
		}
	}

	@Override
	public void excluir(Tarefa tarefa) {
		if (tarefa != null && tarefa.getId() > 0) {
			this.excluir(tarefa.getId());
		}
	}

}
