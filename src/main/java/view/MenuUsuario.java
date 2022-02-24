package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import controller.UsuarioController;
import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;

public class MenuUsuario {

	Scanner teclado = new Scanner(System.in);
	DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private static final int OPCAO_MENU_CADASTRAR_USUARIO = 1;
	private static final int OPCAO_MENU_CONSULTAR_USUARIO = 2;
	private static final int OPCAO_MENU_ATUALIZAR_USUARIO = 3;
	private static final int OPCAO_MENU_EXCLUIR_USUARIO = 4;
	private static final int OPCAO_MENU_USUARIO_SAIR = 9;
	
	private static final int OPCAO_MENU_CONSULTAR_TODOS_USUARIOS = 1;
	private static final int OPCAO_MENU_CONSULTAR_UM_USUARIO = 2;
	private static final int OPCAO_MENU_CONSULTAR_USUARIO_SAIR = 9;
	
	public void apresentarMenuUsuario() {
		int opcao = apresentarOpcoesMenu();
		while (opcao != OPCAO_MENU_USUARIO_SAIR) {
			switch (opcao) {
				case OPCAO_MENU_CADASTRAR_USUARIO: {
					UsuarioVO usuarioVO = new UsuarioVO();
					this.cadastrarUsuario(usuarioVO);
					break;
				}
				case OPCAO_MENU_CONSULTAR_USUARIO: {
					this.consultarUsuario();
					break;
				}
				case OPCAO_MENU_ATUALIZAR_USUARIO: {
					this.atualizarUsuario();
					break;
				}
				case OPCAO_MENU_EXCLUIR_USUARIO: {
					this.excluirUsuario();
					break;
				}
				default: {
					System.out.println("\nOpção Inválida"); 
				}
			}
			opcao = apresentarOpcoesMenu();
		}
	}
	
	private int apresentarOpcoesMenu() {
		System.out.println("\n---- Sistema Socorro Desk ---- \n---- Menu Cadastro de Usuários ----");
		System.out.println("\nOpções:");
		System.out.println(OPCAO_MENU_CADASTRAR_USUARIO + " - Cadastrar Usuário");
		System.out.println(OPCAO_MENU_CONSULTAR_USUARIO + " - Consultar Usuário");
		System.out.println(OPCAO_MENU_ATUALIZAR_USUARIO + " - Atualizar Usuário");
		System.out.println(OPCAO_MENU_EXCLUIR_USUARIO + " - Excluir Usuário");
		System.out.println(OPCAO_MENU_USUARIO_SAIR + " - Voltar");
		System.out.print("\nDigite a Opção: ");
		return Integer.parseInt(teclado.nextLine());
	}
	
	public void criarNovoUsuario(UsuarioVO usuarioVO) {
		this.cadastrarUsuario(usuarioVO);
	}
	
	private void cadastrarUsuario(UsuarioVO usuarioVO) {
		if(usuarioVO.getTipoUsuario() == null) {
			do {
				usuarioVO.setTipoUsuario(TipoUsuarioVO.getTipoUsuarioVOPorValor(apresentarOpcoesTipoUsuario()));
			} while(usuarioVO.getTipoUsuario() == null); 
		}
		System.out.println();
		System.out.print("Digite o nome: ");
		usuarioVO.setNome(teclado.nextLine());
		System.out.print("Digite o CPF: ");
		usuarioVO.setCpf(teclado.nextLine());
		System.out.print("Digite o e-mail: ");
		usuarioVO.setEmail(teclado.nextLine());
		usuarioVO.setDataCadastro(LocalDate.now());
		System.out.print("Digite um login: ");
		usuarioVO.setLogin(teclado.nextLine());
		System.out.print("Digite uma senha: ");
		usuarioVO.setSenha(teclado.nextLine());
		
		if(this.validarCamposCadastro(usuarioVO)) {
			UsuarioController usuarioController = new UsuarioController();
			usuarioVO = usuarioController.cadastrarUsuarioController(usuarioVO);
			if(usuarioVO.getIdUsuario() != 0) {
				System.out.println("Usuário cadastrado com Sucesso!");
			} else {
				System.out.println("Não foi possível cadastrar o Usuário!");
			}
		}
	}
	
	private boolean validarCamposCadastro(UsuarioVO usuarioVO) {
		boolean resultado = true;
		System.out.println();
		if(usuarioVO.getNome() == null || usuarioVO.getNome().isEmpty()) {
			System.out.println("O campo nome é obrigatório.");
			resultado  = false;
		}
		if(usuarioVO.getCpf() == null || usuarioVO.getCpf().isEmpty()) {
			System.out.println("O campo CPF é obrigatório.");
			resultado  = false;
		}
		if(usuarioVO.getEmail() == null || usuarioVO.getEmail().isEmpty()) {
			System.out.println("O campo e-mail é obrigatório.");
			resultado  = false;
		}
		if(usuarioVO.getDataCadastro() == null) {
			System.out.println("O campo data de cadastro é obrigatório.");
			resultado  = false;
		}
		if(usuarioVO.getLogin() == null || usuarioVO.getLogin().isEmpty()) {
			System.out.println("O campo login é obrigatório.");
			resultado  = false;
		}
		if(usuarioVO.getSenha() == null || usuarioVO.getSenha().isEmpty()) {
			System.out.println("O campo senha é obrigatório.");
			resultado  = false;
		}
		return resultado;
	}

	private int apresentarOpcoesTipoUsuario() {
		UsuarioController usuarioController = new UsuarioController();
		ArrayList<TipoUsuarioVO> tipoUsuariosVO = usuarioController.consultarTipoUsuarios();
		System.out.println("\n-------- Tipos de Usuários --------");
		System.out.println("Opções:");
		for(int i = 0; i < tipoUsuariosVO.size(); i++) {
			System.out.println(tipoUsuariosVO.get(i).getValor() + " - " + tipoUsuariosVO.get(i));
		}
		System.out.print("\nDigite a Opção: ");
		return Integer.parseInt(teclado.nextLine());
	}

	private void excluirUsuario() {
		UsuarioVO usuarioVO = new UsuarioVO();
		System.out.print("\nInforme o código do Usuário: ");
		usuarioVO.setIdUsuario(Integer.parseInt(teclado.nextLine()));
		System.out.print("Digite a data de expiração: ");
		usuarioVO.setDataExpiracao(LocalDate.parse(teclado.nextLine(), dataFormatter));
		
		if(usuarioVO.getIdUsuario() == 0 || usuarioVO.getDataExpiracao() == null) {
			System.out.println("Os campos código do usuário e data de expiração são obrigatórios.");
		} else {
			UsuarioController usuarioController = new UsuarioController();
			boolean resultado = usuarioController.excluirUsuarioController(usuarioVO);
			if(resultado){
				System.out.println("\nUsuário excluído com Sucesso.");
			} else {
				System.out.println("Não foi possível excluir o Usuário.");
			}
		}
	}

	private void atualizarUsuario() {
		UsuarioVO usuarioVO = new UsuarioVO();
		System.out.print("\nInforme o código do Usuário: ");
		usuarioVO.setIdUsuario(Integer.parseInt(teclado.nextLine()));
		do {
			usuarioVO.setTipoUsuario(TipoUsuarioVO.getTipoUsuarioVOPorValor(apresentarOpcoesTipoUsuario()));
		} while(usuarioVO.getTipoUsuario() == null); 
		System.out.println();
		System.out.print("Digite o nome: ");
		usuarioVO.setNome(teclado.nextLine());
		System.out.print("Digite o CPF: ");
		usuarioVO.setCpf(teclado.nextLine());
		System.out.print("Digite o e-mail: ");
		usuarioVO.setEmail(teclado.nextLine());
		System.out.print("Digite a data de cadastro: ");
		usuarioVO.setDataCadastro(LocalDate.parse(teclado.nextLine(), dataFormatter));
		System.out.print("Digite um login: ");
		usuarioVO.setLogin(teclado.nextLine());
		System.out.print("Digite uma senha: ");
		usuarioVO.setSenha(teclado.nextLine());

		if(this.validarCamposCadastro(usuarioVO)) {
			UsuarioController usuarioController = new UsuarioController();
			boolean resultado = usuarioController.atualizarUsuarioController(usuarioVO);
			if(resultado){
				System.out.println("\nUsuário atualizado com Sucesso.");
			} else {
				System.out.println("Não foi possível atualizar o Usuário.");
			}
		}
	}

	private void consultarUsuario() {
		int opcao = this.apresentarOpcoesConsulta();
		UsuarioController usuarioController = new UsuarioController();
		while (opcao != OPCAO_MENU_CONSULTAR_USUARIO_SAIR) {
			switch (opcao) {
				case OPCAO_MENU_CONSULTAR_TODOS_USUARIOS: {
					opcao = OPCAO_MENU_CONSULTAR_USUARIO_SAIR;
					ArrayList<UsuarioVO> listaUsuariosVO = usuarioController.consultarTodosUsuariosController();
					System.out.print("\n--------- RESULTADO DA CONSULTA ---------");
					System.out.printf("\n%3s  %-13s  %-20s  %-11s  %-20s  %-15s  %-15s  %-10s  %-10s  ", 
							"ID", "TIPO USUARIO", "NOME", "CPF", "E-MAIL", "DATA CADASTRO", 
							"DATA EXPIRAÇÃO", "LOGIN", "SENHA");
					for (int i = 0; i < listaUsuariosVO.size(); i++) {
						listaUsuariosVO.get(i).imprimir();
					}
					System.out.println();
					break;
				}
				case OPCAO_MENU_CONSULTAR_UM_USUARIO: {
					opcao = OPCAO_MENU_CONSULTAR_USUARIO_SAIR;
					UsuarioVO usuarioVO = new UsuarioVO();
					System.out.print("\nInforme o código do Usuário: ");
					usuarioVO.setIdUsuario(Integer.parseInt(teclado.nextLine()));
					if(usuarioVO.getIdUsuario() != 0) {
						UsuarioVO usuario = usuarioController.consultarUsuarioController(usuarioVO);
						System.out.print("\n--------- RESULTADO DA CONSULTA ---------");
						System.out.printf("\n%3s  %-13s  %-20s  %-11s  %-20s  %-15s  %-15s  %-10s  %-10s  ", 
								"ID", "TIPO USUARIO", "NOME", "CPF", "E-MAIL", "DATA CADASTRO", 
								"DATA EXPIRAÇÃO", "LOGIN", "SENHA");
						usuario.imprimir();
						System.out.println();
					} else {
						System.out.println("O campo código do usuário e obrigatório.");
					}

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
		System.out.println(OPCAO_MENU_CONSULTAR_TODOS_USUARIOS + " - Consultar todos os Usuários");
		System.out.println(OPCAO_MENU_CONSULTAR_UM_USUARIO + " - Consultar um Usuário Específico");
		System.out.println(OPCAO_MENU_CONSULTAR_USUARIO_SAIR + " - Voltar");
		System.out.print("\nDigite a Opção: ");
		return Integer.parseInt(teclado.nextLine());
	}
	
}
