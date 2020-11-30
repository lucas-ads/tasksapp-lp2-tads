package br.edu.ifms.tasksapp.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Programa {

	public static void main(String[] args) throws SQLException {
		
		/* Para executar este exemplo, crie um banco chamado tasksdb em seu MySQL
		 * e crie uma tabela utilizando o seguinte comando SQL:
		 	
		 	CREATE TABLE `tarefas` (
  				`id` int NOT NULL AUTO_INCREMENT,
  				`tarefa` varchar(200) NOT NULL,
  				`prioridade` int NOT NULL,
  				PRIMARY KEY (`id`)
			) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
			
		 */
		
		
		
		
		
		
		/*########################## Conexão ##########################*/
		
		//String de conexão. (Se o SGBD estiver em outro computador, substitua "localhost" pelo IP deste outro computador) 
		String dbURL = "jdbc:mysql://localhost:3306/tasksdb?useTimezone=True&serverTimezone=UTC";
		
		//Nome de usuário utilizado no MySQL (Por padrão é "root" mesmo)
		String username = "root";
		
		//Senha configurada no MySQL (Por padrão é vazio "", para este exemplo configurei a senha "senha" no meu MySQL)
		String password = "senha";
		
		/* Caso você tenha adicionado o connector MySQL/Java ao classpath do projeto 
		 * e configurado corretamente a string de conexão, o usuário e a senha, 
		 * a conexão será estabelecida e armazenada em "conn" */
		Connection conn = DriverManager.getConnection(dbURL, username, password);
		
		//Verifica se a conexão foi estabelecida
		if(conn != null) {
			System.out.println("Conectado!");
		}else {
			System.out.println("Não conectado!");
		}
		
		
		
		
		
		
		/*########################## Insert ##########################*/
		
		//Lê os dados do terminal
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Digite a descrição da tarefa:");
		String tarefa = scanner.nextLine();
		
		System.out.println("Digite a prioridade da tarefa:");
		int prioridade = scanner.nextInt();
		
		//Define a instrução SQL modelo para as inserções de tarefas
		String sqlInsert = "insert into tarefas (tarefa, prioridade) values (?,?)";
		//Utilizando a instrução SQL definida acima e o conexão estabelecida, cria uma instrução pré-compilada (PreparedStatement).
		PreparedStatement prepStatementInsert = conn.prepareStatement(sqlInsert);
		
		//Seta os valores na instrução SQL pré-compilada
		prepStatementInsert.setString(1, tarefa);
		prepStatementInsert.setInt(2, prioridade);
		
		//Executa a inserção no banco e exibe o retorno obtido (executeUpdate retorna um inteiro que indica o número de registros afetados).
		int linhasAfetadasInsert = prepStatementInsert.executeUpdate();
		System.out.println("Retorno do executeUpdate: "+linhasAfetadasInsert);
		
		//Libera recursos de sistema
		prepStatementInsert.close();
		
		
		
		
		
		
		/*########################## Update ##########################*/
		
		//Define uma instrução SQL modelo para updates de tarefas
		String sqlUpdate = "update tarefas set prioridade=? where tarefa like ?";
		PreparedStatement prepStatementUpdate = conn.prepareStatement(sqlUpdate);
		
		/* Observe que com a instrução modelo utilizada neste update e com os valores setados abaixo
		 * todas as tarefas cadastradas na base que possuirem o texto "Estudar" em sua descrição
		 * terão suas prioridade alteradas para 5. */
		prepStatementUpdate.setInt(1, 5);
		prepStatementUpdate.setString(2, "%Estudar%");
		
		int linhasAfetadasUpdate = prepStatementUpdate.executeUpdate();
		
		System.out.println("Retorno do executeUpdate: "+linhasAfetadasUpdate);
		
		//Libera recursos de sistema
		prepStatementUpdate.close();
		
		
		
		
		
		
		/*########################## Delete ##########################*/
		
		//Define uma instrução SQL modelo para deletes de tarefas
		String sqlDelete = "delete from tarefas where id = ?";
		
		PreparedStatement prepStatementDelete = conn.prepareStatement(sqlDelete);
		
		/* Observe que com a instrução modelo utilizada neste delete e
		 * com o valor setado abaixo, a tarefa cadastrada na base com
		 * o id 2 será excluída */
		prepStatementDelete.setInt(1, 2);
		
		int linhasAfetadasDelete = prepStatementDelete.executeUpdate();
		
		System.out.println("Retorno do executeUpdate: "+linhasAfetadasDelete);
		
		//Libera recursos de sistema
		prepStatementDelete.close();
		
		
		
		
		
		
		/*########################## Select ##########################*/
		
		/* Define uma instrução SQL modelo para buscar todas as tarefas do banco.
		 * Quando quiser buscar uma tarefa específica, basta utilizar algo como 
		 * "select * from tarefas where id = ? "
		 */
		String sqlSelect = "select * from tarefas";
		PreparedStatement prepStatementSelect = conn.prepareStatement(sqlSelect);
		
		/* O executeQuery executará o select acima e retornará o conjunto de 
		 * dados selecionados do banco de dados, este conjunto de dados será
		 * armazenado em um ResultSet
		 */
		ResultSet result = prepStatementSelect.executeQuery();
		
		
		/* Este é o meio pelo qual extraímos os dados do ResultSet
		 */
		while(result.next()) {
			System.out.println(result.getString("tarefa"));
			System.out.println(result.getInt("prioridade"));
		}
		
		//Libera recursos de sistema
		result.close();
		prepStatementSelect.close();
		
		//Encerra a conexão com o banco de dados
		conn.close();
	}

} 
