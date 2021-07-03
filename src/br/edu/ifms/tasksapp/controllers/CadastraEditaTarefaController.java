package br.edu.ifms.tasksapp.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import br.edu.ifms.tasksapp.dao.ITarefaDao;
import br.edu.ifms.tasksapp.dao.TarefaDao;
import br.edu.ifms.tasksapp.models.Tarefa;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CadastraEditaTarefaController implements Initializable {

	@FXML
	private BorderPane cadastraTarefaRoot;
	@FXML
	private ComboBox<String> comboboxPrioridade;
	@FXML
	private TextArea textAreaDescricao;
	@FXML
	private Text alertTextAreaDescricao;
	@FXML
	private Label titleFormTarefa;
	@FXML
	private Button btnSalvar;

	private String[] nomesPrioridades = { "Mínima", "Baixa", "Média", "Alta", "Altíssima" };

	private Tarefa tarefa;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		comboboxPrioridade.setItems(FXCollections.observableArrayList(nomesPrioridades));
		comboboxPrioridade.setValue(nomesPrioridades[0]);

		Platform.runLater(() -> ajustarTela());
		
	}

	public void ajustarTela() {
		this.tarefa = (Tarefa) cadastraTarefaRoot.getScene().getWindow().getUserData();

		if (this.tarefa != null) {
			this.titleFormTarefa.setText("Editar Tarefa");
			this.btnSalvar.setText("Salvar");
			this.textAreaDescricao.setText(this.tarefa.getDescricao());
			this.comboboxPrioridade.setValue(this.tarefa.getNomePrioridade());
		}
	}

	public void salvarTarefa() {
		String descricao = textAreaDescricao.getText();

		if (descricao.isEmpty()) {
			alertTextAreaDescricao.setText("Digite a descrição da tarefa!");
		} else {
			int prioridade = comboboxPrioridade.getSelectionModel().getSelectedIndex() + 1;

			if (this.tarefa == null) {
				cadastrarTarefa(descricao, prioridade);
			} else {
				atualizarTarefa(descricao, prioridade);
			}
		}
	}

	public void atualizarTarefa(String descricao, int prioridade) {
		try {
			this.tarefa.setDescricao(descricao);
			this.tarefa.setPrioridade(prioridade);
			
			ITarefaDao tarefaDao = new TarefaDao();
			tarefaDao.atualizar(tarefa);
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Sucesso");
			alert.setHeaderText("Tarefa atualizada");
			alert.setContentText("Tarefa atualizada com sucesso!");
			alert.showAndWait();

			launchMainScene();
			
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erro");
			alert.setHeaderText(null);
			alert.setContentText("Erro ao atualizar: " + e.getMessage());
			alert.showAndWait();
		}
	}

	public void cadastrarTarefa(String descricao, int prioridade) {
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
			alert.setContentText("Erro ao cadastrar: " + e.getMessage());
			alert.showAndWait();
		}
	}

	public void launchMainScene() {
		try {
			BorderPane root = new FXMLLoader(getClass().getResource("/views/MainScene.fxml")).load();
			Scene scene = new Scene(root);

			Stage primaryStage = (Stage) cadastraTarefaRoot.getScene().getWindow();
			
			primaryStage.setUserData(null);
			
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cancelar() {
		launchMainScene();
	}

}
