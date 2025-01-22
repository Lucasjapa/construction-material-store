package projectpoo.construction_material_store.screens;

import projectpoo.construction_material_store.dto.ClientDTO;
import projectpoo.construction_material_store.dto.ProductDTO;
import projectpoo.construction_material_store.screens.components.BackButton;
import projectpoo.construction_material_store.screens.components.ClientModal;
import projectpoo.construction_material_store.screens.components.TableComponent;

import javax.swing.*;
import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ClientFrame extends JFrame {

    private static final String API_URL = "http://localhost:8080/clients"; // URL da sua API
    private final MainScreen mainScreen;

    public ClientFrame(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        setTitle("Tela de Produtos");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configura o layout da tela
        setLayout(new BorderLayout());

        // JLabel com o título
        JLabel label = new JLabel("Lista de Clientes");
        add(label, BorderLayout.NORTH);

        // Inicializa o painel e adiciona ao layout
        JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new BoxLayout(clientPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(clientPanel);// Alinha verticalmente os componentes
//        add(new JScrollPane(clientPanel), BorderLayout.CENTER);

        // Cria a instância do TableComponent
        TableComponent tableComponent = new TableComponent(clientPanel);
        
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
            tableComponent.loadData(API_URL, ClientDTO[].class);
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
                    tableComponent.loadData(searchUrl, ProductDTO[].class);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(searchPanel, "Erro ao buscar produtos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(searchPanel, "Digite um termo para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        listButton.addActionListener(e -> tableComponent.loadData(API_URL, ProductDTO[].class));

        return searchPanel;
    }

    private JPanel getButtonPanel(TableComponent tableComponent) {
        ClientModal clientModal = new ClientModal();
        // Painel principal de botões
        JPanel buttonPanel = new JPanel(new BorderLayout());

        // Adiciona o botão "Voltar"
        BackButton btnBack = new BackButton(this, mainScreen);
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(btnBack);

        // Painel para os outros botões no lado direito
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCreateProduct = new JButton("Criar Cliente");
        btnCreateProduct.addActionListener(e -> clientModal.clientActionModal(tableComponent, null));
        rightPanel.add(btnCreateProduct);

        JButton btnUpdateProduct = new JButton("Editar Cliente");
//        btnUpdateProduct.addActionListener(e -> updateSelectedProduct(tableComponent, productModal));
        rightPanel.add(btnUpdateProduct);

        JButton btnDeleteSelected = new JButton("Deletar Selecionados");
//        btnDeleteSelected.addActionListener(e -> deleteSelectedProducts(tableComponent));
        rightPanel.add(btnDeleteSelected);

        // Adiciona os painéis ao painel principal
        buttonPanel.add(leftPanel, BorderLayout.WEST);  // Botão "Voltar" à esquerda
        buttonPanel.add(rightPanel, BorderLayout.EAST); // Outros botões à direita

        return buttonPanel;
    }
}
