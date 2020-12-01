package br.edu.ifms.tasksapp.models;

import br.edu.ifms.tasksapp.exceptions.AtribuicaoInvalidaIDException;

public class Tarefa {
	private int id;
	private String descricao;
	private int prioridade;
	
	public Tarefa() {
		this.id=0;
	}
	
	public Tarefa(String descricao, int prioridade) throws Exception {
		this.id = 0;
		this.setDescricao(descricao);
		this.setPrioridade(prioridade);
	}
	
	public Tarefa(int id, String descricao, int prioridade) throws Exception {
		this(descricao, prioridade);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) throws Exception {
		if(id<0) {
			throw new Exception("O ID deve ser um inteiro maior que 0!");
		}
		if(this.id != 0) {
			throw new AtribuicaoInvalidaIDException("O ID da tarefa não pode ser alterado.");
		}
			
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) throws Exception {
		if(descricao!=null && descricao.length()>0 && descricao.length() <=200){
			this.descricao = descricao;
		}else {
			throw new Exception("A descrição da tarefa não pode ter mais que 200 caracteres.");
		}
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) throws Exception {
		if(prioridade>=1 && prioridade<=5) {
			this.prioridade = prioridade;
		}else {			
			throw new Exception("A prioridade da tarefa deve ser um inteiro entre 1 e 5.");
		}
	}
	
	public String getNomePrioridade() {
		switch(this.prioridade) {
			case 1:
				return "Mínima";
			case 2:
				return "Baixa";
			case 3:
				return "Média";
			case 4:
				return "Alta";
			case 5:
				return "Altíssima";
			default:
				return null;
		}
	}
	
	public boolean isValid() {
		if(this.descricao!=null && this.prioridade>=1 && this.prioridade <= 5) {
			return true;
		}
		return false;
	}
}
