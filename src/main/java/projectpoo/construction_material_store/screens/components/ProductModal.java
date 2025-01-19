package projectpoo.construction_material_store.screens.components;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.domain.Product;
import projectpoo.construction_material_store.dto.ProductDTO;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeParseException;

public class ProductModal extends JDialog {

    private static final String API_URL = "http://localhost:8080/products"; // URL da sua API

    public ProductModal() {
    }

    //TODO:LUCAS CAVALCANTE - Componentizar para o update;
    public void openCreateProductDialog(TableComponent tableComponent) {
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

        panel.add(new JLabel("Descrição:"));
        JTextField descriptionField = new JTextField();
        panel.add(descriptionField);

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
                "Cimento e Argamassas", "Tintas e Acessórios", "Ferramentas", "Materiais Elétricos",
                "Materiais Hidráulicos", "Madeiras e Compensados", "Ferragens", "Pisos e Revestimentos",
                "Iluminação", "Decoração", "Jardinagem", "Outros"
        });
        panel.add(categoryList);

        // Botão de salvar
        JButton btnSave = new JButton("Salvar");
        btnSave.addActionListener(e -> {
            String name = nameField.getText();
            String codProduct = codProductField.getText();
            String description = descriptionField.getText();
            String totalStockStr = totalStockField.getText();
            String minStockStr = minStockField.getText();
            String priceStr = priceField.getText();
            String salesUnit = salesUnitField.getText();
            String expirationDate = expirationDateField.getText();
            String category = categoryList.getSelectedItem().toString();

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
                createProduct(name, codProduct, totalStock, description, minStock, price, salesUnit, expirationDate, category);
                JOptionPane.showMessageDialog(dialog, "Produto criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                tableComponent.loadData(API_URL, ProductDTO[].class); // Atualizar lista de produtos
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

    private void createProduct(
            String name,
            String codProduct,
            double totalStock,
            String description,
            double minStock,
            double price,
            String salesUnit,
            String expirationDate,
            String category
    ) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Cria o objeto Product com os novos atributos
        ProductDTO newProduct = new ProductDTO(name, codProduct, totalStock, description, minStock, price, salesUnit, expirationDate, category);

        // Envia a requisição POST
        HttpEntity<ProductDTO> request = new HttpEntity<>(newProduct);
        ResponseEntity<Product> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, Product.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Erro ao criar produto: " + response.getStatusCode());
        }
    }
}
