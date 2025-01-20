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
        // Determina se é criação ou edição
        boolean isEdit = productDto != null;
        String dialogTitle = isEdit ? "Editar Produto" : "Criar Produto";

        JDialog dialog = new JDialog(this, dialogTitle, true);
        dialog.setSize(650, 400);
        dialog.setLocationRelativeTo(this);

        // Painel para os campos
        JPanel panel = new JPanel(new GridLayout(13, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campos de entrada
        panel.add(new JLabel("Nome:"));
        JTextField nameField = new JTextField(isEdit ? productDto.getName() : "");
        panel.add(nameField);

        panel.add(new JLabel("Código do Produto:"));
        JTextField codProductField = new JTextField(isEdit ? productDto.getCodProduct() : "");
        panel.add(codProductField);

        panel.add(new JLabel("Descrição:"));
        JTextField descriptionField = new JTextField(isEdit ? productDto.getDescription() : "");
        panel.add(descriptionField);

        panel.add(new JLabel("Quantidade em Estoque:"));
        JTextField totalStockField = new JTextField(isEdit ? String.valueOf(productDto.getTotalStock()) : "");
        panel.add(totalStockField);

        panel.add(new JLabel("Estoque Mínimo:"));
        JTextField minStockField = new JTextField(isEdit ? String.valueOf(productDto.getMinStock()) : "");
        panel.add(minStockField);

        panel.add(new JLabel("Preço:"));
        JTextField priceField = new JTextField(isEdit ? String.valueOf(productDto.getPrice()) : "");
        panel.add(priceField);

        panel.add(new JLabel("Unidade de Venda:"));
        JTextField salesUnitField = new JTextField(isEdit ? productDto.getSalesUnit() : "");
        panel.add(salesUnitField);

        panel.add(new JLabel("Data de Validade: (dd-mm-aaaa)"));
        JTextField expirationDateField = new JTextField(isEdit ? productDto.getExpirationDate() : "");
        panel.add(expirationDateField);

        panel.add(new JLabel("Categorias:"));
        JComboBox<String> categoryList = new JComboBox<>(new String[]{
                "Cimento e Argamassas", "Tintas e Acessórios", "Ferramentas", "Materiais Elétricos",
                "Materiais Hidráulicos", "Madeiras e Compensados", "Ferragens", "Pisos e Revestimentos",
                "Iluminação", "Decoração", "Jardinagem", "Outros"
        });
        // Define a categoria selecionada com base no valor do DTO
        categoryList.setSelectedItem(isEdit ? productDto.getCategory() : "");
        panel.add(categoryList);

        // Botão de salvar
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

            // Validação dos campos
            if (name.isEmpty() || codProduct.isEmpty() || totalStockStr.isEmpty() ||
                    minStockStr.isEmpty() || priceStr.isEmpty() || salesUnit.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos os campos são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (totalStock < 0 || minStock < 0 || price < 0) {
                JOptionPane.showMessageDialog(dialog, "Valores numéricos não podem ser negativos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Criar produto
            btnSave.setEnabled(false); // Desabilita o botão para evitar múltiplos cliques

            try {

                // Criação ou atualização
                if (isEdit) {
                    // Atualizar o produto existente
                    productDto.setName(name);
                    productDto.setCodProduct(codProduct);
                    productDto.setDescription(description);
                    productDto.setTotalStock(totalStock);
                    productDto.setMinStock(minStock);
                    productDto.setPrice(price);
                    productDto.setSalesUnit(salesUnit);
                    productDto.setExpirationDate(expirationDate);
                    productDto.setCategory(category);

                    updateProduct(productDto); // Chame o método de atualização
                    JOptionPane.showMessageDialog(dialog, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Cria o objeto Product com os novos atributos
                    ProductDTO newProduct = new ProductDTO(name, codProduct, totalStock, description, minStock, price, salesUnit, expirationDate, category);

                    // Criar um novo produto
                    createProduct(newProduct);
                    JOptionPane.showMessageDialog(dialog, "Produto criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }

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
        String url = API_URL + "/" + productDto.getId();  // Supondo que o ID do produto seja parte da URL para atualização
        HttpEntity<ProductDTO> request = new HttpEntity<>(productDto);
        ResponseEntity<Product> response = restTemplate.exchange(url, HttpMethod.PUT, request, Product.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Erro ao atualizar produto: " + response.getStatusCode());
        }
    }
}
