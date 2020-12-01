package br.edu.ifms.tasksapp.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import br.edu.ifms.tasksapp.dao.ConnectionFactory;
import br.edu.ifms.tasksapp.models.Tarefa;

public class Programa { 
	
	public static void main(String[] args) throws SQLException {
		
		int opcao;
		Scanner leitorTerminal = new Scanner(System.in);
		
		do {
			System.out.println("#### Digite o número correspondente a opção desejada: ");
			System.out.println("1 - Cadastrar tarefa");
			System.out.println("2 - Editar tarefa");
			System.out.println("3 - Excluir tarefa");
			System.out.println("4 - Listar tarefas");
			System.out.println("0 - Finalizar programa");
			
			opcao = leitorTerminal.nextInt();
			leitorTerminal.nextLine(); //Limpar buffer do console
			
			switch(opcao) {
				case 0:
					break;
				case 1: 
					cadastrarTarefa(leitorTerminal);
					break;
				case 2:
					editarTarefa(leitorTerminal);
					break;
				case 3: 
					excluirTarefa(leitorTerminal);
					break;
				case 4:
					listarTarefas();
					break;
				default:
					System.out.println("Opção inválida!");
			}
		}while(opcao!=0);
	}
	
	public static void cadastrarTarefa(Scanner leitor) throws SQLException {

		System.out.println("----> Cadastrar tarefa\n");
		System.out.println("Informe a tarefa: ");
		String descricao = leitor.nextLine();
		System.out.println("Informe a prioridade da tarefa: ");
		int prioridade = leitor.nextInt();
		
		Connection conn = ConnectionFactory.getConnection();
		
		Tarefa tarefa;
		try {
			tarefa = new Tarefa(descricao, prioridade);
		
			if(conn!=null) {
				String sql = "insert into tarefas (tarefa, prioridade) values (?,?)";
				PreparedStatement prepStatement = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				
				prepStatement.setString(1, tarefa.getDescricao());
				prepStatement.setInt(2, tarefa.getPrioridade());
				
				prepStatement.executeUpdate();
				
				ResultSet result = prepStatement.getGeneratedKeys();
				
				if(result.next()){
					tarefa.setId(result.getInt(1));
				}
				
				result.close();
				prepStatement.close();
				conn.close();
				
				System.out.println("\nTarefa salva sob o ID "+tarefa.getId()+"\n");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void editarTarefa(Scanner leitor) throws SQLException {
		System.out.println("----> Editar tarefa\n");
		System.out.println("Informe o ID da tarefa que deseja editar: ");
		int id = leitor.nextInt();
		leitor.nextLine(); //Limpar buffer do console 
		
		Connection conn = ConnectionFactory.getConnection();
		
		if(conn!=null) {
		
			String sql = "select * from tarefas where id = ?";
			PreparedStatement prepStatement = conn.prepareStatement(sql);
			prepStatement.setInt(1, id);
			
			ResultSet result = prepStatement.executeQuery();
			
			String tarefa="";
			int prioridade=0;
			
			if(result.next()) {
				tarefa = result.getString("tarefa");
				prioridade = result.getInt("prioridade");
				
				System.out.print("Descrição atual: '"+ tarefa+"'.\nDigite a nova descrição: (Dê enter para manter o atual) ");
				String tempTarefa = leitor.nextLine();
				
				if(!tempTarefa.equals("")) {
					tarefa = tempTarefa;
				}
				
				System.out.print("\nPrioridade atual: '"+ prioridade+"'.\nDigite a nova prioridade: ");
				prioridade = leitor.nextInt();
				leitor.nextLine();//Limpar buffer do console
				
				String sqlUpdate = "update tarefas set tarefa=?, prioridade=? where id = ?";
				
				prepStatement = conn.prepareStatement(sqlUpdate);
				prepStatement.setString(1, tarefa);
				prepStatement.setInt(2, prioridade);
				prepStatement.setInt(3, id);
				
				prepStatement.executeUpdate();
				
				System.out.println("\nTarefa Atualizada com sucesso.\n");
				
				result.close();
				prepStatement.close();
				conn.close();
			}else {
				System.out.println("\nDesculpe, não existe uma tarefa com o ID informado...\n");
			}
		}
		
	}

	public static void excluirTarefa(Scanner leitor) throws SQLException {
		System.out.println("----> Excluir tarefa\n");
		System.out.println("Informe o ID da tarefa que deseja excluir: ");
		int id = leitor.nextInt();
		leitor.nextLine(); //Limpar buffer do console 
		
		Connection conn = ConnectionFactory.getConnection();
		
		if(conn!=null) {
			String sql = "delete from tarefas where id = ?";
			
			PreparedStatement prepStatement = conn.prepareStatement(sql);
			prepStatement.setInt(1, id);
			
			int linhasAfetadas = prepStatement.executeUpdate();
			
			if(linhasAfetadas == 1) {
				System.out.println("\nTarefa excluída.\n");
			}else {
				System.out.println("\nDesculpe, não existe uma tarefa com o ID informado...\n");
			}
			
			prepStatement.close();
			conn.close();
		}
	}

	public static void listarTarefas() throws SQLException {
		System.out.println("----> Listar tarefas\n");
		
		Connection conn = ConnectionFactory.getConnection();
		
		if(conn!=null) {
			String sql = "select * from tarefas";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			ResultSet result = statement.executeQuery();
			
			System.err.print("ID\t| Prioridade\t| Tarefa\n");
			
			while(result.next()) {
				System.out.print(result.getInt("id"));
				System.out.print("\t| "+ result.getInt("prioridade"));
				System.out.println("\t\t| "+ result.getString("tarefa"));
			}
			
			result.close();
			statement.close();
			conn.close();
			
			System.out.println("\n");
		}
	}
} 
