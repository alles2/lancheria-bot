/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.itextpdf.text.DocumentException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Cliente;

/**
 *
 * @author m13247
 */
public class Janela_trab extends javax.swing.JFrame {

    private bot.Bot b = new bot.Bot("1034481800:AAFLKQ1hXC4eDCuV1isSTkTi4OBfkZuOf5U"); //instância o bot com o token

    /**
     * Creates new form Janela_trab
     */
    public Janela_trab() {
        initComponents();

        jPanel1.setBorder(BorderFactory.createTitledBorder("Pedidos"));
        jPanel10.setBorder(BorderFactory.createTitledBorder("Pedidos por Entregar"));
        jPanel9.setBorder(BorderFactory.createTitledBorder("Itens do pedido selecionado"));
        atualizaTelaPedidos(); //método que carrega a tela dos pedidos

        new botPedido().start();
    }

    public class botPedido extends Thread { //criando a thread

        public void run() {
            interacaoBot();
        }
    }

    private void interacaoBot() {
        model.Cliente cliente;
        model.Produto produto = new model.Produto();
        model.Pedido pedido = new model.Pedido();
        model.PedidoItem pedidoItem = new model.PedidoItem();

        ArrayList<Cliente> listaCliente = new ArrayList<Cliente>();

        int updateID = 1; //pra iniciar, pode ser qualquer número diferente de 0, só é necessário pra entrar no while

        while (updateID != 0) {
            String mensagem = b.recebeMensagem(updateID); //lê o que tem de primeira mensagem no bot
            
            ArrayList<String> listaUsu;
            if (!"{\"ok\":true,\"result\":[]}".equals(mensagem)) { //se tiver alguma mensagem na fila
                int achaUserID = mensagem.indexOf("from\":{\"id\":"); //pega o local que está o id do usuário
                String userID = mensagem.substring(achaUserID + 12, achaUserID + 21); //recorta as posições destinadas ao id do usuário

                int achaUpdateID = mensagem.indexOf("update_id\":"); //pega o local do novo updateID
                String pegaUpdateID = mensagem.substring(achaUpdateID + 11, achaUpdateID + 20); //recorta as posições destinadas ao updateID do usuário
                updateID = Integer.parseInt(pegaUpdateID); //transforma o updateID de string pra int

                int terminaTXT = mensagem.indexOf("}}"); //pega o local que termina o texto da primeira mensagem da fila
                int iniciaTXT = mensagem.indexOf("text\":"); //pega o local que começa o texto da primeira mensagem da fila
                String recorteMSG = mensagem.substring(iniciaTXT + 7, terminaTXT - 1); //recorta o texto dessa mensagem

                int achaNome = mensagem.indexOf("first_name\":");
                int achaFimNome = mensagem.indexOf("\",");
                String primeiroNome = mensagem.substring(achaNome + 13, achaFimNome);

                int achaSobrenome = mensagem.indexOf("last_name\":");
                int achaFimSobrenome = mensagem.indexOf("\",\"lan");
                String sobrenome = mensagem.substring(achaSobrenome + 12, achaFimSobrenome);

                int idUsuario = Integer.parseInt(userID);
                String nomeUsuario = primeiroNome + " " + sobrenome;

                cliente = null;
                for (int x = 0; x < listaCliente.size(); x++) { //verifica se o cliente já está na lista
                    if (listaCliente.get(x).getUserId() == idUsuario) { //se estiver na lista, pega os dados dele
                        cliente = listaCliente.get(x);
                        break;
                    }
                }

                if (cliente == null) { //caso cliente não estiver na lista, adiciona ele na lista
                    cliente = new Cliente();
                    cliente.setUserId(idUsuario);
                    cliente.setCont(0);
                    cliente.setNomeCliente(nomeUsuario);
                    listaCliente.add(cliente);

                }

                int cont = cliente.getCont();
                cont++;
                cliente.setCont(cont);

                
                /* Esses ifs, validam o passo que cada usuário está, assim 
                para vários usuários ao mesmo tempo fazerem um pedido.
                
                Cada passo, representa um cont, e cada passo, dependendo
                do que o usuário responde, ele atualiza o valor do cont.
                */
                if (cliente.getCont() == 1) {
                    try {
                        b.enviaMensagem(idUsuario, b.encoder(b.mensagemCategorias()));
                        cliente.addCliente(cliente.getNomeCliente());
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                if (cliente.getCont() == 2) {
                    try {
                        cliente.setCont(3);
                        if (!b.verificaMensagemCatgegoria(recorteMSG)) {
                            b.enviaMensagem(idUsuario, b.encoder(b.mensagemComandoInvalidoCategoria()));
                            cliente.setCont(1);
                        }

                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                if (cliente.getCont() == 3) {

                    try {
                        b.enviaMensagem(idUsuario, b.encoder(b.mensagemProdutos(recorteMSG)));
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                if (cliente.getCont() == 4) {
                    try {
                        if (!b.verificaMensagemProduto(recorteMSG)) {
                            b.enviaMensagem(idUsuario, b.encoder(b.mensagemComandoInvalidoProduto()));
                            cliente.setCont(3);

                        } else {
                            cliente.setProdutoASerAdicionado(recorteMSG);
                            cliente.setCont(5);
                        }

                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                if (cliente.getCont() == 5) {
                    try {
                        cliente.setProdutoASerAdicionado(recorteMSG);
                        b.enviaMensagem(idUsuario, b.encoder(b.mensagemQuantidade()));

                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                if (cliente.getCont() == 6) {
                    cliente.setCont(7);
                    if (!b.quantidadeIsNumeric(recorteMSG)) {
                        b.enviaMensagem(idUsuario, b.encoder(b.mensagemComandoInvalidoQuantidade()));
                        cliente.setCont(5);
                    }
                }

                if (cliente.getCont() == 7) {
                    try {
                        cliente.setQuantidadeProduto(recorteMSG);
                        b.enviaMensagem(idUsuario, b.encoder(b.mensagemObservacao()));

                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                if (cliente.getCont() == 8) {
                    try {

                        if (b.pedidoTemObservacao(recorteMSG)) {
                            cliente.setObservacao(recorteMSG);
                        }else{
                            cliente.setObservacao("Sem Observação");
                        }

                        if (!cliente.isTemMaisProdutoNoPedido()) {
                            pedido.addPedido(cliente.getNomeCliente(), cliente.getProdutoASerAdicionado(), cliente.getObservacao(), Integer.parseInt(cliente.getQuantidadeProduto()));
                            cliente.setProdutoASerAdicionado(null);
                            cliente.setObservacao(null);
                            cliente.setQuantidadeProduto(null);
                        } else {
                            pedidoItem.addMaisProdutoPedido(cliente.getNomeCliente(), cliente.getProdutoASerAdicionado(), cliente.getObservacao(), Integer.parseInt(cliente.getQuantidadeProduto()));
                        }

                        b.enviaMensagem(idUsuario, b.encoder(b.mensagemFinalizarPedido(cliente.getNomeCliente())));
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                if (cliente.getCont() == 9) {

                    if (b.pedidoTemMaisProduto(recorteMSG)) {
                        cliente.setTemMaisProdutoNoPedido(true);
                        try {
                            b.enviaMensagem(idUsuario, b.encoder(b.mensagemCategorias()));
                            cliente.setCont(2);
                        } catch (ClassNotFoundException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        try {
                            pedido.finalizaPedido(cliente.getNomeCliente());
                            b.enviaMensagem(idUsuario, b.encoder(b.mensagemPedidoFinalizado()));
                            cliente.setCont(0);
                            cliente.setTemMaisProdutoNoPedido(false);
                        } catch (ClassNotFoundException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

            }
        }
    }

    public void atualizaTelaPedidos() {
        //método para atualizar a tabela com os pedidos
        DefaultTableModel tabela = (DefaultTableModel) jTable1.getModel();
        try {
            tabela.setRowCount(0);

            model.Pedido pedido = new model.Pedido();
            ResultSet dadosPedidos = pedido.getPedidos();

            while (dadosPedidos.next()) {
                String data = dadosPedidos.getString("data_pedido");
                String datahora = mudarData(data);
                tabela.addRow(new Object[]{
                    dadosPedidos.getString("id"),
                    dadosPedidos.getString("Clie.nome"),
                    datahora,
                    dadosPedidos.getString("valor_total"),});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    private String mudarData(String dataMudar) throws ParseException {
        //método para mudar formato da data
        Date data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataMudar);
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(data);
    }

    public void atualizaTelaDadosPedido() {
        //método para atualizar a tabela com os produtos dos pedidos
        DefaultTableModel tabela = (DefaultTableModel) jTable3.getModel();
        try {
            tabela.setRowCount(0);

            model.Pedido pedido = new model.Pedido();
            ResultSet dadosPedidos = pedido.getDadosPedido((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));

            while (dadosPedidos.next()) {

                tabela.addRow(new Object[]{
                    dadosPedidos.getString("Prod.id"),
                    dadosPedidos.getString("Prod.descricao"),
                    dadosPedidos.getString("pedido_item.observacao"),
                    dadosPedidos.getString("pedido_item.quantidade"),
                    dadosPedidos.getString("Prod.preco"),
                    dadosPedidos.getString("pedido_item.preco"),});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void atualizaTelaDadosSemPedido() {
        //método para atualizar a tabela com os produtos
        DefaultTableModel tabela = (DefaultTableModel) jTable3.getModel();
        try {
            tabela.setRowCount(0);
            tabela.addRow(new Object[]{
                "",
                "",
                "",
                "",
                "",
                "",});

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jPanel2 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        jMenu2.setText("jMenu2");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jRadioButton1.setText("jRadioButton1");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Cód Produto", "Descrição", "Observação", "Qtde", "Vl. Unit.", "Vl. Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 727, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, -1, 140));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "N° Pedido", "Cliente", "Data/Hora", "Valor Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/icons8-exportar-pdf-16.png"))); // NOI18N
        jButton3.setText("Gerar PDF dos Pedidos");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/icons8-ok-16.png"))); // NOI18N
        jButton1.setText("Finalizar entrega");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/icons8-sincronizar-16.png"))); // NOI18N
        jButton2.setText("Atualizar Tela");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(154, 154, 154))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        jPanel8.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 0, -1, 190));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jMenu4.setText("Pedidos");
        jMenuBar1.add(jMenu4);

        jMenu5.setText("Categorias");

        jMenuItem1.setText("Exibir categorias");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Adicionar categoria");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem2);

        jMenuBar1.add(jMenu5);

        jMenu6.setText("Produtos");
        jMenu6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu6ActionPerformed(evt);
            }
        });

        jMenuItem4.setText("Exibir produtos");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Adicionar produto");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem5);

        jMenuBar1.add(jMenu6);

        jMenu7.setText("Clientes");

        jMenuItem7.setText("Exibir clientes");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem7);

        jMenuBar1.add(jMenu7);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 83, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        new view.Categoria.indexCategoria().setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        new view.Categoria.addCategoria().setVisible(true);;
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenu6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu6ActionPerformed
        new view.Produto.editProduto().setVisible(true);
    }//GEN-LAST:event_jMenu6ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        new view.Produto.addProduto().setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        new view.Produto.indexProduto().setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        new view.Cliente.indexCliente().setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        atualizaTelaPedidos();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //método para mudar o pedido para entregue
        model.Pedido pedido = new model.Pedido();

        if (jTable1.getSelectedColumnCount() <= 0) {//verifica se um pedido foi selecionado
            JOptionPane.showMessageDialog(null, "Você deve escolher o pedido que deseja marcar como entregue!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            pedido.finalizarEntrega((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));
            atualizaTelaPedidos();
            atualizaTelaDadosSemPedido();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //método para gerar arquivo PDF com os pedidos
        libsUtil.Util util = new libsUtil.Util();
        try {
            util.gerarPdf();
            JOptionPane.showMessageDialog(null, "PDF gerado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (DocumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        atualizaTelaDadosPedido();
    }//GEN-LAST:event_jTable1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Janela_trab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Janela_trab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Janela_trab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Janela_trab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Janela_trab().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables
}
