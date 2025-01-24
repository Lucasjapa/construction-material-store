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
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); // Define layout principal

        // Barra lateral à esquerda
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS)); // Botões organizados verticalmente
        sidePanel.setPreferredSize(new Dimension(200, getHeight()));
        sidePanel.setBackground(Color.LIGHT_GRAY);

        // Botões na barra lateral
        JButton btnProduct = new JButton("Produtos");
        JButton btnClient = new JButton("Clientes");
        JButton btnSale = new JButton("Vendas");

        // Adiciona botões à barra lateral
        sidePanel.add(btnProduct);
        sidePanel.add(Box.createVerticalStrut(10)); // Espaçamento entre os botões
        sidePanel.add(btnClient);
        sidePanel.add(btnSale);

        // Painel principal onde os dados serão exibidos
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout()); // Flexibilidade para diferentes conteúdos
        contentPanel.setBackground(Color.WHITE);

        // Define ações dos botões
        btnProduct.addActionListener(e -> openProductFrame());
        btnClient.addActionListener(e -> openClientFrame());
        btnSale.addActionListener(e -> openSalesFrame());

        // Adiciona os painéis ao frame principal
        add(sidePanel, BorderLayout.WEST); // Barra lateral à esquerda
        add(contentPanel, BorderLayout.CENTER); // Área de exibição central
    }

    private void openProductFrame() {
        // Abre a tela de produtos
        ProductFrame productFrame = new ProductFrame(this); // Passa a MainScreen como referência
        productFrame.setVisible(true);
        this.setVisible(false); // Apenas esconde a MainScreen
    }

    private void openClientFrame() {
        // Abre a tela de clientes
        ClientFrame clientFrame = new ClientFrame(this); // Passa a MainScreen como referência
        clientFrame.setVisible(true);
        this.setVisible(false); // Apenas esconde a MainScreen
    }

    private void openSalesFrame() {
        // Abre a tela de clientes
        SalesFrame salesFrame = new SalesFrame(this); // Passa a MainScreen como referência
        salesFrame.setVisible(true);
        this.setVisible(false); // Apenas esconde a MainScreen
    }

}
