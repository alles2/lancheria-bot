/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banco_dados;

/**
 *
 * @author Luiz
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

	private static final String USUARIO = "root";
	private static final String SENHA = "";
	private static final String URL = "jdbc:mysql://localhost:3306/trabalho_poo";
	private static final String DRIVER = "com.mysql.jdbc.Driver";

	

	// Conectar ao banco
	public Connection conectar(){
		try {
		// Registrar o driver
		Class.forName(DRIVER);
		// Capturar a conexão
		Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
		// Retorna a conexao aberta
		System.out.println("Conexão feita com sucesso");
		return conn;
		
		}catch (Exception e) {
			System.out.println("Erro:" + e.getMessage());
		}
		return null;

	}

}