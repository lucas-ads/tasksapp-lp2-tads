package br.edu.ifms.tasksapp.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import br.edu.ifms.tasksapp.dao.ConnectionFactory;
import br.edu.ifms.tasksapp.dao.TarefaDao;
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
		
		Tarefa tarefa;
		try {
			tarefa = new Tarefa(descricao, prioridade);
		
			TarefaDao tarefaDao= new TarefaDao();
			tarefaDao.cadastrar(tarefa);
			
			System.out.println("Tarefa cadastrada com sucesso.\n");
		} catch (Exception e) {
			System.out.println("Erro ao atualizar tarefa: " + e.getMessage());
		}
	}

	public static void editarTarefa(Scanner leitor) throws SQLException {
		System.out.println("----> Editar tarefa\n");
		System.out.println("Informe o ID da tarefa que deseja editar: ");
		int id = leitor.nextInt();
		leitor.nextLine(); //Limpar buffer do console 
		
		try {
			TarefaDao tarefaDao = new TarefaDao();
			Tarefa tarefa = tarefaDao.buscar(id);
			
			if(tarefa != null) {
				System.out.print("Descrição atual: '"+ tarefa.getDescricao() +"'.\nDigite a nova descrição: (Dê enter para manter o atual) ");
				String tempTarefa = leitor.nextLine();
				
				if(!tempTarefa.equals("")) {
					tarefa.setDescricao(tempTarefa);
				}
				
				System.out.print("\nPrioridade atual: '"+ tarefa.getPrioridade() +"'.\nDigite a nova prioridade: ");
				tarefa.setPrioridade(leitor.nextInt());
				leitor.nextLine();//Limpar buffer do console
				
				tarefaDao.atualizar(tarefa);
				
				System.out.println("\nTarefa Atualizada com sucesso.\n");
			}else {
				System.out.println("\nDesculpe, não existe uma tarefa com o ID informado...\n");
			}
		}catch(Exception e) {
			System.out.println("Erro ao atualizar tarefa: " + e.getMessage());
		}
		
	}

	public static void excluirTarefa(Scanner leitor) throws SQLException {
		System.out.println("----> Excluir tarefa\n");
		System.out.println("Informe o ID da tarefa que deseja excluir: ");
		int id = leitor.nextInt();
		leitor.nextLine(); //Limpar buffer do console 
		
		TarefaDao tarefaDao = new TarefaDao();
		try {
			tarefaDao.excluir(id);
			
			System.out.println("Tarefa excluída com sucesso.\n");
		}catch(Exception e) {
			System.out.println("Erro ao excluir tarefa: " + e.getMessage());
		}
	}

	public static void listarTarefas() throws SQLException {
		System.out.println("----> Listar tarefas\n");
		
		TarefaDao tarefaDao = new TarefaDao();
		List<Tarefa> tarefas;
			
		try {
			tarefas = tarefaDao.buscarTodas();
			
			if(tarefas != null && tarefas.size()>0) {
				System.err.print("ID\t| Prioridade\t| Tarefa\n");
				
				for(Tarefa tarefa : tarefas) {
					System.out.print(tarefa.getId());
					System.out.print("\t| "+ tarefa.getPrioridade());
					System.out.println("\t\t| "+ tarefa.getDescricao());
				}
				
				System.out.println("\n");
			}else {
				System.out.println("Não há tarefas cadastradas.\n");
			}
		}catch(Exception e) {
			System.out.println("Erro ao buscar tarefas: " + e.getMessage());
		}
	}
} 
