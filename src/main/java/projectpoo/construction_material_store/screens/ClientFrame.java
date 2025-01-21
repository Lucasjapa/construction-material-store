package projectpoo.construction_material_store.screens;

import projectpoo.construction_material_store.domain.Product;
import projectpoo.construction_material_store.dto.ProductDTO;
import projectpoo.construction_material_store.screens.components.BackButton;
import projectpoo.construction_material_store.screens.components.TableComponent;

import javax.swing.*;
import java.awt.*;

public class ClientFrame extends JFrame {

    private static final String API_URL = "http://localhost:8080/products"; // URL da sua API
    private final JPanel productPanel;
    private final MainScreen mainScreen;
    private JTable productTable;

    public ClientFrame(MainScreen mainScreen) {
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
        productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS)); // Alinha verticalmente os componentes
        add(new JScrollPane(productPanel), BorderLayout.CENTER);

        // Cria a instância do TableComponent
        TableComponent tableComponent = new TableComponent(productPanel);

        try {
            tableComponent.loadData(API_URL, ProductDTO[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Painel para os botões
        add(getButtonPanel(), BorderLayout.SOUTH);

    }

    private JPanel getButtonPanel() {
        // Painel principal de botões
        JPanel buttonPanel = new JPanel(new BorderLayout());

        // Adiciona o botão "Voltar"
        BackButton btnBack = new BackButton(this, mainScreen);
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(btnBack);

        // Adiciona ao painel principal
        buttonPanel.add(leftPanel, BorderLayout.WEST);

        return buttonPanel;
    }
}
