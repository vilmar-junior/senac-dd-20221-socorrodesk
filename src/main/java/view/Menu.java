package view;

import java.util.Scanner;

import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;

public class Menu {

	Scanner teclado = new Scanner(System.in);
	
	private static final int OPCAO_MENU_CHAMADOS = 1;
	private static final int OPCAO_MENU_ATENDIMENTO = 2;
	private static final int OPCAO_MENU_RELATORIO = 3;
	private static final int OPCAO_MENU_USUARIO = 4;
	private static final int OPCAO_MENU_SAIR = 9;

	public void apresentarMenu(UsuarioVO usuarioVO) {
		int opcao = this.apresentarOpcoesMenu(usuarioVO);
		while (opcao != OPCAO_MENU_SAIR){
			switch(opcao){
				case OPCAO_MENU_CHAMADOS: {
					MenuChamado menuChamado = new MenuChamado();
					menuChamado.apresentarMenuChamado(usuarioVO);
					break;
				}
				case OPCAO_MENU_ATENDIMENTO: {
					if(!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.USUARIO)) {
						MenuAtendimento menuAtendimento = new MenuAtendimento();
						menuAtendimento.apresentarMenuAtendimento(usuarioVO);
					}
					break;
				}
				case OPCAO_MENU_RELATORIO: {
					if(!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.USUARIO)) {
						MenuRelatorio menuRelatorio = new MenuRelatorio();
						menuRelatorio.apresentarMenuRelatorio();
					}
					break;
				}
				case OPCAO_MENU_USUARIO: {
					if(usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.ADMINISTRADOR)) {
						MenuUsuario menuUsuario = new MenuUsuario();
						menuUsuario.apresentarMenuUsuario();
					}
					break;
				}
				default: {
					System.out.println("\nOpÃ§Ã£o Inválida");
				}
			}
			opcao = this.apresentarOpcoesMenu(usuarioVO);
		}
	}
	
	
	private int apresentarOpcoesMenu(UsuarioVO usuarioVO) {
		System.out.println("\n---- Sistema Socorro Desk ----");
		System.out.println("---- Menu Principal ----");
		System.out.println("\nOpÃ§Ãµes:");
		System.out.println(OPCAO_MENU_CHAMADOS + " - Chamados");
		if(!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.USUARIO)) {
			System.out.println(OPCAO_MENU_ATENDIMENTO + " - Atendimento");
			System.out.println(OPCAO_MENU_RELATORIO + " - RelatÃ³rio");
		}
		if(usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.ADMINISTRADOR)) {
			System.out.println(OPCAO_MENU_USUARIO + " - Usuário");
		}
		System.out.println(OPCAO_MENU_SAIR + " - Sair");
		System.out.print("\nDigite a OpÃ§Ã£o: ");
		return Integer.parseInt(teclado.nextLine());
	}
	

}
