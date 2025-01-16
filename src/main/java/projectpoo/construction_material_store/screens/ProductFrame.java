package projectpoo.construction_material_store.screens;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.domain.Product;

import projectpoo.construction_material_store.domain.Product.Category;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ProductFrame extends JFrame {

    private static final String API_URL = "http://localhost:8080/products"; // URL da sua API
    private final JPanel productPanel;
    private JTable productTable;

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
        productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS)); // Alinha verticalmente os componentes
        add(new JScrollPane(productPanel), BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Adiciona um botão para criar um novo produto
        JButton btnCreateProduct = new JButton("Criar Produto");
        btnCreateProduct.addActionListener(e -> openCreateProductDialog());
        buttonPanel.add(btnCreateProduct);

        // Botão para deletar os produtos selecionados
        JButton btnDeleteSelected = new JButton("Deletar Selecionados");
        btnDeleteSelected.addActionListener(e -> deleteSelectedProducts());
        buttonPanel.add(btnDeleteSelected);

        // Adiciona o painel de botões ao layout no local apropriado (parte inferior)
        add(buttonPanel, BorderLayout.SOUTH);

        // Tenta carregar os produtos da API
        try {
            loadProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        productPanel.removeAll();

        // Atualize os nomes das colunas
        String[] columnNames = {"Selecionar", "Nome", "Código", "Estoque Total", "Estoque Mínimo", "Preço", "Unidade", "ID"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        RestTemplate restTemplate = new RestTemplate();

        // Chama a API
        ResponseEntity<List<Product>> response = restTemplate.exchange(API_URL, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        if (response.getStatusCode().is2xxSuccessful()) {
            List<Product> products = response.getBody();

            for (Product product : products) {
                tableModel.addRow(new Object[]{
                        false,
                        product.getName(),
                        product.getCodProcuct(),
                        product.getTotalStock(),
                        product.getMinStock(),
                        product.getPrice(),
                        product.getSalesUnit(),
                        product.getId()
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }

        // Configurar a tabela
        productTable = new JTable(tableModel);

        // Ocultar a coluna "ID"
        productTable.getColumnModel().getColumn(7).setMaxWidth(0);
        productTable.getColumnModel().getColumn(7).setMinWidth(0);
        productTable.getColumnModel().getColumn(7).setPreferredWidth(0);

        JScrollPane scrollPane = new JScrollPane(productTable);
        productPanel.add(scrollPane);

        productPanel.revalidate();
        productPanel.repaint();
    }


    class JCheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setSelected(value != null && (Boolean) value); // Definir se o checkbox está selecionado ou não
            setEnabled(table.isEnabled()); // Define se o checkbox está habilitado
            return this; // Retorna o próprio JCheckBox
        }
    }

    private void openCreateProductDialog() {
        JDialog dialog = new JDialog(this, "Criar Produto", true);
        dialog.setSize(650, 400);
        dialog.setLocationRelativeTo(this);

        // Painel para os campos
        JPanel panel = new JPanel(new GridLayout(13, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campos de entrada
        panel.add(new JLabel("Nome:"));
        JTextField nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Código do Produto:"));
        JTextField codProductField = new JTextField();
        panel.add(codProductField);

        panel.add(new JLabel("Quantidade em Estoque:"));
        JTextField totalStockField = new JTextField();
        panel.add(totalStockField);

        panel.add(new JLabel("Estoque Mínimo:"));
        JTextField minStockField = new JTextField();
        panel.add(minStockField);

        panel.add(new JLabel("Preço:"));
        JTextField priceField = new JTextField();
        panel.add(priceField);

        panel.add(new JLabel("Unidade de Venda:"));
        JTextField salesUnitField = new JTextField();
        panel.add(salesUnitField);

        panel.add(new JLabel("Data de Validade: (dd-mm-aaaa)"));
        JTextField expirationDateField = new JTextField();
        panel.add(expirationDateField);

        panel.add(new JLabel("Categorias:"));
        JComboBox<String> categoryList = new JComboBox<>(new String[]{
                "Cimento", "Tinta", "Ferramentas", "Elétrico", "Hidráulico",
                "Madeiras", "Ferragens", "Pisos", "Iluminação", "Decoração",
                "Jardim", "Outros"
        });
        panel.add(categoryList);

        // Botão de salvar
        JButton btnSave = new JButton("Salvar");
        btnSave.addActionListener(e -> {
            String name = nameField.getText();
            String codProduct = codProductField.getText();
            String totalStockStr = totalStockField.getText();
            String minStockStr = minStockField.getText();
            String priceStr = priceField.getText();
            String salesUnit = salesUnitField.getText();

            // Validação dos campos
            if (name.isEmpty() || codProduct.isEmpty() || totalStockStr.isEmpty() ||
                    minStockStr.isEmpty() || priceStr.isEmpty() || salesUnit.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos os campos são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double totalStock = Double.parseDouble(totalStockStr);
                double minStock = Double.parseDouble(minStockStr);
                double price = Double.parseDouble(priceStr);

                if (totalStock < 0 || minStock < 0 || price < 0) {
                    JOptionPane.showMessageDialog(dialog, "Valores numéricos não podem ser negativos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Criar produto
                btnSave.setEnabled(false); // Desabilita o botão para evitar múltiplos cliques
                createProduct(name, codProduct, totalStock, minStock, price, salesUnit);
                JOptionPane.showMessageDialog(dialog, "Produto criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadProducts(); // Atualizar lista de produtos
                dialog.dispose(); // Fechar o diálogo
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Formato de data inválido. Use dd-mm-aaaa.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Insira valores numéricos válidos para estoque e preço.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao criar produto", "Erro", JOptionPane.ERROR_MESSAGE);
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

    private void createProduct(String name, String codProduct, double totalStock, double minStock, double price, String salesUnit) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Cria o objeto Product com os novos atributos
        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setCodProcuct(codProduct);
        newProduct.setTotalStock(totalStock);
        newProduct.setMinStock(minStock);
        newProduct.setPrice(price);
        newProduct.setSalesUnit(salesUnit);

        // Envia a requisição POST
        HttpEntity<Product> request = new HttpEntity<>(newProduct);
        ResponseEntity<Product> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, Product.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Erro ao criar produto: " + response.getStatusCode());
        }
    }




    private void deleteSelectedProducts() {
        // Lista dos IDs dos produtos a serem deletados
        List<Long> productsToDelete = new ArrayList<>();

        // Obtém o modelo da tabela
        DefaultTableModel tableModel = (DefaultTableModel) productTable.getModel();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            if (isSelected) {
                Long productId = (Long) tableModel.getValueAt(i, 3); // Obtém o id do produto
                if (productId != null) {
                    productsToDelete.add(productId); // Adiciona o id à lista
                }
            }
        }

        // Verifica se a lista de produtos selecionados não está vazia
        if (!productsToDelete.isEmpty()) {
            // Chame a API para deletar os produtos com os IDs armazenados
            deleteProductsFromApi(productsToDelete);
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

        // Atualiza a lista de produtos após a exclusão
        try {
            loadProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}