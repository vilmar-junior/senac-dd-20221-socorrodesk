package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.vo.ChamadoVO;
import model.vo.UsuarioVO;

public class ChamadoDAO {
	
	DateTimeFormatter formaterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public ChamadoVO cadastrarChamadoDAO(ChamadoVO chamadoVO) {
		String query = "INSERT INTO chamados (idUsuario, titulo, descricao, dataAbertura) VALUES (?,?,?,?)";
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query);
		try {
			pstmt.setInt(1, chamadoVO.getIdUsuario());
			pstmt.setString(2, chamadoVO.getTitulo());
			pstmt.setString(3, chamadoVO.getDescricao());
			pstmt.setObject(4, chamadoVO.getDataAbertura());
			pstmt.execute();
			
			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if(generatedKeys.next()) {
				chamadoVO.setIdChamado(generatedKeys.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query de Cadastro do Chamado.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeStatement(pstmt);
			Banco.closeConnection(conn);
		}
		return chamadoVO;
	}

	public boolean verificarExistenciaRegistroPorIdChamado(int idChamado) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		ResultSet resultado = null;
		String query = "SELECT idChamado FROM chamados WHERE idChamado = " + idChamado;
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()){
				retorno = true;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query que verifica existência de Registro por Id.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}
	
	public boolean verificarDonoChamado(ChamadoVO chamadoVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		ResultSet resultado = null;
		String query = "SELECT idChamado FROM chamados WHERE idChamado = " + chamadoVO.getIdChamado() 
					+ " AND idUsuario = " + chamadoVO.getIdUsuario();
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()){
				retorno = true;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query que verifica se o chamado pertence ao usuário.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean verificarFechamentoChamadoPorIdChamadoDAO(int idChamado) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		ResultSet resultado = null;
		String query = "SELECT dataFechamento FROM chamados WHERE idChamado = " + idChamado;
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()){
				String dataFechamento = resultado.getString(1);
				if(dataFechamento != null) {
					retorno = true;
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query que verifica fechamento do chamado por Id.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}
	
	public boolean atualizarChamadoDAO(ChamadoVO chamadoVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		String query = "UPDATE chamados SET titulo = '" + chamadoVO.getTitulo() 
					+ "', descricao = '" + chamadoVO.getDescricao()
					+ "' WHERE idChamado = " + chamadoVO.getIdChamado();
		try{
			if(stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException e){
			System.out.println("Erro ao executar a Query de Atualização do Chamado.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean excluirChamadoDAO(ChamadoVO chamadoVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		String query = "DELETE FROM chamados WHERE idChamado = " + chamadoVO.getIdChamado();
		try{
			if(stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException e){
			System.out.println("Erro ao executar a Query de Exclusão do Chamado.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public ArrayList<ChamadoVO> consultarTodosChamadosUsuarioDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<ChamadoVO> chamadosVO = new ArrayList<ChamadoVO>();
		String query = "SELECT idChamado, idUsuario, idTecnico, titulo, descricao, dataAbertura, "
				+ "solucao, dataFechamento "
				+ "FROM chamados "
				+ "WHERE idUsuario = " + usuarioVO.getIdUsuario(); 
		try{
			resultado = stmt.executeQuery(query);
			while(resultado.next()){
				ChamadoVO chamadoVO = new ChamadoVO();
				chamadoVO.setIdChamado(Integer.parseInt(resultado.getString(1)));
				chamadoVO.setIdUsuario(Integer.parseInt(resultado.getString(2)));
				if(resultado.getString(3) != null) {
					chamadoVO.setIdTecnico(Integer.parseInt(resultado.getString(3)));
				}
				chamadoVO.setTitulo(resultado.getString(4));
				chamadoVO.setDescricao(resultado.getString(5));
				chamadoVO.setDataAbertura(LocalDate.parse(resultado.getString(6), formaterDate));
				if(resultado.getString(7) != null) {
					chamadoVO.setSolucao(resultado.getString(7));
				} else {
					chamadoVO.setSolucao("");
				}
				if(resultado.getString(8) != null) {
					chamadoVO.setDataFechamento(LocalDate.parse(resultado.getString(8), formaterDate));
				}
				chamadosVO.add(chamadoVO);
			}
		} catch (SQLException e){
			System.out.println("Erro ao executar a Query de Consulta de Todos os Chamados do Usuário.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return chamadosVO;
	}

	public ArrayList<ChamadoVO> consultarChamadosAbertosUsuarioDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<ChamadoVO> chamadosVO = new ArrayList<ChamadoVO>();
		String query = "SELECT idChamado, idUsuario, titulo, descricao, dataAbertura "
				+ "FROM chamados "
				+ "WHERE idUsuario = " + usuarioVO.getIdUsuario() + " "
				+ "AND dataFechamento is null"; 
		try{
			resultado = stmt.executeQuery(query);
			while(resultado.next()){
				ChamadoVO chamadoVO = new ChamadoVO();
				chamadoVO.setIdChamado(Integer.parseInt(resultado.getString(1)));
				chamadoVO.setIdUsuario(Integer.parseInt(resultado.getString(2)));
				chamadoVO.setTitulo(resultado.getString(3));
				chamadoVO.setDescricao(resultado.getString(4));
				chamadoVO.setDataAbertura(LocalDate.parse(resultado.getString(5), formaterDate));
				chamadoVO.setSolucao("");
				chamadosVO.add(chamadoVO);
			}
		} catch (SQLException e){
			System.out.println("Erro ao executar a Query de Consulta dos Chamados Abertos do Usuário.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return chamadosVO;
	}

	public ArrayList<ChamadoVO> consultarChamadosFechadosUsuarioDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<ChamadoVO> chamadosVO = new ArrayList<ChamadoVO>();
		String query = "SELECT idChamado, idUsuario, idTecnico, titulo, descricao, dataAbertura, "
				+ "solucao, dataFechamento "
				+ "FROM chamados "
				+ "WHERE idUsuario = " + usuarioVO.getIdUsuario() + " "
				+ "AND dataFechamento is not null"; 
		try{
			resultado = stmt.executeQuery(query);
			while(resultado.next()){
				ChamadoVO chamadoVO = new ChamadoVO();
				chamadoVO.setIdChamado(Integer.parseInt(resultado.getString(1)));
				chamadoVO.setIdUsuario(Integer.parseInt(resultado.getString(2)));
				chamadoVO.setIdTecnico(Integer.parseInt(resultado.getString(3)));
				chamadoVO.setTitulo(resultado.getString(4));
				chamadoVO.setDescricao(resultado.getString(5));
				chamadoVO.setDataAbertura(LocalDate.parse(resultado.getString(6), formaterDate));
				chamadoVO.setSolucao(resultado.getString(7));
				chamadoVO.setDataFechamento(LocalDate.parse(resultado.getString(8), formaterDate));
				chamadosVO.add(chamadoVO);
			}
		} catch (SQLException e){
			System.out.println("Erro ao executar a Query de Consulta dos Chamados Fechados do Usuário.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return chamadosVO;
	}

	public ArrayList<ChamadoVO> listarChamadosAbertosDAO() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<ChamadoVO> chamadosVO = new ArrayList<ChamadoVO>();
		String query = "SELECT idChamado, idUsuario, titulo, descricao, dataAbertura "
				+ "FROM chamados "
				+ "WHERE dataFechamento is null"; 
		try{
			resultado = stmt.executeQuery(query);
			while(resultado.next()){
				ChamadoVO chamadoVO = new ChamadoVO();
				chamadoVO.setIdChamado(Integer.parseInt(resultado.getString(1)));
				chamadoVO.setIdUsuario(Integer.parseInt(resultado.getString(2)));
				chamadoVO.setTitulo(resultado.getString(3));
				chamadoVO.setDescricao(resultado.getString(4));
				chamadoVO.setDataAbertura(LocalDate.parse(resultado.getString(5), formaterDate));
				chamadoVO.setSolucao("");
				chamadosVO.add(chamadoVO);
			}
		} catch (SQLException e){
			System.out.println("Erro ao executar a Query que Lista os Chamados Abertos.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return chamadosVO;
	}

	public ArrayList<ChamadoVO> listarChamadosFechadosTecnicoDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<ChamadoVO> chamadosVO = new ArrayList<ChamadoVO>();
		String query = "SELECT idChamado, idUsuario, idTecnico, titulo, descricao, dataAbertura, "
				+ "solucao, dataFechamento "
				+ "FROM chamados "
				+ "WHERE idTecnico = " + usuarioVO.getIdUsuario() + " "
				+ "AND dataFechamento is not null"; 
		try{
			resultado = stmt.executeQuery(query);
			while(resultado.next()){
				ChamadoVO chamadoVO = new ChamadoVO();
				chamadoVO.setIdChamado(Integer.parseInt(resultado.getString(1)));
				chamadoVO.setIdUsuario(Integer.parseInt(resultado.getString(2)));
				chamadoVO.setIdTecnico(Integer.parseInt(resultado.getString(3)));
				chamadoVO.setTitulo(resultado.getString(4));
				chamadoVO.setDescricao(resultado.getString(5));
				chamadoVO.setDataAbertura(LocalDate.parse(resultado.getString(6), formaterDate));
				chamadoVO.setSolucao(resultado.getString(7));
				chamadoVO.setDataFechamento(LocalDate.parse(resultado.getString(8), formaterDate));
				chamadosVO.add(chamadoVO);
			}
		} catch (SQLException e){
			System.out.println("Erro ao executar a Query de Consulta dos Chamados Fechados do Usuário.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return chamadosVO;
	}

	public ChamadoVO atenderChamadoDAO(ChamadoVO chamadoVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ChamadoVO retorno = new ChamadoVO();
		String query = "UPDATE chamados SET idTecnico = " + chamadoVO.getIdTecnico() 
					+ ", solucao = '" + chamadoVO.getSolucao()
					+ "', dataFechamento = '" + chamadoVO.getDataFechamento()
					+ "' WHERE idChamado = " + chamadoVO.getIdChamado();
		try{
			if(stmt.executeUpdate(query) == 1) {
				retorno = this.consultarChamadoAtendido(chamadoVO.getIdChamado());
			}
		} catch (SQLException e){
			System.out.println("Erro ao executar a Query de Atendimento do Chamado.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	private ChamadoVO consultarChamadoAtendido(int idChamado) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ChamadoVO chamadoVO = new ChamadoVO();
		String query = "SELECT idChamado, idUsuario, idTecnico, titulo, descricao, dataAbertura, "
				+ "solucao, dataFechamento "
				+ "FROM chamados "
				+ "WHERE idChamado = " + idChamado; 
		try{
			resultado = stmt.executeQuery(query);
			while(resultado.next()){
				chamadoVO.setIdChamado(Integer.parseInt(resultado.getString(1)));
				chamadoVO.setIdUsuario(Integer.parseInt(resultado.getString(2)));
				chamadoVO.setIdTecnico(Integer.parseInt(resultado.getString(3)));
				chamadoVO.setTitulo(resultado.getString(4));
				chamadoVO.setDescricao(resultado.getString(5));
				chamadoVO.setDataAbertura(LocalDate.parse(resultado.getString(6), formaterDate));
				chamadoVO.setSolucao(resultado.getString(7));
				chamadoVO.setDataFechamento(LocalDate.parse(resultado.getString(8), formaterDate));
			}
		} catch (SQLException e){
			System.out.println("Erro ao executar a Query de Consulta do Chamado pelo código.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return chamadoVO;
	}

}
