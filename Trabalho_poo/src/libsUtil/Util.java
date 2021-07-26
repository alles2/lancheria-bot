/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libsUtil;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author m13247
 */
public class Util { //classe para métodos que podem ser usados em vários lugares

    public int transformaIdParaInt(String stringId) throws Exception { //recebe o id em string e retorna em id
        boolean isNumero;
        int id = 0;
        try {
            id = Integer.parseInt(stringId);
            isNumero = true;
        } catch (NumberFormatException e) {
            isNumero = false;
        }

        if (!isNumero) {
            throw new Exception("Selecione o id da categoria para ser deletada!");
        }

        return id;
    }

    public void gerarPdf() throws DocumentException, ClassNotFoundException, SQLException {
        model.Pedido pedido = new model.Pedido();
        ResultSet dadosPedidos = pedido.getPedidos();
        Document doc = new Document();

        // para salvar o arquivo pdf em sua máquina, deve ser mudado o caminho abaixo, para um local de sua máquina, para baixar corretamente o arquivo
        try {
            PdfWriter.getInstance(doc, new FileOutputStream("C:\\Users\\Luiz\\Documents\\NetBeansProjects\\Trabalho_poo\\pedidos.pdf"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        doc.open();

        // adicionando um parágrafo no documento
        while (dadosPedidos.next()) {
            doc.add(new Paragraph("Código Pedido: " + dadosPedidos.getInt("id") + " Cliente: " + dadosPedidos.getString("Clie.nome") + " Valor total: R$" + dadosPedidos.getDouble("valor_total")));
        }
        
        doc.close();
    }

}
