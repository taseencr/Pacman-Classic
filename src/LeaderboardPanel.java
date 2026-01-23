import javax.swing.*;
import java.awt.*;;

public class LeaderboardPanel extends JPanel{
    public LeaderboardPanel(GameWindow window, Leaderboard leaderBoard){
        setPreferredSize(new Dimension(608, 672));
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("LEADERBOARD", JLabel.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JTextArea scoresArea = new JTextArea();
        scoresArea.setEditable(false);
        scoresArea.setBackground(Color.BLACK);
        scoresArea.setForeground(Color.YELLOW);
        scoresArea.setFont(new Font("Monospaced", Font.PLAIN, 18));

        StringBuilder sb = new StringBuilder();
        HighScore[] scores = leaderBoard.getHighScores();
        for (int i = 0; i < scores.length; i++){
            if (scores[i] != null) {
                sb.append(" " + (i + 1) + ". " + scores[i].name + " - " + scores[i].score + "\n");
            }
        }
        scoresArea.setText(sb.toString());

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> window.showScreen("HOME"));

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(scoresArea), BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);
    }
}
