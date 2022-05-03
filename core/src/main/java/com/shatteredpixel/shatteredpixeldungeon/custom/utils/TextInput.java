package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.badlogic.gdx.Input;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TextInput implements Input.TextInputListener {
    @Override
    public void input (String text) {

    }

    @Override
    public void canceled () {

    }

    public static void getTextInput (final Input.TextInputListener listener, final String title, final String text, final String hint) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                final JPanel panel = new JPanel(new FlowLayout());

                JPanel textPanel = new JPanel() {
                    public boolean isOptimizedDrawingEnabled () {
                        return false;
                    };
                };

                textPanel.setLayout(new OverlayLayout(textPanel));
                panel.add(textPanel);

                final JTextField textField = new JTextField(20);
                textField.setText(text);
                textField.setAlignmentX(0.0f);
                textPanel.add(textField);

                final JLabel placeholderLabel = new JLabel(hint);
                placeholderLabel.setForeground(Color.GRAY);
                placeholderLabel.setAlignmentX(0.0f);
                textPanel.add(placeholderLabel, 0);

                textField.getDocument().addDocumentListener(new DocumentListener() {

                    @Override
                    public void removeUpdate (DocumentEvent arg0) {
                        this.updated();
                    }

                    @Override
                    public void insertUpdate (DocumentEvent arg0) {
                        this.updated();
                    }

                    @Override
                    public void changedUpdate (DocumentEvent arg0) {
                        this.updated();
                    }

                    private void updated () {
                        if (textField.getText().length() == 0)
                            placeholderLabel.setVisible(true);
                        else
                            placeholderLabel.setVisible(false);
                    }
                });

                final JOptionPane pane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, null,
                        null);

                pane.setInitialValue(null);
                pane.setComponentOrientation(JOptionPane.getRootFrame().getComponentOrientation());

                Border border = textField.getBorder();
                placeholderLabel.setBorder(new EmptyBorder(border.getBorderInsets(textField)));


                final JDialog dialog = pane.createDialog(null, title);
                panel.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {}

                    @Override
                    public void mousePressed(MouseEvent e) {
                        Point mousePos = MouseInfo.getPointerInfo().getLocation();
                        Rectangle bounds = panel.getBounds();
                        bounds.setLocation(panel.getLocationOnScreen());
                        if(bounds.contains(mousePos)) {
                            listener.canceled();
                            dialog.dispose();
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {}

                    @Override
                    public void mouseEntered(MouseEvent e) {}

                    @Override
                    public void mouseExited(MouseEvent e) {}
                });
                pane.selectInitialValue();

                dialog.addWindowFocusListener(new WindowFocusListener() {

                    @Override
                    public void windowLostFocus (WindowEvent arg0) {
                        listener.canceled();
                        dialog.dispose();
                    }

                    @Override
                    public void windowGainedFocus (WindowEvent arg0) {
                        textField.requestFocusInWindow();
                    }

                });

                dialog.setAlwaysOnTop(true);
                dialog.toFront();
                for(int i=0; i<10;i++) {
                    dialog.requestFocus();
                }
                dialog.setVisible(true);
                dialog.dispose();

                final Object selectedValue = pane.getValue();

                //update the text needs time, so switch to another thread
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        if (selectedValue != null && (selectedValue instanceof Integer)
                                && ((Integer)selectedValue).intValue() == JOptionPane.OK_OPTION) {
                            listener.input(textField.getText());
                        } else {
                            listener.canceled();
                        }
                    }
                });
            }
        });


    }
}
