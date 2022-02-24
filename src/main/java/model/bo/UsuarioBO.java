package model.bo;

import java.util.ArrayList;

import model.dao.UsuarioDAO;
import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;

public class UsuarioBO {
	
	public ArrayList<TipoUsuarioVO> consultarTipoUsuariosBO() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.consultarTipoUsuariosDAO();
	}

	public UsuarioVO realizarLoginBO(UsuarioVO usuarioVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.realizarLoginDAO(usuarioVO);
	}

	public UsuarioVO cadastrarUsuarioBO(UsuarioVO usuarioVO) {
		UsuarioVO usuario = new UsuarioVO();
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		if(usuarioDAO.verificarExistenciaRegistroPorCpfDAO(usuarioVO.getCpf())){
			System.out.println("\nUsuário já Cadastrado!");
		} else {
			usuario = usuarioDAO.cadastrarUsuarioDAO(usuarioVO);
		}
		return usuario;
	}

	public boolean excluirUsuarioBO(UsuarioVO usuarioVO) {
		boolean resultado = false;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		if(usuarioDAO.verificarExistenciaRegistroPorIdUsuario(usuarioVO.getIdUsuario())){
			if(usuarioDAO.verificarDesligamentoUsuarioPorIdUsuarioDAO(usuarioVO.getIdUsuario())) {
				System.out.println("\nUsuário já se encontra desligado na base da dados.");
			} else {
				resultado = usuarioDAO.excluirUsuarioDAO(usuarioVO);
			}
		} else {
			System.out.println("\nUsuário não existe na base da dados.");
		}
		return resultado;
	}

	public boolean atualizarUsuarioBO(UsuarioVO usuarioVO) {
		boolean resultado = false;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		if(usuarioDAO.verificarExistenciaRegistroPorIdUsuario(usuarioVO.getIdUsuario())){
			if(usuarioDAO.verificarDesligamentoUsuarioPorIdUsuarioDAO(usuarioVO.getIdUsuario())) {
				System.out.println("\nUsuário já se encontra desligado na base da dados.");
			} else {
				resultado = usuarioDAO.atualizarUsuarioDAO(usuarioVO);
			}
		} else {
			System.out.println("\nUsuário ainda não foi cadastrado.");
		}
		return resultado;
	}

	public ArrayList<UsuarioVO> consultarTodosUsuarioBO() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		ArrayList<UsuarioVO> listaUsuariosVO = usuarioDAO.consultarTodosUsuariosDAO();
		if(listaUsuariosVO.isEmpty()){
			System.out.println("\nLista de Usuários está vazia.");
		}
		return listaUsuariosVO;
	}

	public UsuarioVO consultarUsuarioBO(UsuarioVO usuarioVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		UsuarioVO usuario = usuarioDAO.consultarUsuarioDAO(usuarioVO);
		if(usuario == null){
			System.out.println("\nUsuário não Localizado.");
		}
		return usuario;
	}

}
