import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomePanel extends JPanel{
    public HomePanel(GameWindow window){
        setPreferredSize(new Dimension(608, 672));
        setBackground(new Color(0, 0, 0));
        setLayout(new BorderLayout());

        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(new Color(0, 0, 0));
        mainContainer.setBorder(new EmptyBorder(80, 50, 80, 50));
        mainContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("PAC-MAN");
        title.setFont(new Font("Verdana", Font.BOLD, 64));
        title.setForeground(new Color(255, 255, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Classic Arcade Game");
        subtitle.setFont(new Font("Verdana", Font.ITALIC, 18));
        subtitle.setForeground(new Color(200, 200, 200));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel versionLabel = new JLabel("v2.1");
        subtitle.setFont(new Font("Verdana", Font.ITALIC, 17));
        subtitle.setForeground(new Color(200, 200, 200));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(0, 0, 0));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));

        JButton playBtn = createStyledButton("PLAY GAME", new Color(255, 255, 0), new Color(200, 200, 0));
        JButton lbBtn = createStyledButton("LEADERBOARD", new Color(0, 200, 255), new Color(0, 150, 200));
        JButton creditsBtn = createStyledButton("CREDITS", new Color(200, 200, 200), new Color(150, 150, 150));

        playBtn.addActionListener(e -> window.showScreen("Play Game"));
        lbBtn.addActionListener(e -> window.showScreen("Leaderboard"));
        creditsBtn.addActionListener(e -> window.showScreen("Credits"));

        buttonPanel.add(playBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(lbBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(creditsBtn);

        mainContainer.add(title);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 55)));
        mainContainer.add(subtitle);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        mainContainer.add(versionLabel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 61)));
        mainContainer.add(buttonPanel);
        mainContainer.add(Box.createVerticalGlue());

        add(mainContainer, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor){
        JButton button = new JButton(text);
        button.setFont(new Font("Verdana", Font.BOLD, 20));
        button.setForeground(Color.BLACK);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 150), 3),
            BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }
}
