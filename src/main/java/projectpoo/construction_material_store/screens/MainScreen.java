package projectpoo.construction_material_store.screens;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class MainScreen extends JFrame {

    public MainScreen() {
        setTitle("Tela Inicial");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnProduct = new JButton("Ir para Produtos");
        JButton btnClient = new JButton("Ir para Clientes");

        // Definir layout para o painel (opcional, mas recomendado para controle do layout)
        JPanel panel = new JPanel();

        // Adicionar os botões ao painel
        panel.add(btnProduct);
        panel.add(btnClient);

        btnProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProductFrame();
            }
        });

        btnClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openClientFrame();
            }
        });

        add(panel);
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

}


//// Na tela de Produtos
//loadData("https://api.example.com/products", Product[].class);
//
//// Na tela de Clientes
//loadData("https://api.example.com/clients", Client[].class);
//
//// Na tela de Vendas
//loadData("https://api.example.com/sales", Sale[].class);