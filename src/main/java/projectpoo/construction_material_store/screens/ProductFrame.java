package projectpoo.construction_material_store.screens;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.domain.Product;
import projectpoo.construction_material_store.dto.ProductDTO;
import projectpoo.construction_material_store.screens.components.ProductModal;
import projectpoo.construction_material_store.screens.components.TableComponent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ProductFrame extends JFrame {

    private static final String API_URL = "http://localhost:8080/products"; // URL da sua API

    public ProductFrame() {
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
        ProductModal productModal = new ProductModal();

        // Painel de botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Adiciona um botão para criar um novo produto
        JButton btnCreateProduct = new JButton("Criar Produto");
        btnCreateProduct.addActionListener(e -> productModal.openCreateProductDialog(tableComponent));
        buttonPanel.add(btnCreateProduct);

        // Adiciona um botão para criar um novo produto
        JButton btnUpdateProduct = new JButton("Editar Produto");
        btnUpdateProduct.addActionListener(e -> updateSelectedProduct(tableComponent));
        buttonPanel.add(btnUpdateProduct);

        // Botão para deletar os produtos selecionados
        JButton btnDeleteSelected = new JButton("Deletar Selecionados");
        btnDeleteSelected.addActionListener(e -> deleteSelectedProducts(tableComponent));
        buttonPanel.add(btnDeleteSelected);

        // Adiciona o painel de botões ao layout no local apropriado (parte inferior)
        add(buttonPanel, BorderLayout.SOUTH);

        // Tenta carregar os produtos da API
        try {
            tableComponent.loadData(API_URL, ProductDTO[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCreateProductDialog(TableComponent tableComponent, ProductDTO productDto) {
        // Criar uma instância do diálogo
        JDialog dialog = new JDialog(this, "Editar Produto", true);
        dialog.setSize(650, 400);
        dialog.setLocationRelativeTo(this);

        // Painel para os campos
        JPanel panel = new JPanel(new GridLayout(13, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campos de entrada
        panel.add(new JLabel("Nome:"));
        JTextField nameField = new JTextField(productDto.getName());
        panel.add(nameField);

        panel.add(new JLabel("Código do Produto:"));
        JTextField codProductField = new JTextField(productDto.getCodProduct());
        panel.add(codProductField);

        panel.add(new JLabel("Descrição:"));
        JTextField descriptionField = new JTextField(productDto.getCodProduct());
        panel.add(descriptionField);

        panel.add(new JLabel("Quantidade em Estoque:"));
        JTextField totalStockField = new JTextField(String.valueOf(productDto.getTotalStock()));
        panel.add(totalStockField);

        panel.add(new JLabel("Estoque Mínimo:"));
        JTextField minStockField = new JTextField(String.valueOf(productDto.getMinStock()));
        panel.add(minStockField);

        panel.add(new JLabel("Preço:"));
        JTextField priceField = new JTextField(String.valueOf(productDto.getPrice()));
        panel.add(priceField);

        panel.add(new JLabel("Unidade de Venda:"));
        JTextField salesUnitField = new JTextField(productDto.getSalesUnit());
        panel.add(salesUnitField);

        panel.add(new JLabel("Data de Validade: (dd-mm-aaaa)"));
        JTextField expirationDateField = new JTextField(productDto.getExpirationDate());
        panel.add(expirationDateField);

        panel.add(new JLabel("Categorias:"));
        JComboBox<String> categoryList = new JComboBox<>(new String[]{
                "Cimento e Argamassas", "Tintas e Acessórios", "Ferramentas", "Materiais Elétricos",
                "Materiais Hidráulicos", "Madeiras e Compensados", "Ferragens", "Pisos e Revestimentos",
                "Iluminação", "Decoração", "Jardinagem", "Outros"
        });
        // Define a categoria selecionada com base no valor do DTO
        categoryList.setSelectedItem(productDto.getCategory());
        panel.add(categoryList);

        // Botão de salvar
        JButton btnSave = new JButton("Salvar");
        btnSave.addActionListener(e -> {
            productDto.setName(nameField.getText());
            productDto.setCodProduct(codProductField.getText());
            productDto.setDescription(descriptionField.getText());
            productDto.setTotalStock(Double.parseDouble(totalStockField.getText()));
            productDto.setMinStock(Double.parseDouble(minStockField.getText()));
            productDto.setPrice(Double.parseDouble(priceField.getText()));
            productDto.setSalesUnit(salesUnitField.getText());
            productDto.setExpirationDate(expirationDateField.getText());
            productDto.setCategory(categoryList.getSelectedItem().toString());

            // Validação dos campos
            if (productDto.getName().isEmpty() || productDto.getCodProduct().isEmpty() || productDto.getSalesUnit().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos os campos são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (productDto.getTotalStock() < 0 || productDto.getMinStock() < 0 || productDto.getPrice() < 0) {
                    JOptionPane.showMessageDialog(dialog, "Valores numéricos não podem ser negativos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Criar produto
                btnSave.setEnabled(false); // Desabilita o botão para evitar múltiplos cliques
                updateProduct(productDto);
                JOptionPane.showMessageDialog(dialog, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                tableComponent.loadData(API_URL, ProductDTO[].class); // Atualizar lista de produtos
                dialog.dispose(); // Fechar o diálogo
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Formato de data inválido. Use dd-mm-aaaa.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Insira valores numéricos válidos para estoque e preço.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao atualizar produto", "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                btnSave.setEnabled(true); // Reativa o botão, mesmo em caso de erro
            }
        });

        panel.add(new JLabel()); // Espaço vazio para alinhar o botão
        panel.add(btnSave);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void updateProduct(ProductDTO productDto) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Envia a requisição PUT com o identificador do produto
        String url = API_URL + "/" + productDto.getId();  // Supondo que o ID do produto seja parte da URL para atualização
        HttpEntity<ProductDTO> request = new HttpEntity<>(productDto);
        ResponseEntity<Product> response = restTemplate.exchange(url, HttpMethod.PUT, request, Product.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Erro ao atualizar produto: " + response.getStatusCode());
        }
    }

    private void updateSelectedProduct(TableComponent tableComponent) {
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
            updateCreateProductDialog(tableComponent, productDTO);
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

    // TODO: FAZER A FUNCIONALIDADE DE FORMA CORRETA
    private ProductDTO updateProductsFromApi(Long productId) {
        // Criar uma instância de RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Chama a API para obter o produto
        String getUrl = API_URL + "/" + productId;
        return restTemplate.getForObject(getUrl, ProductDTO.class);
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