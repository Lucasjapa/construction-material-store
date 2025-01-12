package projectpoo.construction_material_store.screens;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.domain.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProductFrame extends JFrame {

    private static final String API_URL = "http://localhost:8080/products"; // URL da sua API
    private final JPanel productPanel;
    private JTable productTable;

    public ProductFrame() {
        setTitle("Tela de Produtos");
        setSize(400, 300);
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

        // Criar um modelo de tabela
        String[] columnNames = {"Selecionar", "Nome do Produto", "Quantidade", "ID"}; // "ID" será a coluna invisível
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Criar uma instância de RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Chama a API para obter a lista de produtos
        ResponseEntity<List<Product>> response = restTemplate.exchange(API_URL, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

        // Verifica se a resposta é válida
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Product> products = response.getBody();

            for (Product product : products) {
                // Adiciona uma linha à tabela para cada produto
                tableModel.addRow(new Object[]{
                        false, // Checkbox de seleção
                        product.getName(), // Nome do produto
                        product.getQuantity(), // Quantidade
                        product.getId() // ID, que será armazenado na coluna "ID"
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }

        // Tabela com scroll pane
        productTable = new JTable(tableModel);
        productTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox())); // Checkbox na primeira coluna

        // Torna a primeira coluna (de seleção) um JCheckBox
        productTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox())); // Checkbox na primeira coluna
        productTable.getColumnModel().getColumn(0).setCellRenderer(new JCheckBoxRenderer()); // Renderer para exibir o JCheckBox

        // Ocultar a coluna "ID"
        productTable.getColumnModel().getColumn(3).setMaxWidth(0); // Definir a largura máxima como 0
        productTable.getColumnModel().getColumn(3).setMinWidth(0); // Definir a largura mínima como 0
        productTable.getColumnModel().getColumn(3).setPreferredWidth(0); // Definir a largura preferencial como 0

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
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        // Campos de entrada para o produto
        JPanel panel = new JPanel(new GridLayout(3, 3));
        panel.add(new JLabel("Nome:"));
        JTextField nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Quantidade:"));
        JTextField quantityField = new JTextField();
        panel.add(quantityField);

        JButton btnSave = new JButton("Salvar");
        btnSave.addActionListener(e -> {
            String name = nameField.getText();
            String quantityStr = quantityField.getText();

            if (name.isEmpty() || quantityStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos os campos são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double quantity = Double.parseDouble(quantityStr);
                createProduct(name, quantity);
                JOptionPane.showMessageDialog(dialog, "Produto criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadProducts();
                dialog.dispose(); // Fecha o diálogo após salvar
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Quantidade deve ser um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Erro ao criar produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(btnSave);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void createProduct(String name, double quantity) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Cria o produto a ser enviado
        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setQuantity(quantity);

        // Envia a requisição POST para criar o produto
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