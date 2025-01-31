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

    public void productActionModal(TableComponent tableComponent, ProductDTO productDto) {
        // Determine if it is creation or editing
        boolean isEdit = productDto != null;
        String dialogTitle = isEdit ? "Editar Produto" : "Criar Produto";

        JDialog dialog = new JDialog(this, dialogTitle, true);
        dialog.setSize(650, 400);
        dialog.setLocationRelativeTo(this);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel for the fields
        JPanel panel = new JPanel(new GridLayout(9, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input fields
        panel.add(new JLabel("Nome: (ex: Cimento Poty)"));
        JTextField nameField = new JTextField(isEdit ? productDto.getName() : "");
        panel.add(nameField);

        panel.add(new JLabel("Unidade de Venda: (ex: Saco de 50Kg)"));
        JTextField salesUnitField = new JTextField(isEdit ? productDto.getSalesUnit() : "");
        panel.add(salesUnitField);

        panel.add(new JLabel("Descrição: (Opcional)"));
        JTextField descriptionField = new JTextField(isEdit ? productDto.getDescription() : "");
        panel.add(descriptionField);

        panel.add(new JLabel("Código do Produto:"));
        JTextField codProductField = new JTextField(isEdit ? productDto.getCodProduct() : "");
        panel.add(codProductField);

        panel.add(new JLabel("Quantidade em Estoque:"));
        JTextField totalStockField = new JTextField(isEdit ? String.valueOf(productDto.getTotalStock()) : "");
        panel.add(totalStockField);

        panel.add(new JLabel("Estoque Mínimo:"));
        JTextField minStockField = new JTextField(isEdit ? String.valueOf(productDto.getMinStock()) : "");
        panel.add(minStockField);

        panel.add(new JLabel("Preço:"));
        JTextField priceField = new JTextField(isEdit ? String.valueOf(productDto.getPrice()) : "");
        panel.add(priceField);

        panel.add(new JLabel("Data de Validade: (dd-mm-aaaa)"));
        JTextField expirationDateField = new JTextField(isEdit ? productDto.getExpirationDate() : "");
        panel.add(expirationDateField);

        panel.add(new JLabel("Categorias:"));
        JComboBox<String> categoryList = new JComboBox<>(new String[]{
                "Cimento e Argamassas", "Tintas e Acessórios", "Ferramentas", "Materiais Elétricos",
                "Materiais Hidráulicos", "Madeiras e Compensados", "Ferragens", "Pisos e Revestimentos",
                "Iluminação", "Decoração", "Jardinagem", "Outros"
        });
        categoryList.setSelectedItem(isEdit ? productDto.getCategory() : "");
        panel.add(categoryList);

        // Panel for the save button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton(isEdit ? "Atualizar" : "Salvar");
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

            double totalStock = Double.parseDouble(totalStockStr);
            double minStock = Double.parseDouble(minStockStr);
            double price = Double.parseDouble(priceStr);

            // Field validation
            if (name.isEmpty() || codProduct.isEmpty() || totalStockStr.isEmpty() ||
                    minStockStr.isEmpty() || priceStr.isEmpty() || salesUnit.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos os campos são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (totalStock < 0 || minStock < 0 || price < 0) {
                JOptionPane.showMessageDialog(dialog, "Valores numéricos não podem ser negativos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create or update product
            btnSave.setEnabled(false); // Disable the button to avoid multiple clicks

            try {
                if (isEdit) {
                    productDto.setName(name);
                    productDto.setCodProduct(codProduct);
                    productDto.setDescription(description);
                    productDto.setTotalStock(totalStock);
                    productDto.setMinStock(minStock);
                    productDto.setPrice(price);
                    productDto.setSalesUnit(salesUnit);
                    productDto.setExpirationDate(expirationDate);
                    productDto.setCategory(category);

                    updateProduct(productDto); // Call the update method
                    JOptionPane.showMessageDialog(dialog, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    ProductDTO newProduct = new ProductDTO(name, codProduct, totalStock, description, minStock, price, salesUnit, expirationDate, category);

                    createProduct(newProduct);
                    JOptionPane.showMessageDialog(dialog, "Produto criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }

                tableComponent.loadData(API_URL, ProductDTO[].class); // Update product list
                dialog.dispose(); // Close the dialog
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Formato de data inválido. Use dd-mm-aaaa.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Insira valores numéricos válidos para estoque e preço.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao criar produto", "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                btnSave.setEnabled(true); // Re-enable the button, even in case of error
            }
        });

        buttonPanel.add(btnSave);
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void createProduct(ProductDTO newProduct) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Envia a requisição POST
        HttpEntity<ProductDTO> request = new HttpEntity<>(newProduct);
        ResponseEntity<Product> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, Product.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Erro ao criar produto: " + response.getStatusCode());
        }
    }

    private void updateProduct(ProductDTO productDto) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Envia a requisição PUT com o identificador do produto
        String url = API_URL + "/updateproduct/" + productDto.getId();  // Supondo que o ID do produto seja parte da URL para atualização
        HttpEntity<ProductDTO> request = new HttpEntity<>(productDto);
        ResponseEntity<Product> response = restTemplate.exchange(url, HttpMethod.PUT, request, Product.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Erro ao atualizar produto: " + response.getStatusCode());
        }
    }
}
