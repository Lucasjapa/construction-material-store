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
        btnProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProductFrame();
            }
        });

        add(btnProduct);
    }

    private void openProductFrame() {
        // Abre a tela de produto
        ProductFrame productFrame = new ProductFrame();
        productFrame.setVisible(true);
        this.setVisible(false); // Fecha a tela inicial
    }
}
