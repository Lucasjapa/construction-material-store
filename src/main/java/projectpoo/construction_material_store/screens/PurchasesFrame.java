package projectpoo.construction_material_store.screens;

import org.springframework.web.client.RestTemplate;
import projectpoo.construction_material_store.dto.InvoiceDTO;
import projectpoo.construction_material_store.dto.PurchaseDTO;
import projectpoo.construction_material_store.screens.components.BackButton;
import projectpoo.construction_material_store.screens.components.SearchBar;
import projectpoo.construction_material_store.screens.components.TableComponent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PurchasesFrame extends JFrame {

    private static final String API_URL = "http://localhost:8080/purchases"; // URL da sua API
    private final MainScreen mainScreen;

    public PurchasesFrame(MainScreen mainScreen, DashboardScreen dashboardScreen) {
        this.mainScreen = mainScreen;
        setTitle("Compras");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configura o layout da tela
        setLayout(new BorderLayout());

        // JLabel com o título
        JLabel label = new JLabel("Lista de Compras");
        add(label, BorderLayout.NORTH);

        // Inicializa o painel e adiciona ao layout
        JPanel salesPanel = new JPanel();
        salesPanel.setLayout(new BoxLayout(salesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(salesPanel);// Alinha verticalmente os componentes

        // Cria a instância do TableComponent
        TableComponent tableComponent = new TableComponent(salesPanel);
        SearchBar searchBar = new SearchBar();

        // Adiciona o painel de busca acima da tabela
        JPanel searchPanel = searchBar.getSearchPanel(tableComponent,API_URL, API_URL + "/search-invoices/", InvoiceDTO[].class);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH); // Barra de busca acima
        centerPanel.add(scrollPane, BorderLayout.CENTER); // Tabela no centro
        add(centerPanel, BorderLayout.CENTER);

        // Adiciona o painel de botões ao layout no local apropriado (parte inferior)
        JPanel buttonPanel = getButtonPanel(tableComponent, dashboardScreen);
        add(buttonPanel, BorderLayout.SOUTH);

        try {
            tableComponent.loadData(API_URL, PurchaseDTO[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private JPanel getButtonPanel(TableComponent tableComponent, DashboardScreen dashboardScreen) {
        PurchaseModal purchaseModal = new PurchaseModal();

        // Painel principal de botões com BorderLayout
        JPanel buttonPanel = new JPanel(new BorderLayout());

        // Botão "Voltar"
        BackButton btnBack = new BackButton(this, mainScreen, dashboardScreen);

        // Painel para o botão "Voltar" no lado esquerdo
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(btnBack);

        // Painel para os outros botões no lado direito
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCreateProduct = new JButton("Nova Compra");
        btnCreateProduct.addActionListener(e -> purchaseModal.purchaseActionModal(tableComponent, null, dashboardScreen));
        rightPanel.add(btnCreateProduct);

        // Adiciona os painéis ao painel principal
        buttonPanel.add(leftPanel, BorderLayout.WEST);  // Botão "Voltar" à esquerda
        buttonPanel.add(rightPanel, BorderLayout.EAST); // Outros botões à direita

        return buttonPanel;
    }
}
