package projectpoo.construction_material_store.screens.components;

import projectpoo.construction_material_store.dto.ProductDTO;

import javax.swing.*;
import java.awt.*;

public class SaleItemModal extends JDialog {

    private static final String API_URL = "http://localhost:8080/products";

    public SaleItemModal() {
    }

    public void saleItemActionModal() {
        // Determina se é criação ou edição
        String dialogTitle = "Novo item";

        JDialog dialog = new JDialog(this, dialogTitle, true);
        dialog.setLayout(new BorderLayout()); // Configura o layout do diálogo

        // Painel para os campos
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Usando BoxLayout para empilhar os componentes verticalmente
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Adiciona o título "Produtos"
        JPanel productsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productsPanel.add(new JLabel("Produtos:"));
        panel.add(productsPanel); // Adiciona o painel de título ao painel principal

        // Painel da tabela
        JPanel tablePanel = new JPanel(new BorderLayout());

        // Instancia o componente de tabela
        TableComponent tableComponent = new TableComponent(new JPanel());

        try {
            // Carrega os dados antes de criar a tabela
            tableComponent.loadData(API_URL, ProductDTO[].class);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Erro ao carregar produtos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

        // Cria o JScrollPane para a tabela e adiciona ao painel
        JScrollPane tableScrollPane = new JScrollPane(tableComponent.getProductTable());
        tablePanel.add(tableScrollPane, BorderLayout.CENTER); // Tabela ocupa o centro
        panel.add(tablePanel); // Adiciona o painel da tabela ao painel principal

        // Painel para o campo de "Quantidade"
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)); // Alinha à esquerda com espaçamento
        quantityPanel.add(new JLabel("Quantidade:"));

        JTextField quantitySaleField = new JTextField(10); // Define a largura preferida do campo
        quantityPanel.add(quantitySaleField);

        // Adiciona o painel ao painel principal
        panel.add(quantityPanel);

        // Painel para o botão "Adicionar"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Alinha o botão à direita
        JButton btnSave = new JButton("Adicionar");
        btnSave.addActionListener(e -> {
            String cpfCnpj = quantitySaleField.getText();

            btnSave.setEnabled(false); // Desabilita o botão para evitar múltiplos cliques
            try {
                tableComponent.loadData(API_URL, ProductDTO[].class); // Atualizar a lista de produtos
                dialog.dispose(); // Fecha o diálogo
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao salvar item.", "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                btnSave.setEnabled(true); // Reativa o botão, mesmo em caso de erro
            }
        });
        buttonPanel.add(btnSave); // Adiciona o botão ao painel

        // Adiciona o painel de botão ao painel principal
        panel.add(buttonPanel);

        // Adiciona o painel principal ao diálogo
        dialog.add(panel, BorderLayout.CENTER);

        // Ajusta o tamanho do diálogo com base no conteúdo
        dialog.pack();

        // Centraliza o diálogo
        dialog.setLocationRelativeTo(this);

        // Torna o diálogo visível
        dialog.setVisible(true);
    }
}
