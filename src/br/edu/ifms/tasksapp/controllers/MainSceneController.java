package br.edu.ifms.tasksapp.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import br.edu.ifms.tasksapp.dao.ITarefaDao;
import br.edu.ifms.tasksapp.dao.TarefaDao;
import br.edu.ifms.tasksapp.models.Tarefa;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainSceneController implements Initializable{
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
		
		ITarefaDao tarefaDao = new TarefaDao();
		tableTarefas.setItems(FXCollections.observableArrayList( tarefaDao.buscarTodas() ));
	}
}
