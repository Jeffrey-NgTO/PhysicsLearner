import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by ngj9136 on 12/12/2017.
 */
public class TwoDMotionPanel extends JPanel {

    //variables
    private Mechanics mechanics = new Mechanics();

    //fonts
    private Font titleFont = new Font("TimesRoman", Font.BOLD, 100);
    private Font fieldFont = new Font("TimesRoman", Font.BOLD, 30);
    private Font descriptionFont = new Font("TimesRoman", Font.BOLD, 15);
    private Font timeFont = new Font("TimesRoman", Font.BOLD, 60);
    private KinematicsBall kinematicsBall = new KinematicsBall(5, 30, 300, 950);

    //colour
    private Color grey = new Color(50, 50, 50);

    //boolean and numbers
    private boolean projectileLaunched = false;
    private double kinematicsBallYVelocity = 0;
    private double time;
    private String roundedTime = "";

    //labels
    private JLabel timeLabel = new JLabel("Time: " + roundedTime);

    //decimal format
    private DecimalFormat df = new DecimalFormat("#.#");

    //lists
    private ArrayList<Integer> xCoords = new ArrayList<>();
    private ArrayList<Integer> yCoords = new ArrayList<>();

    //constructor
    public TwoDMotionPanel() {
        //set up the panel with buttons
        setLayout(null);
        setFocusable(true);
        add(new ExitButton().getExitButton());
        add(addReturnButton());
        add(addResetButton());
        add(addLaunchButton());
        add(addPlusXButton());
        add(addPlusYButton());
        add(addMinusXButton());
        add(addMinusYButton());
        add(addDescription());
        addInstructionsButton();
        addTimeLabel(timeLabel);

        //setup kinematics ball
        kinematicsBall.setvY(0);
        kinematicsBall.setvX(0);

        //timers
        java.util.Timer timer = new java.util.Timer();
        java.util.Timer paintLineTimer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (projectileLaunched) {
                    if (kinematicsBall.getxCoordinate() < 900 && kinematicsBall.getyCoordinate() > 350) {
                        time += 0.03;
                        roundedTime = df.format(time);

                        timeLabel.setText("Time: " + roundedTime + " seconds");
                        timeLabel.repaint();

                        kinematicsBall.setxCoordinate(kinematicsBall.getxCoordinate() + kinematicsBall.getvX());
                        kinematicsBall.setyCoordinate(kinematicsBall.getyCoordinate() - kinematicsBall.getvY());

                        repaint();
                    } else {
                        kinematicsBall.setvX(0);
                        kinematicsBall.setvY(0);
                        projectileLaunched = false;
                    }
                }
                repaint();
            }
        }, 1, 30);

        //Add the dots after the ball
        paintLineTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (projectileLaunched) {
                    xCoords.add((int) kinematicsBall.getxCoordinate() + 20);
                    yCoords.add((int) kinematicsBall.getyCoordinate() + 20);
                }
            }
        }, 2000, 300);

    }

    //add instructions button
    public void addInstructionsButton(){
        CustomButton instructionsButton = new CustomButton("?");
        instructionsButton.setFont(timeFont);
        instructionsButton.setBounds(20, 150, 100, 100);
        instructionsButton.setBackground(Color.gray);
        instructionsButton.setNormalColor(Color.gray);
        instructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InstructionsFrame(2);
            }
        });
        add(instructionsButton);
    }

    //add button to reset demonstration
    public CustomButton addResetButton() {
        CustomButton resetButton = new CustomButton("RESET");
        resetButton.setFont(fieldFont);
        resetButton.setBounds(1200, 500, 200, 40);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xCoords.clear();
                yCoords.clear();
                kinematicsBall.setxCoordinate(300);
                kinematicsBall.setyCoordinate(950);
                kinematicsBall.setvX(0);
                kinematicsBall.setvY(0);
                time = 0;
                timeLabel.setText("Time: ");
                projectileLaunched = false;
                repaint();
            }
        });
        return resetButton;
    }

    //add button to control launcher
    public CustomButton addLaunchButton() {
        CustomButton launchButton = new CustomButton("LAUNCH");
        launchButton.setFont(fieldFont);
        launchButton.setBounds(1200, 560, 200, 40);
        launchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                projectileLaunched = true;
                repaint();
            }
        });
        return launchButton;
    }

    //add to the x velocity
    public CustomButton addPlusXButton() {
        CustomButton plusButton = new CustomButton("+ Vx");
        plusButton.setFont(fieldFont);
        plusButton.setBounds(1200, 620, 200, 40);
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!projectileLaunched) {
                    kinematicsBall.setvX(kinematicsBall.getvX() + 1);
                }
            }
        });
        return plusButton;
    }

    //add to the y velocity button
    public CustomButton addPlusYButton() {
        CustomButton plusButton = new CustomButton("+ Vy");
        plusButton.setFont(fieldFont);
        plusButton.setBounds(1200, 680, 200, 40);
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!projectileLaunched) {
                    kinematicsBall.setvY(kinematicsBall.getvY() + 1);
                    repaint();
                }

            }
        });
        return plusButton;

    }

    //minus from x velocity button
    public CustomButton addMinusXButton() {
        CustomButton minusButton = new CustomButton("- Vx");
        minusButton.setFont(fieldFont);
        minusButton.setBounds(1200, 740, 200, 40);
        minusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!projectileLaunched) {
                    if (kinematicsBall.getvX() >= 1) {
                        kinematicsBall.setvX(kinematicsBall.getvX() - 1);
                        repaint();
                    }
                }

            }
        });
        return minusButton;

    }

    //minus from y velocity button
    public CustomButton addMinusYButton() {
        CustomButton minusButton = new CustomButton("- Vy");
        minusButton.setFont(fieldFont);
        minusButton.setBounds(1200, 800, 200, 40);
        minusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!projectileLaunched) {
                    if (kinematicsBallYVelocity >= 1) {
                        kinematicsBallYVelocity -= 1;
                        repaint();
                    }
                }
            }
        });
        return minusButton;

    }

    //return to the original screen button
    public CustomButton addReturnButton() {
        CustomButton returnButton = new CustomButton("GO BACK");
        returnButton.setFont(fieldFont);
        returnButton.setBounds(20, 80, 200, 40);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xCoords.clear();
                yCoords.clear();

                Mechanics.frame.remove(Mechanics.twoDMotionPanel);
                Mechanics.frame.repaint();
                Mechanics.frame.add(Mechanics.tutorialMenu);
                Mechanics.frame.repaint();
                Mechanics.frame.setVisible(true);

                kinematicsBall.setvX(0);
                kinematicsBall.setvY(0);
                time = 0;
                timeLabel.setText("Time: ");

                kinematicsBall.setxCoordinate(300);
                kinematicsBall.setyCoordinate(950);
                projectileLaunched = false;
            }
        });
        return returnButton;
    }

    //add a description of the topic
    public JTextArea addDescription() {
        JTextArea description = new JTextArea("2D Motion occurs when an object or particle has a velocity in both the" +
                "x-direction and the y-direction");
        description.setForeground(Color.WHITE);
        description.setFont(descriptionFont);
        description.setBounds(1100, 370, 400, 100);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setOpaque(false);
        description.setEditable(false);
        return description;
    }

    //add time label method
    public void addTimeLabel(JLabel label) {
        label.setFont(timeFont);
        label.setBounds(1000, 130, 900, 100);
        label.setForeground(Color.white);
        add(label);
    }

    //paint method
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, 1920, 1280);

        //draw the background
        g.setColor(grey);
        g.fillRect(300, 350, 650, 650);
        g.fillRect(300, 130, 1300, 120);

        g.setColor(Color.white);
        g.fillRect(1190, 490, 220, 360);
        g.drawRect(300, 130, 1300, 120);

        //draw the box
        g.drawLine(300, 1000, 950, 1000);
        g.drawLine(300, 1000, 300, 350);
        g.drawLine(300, 350, 950, 350);
        g.drawLine(950, 350, 950, 1000);

        //draw grid
        g.setColor(Color.gray);
        for(int i = 350; i<950; i+=50){
            g.drawLine(i, 350, i, 1000);
        }
        for (int i = 400; i< 1000; i+=50){
            g.drawLine(300, i, 950, i);
        }
        g.setColor(Color.white);

        //draw the dots
        for (int i = 0; i < xCoords.size(); i++) {
            g.fillOval(xCoords.get(i), yCoords.get(i), 10, 10);
        }

        //draw the labels
        g.setFont(titleFont);
        g.drawString("2D MOTION", (int) (mechanics.getScreenWidth() / 3.2), 100);

        g.setFont(fieldFont);
        g.drawString("Velocity (X) = " + String.valueOf(kinematicsBall.getvX()), 350, 175);
        g.drawString("Velocity (Y) = " + String.valueOf(kinematicsBall.getvY()), 350, 225);

        g.drawString("Position (X) = " + String.valueOf((kinematicsBall.getxCoordinate() - 300) * 3 / 100) + " m", 650, 175);
        g.drawString("Position (Y) = " + String.valueOf((-kinematicsBall.getyCoordinate() + 950) * 3 / 100) + " m", 650, 225);

        g.drawString("18m", 990, 650);
        g.drawString("18m", 600, 320);

        //draw ball
        g.fillOval((int) kinematicsBall.getxCoordinate(), (int) kinematicsBall.getyCoordinate(), 50, 50);
    }
}