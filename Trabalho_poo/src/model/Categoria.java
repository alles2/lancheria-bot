/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import libsUtil.Util;

/**
 *
 * @author m13247
 */
public class Categoria {

    public void addCategoria(String nomeCategoria) throws ClassNotFoundException, SQLException, Exception {
        //método para adicionar nova categoria
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        ResultSet categorias = comandos.getCategoria(nomeCategoria);
        String categoria = "";
        
        while (categorias.next()) {
            categoria = categorias.getString("descricao");
        }
        
        if(!categoria.isEmpty()){ //verifica se a categoria existe, se não existir, adiciona
            throw new Exception("Esta categoria já está cadastrada!");
        }
        comandos.addCategoria(nomeCategoria);
    }

    public ResultSet getCategorias() throws ClassNotFoundException, SQLException {
        //método que retorna todas as categorias
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        return comandos.getCategorias();
    }

    public void deleteCategoria(String categoria) throws Exception {
        //método para deletar uma categoria
        libsUtil.Util lib = new libsUtil.Util();
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        int id =  lib.transformaIdParaInt(categoria);
        
        ResultSet dadosCatAdicionadas = comandos.getCategoriasComProduto();
        
        boolean podeExcluirCategoria = true;
        
        while(dadosCatAdicionadas.next()){ 
            if(dadosCatAdicionadas.getString("id").equals(categoria)){
                podeExcluirCategoria = false;
            }
        }
        
        if(!podeExcluirCategoria){ //verifica se a categoria está castrada num produto, se estiver, lança exception
            throw new Exception("Você não pode excluir essa categoria, pois há produtos vinculados nesta categoria!");
        }

        comandos.deleteCategoria(id);
    }

    public void editCategoria(int id, String nomeCategoria) throws Exception {
        //método para atualizar categoria
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();

        if (nomeCategoria.isEmpty()) {
            throw new Exception("Você preencher o campo com o novo nome da categoria");
        }

        comandos.updateCategoria(id, nomeCategoria);
    }
    
    public ResultSet getCategoriasComProduto() throws ClassNotFoundException, SQLException{
       //método que retorna todas as categorias com produtos
        banco_dados.ComandosBd comandos = new banco_dados.ComandosBd();
        return comandos.getCategoriasComProduto(); 
    }
    
}
