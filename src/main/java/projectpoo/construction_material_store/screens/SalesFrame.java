package projectpoo.construction_material_store.screens;

import projectpoo.construction_material_store.dto.ClientDTO;
import projectpoo.construction_material_store.dto.InvoiceDTO;
import projectpoo.construction_material_store.dto.ProductDTO;
import projectpoo.construction_material_store.screens.components.BackButton;
import projectpoo.construction_material_store.screens.components.ProductModal;
import projectpoo.construction_material_store.screens.components.SaleModal;
import projectpoo.construction_material_store.screens.components.TableComponent;

import javax.swing.*;
import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SalesFrame extends JFrame {

    private static final String API_URL = "http://localhost:8080/invoices"; // URL da sua API
    private final MainScreen mainScreen;

    public SalesFrame(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        setTitle("Vendas");
        setSize(700, 600);
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

        // Adiciona o painel de busca acima da tabela
        JPanel searchPanel = getSearchPanel(tableComponent);
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

    private JPanel getSearchPanel(TableComponent tableComponent) {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Layout alinhado à esquerda

        // Campo de busca
        JTextField searchField = new JTextField(20); // Tamanho do campo
        searchPanel.add(searchField);

        // Botão de busca
        JButton searchButton = new JButton("Buscar");
        searchPanel.add(searchButton);

        // Botão para listar todos os produtos
        JButton listButton = new JButton("Listar todos");
        searchPanel.add(listButton);

        // Ação do botão de busca
        searchButton.addActionListener(e -> {
            String searchParam = searchField.getText().trim();
            if (!searchParam.isEmpty()) {
                try {
                    String searchUrl = API_URL + "/searchproducts/" + URLEncoder.encode(searchParam, StandardCharsets.UTF_8);
                    // Carrega os dados da API usando o TableComponent
                    tableComponent.loadData(searchUrl, InvoiceDTO[].class);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(searchPanel, "Erro ao buscar produtos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(searchPanel, "Digite um termo para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        listButton.addActionListener(e -> tableComponent.loadData(API_URL, InvoiceDTO[].class));

        return searchPanel;
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
//        btnUpdateProduct.addActionListener(e -> updateSelectedProduct(tableComponent, productModal));
        rightPanel.add(btnUpdateProduct);

        // Adiciona os painéis ao painel principal
        buttonPanel.add(leftPanel, BorderLayout.WEST);  // Botão "Voltar" à esquerda
        buttonPanel.add(rightPanel, BorderLayout.EAST); // Outros botões à direita

        return buttonPanel;
    }
}
