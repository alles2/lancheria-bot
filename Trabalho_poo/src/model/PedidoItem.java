/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author m13247
 */
public class PedidoItem {
    
    public void addPedidoItem(int pedido_id, int produto_id, int quantidade, String obervacao, double precoPedido) throws ClassNotFoundException, SQLException{
        banco_dados.ComandosBd pedidoItem = new banco_dados.ComandosBd();
        pedidoItem.addPedidoProduto(pedido_id, produto_id, quantidade, obervacao, precoPedido);
    }
    
    public void addMaisProdutoPedido(String nomeCliente, String nomeProduto, String observacao, int quantidade) throws ClassNotFoundException, SQLException{
        model.Produto produto = new model.Produto();
        model.PedidoItem pedidoItem = new model.PedidoItem();
        
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        ResultSet cliente = comandos.getCliente(nomeCliente);
        
        int idCliente = 0;
        while(cliente.next()){
            idCliente = cliente.getInt("id");
        }
        
        ResultSet pedido = comandos.getIdPedido(idCliente);
        int pedidoId = 0;
        while(pedido.next()){
            pedidoId = pedido.getInt("id");
        }
        
        int produtoId = produto.getProdutoId(nomeProduto);
        double precoProduto = produto.getProdutoPreco(nomeProduto);
        double precoPedido = precoProduto * quantidade;
        
        pedidoItem.addPedidoItem(pedidoId, produtoId, quantidade, observacao, precoPedido);
        
    }
    
    public ResultSet getPedidoItem(String nomeCliente) throws ClassNotFoundException, SQLException{
         model.PedidoItem pedidoItem = new model.PedidoItem();
        
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        ResultSet cliente = comandos.getCliente(nomeCliente);
        
        int idCliente = 0;
        while(cliente.next()){
            idCliente = cliente.getInt("id");
        }
        
        ResultSet pedido = comandos.getIdPedido(idCliente);
        int pedidoId = 0;
        while(pedido.next()){
            pedidoId = pedido.getInt("id");
        }
        
        ResultSet dadosPedido = comandos.getPedidoItem(pedidoId);
        return dadosPedido;
    }
}
