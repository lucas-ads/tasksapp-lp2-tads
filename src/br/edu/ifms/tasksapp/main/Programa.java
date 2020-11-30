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
		
		
		
		
		
		
		/*########################## Conex�o ##########################*/
		
		//String de conex�o. (Se o SGBD estiver em outro computador, substitua "localhost" pelo IP deste outro computador) 
		String dbURL = "jdbc:mysql://localhost:3306/tasksdb?useTimezone=True&serverTimezone=UTC";
		
		//Nome de usu�rio utilizado no MySQL (Por padr�o � "root" mesmo)
		String username = "root";
		
		//Senha configurada no MySQL (Por padr�o � vazio "", para este exemplo configurei a senha "senha" no meu MySQL)
		String password = "senha";
		
		/* Caso voc� tenha adicionado o connector MySQL/Java ao classpath do projeto 
		 * e configurado corretamente a string de conex�o, o usu�rio e a senha, 
		 * a conex�o ser� estabelecida e armazenada em "conn" */
		Connection conn = DriverManager.getConnection(dbURL, username, password);
		
		//Verifica se a conex�o foi estabelecida
		if(conn != null) {
			System.out.println("Conectado!");
		}else {
			System.out.println("N�o conectado!");
		}
		
		
		
		
		
		
		/*########################## Insert ##########################*/
		
		//L� os dados do terminal
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Digite a descri��o da tarefa:");
		String tarefa = scanner.nextLine();
		
		System.out.println("Digite a prioridade da tarefa:");
		int prioridade = scanner.nextInt();
		
		//Define a instru��o SQL modelo para as inser��es de tarefas
		String sqlInsert = "insert into tarefas (tarefa, prioridade) values (?,?)";
		//Utilizando a instru��o SQL definida acima e o conex�o estabelecida, cria uma instru��o pr�-compilada (PreparedStatement).
		PreparedStatement prepStatementInsert = conn.prepareStatement(sqlInsert);
		
		//Seta os valores na instru��o SQL pr�-compilada
		prepStatementInsert.setString(1, tarefa);
		prepStatementInsert.setInt(2, prioridade);
		
		//Executa a inser��o no banco e exibe o retorno obtido (executeUpdate retorna um inteiro que indica o n�mero de registros afetados).
		int linhasAfetadasInsert = prepStatementInsert.executeUpdate();
		System.out.println("Retorno do executeUpdate: "+linhasAfetadasInsert);
		
		//Libera recursos de sistema
		prepStatementInsert.close();
		
		
		
		
		
		
		/*########################## Update ##########################*/
		
		//Define uma instru��o SQL modelo para updates de tarefas
		String sqlUpdate = "update tarefas set prioridade=? where tarefa like ?";
		PreparedStatement prepStatementUpdate = conn.prepareStatement(sqlUpdate);
		
		/* Observe que com a instru��o modelo utilizada neste update e com os valores setados abaixo
		 * todas as tarefas cadastradas na base que possuirem o texto "Estudar" em sua descri��o
		 * ter�o suas prioridade alteradas para 5. */
		prepStatementUpdate.setInt(1, 5);
		prepStatementUpdate.setString(2, "%Estudar%");
		
		int linhasAfetadasUpdate = prepStatementUpdate.executeUpdate();
		
		System.out.println("Retorno do executeUpdate: "+linhasAfetadasUpdate);
		
		//Libera recursos de sistema
		prepStatementUpdate.close();
		
		
		
		
		
		
		/*########################## Delete ##########################*/
		
		//Define uma instru��o SQL modelo para deletes de tarefas
		String sqlDelete = "delete from tarefas where id = ?";
		
		PreparedStatement prepStatementDelete = conn.prepareStatement(sqlDelete);
		
		/* Observe que com a instru��o modelo utilizada neste delete e
		 * com o valor setado abaixo, a tarefa cadastrada na base com
		 * o id 2 ser� exclu�da */
		prepStatementDelete.setInt(1, 2);
		
		int linhasAfetadasDelete = prepStatementDelete.executeUpdate();
		
		System.out.println("Retorno do executeUpdate: "+linhasAfetadasDelete);
		
		//Libera recursos de sistema
		prepStatementDelete.close();
		
		
		
		
		
		
		/*########################## Select ##########################*/
		
		/* Define uma instru��o SQL modelo para buscar todas as tarefas do banco.
		 * Quando quiser buscar uma tarefa espec�fica, basta utilizar algo como 
		 * "select * from tarefas where id = ? "
		 */
		String sqlSelect = "select * from tarefas";
		PreparedStatement prepStatementSelect = conn.prepareStatement(sqlSelect);
		
		/* O executeQuery executar� o select acima e retornar� o conjunto de 
		 * dados selecionados do banco de dados, este conjunto de dados ser�
		 * armazenado em um ResultSet
		 */
		ResultSet result = prepStatementSelect.executeQuery();
		
		
		/* Este � o meio pelo qual extra�mos os dados do ResultSet
		 */
		while(result.next()) {
			System.out.println(result.getString("tarefa"));
			System.out.println(result.getInt("prioridade"));
		}
		
		//Libera recursos de sistema
		result.close();
		prepStatementSelect.close();
		
		//Encerra a conex�o com o banco de dados
		conn.close();
	}

} 
