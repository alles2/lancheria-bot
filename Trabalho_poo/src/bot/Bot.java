/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

/**
 *
 * @author Luiz
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bot {

    private final String token; //token do bot, não vai ser mudado

    //construtor
    public Bot(String token) {
        this.token = token;
    }

    /**
     * Abre uma URL dedicada pra enviar a mensagem desejada.
     *
     * @param userID userID do usuário.
     * @param mensagem mensagem passada por parâmetro.
     */
    public void enviaMensagem(int userID, String mensagem) {
        String url = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s", token, userID, mensagem);
        try {
            URL telegram = new URL(url); //instancia uma url
            URLConnection con = telegram.openConnection(); //abre a URL que é a mensagem a ser enviada ao usuário
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Abre uma URL dedicada pra ver as mensagens, recebe a mensagem em JSON.
     *
     * @param updateID updateid da mensagem, é incrementado em UM pra poder ler
     * as próximas mensagens.
     * @return Mensagem em JSON que foi lida naquela URL que depois vai ser
     * recortada.
     */
    public String recebeMensagem(int updateID) {
        String url = String.format("https://api.telegram.org/bot%s/getUpdates?offset=%d", token, updateID + 1);
        String ret = ""; //declara variável que é a mensagem inteira recebida em JSON
        try {
            URL telegram = new URL(url); //instancia uma url
            URLConnection con = telegram.openConnection(); //abre a URL que é destinada à mensagem do usuário
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); //lê o que tem nessa URL

            String linha;
            while ((linha = in.readLine()) != null) {
                ret += linha; //vai concatenando a(s) linha(s)
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }

    /**
     * Codifica a string pra linguagem de URL, espaço em branco vira %20, por
     * exemplo.
     *
     * @param mensagem resposta que vai ser enviada ao usuário antes de
     * codificar pra URL.
     * @return Resposta codificada pra URL.
     */
    public String encoder(String mensagem) {
        try {
            return URLEncoder.encode(mensagem, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("ERRO NA CODIFICAÇÃO: " + ex.getMessage());
        }
    }


    public String mensagemCategorias() throws ClassNotFoundException, SQLException {
        //método que retorna a mensagem que será enviada ao cliente para selecionar a categoria
        model.Categoria cat = new model.Categoria();
        ResultSet dadosCategorias = cat.getCategoriasComProduto();

        String mensagemCat = "Olá, selecione a categoria do produto desejada: \n";

        while (dadosCategorias.next()) {
            String descricao = dadosCategorias.getString("descricao");

            mensagemCat += descricao + "\n";
        }

        return mensagemCat;
    }

    public String mensagemProdutos(String nomeCategoria) throws ClassNotFoundException, SQLException {
        //método que retorna a mensagem que será enviada ao cliente para selecionar o produto
        model.Produto prod = new model.Produto();
        ResultSet dadosProdutos = prod.getProdutosDeCategoria(nomeCategoria);

        String mensagemProd = "Selecione o produto desejado: \n";

        while (dadosProdutos.next()) {
            int id = dadosProdutos.getInt("id");
            String descricao = dadosProdutos.getString("descricao");
            Double preco = dadosProdutos.getDouble("preco");

            mensagemProd += "Id: " + id + " Descrição: " + descricao + " Preço: " + preco + "\n";
        }

        return mensagemProd;
    }

    public String mensagemQuantidade() throws ClassNotFoundException, SQLException {
        //método que retorna a mensagem para o cliente selecionar a quantidade do produto no pedido
        String mensagem = "Informe a quantidade que você deseja!";

        return mensagem;
    }

    public String mensagemObservacao() throws ClassNotFoundException, SQLException {
        //método que retorna a mensagem perguntando ao usuário se ele tem alguma observação no seu pedido
        String mensagem = "Você tem alguma observação?";

        return mensagem;
    }

    public String mensagemFinalizado() throws ClassNotFoundException, SQLException {
        //método que retorna a mensagem perguntando ao usuário se ele irá inserir mais produtos no pedido
        String mensagem = "Seu pedido foi finalizado com sucesso, você deseja adicionar mais um produto no pedido?";

        return mensagem;
    }

    public boolean verificaMensagemCatgegoria(String categoria) throws ClassNotFoundException, SQLException {
        //verifica se a categoria que o usuário respondeu, existe
        model.Categoria cat = new model.Categoria();
        ResultSet dadosCategorias = cat.getCategorias();

        boolean temCategoriaNaMensagem = false;
        while (dadosCategorias.next()) {
            String descricao = dadosCategorias.getString("descricao");

            if (descricao.toLowerCase().equals(categoria.toLowerCase())) {
                temCategoriaNaMensagem = true;
            }
        }
        return temCategoriaNaMensagem;
    }

    public boolean verificaMensagemProduto(String produto) throws ClassNotFoundException, SQLException {
        //verifica se o produto que o usuário respondeu, existe
        model.Produto prod = new model.Produto();
        ResultSet dadosProdutos = prod.getProdutos();

        boolean temProdutoNaMensagem = false;

        while (dadosProdutos.next()) {
            String descricao = dadosProdutos.getString("produto.descricao");
            if (descricao.toLowerCase().equals(produto.toLowerCase())) { //verifica os produtos do bd com o produto que o usuário respondeu
                temProdutoNaMensagem = true;
            }
        }
        return temProdutoNaMensagem;
    }

    public String mensagemComandoInvalidoCategoria() {
        //método que retorna a mensagem perguntando ao usuário se ele irá inserir mais produtos no pedido
        String mensagem = "Comando inválido, digite o nome correto da categoria que você deseja!";

        return mensagem;
    }

    public String mensagemComandoInvalidoProduto() {
        //método que retorna a mensagem perguntando ao usuário se ele irá inserir mais produtos no pedido
        String mensagem = "Comando inválido, digite o nome correto do produto que você deseja!";

        return mensagem;
    }

    public String mensagemComandoInvalidoQuantidade() {
        //método que retorna a mensagem perguntando ao usuário se ele irá inserir mais produtos no pedido
        String mensagem = "Comando inválido, digite a quantidade com números!";

        return mensagem;
    }

    public boolean pedidoTemObservacao(String mensagem) {
       //método para verificar se tem mais produtos no pedido
        boolean temObservacao = false;
        
        if (!mensagem.toLowerCase().equals("n\\u00e3o")) {
            temObservacao = true;
        }

        return temObservacao;
    }

    public boolean quantidadeIsNumeric(String mensagem) {
        //verifica se o usuário respondeu corretamente a quantidade com números
        boolean isNumeric = false;
        if (mensagem.matches("[0-9]+")) {
            isNumeric = true;
        }

        return isNumeric;
    }

    public boolean pedidoTemMaisProduto(String mensagem) {
        //método que verifica se tem mais produtos no pedido
        boolean temMaisProduto = false;
        if (mensagem.toLowerCase().equals("sim")) {
            temMaisProduto = true;
        }

        return temMaisProduto;
    }

    public String mensagemPedidoFinalizado() {
        //método que retorna uma mensagem de finalização do pedido
        String mensagem = "Seu pedido foi finalizado com sucesso, você pode retirar o pedido em 40 minutos!";

        return mensagem;
    }

    public String mensagemFinalizarPedido(String nomeCliente) throws ClassNotFoundException, SQLException {
        //método que verifica se usuário quer finalizar pedido
        model.PedidoItem pedidoItem = new model.PedidoItem();
        String mensagem = "Seu pedido é este: \n";

        ResultSet dadosPedido = pedidoItem.getPedidoItem(nomeCliente);

        while (dadosPedido.next()) {
            mensagem += "Produto: " + dadosPedido.getString("Prod.descricao") + " - Quantidade: " + dadosPedido.getInt("pedido_item.quantidade") + " - Valor total: " + dadosPedido.getDouble("preco") + "\n";
        }

        mensagem += "Você deseja adicionar mais algum produto no pedido? \n";
        return mensagem;
    }

}
