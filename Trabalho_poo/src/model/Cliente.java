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
 * @author Luiz
 */
public class Cliente {

    private String quantidadeProduto;
    private String produtoASerAdicionado;
    private String observacao;
    private int cont;
    private boolean temMaisProdutoNoPedido;
    private int userId;
    private String nomeCliente;

    public ResultSet getClientes() throws ClassNotFoundException, SQLException {
        //método para trazer todos os clientes
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        return comandos.getClientes();
    }

    public void addCliente(String nome) throws ClassNotFoundException, SQLException {
        //método para adicionar um novo cliente
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        ResultSet clientes = getClientes();

        boolean clienteIsCadastrado = false;
        while (clientes.next()) {
            if (clientes.getString("nome").equals(nome)) {
                clienteIsCadastrado = true;
            }
        }

        if (!clienteIsCadastrado) { //verifica se o nome do cliente já não está cadastrado
            comandos.addCliente(nome);
        }
    }

    /**
     * @return the quantidadeProduto
     */
    public String getQuantidadeProduto() {
        return quantidadeProduto;
    }

    /**
     * @param quantidadeProduto the quantidadeProduto to set
     */
    public void setQuantidadeProduto(String quantidadeProduto) {
        this.quantidadeProduto = quantidadeProduto;
    }

    /**
     * @return the produtoASerAdicionado
     */
    public String getProdutoASerAdicionado() {
        return produtoASerAdicionado;
    }

    /**
     * @param produtoASerAdicionado the produtoASerAdicionado to set
     */
    public void setProdutoASerAdicionado(String produtoASerAdicionado) {
        this.produtoASerAdicionado = produtoASerAdicionado;
    }

    /**
     * @return the observacao
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * @param observacao the observacao to set
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * @return the cont
     */
    public int getCont() {
        return cont;
    }

    /**
     * @param cont the cont to set
     */
    public void setCont(int cont) {
        this.cont = cont;
    }

    /**
     * @return the temMaisProdutoNoPedido
     */
    public boolean isTemMaisProdutoNoPedido() {
        return temMaisProdutoNoPedido;
    }

    /**
     * @param temMaisProdutoNoPedido the temMaisProdutoNoPedido to set
     */
    public void setTemMaisProdutoNoPedido(boolean temMaisProdutoNoPedido) {
        this.temMaisProdutoNoPedido = temMaisProdutoNoPedido;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the nomeCliente
     */
    public String getNomeCliente() {
        return nomeCliente;
    }

    /**
     * @param nomeCliente the nomeCliente to set
     */
    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
    
    
    
}
