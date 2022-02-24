package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import controller.ChamadoController;
import model.vo.ChamadoVO;
import model.vo.UsuarioVO;

public class MenuChamado {
	
	Scanner teclado = new Scanner(System.in);
	DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private static final int OPCAO_MENU_CADASTRAR_CHAMADO = 1;
	private static final int OPCAO_MENU_CONSULTAR_CHAMADO = 2;
	private static final int OPCAO_MENU_ATUALIZAR_CHAMADO = 3;
	private static final int OPCAO_MENU_EXCLUIR_CHAMADO = 4;
	private static final int OPCAO_MENU_CHAMADO_SAIR = 9;
	
	private static final int OPCAO_MENU_CONSULTAR_TODOS_CHAMADOS = 1;
	private static final int OPCAO_MENU_CONSULTAR_CHAMADOS_ABERTOS = 2;
	private static final int OPCAO_MENU_CONSULTAR_CHAMADOS_FECHADOS = 3;
	private static final int OPCAO_MENU_CONSULTAR_CHAMADOS_SAIR = 9;

	public void apresentarMenuChamado(UsuarioVO usuarioVO) {
		int opcao = apresentarOpcoesMenu(usuarioVO);
		while (opcao != OPCAO_MENU_CHAMADO_SAIR) {
			switch (opcao) {
				case OPCAO_MENU_CADASTRAR_CHAMADO: {
					this.cadastrarChamado(usuarioVO);
					break;
				}
				case OPCAO_MENU_CONSULTAR_CHAMADO: {
					this.consultarChamado(usuarioVO);
					break;
				}
				case OPCAO_MENU_ATUALIZAR_CHAMADO: {
					this.atualizarChamado(usuarioVO);
					break;
				}
				case OPCAO_MENU_EXCLUIR_CHAMADO: {
					this.excluirChamado(usuarioVO);
					break;
				}
				default: {
					System.out.println("\nOpção Inválida"); 
				}
			}
			opcao = apresentarOpcoesMenu(usuarioVO);
		}
	}
	
	
	private int apresentarOpcoesMenu(UsuarioVO usuarioVO) {
		System.out.println("\n---- Sistema Socorro Desk ---- \n---- Menu Cadastro de Chamados ----");
		System.out.println("\nOpções:");
		System.out.println(OPCAO_MENU_CADASTRAR_CHAMADO + " - Cadastrar Chamado");
		System.out.println(OPCAO_MENU_CONSULTAR_CHAMADO + " - Consultar Chamado");
		System.out.println(OPCAO_MENU_ATUALIZAR_CHAMADO + " - Atualizar Chamado");
		System.out.println(OPCAO_MENU_EXCLUIR_CHAMADO + " - Excluir Chamado");
		System.out.println(OPCAO_MENU_CHAMADO_SAIR + " - Voltar");
		System.out.print("\nDigite a Opção: ");
		return Integer.parseInt(teclado.nextLine());
	}
	
	private void cadastrarChamado(UsuarioVO usuarioVO) {
		ChamadoVO chamadoVO = new ChamadoVO();
		chamadoVO.setIdUsuario(usuarioVO.getIdUsuario());
		System.out.print("\nDigite o título: ");
		chamadoVO.setTitulo(teclado.nextLine());
		System.out.print("Digite a descrição: ");
		chamadoVO.setDescricao(teclado.nextLine());
		chamadoVO.setDataAbertura(LocalDate.now());
		
		if(chamadoVO.getTitulo().isEmpty() || chamadoVO.getDescricao().isEmpty()) {
			System.out.println("Os campos título e descrição são obrigatórios.");
		} else {
			ChamadoController chamadoController = new ChamadoController();
			chamadoVO = chamadoController.cadastrarChamadoController(chamadoVO);
			if(chamadoVO.getIdChamado() != 0) {
				System.out.println("Chamado cadastrado com Sucesso!");
			} else {
				System.out.println("Não foi possível cadastrar o Chamado!");
			}
		}
	}
	
	private void atualizarChamado(UsuarioVO usuarioVO) {
		ChamadoVO chamadoVO = new ChamadoVO();
		chamadoVO.setIdUsuario(usuarioVO.getIdUsuario());
		System.out.print("\nDigite o código do chamado: ");
		chamadoVO.setIdChamado(Integer.parseInt(teclado.nextLine()));
		System.out.print("Digite o título: ");
		chamadoVO.setTitulo(teclado.nextLine());
		System.out.print("Digite a descrição: ");
		chamadoVO.setDescricao(teclado.nextLine());
		
		if(chamadoVO.getIdChamado() == 0 || chamadoVO.getTitulo().isEmpty() || chamadoVO.getDescricao().isEmpty()) {
			System.out.println("Os campos código, título e descrição são obrigatórios.");
		} else {
			ChamadoController chamadoController = new ChamadoController();
			boolean resultado = chamadoController.atualizarChamadoController(chamadoVO);
			if(resultado) {
				System.out.println("Chamado atualizado com Sucesso!");
			} else {
				System.out.println("Não foi possível atualizar o Chamado!");
			}
		}	
	}
	
	private void excluirChamado(UsuarioVO usuarioVO) {
		ChamadoVO chamadoVO = new ChamadoVO();
		System.out.print("\nInforme o código do Chamado: ");
		chamadoVO.setIdChamado(Integer.parseInt(teclado.nextLine()));
		chamadoVO.setIdUsuario(usuarioVO.getIdUsuario());
		
		if(chamadoVO.getIdChamado() != 0) {
			ChamadoController chamadoController = new ChamadoController();
			boolean resultado = chamadoController.excluirChamadoController(chamadoVO);
			if(resultado){
				System.out.println("\nChamado excluído com Sucesso.");
			} else {
				System.out.println("Não foi possível excluir o Chamado.");
			}
		} else {
			System.out.println("O campo código do chamado é obrigatório.");
		}

	}
	
	private void consultarChamado(UsuarioVO usuarioVO) {
		int opcao = this.apresentarOpcoesConsulta();
		ChamadoController chamadoController = new ChamadoController();
		while (opcao != OPCAO_MENU_CONSULTAR_CHAMADOS_SAIR) {
			switch (opcao) {
				case OPCAO_MENU_CONSULTAR_TODOS_CHAMADOS: {
					opcao = OPCAO_MENU_CONSULTAR_CHAMADOS_SAIR;
					ArrayList<ChamadoVO> listaChamadosVO = chamadoController.consultarTodosChamadosUsuarioController(usuarioVO);
					System.out.print("\n--------- RESULTADO DA CONSULTA ---------");
					System.out.printf("\n%10s  %10s  %10s  %-30s  %-50s  %-15s  %-30s  %-15s  ", 
							"ID CHAMADO", "ID USUÁRIO", "ID TÉCNICO", "TÍTULO", "DESCRIÇÃO", "DATA ABERTURA", 
							"SOLUÇÃO", "DATA FECHAMENTO");
					for (int i = 0; i < listaChamadosVO.size(); i++) {
						listaChamadosVO.get(i).imprimir();
					}
					System.out.println();
					break;
				}
				case OPCAO_MENU_CONSULTAR_CHAMADOS_ABERTOS: {
					opcao = OPCAO_MENU_CONSULTAR_CHAMADOS_SAIR;
					ArrayList<ChamadoVO> listaChamadosVO = chamadoController.consultarChamadosAbertosUsuarioController(usuarioVO);
					System.out.print("\n--------- RESULTADO DA CONSULTA ---------");
					System.out.printf("\n%10s  %10s  %10s  %-30s  %-50s  %-15s  %-30s  %-15s  ", 
							"ID CHAMADO", "ID USUÁRIO", "ID TÉCNICO", "TÍTULO", "DESCRIÇÃO", "DATA ABERTURA", 
							"SOLUÇÃO", "DATA FECHAMENTO");
					for (int i = 0; i < listaChamadosVO.size(); i++) {
						listaChamadosVO.get(i).imprimir();
					}
					System.out.println();
					break;
				}
				case OPCAO_MENU_CONSULTAR_CHAMADOS_FECHADOS: {
					opcao = OPCAO_MENU_CONSULTAR_CHAMADOS_SAIR;
					ArrayList<ChamadoVO> listaChamadosVO = chamadoController.consultarChamadosFechadosUsuarioController(usuarioVO);
					System.out.print("\n--------- RESULTADO DA CONSULTA ---------");
					System.out.printf("\n%10s  %10s  %10s  %-30s  %-50s  %-15s  %-30s  %-15s  ", 
							"ID CHAMADO", "ID USUÁRIO", "ID TÉCNICO", "TÍTULO", "DESCRIÇÃO", "DATA ABERTURA", 
							"SOLUÇÃO", "DATA FECHAMENTO");
					for (int i = 0; i < listaChamadosVO.size(); i++) {
						listaChamadosVO.get(i).imprimir();
					}
					System.out.println();
					break;
				}
				default: {
					System.out.println("\nOpção Inválida");
					opcao = this.apresentarOpcoesConsulta();
				}
			}
		}
	}
	
	private int apresentarOpcoesConsulta() {
		System.out.println("\nInforme o tipo de consulta a ser realizada");
		System.out.println(OPCAO_MENU_CONSULTAR_TODOS_CHAMADOS + " - Consultar todos os Chamados");
		System.out.println(OPCAO_MENU_CONSULTAR_CHAMADOS_ABERTOS + " - Consultar todos os Chamados Abertos");
		System.out.println(OPCAO_MENU_CONSULTAR_CHAMADOS_FECHADOS + " - Consultar todos os Chamados Fechados");
		System.out.println(OPCAO_MENU_CONSULTAR_CHAMADOS_SAIR + " - Voltar");
		System.out.print("\nDigite a Opção: ");
		return Integer.parseInt(teclado.nextLine());
	}
	
}
