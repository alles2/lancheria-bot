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
public class Produto {

    public void addProduto(String descricao, String categoria, double preco) throws ClassNotFoundException, SQLException, Exception {
        //método para adicionar um novo produto
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();

        ResultSet categorias = comandos.getCategoria(categoria);
        int categoriaId = 0;
        while (categorias.next()) {
            categoriaId = categorias.getInt("id");
        }

        comandos.addProduto(descricao, categoriaId, preco);
    }

    public ResultSet getProdutos() throws ClassNotFoundException, SQLException {
        //método que retorna todos os produtos
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        return comandos.getProdutos();

    }

    public void deleteProduto(String produto) throws Exception {
        //método para deletar um produto
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        libsUtil.Util lib = new libsUtil.Util();
        int id = lib.transformaIdParaInt(produto); //é usada a lib, para transformar o id em string para int, pois o dado foi capturado no jtable

        ResultSet dadosProdAdicionadas = comandos.getProdutosComPedido();

        boolean podeExcluirProduto = true;

        while (dadosProdAdicionadas.next()) {
            if (dadosProdAdicionadas.getString("id").equals(produto)) {
                podeExcluirProduto = false;
            }
        }
        
        if(!podeExcluirProduto){
            throw new Exception("Você não pode excluir esse produto, pois ele está cadastrado em um pedido!");
        }

        comandos.deleteProduto(id);
    }

    public void updateProduto(int id, String descricao, String categoria, double preco) throws Exception {
        //método para atualizar produto
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();

        ResultSet categorias = comandos.getCategoria(categoria);

        int categoriaId = 0;
        while (categorias.next()) {
            categoriaId = categorias.getInt("id");
        }

        comandos.updateProduto(id, descricao, categoriaId, preco);
    }

    public ResultSet getProdutosDeCategoria(String descricaoCategoria) throws ClassNotFoundException, SQLException {
        //retorna produtos de determinada categoria
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        return comandos.getProdutosComCategoria(descricaoCategoria);

    }

    public int getProdutoId(String descricao) throws ClassNotFoundException, SQLException {
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        ResultSet produto = comandos.getProduto(descricao);

        int produtoId = 0;
        while (produto.next()) {
            produtoId = produto.getInt("id");
        }

        return produtoId;

    }

    public double getProdutoPreco(String descricao) throws ClassNotFoundException, SQLException {
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        ResultSet produto = comandos.getProduto(descricao);

        double preco = 0;
        while (produto.next()) {
            preco = produto.getDouble("preco");
        }

        return preco;
    }

}
