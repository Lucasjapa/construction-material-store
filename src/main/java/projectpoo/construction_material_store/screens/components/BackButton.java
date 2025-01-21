package projectpoo.construction_material_store.screens.components;

import javax.swing.*;
import java.awt.event.ActionListener;

public class BackButton extends JButton {

    public BackButton(JFrame currentFrame, JFrame mainScreen) {
        super("Voltar"); // Define o texto do botão

        // Adiciona ação ao botão
        addActionListener(e -> {
            currentFrame.dispose(); // Fecha a tela atual
            mainScreen.setVisible(true); // Torna a MainScreen visível novamente
        });
    }
}
