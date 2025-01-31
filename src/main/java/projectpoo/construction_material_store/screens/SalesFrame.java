package projectpoo.construction_material_store.screens;

import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.dto.ClientDTO;
import projectpoo.construction_material_store.dto.InvoiceDTO;
import projectpoo.construction_material_store.screens.components.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SalesFrame extends JFrame {

    private static final String API_URL = "http://localhost:8080/invoices"; // URL da sua API
    private final MainScreen mainScreen;

    public SalesFrame(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        setTitle("Vendas");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configura o layout da tela
        setLayout(new BorderLayout());

        // JLabel com o título
        JLabel label = new JLabel("Lista de Vendas");
        add(label, BorderLayout.NORTH);

        // Inicializa o painel e adiciona ao layout
        JPanel salesPanel = new JPanel();
        salesPanel.setLayout(new BoxLayout(salesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(salesPanel);// Alinha verticalmente os componentes

        // Cria a instância do TableComponent
        TableComponent tableComponent = new TableComponent(salesPanel);
        SearchBar searchBar = new SearchBar();

        // Adiciona o painel de busca acima da tabela
        JPanel searchPanel = searchBar.getSearchPanel(tableComponent,API_URL, API_URL + "/search-invoices/", InvoiceDTO[].class);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH); // Barra de busca acima
        centerPanel.add(scrollPane, BorderLayout.CENTER); // Tabela no centro
        add(centerPanel, BorderLayout.CENTER);

        // Adiciona o painel de botões ao layout no local apropriado (parte inferior)
        JPanel buttonPanel = getButtonPanel(tableComponent);
        add(buttonPanel, BorderLayout.SOUTH);

        try {
            tableComponent.loadData(API_URL, InvoiceDTO[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private JPanel getButtonPanel(TableComponent tableComponent) {
        SaleModal saleModal = new SaleModal();

        // Painel principal de botões com BorderLayout
        JPanel buttonPanel = new JPanel(new BorderLayout());

        // Botão "Voltar"
        BackButton btnBack = new BackButton(this, mainScreen);

        // Painel para o botão "Voltar" no lado esquerdo
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(btnBack);

        // Painel para os outros botões no lado direito
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCreateProduct = new JButton("Nova Venda");
        btnCreateProduct.addActionListener(e -> saleModal.saleActionModal(tableComponent, null));
        rightPanel.add(btnCreateProduct);

        JButton btnUpdateProduct = new JButton("Editar Venda");
        btnUpdateProduct.addActionListener(e -> updateSelectedInvoice(tableComponent, saleModal));
        rightPanel.add(btnUpdateProduct);

        // Adiciona os painéis ao painel principal
        buttonPanel.add(leftPanel, BorderLayout.WEST);  // Botão "Voltar" à esquerda
        buttonPanel.add(rightPanel, BorderLayout.EAST); // Outros botões à direita

        return buttonPanel;
    }

    private void updateSelectedInvoice(TableComponent tableComponent, SaleModal saleModal) {
        Long invoiceId = null;

        // Obtém o modelo da tabela
        DefaultTableModel tableModel = (DefaultTableModel) tableComponent.getInvoiceTable().getModel();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            if (isSelected) {
                invoiceId = (Long) tableModel.getValueAt(i, 6);
            }
        }

        // Verifica se a lista de produtos selecionados não está vazia
        if (invoiceId != null) {
            // Chame a API para deletar os produtos com os IDs armazenados
            InvoiceDTO invoiceDTO = getInvoiceById(invoiceId);
            saleModal.saleActionModal(tableComponent, invoiceDTO);
        } else {
            JOptionPane.showMessageDialog(this, "Nenhuma nota fiscal selecionada para edição.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private InvoiceDTO getInvoiceById(Long invoiceId) {
        // Criar uma instância de RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Chama a API para obter o produto
        String getUrl = API_URL + "/" + invoiceId;
        return restTemplate.getForObject(getUrl, InvoiceDTO.class);
    }
}
