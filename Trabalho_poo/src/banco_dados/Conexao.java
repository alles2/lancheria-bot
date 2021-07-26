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
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Conexao {

	private static final String usuario = "root";
	private static final String senha = "";
	private static final String url = "jdbc:mysql://localhost:3306/trabalho_poo";
	private static final String driver = "com.mysql.jdbc.Driver";
        private final Connection conexao;  

	

    public Conexao() throws ClassNotFoundException, SQLException{
        Class.forName(driver);
        conexao = DriverManager.getConnection(url, usuario, senha);
    }
    
    public PreparedStatement prepareStatement(String sql) throws SQLException{
        return conexao.prepareStatement(sql);
    }

}