package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;

public class UsuarioDAO {
	
	DateTimeFormatter formaterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public ArrayList<TipoUsuarioVO> consultarTipoUsuariosDAO() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<TipoUsuarioVO> tipoUsuariosVO = new ArrayList<TipoUsuarioVO>();
		String query = "SELECT descricao FROM tipousuario";
		try{
			resultado = stmt.executeQuery(query);
			while(resultado.next()){
				TipoUsuarioVO tipoUsuarioVO = TipoUsuarioVO.valueOf(resultado.getString(1));
				tipoUsuariosVO.add(tipoUsuarioVO);
			}
		} catch (SQLException e){
			System.out.println("Erro ao executar a Query de Consulta de Tipo de Usuários.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return tipoUsuariosVO;
	}

	
	public UsuarioVO realizarLoginDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		String query = "SELECT u.idusuario, tipo.descricao, u.nome, u.cpf, u.email, u.datacadastro, u.dataexpiracao "
				+ " FROM usuario u, tipousuario tipo "
				+ " WHERE u.login = '" + usuarioVO.getLogin() + "' "
				+ " AND u.senha = '" + usuarioVO.getSenha() + "'"
				+ " AND u.idtipousuario = tipo.idtipousuario";
		try{
			resultado = stmt.executeQuery(query);
			if(resultado.next()){
				usuarioVO.setIdUsuario(Integer.parseInt(resultado.getString(1)));
				usuarioVO.setTipoUsuario(TipoUsuarioVO.valueOf(resultado.getString(2)));
				usuarioVO.setNome(resultado.getString(3));
				usuarioVO.setCpf(resultado.getString(4));
				usuarioVO.setEmail(resultado.getString(5));
				usuarioVO.setDataCadastro(LocalDate.parse(resultado.getString(6), formaterDate));
				if(resultado.getString(7) != null) {
					usuarioVO.setDataExpiracao(LocalDate.parse(resultado.getString(7), formaterDate));
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query que realiza o Login.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return usuarioVO;
	}
	
	
	public boolean verificarExistenciaRegistroPorCpfDAO(String cpf) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		ResultSet resultado = null;
		String query = "SELECT cpf FROM usuario WHERE cpf = '" + cpf + "'";
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()){
				retorno = true;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query que verifica existência de Usuário por CPF.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	
	public UsuarioVO cadastrarUsuarioDAO(UsuarioVO usuarioVO) {

		String query = "INSERT INTO usuario (idtipousuario, nome, cpf, email, datacadastro, login, senha) VALUES (?,?,?,?,?,?,?)";
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query);
		try {
			pstmt.setInt(1, usuarioVO.getTipoUsuario().getValor());
			pstmt.setString(2, usuarioVO.getNome());
			pstmt.setString(3, usuarioVO.getCpf());
			pstmt.setString(4, usuarioVO.getEmail());
			pstmt.setObject(5, usuarioVO.getDataCadastro());
			pstmt.setString(6, usuarioVO.getLogin());
			pstmt.setString(7, usuarioVO.getSenha());
			pstmt.execute();
			
			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if(generatedKeys.next()) {
				usuarioVO.setIdUsuario(generatedKeys.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query de Cadastro do Usuário.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeStatement(pstmt);
			Banco.closeConnection(conn);
		}
		return usuarioVO;
	}

	
	public boolean verificarExistenciaRegistroPorIdUsuario(int idUsuario) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		ResultSet resultado = null;
		String query = "SELECT idUsuario FROM usuario WHERE idUsuario = " + idUsuario;
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
	
	
	public boolean verificarDesligamentoUsuarioPorIdUsuarioDAO(int idUsuario) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		ResultSet resultado = null;
		String query = "SELECT dataExpiracao FROM usuario WHERE idUsuario = " + idUsuario;
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()){
				String dataExpiracao = resultado.getString(1);
				if(dataExpiracao != null) {
					retorno = true;
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query que verifica desligamento do usuário por Id.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	
	public boolean excluirUsuarioDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		String query = "UPDATE usuario SET dataexpiracao = '" + usuarioVO.getDataExpiracao() 
					+ "' WHERE idusuario = " + usuarioVO.getIdUsuario();
		try {
			if(stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query de Exclusão do Usuário.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	
	public boolean atualizarUsuarioDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		String query = "UPDATE usuario SET idtipousuario = " + usuarioVO.getTipoUsuario().getValor()
					+ ", nome = '" + usuarioVO.getNome() 
					+ "', cpf = '" + usuarioVO.getCpf() 
					+ "', email = '" + usuarioVO.getEmail() 
					+ "', datacadastro = '" + usuarioVO.getDataCadastro() 
					+ "', login = '" + usuarioVO.getLogin() 
					+ "', senha = '" + usuarioVO.getSenha() 
					+ "' WHERE idusuario = " + usuarioVO.getIdUsuario();
		try {
			if(stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query de Atualização do Usuário.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	
	public ArrayList<UsuarioVO> consultarTodosUsuariosDAO() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<UsuarioVO> listaUsuariosVO = new ArrayList<UsuarioVO>();
		String query = "SELECT u.idUsuario, tipo.descricao, u.nome, u.cpf, u.email, u.dataCadastro, "
				+ "u.dataExpiracao, u.login, u.senha "
				+ "FROM usuario u, tipoUsuario tipo "
				+ "WHERE u.idTipoUsuario = tipo.idTipoUsuario";
		try{
			resultado = stmt.executeQuery(query);
			while(resultado.next()){
				UsuarioVO usuarioVO = new UsuarioVO();
				usuarioVO.setIdUsuario(Integer.parseInt(resultado.getString(1)));
				usuarioVO.setTipoUsuario(TipoUsuarioVO.valueOf(resultado.getString(2)));
				usuarioVO.setNome(resultado.getString(3));
				usuarioVO.setCpf(resultado.getString(4));
				usuarioVO.setEmail(resultado.getString(5));
				usuarioVO.setDataCadastro(LocalDate.parse(resultado.getString(6), formaterDate));
				if(resultado.getString(7) != null) {
					usuarioVO.setDataExpiracao(LocalDate.parse(resultado.getString(7), formaterDate));
				}
				usuarioVO.setLogin(resultado.getString(8));
				usuarioVO.setSenha(resultado.getString(9));
				listaUsuariosVO.add(usuarioVO);
			}
		} catch (SQLException e){
			System.out.println("Erro ao executar a Query de Consulta de Usuários.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return listaUsuariosVO;
	}

	
	public UsuarioVO consultarUsuarioDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		UsuarioVO usuario = new UsuarioVO();
		String query = "SELECT u.idUsuario, tipo.descricao, u.nome, u.cpf, u.email, u.dataCadastro, "
				+ "u.dataExpiracao, u.login, u.senha "
				+ "FROM usuario u, tipoUsuario tipo "
				+ "WHERE u.idTipoUsuario = tipo.idTipoUsuario "
				+ "AND u.idUsuario = " + usuarioVO.getIdUsuario(); 
		try{
			resultado = stmt.executeQuery(query);
			if(resultado.next()){
				usuario.setIdUsuario(Integer.parseInt(resultado.getString(1)));
				usuario.setTipoUsuario(TipoUsuarioVO.valueOf(resultado.getString(2)));
				usuario.setNome(resultado.getString(3));
				usuario.setCpf(resultado.getString(4));
				usuario.setEmail(resultado.getString(5));
				usuario.setDataCadastro(LocalDate.parse(resultado.getString(6), formaterDate));
				if(resultado.getString(7) != null) {
					usuario.setDataExpiracao(LocalDate.parse(resultado.getString(7), formaterDate));
				}
				usuario.setLogin(resultado.getString(8));
				usuario.setSenha(resultado.getString(9));
			}
		} catch (SQLException e){
			System.out.println("Erro ao executar a Query de Consulta de Usuário.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return usuario;
	}

}

