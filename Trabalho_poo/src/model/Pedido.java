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
public class Pedido {
    
    public void addPedido(String nomeCliente, String nomeProduto, String observacao, int quantidade) throws ClassNotFoundException, SQLException{
        model.Produto produto = new model.Produto();
        model.PedidoItem pedidoItem = new model.PedidoItem();
        
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        ResultSet cliente = comandos.getCliente(nomeCliente);
        
        int idCliente = 0;
        while(cliente.next()){
            idCliente = cliente.getInt("id");
        }
        
        comandos.addPedido(idCliente);
        
        ResultSet pedido = comandos.getUltimoPedido();
        int pedidoId = 0;
        while(pedido.next()){
            pedidoId = pedido.getInt("id");
        }

        int produtoId = produto.getProdutoId(nomeProduto);
        double precoProduto = produto.getProdutoPreco(nomeProduto);
        double precoPedido = precoProduto * quantidade;
        
        pedidoItem.addPedidoItem(pedidoId, produtoId, quantidade, observacao, precoPedido);
        
    }
    
    public void finalizaPedido(String nomeCliente) throws ClassNotFoundException, SQLException{
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
        
        ResultSet pedidoItem = comandos.getValorTotalPedido(pedidoId);
        double valorTotal = 0;
        while(pedidoItem.next()){
            valorTotal = pedidoItem.getDouble("valor_total");
        }
        
        comandos.finalizaPedido(pedidoId, valorTotal);
    }
    
    public ResultSet getPedidos() throws ClassNotFoundException, SQLException{
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        ResultSet dadosPedidos = comandos.getPedidos();
        
        return dadosPedidos;
    }
    
    public ResultSet getDadosPedido(String pedidoId) throws ClassNotFoundException, SQLException, Exception{
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        libsUtil.Util lib = new libsUtil.Util();
        int id =  lib.transformaIdParaInt(pedidoId);
        
        ResultSet dadosPedidos = comandos.getDadosPedido(id);
        
        return dadosPedidos;
    }
    
    public void finalizarEntrega(String pedidoId) throws Exception{
       libsUtil.Util lib = new libsUtil.Util();
       int id = lib.transformaIdParaInt(pedidoId);
       
       banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
       comandos.finalizaEntrega(id);
    }
    
}
