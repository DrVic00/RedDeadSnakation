import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame {
    Frame(){
        this.setTitle("RedDeadSnakejszyn2");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);

        ImageIcon headIcon = new ImageIcon("sheriff.png");
        this.setIconImage(headIcon.getImage());

        this.getContentPane().setBackground(new Color(166, 20, 2));

        JLabel label = new JLabel("Red Dead Snakeption ");
        label.setFont(new Font("Serif", Font.BOLD, 36));
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.TOP);
        ImageIcon mainImg = new ImageIcon("sheriff.png");
        label.setIcon(mainImg);

        JButton button = new JButton("Play");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SnakeGame();
            }
        });

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(label, gbc);

        gbc.gridy = 1;
        this.add(button, gbc);

        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
