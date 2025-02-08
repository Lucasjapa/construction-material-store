package projectpoo.construction_material_store.screens.components;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.dto.*;
import projectpoo.construction_material_store.screens.DashboardItensModal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TableComponent {

    private final JPanel panel;
    private JTable productTable;
    private JTable clientTable;
    private JTable invoiceTable;
    private JTable purchaseTable;
    private static List<ProductDTO> products = new ArrayList<>();

    private static final String API_URL_PURCHASE = "http://localhost:8080/purchases";
    private static final String API_URL_INVOICE = "http://localhost:8080/invoices";

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
        if (items instanceof ProductDTO[]) {
            // Configura a tabela
            productTable = new JTable(tableModel);
            configureProductTable(productTable);
            JScrollPane scrollPane = new JScrollPane(productTable);
            panel.add(scrollPane);
        } else if (items instanceof ClientDTO[]) {
            // Configura a tabela
            clientTable = new JTable(tableModel);
            configureClientTable(clientTable); // Configuração genérica
            JScrollPane scrollPane = new JScrollPane(clientTable);
            panel.add(scrollPane);
        } else if (items instanceof InvoiceDTO[]) {
            // Configura a tabela
            invoiceTable = new JTable(tableModel);
            configureInvoiceTable(invoiceTable); // Configuração genérica
            JScrollPane scrollPane = new JScrollPane(invoiceTable);
            panel.add(scrollPane);
        } else if (items instanceof PurchaseDTO[]) {
            // Configura a tabela
            purchaseTable = new JTable(tableModel);
            configurePurchaseTable(purchaseTable); // Configuração genérica
            JScrollPane scrollPane = new JScrollPane(purchaseTable);
            panel.add(scrollPane);
        }

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
        if (items instanceof ProductDTO[]) {
            String[] columnNames = {"Selecionar", "Nome", "Código", "Estoque Total", "Estoque Mínimo", "Preço", "Unidade", "ID"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            for (ProductDTO productDTO : (ProductDTO[]) items) {
                tableModel.addRow(new Object[]{
                        false,
                        productDTO.getName(),
                        productDTO.getCodProduct(),
                        productDTO.getTotalStock(),
                        productDTO.getMinStock(),
                        String.format("R$ %.2f", productDTO.getPrice()),
                        productDTO.getSalesUnit(),
                        productDTO.getId()
                });
            }

            return tableModel;

        } else if (items instanceof ClientDTO[]) {
            String[] columnNames = {"Selecionar", "Nome", "CPF/CNPJ", "Email", "Telefone", "ID"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            for (ClientDTO clientDTO : (ClientDTO[]) items) {
                tableModel.addRow(new Object[]{
                        false,
                        clientDTO.getName(),
                        clientDTO.getCpfCnpj(),
                        clientDTO.getEmail(),
                        clientDTO.getPhoneNumber(),
                        clientDTO.getId()
                });
            }

            return tableModel;

        } else if (items instanceof InvoiceDTO[]) {
            String[] columnNames = {"Selecionar", "Cod. Nota Fiscal", "CPF/CNPJ", "Data da venda", "Total", "Status", "Itens", "ID"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 0 || column == 6; // Permite edição apenas na seleção e no botão
                }
            };

            for (InvoiceDTO invoiceDTO : (InvoiceDTO[]) items) {
                tableModel.addRow(new Object[]{
                        false,
                        invoiceDTO.getCodInvoice(),
                        invoiceDTO.getClient().getCpfCnpj(),
                        invoiceDTO.getSaleDate(),
                        String.format("R$ %.2f", invoiceDTO.getTotalPrice()),
                        invoiceDTO.getStatus(),
                        "Ver Itens",
                        invoiceDTO.getId()
                });
            }
            return tableModel;
        } else if (items instanceof PurchaseDTO[]) {
            String[] columnNames = {"Data da Compra", "Total", "Itens", "ID"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 0 || column == 2; // Permite edição apenas na seleção e no botão
                }
            };

            for (PurchaseDTO purchaseDTO : (PurchaseDTO[]) items) {
                tableModel.addRow(new Object[]{
                        purchaseDTO.getPurchaseDate(),
                        String.format("R$ %.2f", purchaseDTO.getTotalPrice()),
                        "Ver Itens",
                        purchaseDTO.getId()
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
        table.getColumnModel().getColumn(5).setMaxWidth(0);
        table.getColumnModel().getColumn(5).setMinWidth(0);
        table.getColumnModel().getColumn(5).setPreferredWidth(0);
    }

    private void configureInvoiceTable(JTable table) {
        // Torna a primeira coluna (de seleção) um JCheckBox
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox())); // Checkbox na primeira coluna
        table.getColumnModel().getColumn(0).setCellRenderer(new JCheckBoxRenderer()); // Renderer para exibir o JCheckBox

        // Definir o renderizador e o editor do botão na coluna "Itens"
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonAction(new JCheckBox(), table));

        // Ocultar a coluna "ID"
        table.getColumnModel().getColumn(7).setMaxWidth(0);
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setPreferredWidth(0);
    }

    private void configurePurchaseTable(JTable table) {
        // Definir o renderizador e o editor do botão na coluna "Itens"
        table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(2).setCellEditor(new ButtonAction(new JCheckBox(), table));

        // Ocultar a coluna "ID"
        table.getColumnModel().getColumn(3).setMaxWidth(0);
        table.getColumnModel().getColumn(3).setMinWidth(0);
        table.getColumnModel().getColumn(3).setPreferredWidth(0);
    }

    // Renderizador do botão
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Ver Itens");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Editor do botão
    public class ButtonAction extends DefaultCellEditor {
        private final JButton button;
        private int selectedRow;
        private final JTable table;

        public ButtonAction(JCheckBox checkBox, JTable table) {
            super(checkBox);
            this.table = table;
            button = new JButton("Ver Itens");
            button.addActionListener(e -> showPurchaseInvoiceProducts());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Ver Itens";
        }

        private void showPurchaseInvoiceProducts() {
            if(table.getModel().getColumnCount() == 4){
                Long selectedObject = (Long) table.getValueAt(selectedRow, 3);
                PurchaseDTO purchaseDTO = getPurchaseItemById(selectedObject);
                new DashboardItensModal().showItensModal(purchaseDTO.getPurchaseItens());

            } else if(table.getModel().getColumnCount() == 8){
                Long selectedObject = (Long) table.getValueAt(selectedRow, 7);
                InvoiceDTO invoiceDTO = getInvoiceById(selectedObject);
                new DashboardItensModal().showItensModal(invoiceDTO.getInvoiceItens());
            }
        }

    }

    static class JCheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setSelected(value != null && (Boolean) value); // Definir se o checkbox está selecionado ou não
            setEnabled(table.isEnabled()); // Define se o checkbox está habilitado
            return this; // Retorna o próprio JCheckBox
        }
    }

    public int getSelectedRows(JTable table) {
        int selectedRows = 0;

        // Itera sobre todas as linhas da tabela
        for (int i = 0; i < table.getRowCount(); i++) {
            // Obtém o valor do checkbox na primeira coluna
            Boolean isSelected = (Boolean) table.getValueAt(i, 0);
            if (isSelected != null && isSelected) {
                selectedRows++; // Adiciona a linha à lista se o checkbox estiver marcado
            }
        }
        return selectedRows;
    }

    public JTable getProductTable() {
        return productTable;
    }

    public JTable getClientTable() {
        return clientTable;
    }

    public JTable getInvoiceTable() {
        return invoiceTable;
    }

    private PurchaseDTO getPurchaseItemById(Long purchaseItemId) {
        // Criar uma instância de RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Chama a API para obter o produto
        String getUrl = API_URL_PURCHASE + "/" + purchaseItemId;
        return restTemplate.getForObject(getUrl, PurchaseDTO.class);
    }

    private InvoiceDTO getInvoiceById(Long invoiceItemId) {
        // Criar uma instância de RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Chama a API para obter o produto
        String getUrl = API_URL_INVOICE + "/" + invoiceItemId;
        return restTemplate.getForObject(getUrl, InvoiceDTO.class);
    }

}