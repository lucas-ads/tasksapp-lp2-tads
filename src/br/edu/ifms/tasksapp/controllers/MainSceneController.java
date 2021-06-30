package br.edu.ifms.tasksapp.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import br.edu.ifms.tasksapp.dao.ITarefaDao;
import br.edu.ifms.tasksapp.dao.TarefaDao;
import br.edu.ifms.tasksapp.models.Tarefa;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainSceneController implements Initializable{
	@FXML
	private BorderPane mainSceneRoot;
	@FXML
	private TableView<Tarefa> tableTarefas;
	@FXML
	private TableColumn<Tarefa, Integer> colID;
	@FXML
	private TableColumn<Tarefa, String> colDescricao;
	@FXML
	private TableColumn<Tarefa, Integer> colPrioridade;
	@Override
	
	public void initialize(URL location, ResourceBundle resources) {
		colID.setCellValueFactory(new PropertyValueFactory<>("id"));
		colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		colPrioridade.setCellValueFactory(new PropertyValueFactory<>("prioridade"));
		
		carregaTarefas();
	}
	
	private void carregaTarefas() {
		ITarefaDao tarefaDao = new TarefaDao();
		tableTarefas.setItems(FXCollections.observableArrayList( tarefaDao.buscarTodas() ));
	}
	
	public void showCadastraTarefa() {
		try {
			BorderPane root = new FXMLLoader(getClass().getResource("/views/CadastraTarefa.fxml")).load();
			Scene scene = new Scene(root);
			
			Stage primaryStage = (Stage) mainSceneRoot.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void excluirTarefa() {
		Tarefa tarefa = tableTarefas.getSelectionModel().getSelectedItem();
		Alert alerta;
		
		if(tarefa != null) {
			alerta = new Alert(AlertType.CONFIRMATION);
			alerta.setTitle("Atenção");
			alerta.setHeaderText("Confirmação de exclusão");
			alerta.setContentText("Tem certeza que deseja excluir a tarefa de id "+tarefa.getId()+"?");
			Optional<ButtonType> escolha = alerta.showAndWait();
			
			if(escolha.isPresent() && escolha.get() == ButtonType.OK ) {
				ITarefaDao daoTarefa = new TarefaDao();
				daoTarefa.excluir(tarefa);
				
				carregaTarefas();
			}
		}else {
			alerta = new Alert(AlertType.INFORMATION);
			alerta.setTitle("Atenção");
			alerta.setHeaderText("Nenhuma tarefa selecionada!");
			alerta.setContentText("Selecione a tarefa que deseja excluir.");
			alerta.showAndWait();
		}
	}
}
