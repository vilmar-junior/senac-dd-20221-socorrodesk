package model.dto;

import java.time.LocalDate;

public class RelatorioDTO {

	private int idChamado;
	private String nomeSolicitante;
	private LocalDate dataAbertura;
	
	public RelatorioDTO() {
		super();
	}

	public RelatorioDTO(int idChamado, String nomeSolicitante, LocalDate dataAbertura) {
		super();
		this.idChamado = idChamado;
		this.nomeSolicitante = nomeSolicitante;
		this.dataAbertura = dataAbertura;
	}
	
	public int getIdChamado() {
		return idChamado;
	}
	
	public void setIdChamado(int idChamado) {
		this.idChamado = idChamado;
	}
	
	public String getNomeSolicitante() {
		return nomeSolicitante;
	}
	
	public void setNomeSolicitante(String nomeSolicitante) {
		this.nomeSolicitante = nomeSolicitante;
	}
	
	public LocalDate getDataAbertura() {
		return dataAbertura;
	}
	
	public void setDataAbertura(LocalDate dataAbertura) {
		this.dataAbertura = dataAbertura;
	}
}
