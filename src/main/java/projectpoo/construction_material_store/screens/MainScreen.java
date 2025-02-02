package projectpoo.construction_material_store.screens;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class MainScreen extends JFrame {

    private JPanel contentPanel; // Painel principal onde os frames serão exibidos

    public MainScreen() {
        setTitle("Tela Inicial");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel lateral à esquerda
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(200, 600));
        sidePanel.setBackground(Color.LIGHT_GRAY);
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margem interna

        // Criando um painel para os botões (mantendo-os no topo)
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setOpaque(false); // Fundo transparente para manter a cor do sidePanel

        // Criando botões
        JButton btnProduct = new JButton("Produtos");
        JButton btnClient = new JButton("Clientes");
        JButton btnSale = new JButton("Vendas");

        // Definir tamanho fixo dos botões
        Dimension buttonSize = new Dimension(140, 30);
        btnProduct.setMaximumSize(buttonSize);
        btnClient.setMaximumSize(buttonSize);
        btnSale.setMaximumSize(buttonSize);

        // Alinhar os botões ao centro horizontalmente (não afeta o alinhamento no eixo Y)
        btnProduct.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        btnClient.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        btnSale.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // Adicionando os botões ao painel de botões
        buttonContainer.add(btnProduct);
        buttonContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Espaço entre botões
        buttonContainer.add(btnClient);
        buttonContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonContainer.add(btnSale);

        // Adicionando o painel de botões no topo do sidePanel
        sidePanel.add(buttonContainer);

        // Painel principal
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        DashboardScreen dashboardScreen = new DashboardScreen();
        contentPanel.add(dashboardScreen, BorderLayout.CENTER);

        // Definir ações dos botões
        btnProduct.addActionListener(e -> openProductFrame(dashboardScreen));
        btnClient.addActionListener(e -> openClientFrame(dashboardScreen));
        btnSale.addActionListener(e -> openSalesFrame(dashboardScreen));

        // Adiciona os painéis ao frame principal
        add(sidePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }


    private void openProductFrame(DashboardScreen dashboardScreen) {
        // Abre a tela de produtos
        ProductFrame productFrame = new ProductFrame(this, dashboardScreen); // Passa a MainScreen como referência
        productFrame.setVisible(true);
        this.setVisible(false); // Apenas esconde a MainScreen
    }

    private void openClientFrame(DashboardScreen dashboardScreen) {
        // Abre a tela de clientes
        ClientFrame clientFrame = new ClientFrame(this, dashboardScreen); // Passa a MainScreen como referência
        clientFrame.setVisible(true);
        this.setVisible(false); // Apenas esconde a MainScreen
    }

    private void openSalesFrame(DashboardScreen dashboardScreen) {
        // Abre a tela de clientes
        SalesFrame salesFrame = new SalesFrame(this, dashboardScreen); // Passa a MainScreen como referência
        salesFrame.setVisible(true);
        this.setVisible(false); // Apenas esconde a MainScreen
    }

}
