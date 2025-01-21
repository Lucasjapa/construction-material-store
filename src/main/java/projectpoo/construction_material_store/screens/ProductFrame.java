package projectpoo.construction_material_store.screens;

import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.dto.ProductDTO;
import projectpoo.construction_material_store.screens.components.BackButton;
import projectpoo.construction_material_store.screens.components.ProductModal;
import projectpoo.construction_material_store.screens.components.TableComponent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProductFrame extends JFrame {

    private static final String API_URL = "http://localhost:8080/products"; // URL da sua API
    private final MainScreen mainScreen;

    public ProductFrame(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        setTitle("Tela de Produtos");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configura o layout da tela
        setLayout(new BorderLayout());

        // JLabel com o título
        JLabel label = new JLabel("Lista de Produtos");
        add(label, BorderLayout.NORTH);

        // Inicializa o painel e adiciona ao layout
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS)); // Alinha verticalmente os componentes
        add(new JScrollPane(productPanel), BorderLayout.CENTER);

        // Cria a instância do TableComponent
        TableComponent tableComponent = new TableComponent(productPanel);
        JPanel buttonPanel = getButtonPanel(tableComponent);

        // Adiciona o painel de botões ao layout no local apropriado (parte inferior)
        add(buttonPanel, BorderLayout.SOUTH);

        // Tenta carregar os produtos da API
        try {
            tableComponent.loadData(API_URL, ProductDTO[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel getButtonPanel(TableComponent tableComponent) {
        ProductModal productModal = new ProductModal();

        // Painel principal de botões com BorderLayout
        JPanel buttonPanel = new JPanel(new BorderLayout());

        // Botão "Voltar"
        BackButton btnBack = new BackButton(this, mainScreen);

        // Painel para o botão "Voltar" no lado esquerdo
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(btnBack);

        // Painel para os outros botões no lado direito
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCreateProduct = new JButton("Criar Produto");
        btnCreateProduct.addActionListener(e -> productModal.productActionModal(tableComponent, null));
        rightPanel.add(btnCreateProduct);

        JButton btnUpdateProduct = new JButton("Editar Produto");
        btnUpdateProduct.addActionListener(e -> updateSelectedProduct(tableComponent, productModal));
        rightPanel.add(btnUpdateProduct);

        JButton btnDeleteSelected = new JButton("Deletar Selecionados");
        btnDeleteSelected.addActionListener(e -> deleteSelectedProducts(tableComponent));
        rightPanel.add(btnDeleteSelected);

        // Adiciona os painéis ao painel principal
        buttonPanel.add(leftPanel, BorderLayout.WEST);  // Botão "Voltar" à esquerda
        buttonPanel.add(rightPanel, BorderLayout.EAST); // Outros botões à direita

        return buttonPanel;
    }

    private void updateSelectedProduct(TableComponent tableComponent, ProductModal productModal) {
        Long productId = null;

        // Obtém o modelo da tabela
        DefaultTableModel tableModel = (DefaultTableModel) tableComponent.getProductTable().getModel();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            if (isSelected) {
                productId = (Long) tableModel.getValueAt(i, 7);
            }
        }

        // Verifica se a lista de produtos selecionados não está vazia
        if (productId != null) {
            // Chame a API para deletar os produtos com os IDs armazenados
            ProductDTO productDTO = getProductById(productId);
            productModal.productActionModal(tableComponent, productDTO);
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum produto selecionado para edição.", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    private ProductDTO getProductById(Long productId) {
        // Criar uma instância de RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Chama a API para obter o produto
        String getUrl = API_URL + "/" + productId;
        return restTemplate.getForObject(getUrl, ProductDTO.class);
    }

    private void deleteSelectedProducts(TableComponent tableComponent) {
        // Lista dos IDs dos produtos a serem deletados
        List<Long> productsToDelete = new ArrayList<>();

        // Obtém o modelo da tabela
        DefaultTableModel tableModel = (DefaultTableModel) tableComponent.getProductTable().getModel();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            if (isSelected) {
                Long productId = (Long) tableModel.getValueAt(i, 7); // Obtém o id do produto
                if (productId != null) {
                    productsToDelete.add(productId); // Adiciona o id à lista
                }
            }
        }

        // Verifica se a lista de produtos selecionados não está vazia
        if (!productsToDelete.isEmpty()) {
            // Chame a API para deletar os produtos com os IDs armazenados
            deleteProductsFromApi(productsToDelete);
            tableComponent.loadData(API_URL, ProductDTO[].class);
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum produto selecionado para deletar.", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    // Método para deletar os produtos da API
    private void deleteProductsFromApi(List<Long> productsToDelete) {
        // Criar uma instância de RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Chama a API para deletar os produtos
        for (Long productId : productsToDelete) {
            String deleteUrl = API_URL + "/" + productId;
            restTemplate.delete(deleteUrl);
        }

    }
}