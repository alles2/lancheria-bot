/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banco_dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author m13247
 */
public class ComandosBd {

    public void addCategoria(String nomeCategoria) throws ClassNotFoundException, SQLException {
        //método para adicionar categoria
        Conexao conn = new Conexao();

        String sql = "INSERT INTO categoria(descricao) VALUES(?)";

        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, nomeCategoria);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ResultSet getCategorias() throws ClassNotFoundException, SQLException {
        //método que retorna todas as categorias
        Conexao conn = new Conexao();

        String sql = "SELECT * FROM categoria";

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);

    }

    public ResultSet getCategoria(String descricao) throws ClassNotFoundException, SQLException {
        //método que retorna determinada categoria
        Conexao conn = new Conexao();

        String sql = "SELECT * FROM categoria WHERE descricao =" + "'" + descricao + "'";

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);

    }

    public ResultSet getProduto(String descricao) throws ClassNotFoundException, SQLException {
        //método que retorna determinada categoria
        Conexao conn = new Conexao();

        String sql = "SELECT * FROM produto WHERE descricao =" + "'" + descricao + "'";

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);

    }

    public void deleteCategoria(int id) throws ClassNotFoundException, SQLException {
        //método para deletar categoria
        Conexao conn = new Conexao();

        String sql = "DELETE FROM categoria WHERE id = ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, id);
        st.executeUpdate();
        st.close();
    }

    public void updateCategoria(int id, String descricao) throws ClassNotFoundException, SQLException {
        //método para atualizar categoria
        Conexao conn = new Conexao();

        String sql = "UPDATE categoria SET descricao = ? WHERE id = ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, descricao);
        st.setInt(2, id);
        st.executeUpdate();
        st.close();
    }

    public ResultSet getClientes() throws ClassNotFoundException, SQLException {
        //método para pegar todos os clientes
        Conexao conn = new Conexao();

        String sql = "SELECT * FROM cliente";

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);

    }

    public void addProduto(String descricao, int categoriaId, double preco) throws ClassNotFoundException, SQLException {
        //método para adicionar novo produto
        Conexao conn = new Conexao();

        String sql = "INSERT INTO produto(categoria_id, descricao, preco) VALUES(?, ?, ?)";

        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, categoriaId);
            st.setString(2, descricao);
            st.setDouble(3, preco);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ResultSet getProdutos() throws ClassNotFoundException, SQLException {
        //método que retorna todos os produtos e o nome da categoria
        Conexao conn = new Conexao();

        String sql = "SELECT Cat.descricao, produto.* FROM produto "
                + "INNER JOIN categoria Cat on produto.categoria_id = Cat.id";

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);

    }

    public void deleteProduto(int id) throws ClassNotFoundException, SQLException {
        //método para excluir categoria
        Conexao conn = new Conexao();

        String sql = "DELETE FROM produto WHERE id = ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, id);
        st.executeUpdate();
        st.close();
    }

    public void updateProduto(int id, String descricao, int categoria_id, double preco) throws ClassNotFoundException, SQLException {
        //método para atualizar produto
        Conexao conn = new Conexao();

        String sql = "UPDATE produto SET descricao = ?, categoria_id = ?, preco = ? WHERE id = ?";

        PreparedStatement st = conn.prepareStatement(sql);

        st.setString(1, descricao);
        st.setInt(2, categoria_id);
        st.setDouble(3, preco);
        st.setInt(4, id);
        st.executeUpdate();
        st.close();
    }

    public ResultSet getCategoriasComProduto() throws ClassNotFoundException, SQLException {
        //retorna as categorias que estão adicionas em produtos
        Conexao conn = new Conexao();

        String sql = "SELECT * FROM categoria INNER JOIN produto Prod on categoria.id = Prod.categoria_id GROUP BY categoria.id";
              

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);
    }

    public ResultSet getProdutosComCategoria(String descricao) throws ClassNotFoundException, SQLException {   // retorna o produto de determinada categoria
        Conexao conn = new Conexao();

        String sql = "SELECT * FROM produto INNER JOIN categoria Cat on produto.categoria_id = Cat.id "
                + "WHERE Cat.descricao =" + "'" + descricao + "'";

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);
    }

    public void addCliente(String nomeCliente) throws ClassNotFoundException, SQLException {
        //método para adicionar novo cliente
        Conexao conn = new Conexao();

        String sql = "INSERT INTO cliente(nome) VALUES(?)";

        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, nomeCliente);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void addPedido(int clienteId) throws ClassNotFoundException, SQLException {
        //método para adicionar novo pedido
        Date data = new Date();
        Timestamp time = new Timestamp(data.getTime());
        
        Conexao conn = new Conexao();

        String sql = "INSERT INTO pedido(cliente_id, data_pedido) VALUES(?, ?)";

        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, clienteId);
            st.setTimestamp(2, time);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet getUltimoPedido() throws ClassNotFoundException, SQLException {
        Conexao conn = new Conexao();

        String sql = "SELECT id FROM pedido WHERE finalizado = 0 ORDER BY id desc limit 1";

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);
    }

    public void addPedidoProduto(int pedido_id, int produto_id, int quantidade, String observacao, double precoPedido) throws ClassNotFoundException, SQLException {
        //método para adicionar pedido produto
        Conexao conn = new Conexao();

        String sql = "INSERT INTO pedido_item(pedido_id, produto_id, quantidade, observacao, preco) VALUES(?, ?, ?, ?, ?)";

        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, pedido_id);
            st.setInt(2, produto_id);
            st.setInt(3, quantidade);
            st.setString(4, observacao);
            st.setDouble(5, precoPedido);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet getCliente(String nome) throws ClassNotFoundException, SQLException {
        //método que retorna determinada categoria
        Conexao conn = new Conexao();

        String sql = "SELECT id FROM cliente WHERE nome =" + "'" + nome + "'";

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);

    }

    public ResultSet getIdPedido(int clienteId) throws ClassNotFoundException, SQLException {
        //método que retorna o último pedido do cliente
        Conexao conn = new Conexao();

        String sql = "SELECT id FROM pedido WHERE finalizado = 0 AND cliente_id = " + clienteId + " ORDER BY id DESC limit 1";

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);

    }

    public void finalizaPedido(int pedidoId, double valorTotal) throws ClassNotFoundException, SQLException {
        //método para finalizar pedido
        Conexao conn = new Conexao();

        String sql = "UPDATE pedido SET finalizado = 1, valor_total = ? WHERE id = ? AND finalizado = 0";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setDouble(1, valorTotal);
        st.setInt(2, pedidoId);
        st.executeUpdate();
        st.close();

    }

    public ResultSet getPedidoItem(int pedidoId) throws ClassNotFoundException, SQLException {
        //método que retorna o último pedido do cliente
        Conexao conn = new Conexao();

        String sql = "SELECT pedido_item.*, Prod.*, Ped.*  FROM pedido_item "
                + " INNER JOIN produto Prod on Prod.id = pedido_item.produto_id "
                + " INNER JOIN pedido Ped on Ped.id = pedido_item.pedido_id"
                + " WHERE pedido_item.pedido_id = " + pedidoId;

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);

    }

    public ResultSet getValorTotalPedido(int pedidoId) throws ClassNotFoundException, SQLException {
        Conexao conn = new Conexao();

        String sql = "SELECT SUM(preco) as valor_total FROM pedido_item WHERE pedido_id = " + pedidoId + " GROUP BY pedido_id";

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);
    }

    public ResultSet getPedidos() throws ClassNotFoundException, SQLException {
        //método para pegar todos os clientes
        Conexao conn = new Conexao();

        String sql = "SELECT pedido.*, Clie.nome FROM pedido"
                + " INNER JOIN cliente Clie on Clie.id = pedido.cliente_id"
                + " where finalizado = 1 AND entregue = 0 ORDER BY pedido.id";

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);

    }
    
    public ResultSet getDadosPedido(int pedidoId) throws ClassNotFoundException, SQLException{
        Conexao conn = new Conexao();

        String sql = "SELECT pedido_item.*, Prod.* FROM pedido_item"
                + " INNER JOIN produto Prod on Prod.id = pedido_item.produto_id"
                + " where pedido_item.pedido_id = " + pedidoId + " ORDER BY Prod.id";

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);
    }
    
    public void finalizaEntrega(int pedidoId) throws ClassNotFoundException, SQLException {
        //método para finalizar pedido
        Conexao conn = new Conexao();

        String sql = "UPDATE pedido SET entregue = 1 WHERE id = ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, pedidoId);
        st.executeUpdate();
        st.close();

    }
    
    public ResultSet getProdutosComPedido() throws ClassNotFoundException, SQLException {
        //retorna os produtos que estão em algum pedido
        Conexao conn = new Conexao();

        String sql = "SELECT * FROM produto INNER JOIN pedido_item Ped on Ped.produto_id = produto.id";

        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery(sql);
    }

}
