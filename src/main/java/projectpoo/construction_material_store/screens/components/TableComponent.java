package projectpoo.construction_material_store.screens.components;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.domain.Client;
import projectpoo.construction_material_store.domain.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TableComponent {

    private JPanel panel;
    private JTable productTable;
    private JTable clientTable;

    public TableComponent(JPanel panel) {
        this.panel = panel;
    }

    public <T> void loadData(String apiUrl, Class<T[]> clazz) {
        panel.removeAll();

        // Atualize os dados do objeto específico
        T[] items = loadItemsFromApi(apiUrl, clazz);

        if (items == null) {
            JOptionPane.showMessageDialog(panel, "Erro ao carregar dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Prepara o modelo da tabela com os dados
        DefaultTableModel tableModel = createTableModel(items);

        // Configuração adicional específica para Produtos
        if (items instanceof Product[]) {
            // Configura a tabela
            productTable= new JTable(tableModel);
            configureProductTable(productTable);  // Configurações específicas para Produto
        }
        else {
            // Configura a tabela
            clientTable= new JTable(tableModel);
            configureClientTable(clientTable); // Configuração genérica
        }

        // Adiciona a tabela ao painel
        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane);

        // Revalida e repinta o painel
        panel.revalidate();
        panel.repaint();
    }

    // Método para carregar os itens de qualquer tipo (Product, Client, Sale, etc)
    private <T> T[] loadItemsFromApi(String apiUrl, Class<T[]> clazz) {
        RestTemplate restTemplate = new RestTemplate();

        // Chama a API e obtém os dados como um array de objetos genéricos
        ResponseEntity<T[]> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, clazz);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }

    // Método para criar o modelo de tabela, baseado no tipo de dados
    private <T> DefaultTableModel createTableModel(T[] items) {
        // Aqui você pode adicionar lógica específica para cada tipo de dado,
        // para gerar o modelo de tabela conforme necessário.

        // Exemplo para produtos:
        if (items instanceof Product[]) {
            String[] columnNames = {"Selecionar", "Nome", "Código", "Estoque Total", "Estoque Mínimo", "Preço", "Unidade", "ID"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            for (Product product : (Product[]) items) {
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

            return tableModel;
        } else if (items instanceof Client[]) {
            String[] columnNames = {"Selecionar", "Nome", "Email", "Telefone", "ID"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            for (Client client : (Client[]) items) {
                tableModel.addRow(new Object[]{
                        false,
                        client.getName(),
                        client.getEmail(),
                        client.getId()
                });
            }
            return tableModel;
        }

        // Para outros tipos de objetos (venda, etc.), adicione o mesmo padrão.
        return null;
    }

    private void configureProductTable(JTable table) {
        // Torna a primeira coluna (de seleção) um JCheckBox
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox())); // Checkbox na primeira coluna
        table.getColumnModel().getColumn(0).setCellRenderer(new JCheckBoxRenderer()); // Renderer para exibir o JCheckBox

        // Ocultar a coluna "ID"
        table.getColumnModel().getColumn(7).setMaxWidth(0);
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setPreferredWidth(0);
    }

    private void configureClientTable(JTable table) {
        // Torna a primeira coluna (de seleção) um JCheckBox
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox())); // Checkbox na primeira coluna
        table.getColumnModel().getColumn(0).setCellRenderer(new JCheckBoxRenderer()); // Renderer para exibir o JCheckBox

        // Ocultar a coluna "ID"
        table.getColumnModel().getColumn(7).setMaxWidth(0);
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setPreferredWidth(0);
    }

    static class JCheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setSelected(value != null && (Boolean) value); // Definir se o checkbox está selecionado ou não
            setEnabled(table.isEnabled()); // Define se o checkbox está habilitado
            return this; // Retorna o próprio JCheckBox
        }
    }

    public JTable getProductTable() {
        return productTable;
    }

}