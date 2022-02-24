package model.bo;

import java.util.ArrayList;

import model.dao.ChamadoDAO;
import model.vo.ChamadoVO;
import model.vo.UsuarioVO;


public class ChamadoBO {

	public ChamadoVO cadastrarChamadoBO(ChamadoVO chamadoVO) {
		ChamadoDAO chamadoDAO = new ChamadoDAO();
		return chamadoDAO.cadastrarChamadoDAO(chamadoVO);
	}
	
	public boolean atualizarChamadoBO(ChamadoVO chamadoVO) {
		boolean resultado = false;
		ChamadoDAO chamadoDAO = new ChamadoDAO();
		if(chamadoDAO.verificarExistenciaRegistroPorIdChamado(chamadoVO.getIdChamado())){
			if(chamadoDAO.verificarDonoChamado(chamadoVO)) {
				if(chamadoDAO.verificarFechamentoChamadoPorIdChamadoDAO(chamadoVO.getIdChamado())) {
					System.out.println("\nChamado já se encontra fechado na base da dados.");
				} else {
					resultado = chamadoDAO.atualizarChamadoDAO(chamadoVO);
				}
			} else {
				System.out.println("\nO chamado não pertence a esse usuário.");
			}
		} else {
			System.out.println("\nChamado não existe na base da dados.");
		}
		return resultado;
	}
	
	public boolean excluirChamadoBO(ChamadoVO chamadoVO) {
		boolean resultado = false;
		ChamadoDAO chamadoDAO = new ChamadoDAO();
		if(chamadoDAO.verificarExistenciaRegistroPorIdChamado(chamadoVO.getIdChamado())){
			if(chamadoDAO.verificarDonoChamado(chamadoVO)) {
				if(chamadoDAO.verificarFechamentoChamadoPorIdChamadoDAO(chamadoVO.getIdChamado())) {
					System.out.println("\nChamado já se encontra fechado na base da dados.");
				} else {
					resultado = chamadoDAO.excluirChamadoDAO(chamadoVO);
				}
			} else {
				System.out.println("\nO chamado não pertence a esse usuário.");
			}
		} else {
			System.out.println("\nChamado não existe na base da dados.");
		}
		return resultado;
	}

	public ArrayList<ChamadoVO> consultarTodosChamadosUsuarioBO(UsuarioVO usuarioVO) {
		ChamadoDAO chamadoDAO = new ChamadoDAO();
		ArrayList<ChamadoVO> chamadosVO = chamadoDAO.consultarTodosChamadosUsuarioDAO(usuarioVO);
		if(chamadosVO.isEmpty()){
			System.out.println("\nLista de Chamados está vazia.");
		}
		return chamadosVO;
	}

	public ArrayList<ChamadoVO> consultarChamadosAbertosUsuarioBO(UsuarioVO usuarioVO) {
		ChamadoDAO chamadoDAO = new ChamadoDAO();
		ArrayList<ChamadoVO> chamadosVO = chamadoDAO.consultarChamadosAbertosUsuarioDAO(usuarioVO);
		if(chamadosVO.isEmpty()){
			System.out.println("\nLista de Chamados está vazia.");
		}
		return chamadosVO;
	}

	public ArrayList<ChamadoVO> consultarChamadosFechadosUsuarioBO(UsuarioVO usuarioVO) {
		ChamadoDAO chamadoDAO = new ChamadoDAO();
		ArrayList<ChamadoVO> chamadosVO = chamadoDAO.consultarChamadosFechadosUsuarioDAO(usuarioVO);
		if(chamadosVO.isEmpty()){
			System.out.println("\nLista de Chamados está vazia.");
		}
		return chamadosVO;
	}

	public ArrayList<ChamadoVO> listarChamadosAbertosBO() {
		ChamadoDAO chamadoDAO = new ChamadoDAO();
		ArrayList<ChamadoVO> chamadosVO = chamadoDAO.listarChamadosAbertosDAO();
		if(chamadosVO.isEmpty()){
			System.out.println("\nLista de Chamados está vazia.");
		}
		return chamadosVO;
	}

	public ArrayList<ChamadoVO> listarChamadosFechadosTecnicoBO(UsuarioVO usuarioVO) {
		ChamadoDAO chamadoDAO = new ChamadoDAO();
		ArrayList<ChamadoVO> chamadosVO = chamadoDAO.listarChamadosFechadosTecnicoDAO(usuarioVO);
		if(chamadosVO.isEmpty()){
			System.out.println("\nLista de Chamados está vazia.");
		}
		return chamadosVO;
	}

	public ChamadoVO atenderChamadoBO(ChamadoVO chamadoVO) {
		ChamadoVO retorno = new ChamadoVO();
		ChamadoDAO chamadoDAO = new ChamadoDAO();
		if(chamadoDAO.verificarExistenciaRegistroPorIdChamado(chamadoVO.getIdChamado())){
			if(chamadoDAO.verificarFechamentoChamadoPorIdChamadoDAO(chamadoVO.getIdChamado())) {
				System.out.println("\nChamado já se encontra fechado na base da dados.");
			} else {
				retorno = chamadoDAO.atenderChamadoDAO(chamadoVO);
			}
		} else {
			System.out.println("\nChamado não existe na base da dados.");
		}
		return retorno;
	}

}
