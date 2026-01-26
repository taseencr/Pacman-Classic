import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.net.URI;

public class CreditsPanel extends JPanel{
    public CreditsPanel(GameWindow window){
        setPreferredSize(new Dimension(608, 672));
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JEditorPane credits = new JEditorPane();
        credits.setContentType("text/html");
        credits.setEditable(false);
        credits.setBackground(Color.BLACK);
        credits.setForeground(Color.YELLOW);
        
        String htmlContent = "<html><body style='font-family: Verdana; font-size: 20px; color: #ffffff; background-color: #000000; padding: 40px;'>" +
                "<div style='text-align: center;'>" +
                "<h1 style='color: #FFFF00;'>PAC-MAN: Classic Arcade Game</h1><hr><br>" +
                "<p>Developed By:</p>" +
                "<p>ABRAR ABIR TASEEN</p><p>(Taseen CR)</p>" +
                "<p><a href='https://taseencr.vercel.app' style='color: #00C8FF; text-decoration: none;'>https://taseencr.vercel.app</a></p>" +
                "<p><a href='https://github.com/taseencr' style='color: #00C8FF; text-decoration: none;'>https://github.com/TaseenCR</a></p><br><br>" +
                "<p>Java Project</p>" +
                "<p>©2026</p>" +
                "</div></body></html>";
        
        credits.setText(htmlContent);
        credits.setMargin(new Insets(40, 40, 40, 40));
        
        credits.addHyperlinkListener(new HyperlinkListener(){
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e){
                if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
                    try{
                        Desktop.getDesktop().browse(new URI(e.getURL().toString()));
                    } catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> window.showScreen("HOME"));

        add(credits, BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);
    }
}
