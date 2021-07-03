package br.edu.ifms.tasksapp.controllers;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CadastraTarefaController implements Initializable{
	
	@FXML
	private BorderPane cadastraTarefaRoot;
	@FXML
	private ComboBox<String> comboboxPrioridade;
	@FXML
	private TextArea textAreaDescricao;
	@FXML
	private Text alertTextAreaDescricao;
	
	String[] nomesPrioridades = {"Baixíssima", "Baixa", "Média", "Alta", "Altíssima"};
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		comboboxPrioridade.setItems(FXCollections.observableArrayList(nomesPrioridades));
		comboboxPrioridade.setValue(nomesPrioridades[0]);
	}
	
	public void cadastrarTarefa() {
		String descricao = textAreaDescricao.getText();
		
		if(descricao.isEmpty()) {
			alertTextAreaDescricao.setText("Digite a descrição da tarefa!");
		}else {
			int prioridade = comboboxPrioridade.getSelectionModel().getSelectedIndex() + 1;
			
			Tarefa tarefa = new Tarefa();
			try {
				tarefa.setDescricao(descricao);
				tarefa.setPrioridade(prioridade);
				
				ITarefaDao tarefaDao = new TarefaDao();
				tarefaDao.cadastrar(tarefa);
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Cadastro realizado");
				alert.setHeaderText(null);
				alert.setContentText("Tarefa cadastrada com sucesso!");
				alert.showAndWait();
				
				launchMainScene();
				
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erro");
				alert.setHeaderText(null);
				alert.setContentText("Erro: " + e.getMessage());
				alert.showAndWait();
			}
		}
	}
	
	public void launchMainScene() {
		try {
			BorderPane root = new FXMLLoader(getClass().getResource("/views/MainScene.fxml")).load();
			Scene scene = new Scene(root);
			
			Stage primaryStage = (Stage) cadastraTarefaRoot.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cancelar() {
		launchMainScene();
	}

	
}
