package com.k11.testautomation.core_java_lessons.swing_examples;

import javax.swing.*;

public class JRadioButtonExample {
    JFrame f;

    public JRadioButtonExample() { // Constructor name should match the class name
        f = new JFrame("Radio Button Example");
        JRadioButton r1 = new JRadioButton("A) Male");
        JRadioButton r2 = new JRadioButton("B) Female");
        r1.setBounds(75, 50, 100, 30);
        r2.setBounds(75, 100, 100, 30);

        ButtonGroup bg = new ButtonGroup();
        bg.add(r1);
        bg.add(r2);

        f.add(r1);
        f.add(r2);

        f.setSize(300, 300);
        f.setLayout(null); // It's okay to use null layout, but generally not recommended
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensure the application closes when the window is closed
    }

    public static void main(String[] args) {
        new JRadioButtonExample(); // Corrected to match the class name
    }
}
