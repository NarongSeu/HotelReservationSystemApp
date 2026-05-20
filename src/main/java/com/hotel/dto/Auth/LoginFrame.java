package com.hotel.dto.Auth;

import com.hotel.dto.MainDashboard;
import com.hotel.dto.components.GradientPanel;
import com.hotel.model.User;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {

    private static final Dimension INPUT_SIZE  = new Dimension(340, 48);
    private static final Dimension BUTTON_SIZE = new Dimension(340, 50);

    private static final Color BRAND_DEEP      = new Color(15,  23,  42);
    private static final Color BRAND_MID       = new Color(30,  41,  59);
    private static final Color BRAND_ACCENT    = new Color(99, 102, 241);
    private static final Color BRAND_ACCENT_HV = new Color(79,  70, 229);
    private static final Color SURFACE         = new Color(248, 250, 252);
    private static final Color BORDER_DEFAULT  = new Color(203, 213, 225);
    private static final Color BORDER_FOCUS    = new Color(99,  102, 241);
    private static final Color BORDER_ERROR    = new Color(239,  68,  68);
    private static final Color TEXT_MAIN       = new Color(15,  23,  42);
    private static final Color TEXT_MUTED      = new Color(100, 116, 139);
    private static final Color TEXT_PLACEHOLDER= new Color(148, 163, 184);

    private final AuthService authService;
    private PlaceholderTextField     emailField;
    private PlaceholderPasswordField passwordField;
    private JButton       loginButton;
    private JLabel        statusLabel;
    private JToggleButton showPassBtn;

    public LoginFrame() {
        this.authService = new AuthService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Hotel Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 500));

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.add(createBrandingPanel());
        root.add(createFormPanel());
        add(root);

        assert passwordField != null;
        passwordField.addActionListener(e -> handleLogin());
        assert emailField != null;
        emailField.addActionListener(e -> passwordField.requestFocusInWindow());
    }

    private JPanel createBrandingPanel() {
        GradientPanel panel = new GradientPanel(BRAND_DEEP, BRAND_MID);
        panel.setLayout(new GridBagLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel headline = new JLabel("<html>Hotel Reservation<br>Management System</html>");
        headline.setForeground(Color.WHITE);
        headline.setFont(new Font("Serif", Font.BOLD, 42));
        headline.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Reservations · Rooms · Guests · Billing");
        sub.setForeground(new Color(100, 116, 139));
        sub.setFont(new Font("Dialog", Font.PLAIN, 15));
        sub.setAlignmentX(LEFT_ALIGNMENT);
        sub.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        content.add(headline);
        content.add(sub);

        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(0, 64, 0, 64);
        panel.add(content, g);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(SURFACE);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                new RoundLineBorder(BORDER_DEFAULT, 1, 20),
                BorderFactory.createEmptyBorder(44, 44, 40, 44)
        ));
        form.setPreferredSize(new Dimension(420, 440));

        GridBagConstraints g = new GridBagConstraints();
        g.fill    = GridBagConstraints.HORIZONTAL;
        g.gridx   = 0;
        g.weightx = 1.0;

        // Heading
        JLabel title = new JLabel("Sign in");
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(TEXT_MAIN);
        g.gridy = 0; g.insets = new Insets(0, 0, 4, 0);
        form.add(title, g);

        JLabel sub = new JLabel("Admin access only");
        sub.setFont(new Font("Dialog", Font.PLAIN, 14));
        sub.setForeground(TEXT_MUTED);
        g.gridy = 1; g.insets = new Insets(0, 0, 36, 0);
        form.add(sub, g);

        // Email
        form.add(fieldLabel("Email"), row(g, 2, 0, 6));
        emailField = new PlaceholderTextField("admin@hotel.com");
        emailField.setPreferredSize(INPUT_SIZE);
        form.add(emailField, row(g, 3, 0, 14));

        // Password + show toggle
        form.add(fieldLabel("Password"), row(g, 4, 0, 6));

        JPanel passRow = new JPanel(new BorderLayout());
        passRow.setOpaque(false);
        passwordField = new PlaceholderPasswordField("Enter password");
        passRow.add(passwordField, BorderLayout.CENTER);

        showPassBtn = new JToggleButton("Show");
        styleToggleBtn(showPassBtn);
        showPassBtn.addItemListener(e -> {
            boolean show = showPassBtn.isSelected();
            passwordField.setEchoChar(show ? (char) 0 : '•');
            showPassBtn.setText(show ? "Hide" : "Show");
        });
        passRow.add(showPassBtn, BorderLayout.EAST);
        passRow.setPreferredSize(INPUT_SIZE);
        form.add(passRow, row(g, 5, 0, 32));

        // Button
        loginButton = new GradientButton("Sign In", BRAND_ACCENT, BRAND_ACCENT_HV);
        loginButton.setPreferredSize(BUTTON_SIZE);
        loginButton.addActionListener(e -> handleLogin());
        g.gridy = 6; g.insets = new Insets(0, 0, 14, 0);
        form.add(loginButton, g);

        // Status
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
        statusLabel.setForeground(TEXT_MUTED);
        g.gridy = 7; g.insets = new Insets(0, 0, 0, 0);
        form.add(statusLabel, g);

        wrapper.add(form);
        return wrapper;
    }

    private void handleLogin() {
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            showStatus("Please enter both email and password.", BORDER_ERROR);
            shakeComponent(loginButton);
            return;
        }

        loginButton.setEnabled(false);
        showStatus("Authenticating…", BRAND_ACCENT);

        new Thread(() -> {
            try {
                User user = authService.login(email, password);
                if (!user.isAdmin()) {
                    authService.logout();
                    throw new Exception("Access denied — admin role required.");
                }
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    new MainDashboard(user);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    showStatus(ex.getMessage(), BORDER_ERROR);
                    shakeComponent(loginButton);
                    loginButton.setEnabled(true);
                });
            }
        }).start();
    }

    private void showStatus(String msg, Color color) {
        statusLabel.setText(msg);
        statusLabel.setForeground(color);
    }

    private void shakeComponent(JComponent comp) {
        Point origin = comp.getLocation();
        int[] offsets = {6, -6, 5, -5, 3, -3, 1, 0};
        int[] tick = {0};
        Timer t = new Timer(40, null);
        t.addActionListener(e -> {
            if (tick[0] < offsets.length) {
                comp.setLocation(origin.x + offsets[tick[0]++], origin.y);
            } else {
                comp.setLocation(origin);
                t.stop();
            }
        });
        t.start();
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Dialog", Font.BOLD, 13));
        l.setForeground(TEXT_MAIN);
        return l;
    }

    private GridBagConstraints row(GridBagConstraints g, int y, int top, int bottom) {
        g.gridy  = y;
        g.insets = new Insets(top, 0, bottom, 0);
        return g;
    }

    private void styleToggleBtn(JToggleButton btn) {
        btn.setFont(new Font("Dialog", Font.BOLD, 11));
        btn.setForeground(BRAND_ACCENT);
        btn.setBackground(new Color(238, 242, 255));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(52, 48));
    }


    private static class PlaceholderTextField extends JTextField {
        private final String ph;
        PlaceholderTextField(String ph) {
            this.ph = ph;
            applyStyle(this);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !hasFocus()) drawPlaceholder(g, ph, this);
        }
    }

    private static class PlaceholderPasswordField extends JPasswordField {
        private final String ph;
        PlaceholderPasswordField(String ph) {
            this.ph = ph;
            setEchoChar('•');
            applyStyle(this);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getPassword().length == 0 && !hasFocus()) drawPlaceholder(g, ph, this);
        }
    }

    private static void applyStyle(JTextField f) {
        f.setFont(new Font("Dialog", Font.PLAIN, 14));
        f.setForeground(TEXT_MAIN);
        f.setBackground(Color.WHITE);
        f.setCaretColor(BRAND_ACCENT);
        f.setSelectionColor(new Color(199, 210, 254));
        f.setBorder(new FocusableBorder(BORDER_DEFAULT, BORDER_FOCUS, 10));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { f.repaint(); }
            public void focusLost (FocusEvent e) { f.repaint(); }
        });
    }

    private static void drawPlaceholder(Graphics g, String text, JComponent c) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(TEXT_PLACEHOLDER);
        g2.setFont(new Font("Dialog", Font.ITALIC, 14));
        Insets ins = c.getInsets();
        FontMetrics fm = g2.getFontMetrics();
        int y = (c.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, ins.left + 2, y);
        g2.dispose();
    }

    private static class GradientButton extends JButton {
        private final Color c1, c2;
        private boolean hovered;
        GradientButton(String label, Color c1, Color c2) {
            super(label);
            this.c1 = c1; this.c2 = c2;
            setFont(new Font("Dialog", Font.BOLD, 15));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
            });
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color a = hovered ? c2 : c1, b = hovered ? c1 : c2.darker();
            g2.setPaint(new GradientPaint(0, 0, a, getWidth(), getHeight(), b));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class FocusableBorder extends AbstractBorder {
        private final Color normal, focused;
        private final int arc;
        FocusableBorder(Color normal, Color focused, int arc) {
            this.normal = normal; this.focused = focused; this.arc = arc;
        }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            boolean f = c.hasFocus();
            if (f) {
                g2.setColor(new Color(99, 102, 241, 40));
                g2.setStroke(new BasicStroke(3));
                g2.draw(new RoundRectangle2D.Float(x - 1, y - 1, w + 1, h + 1, arc + 2, arc + 2));
            }
            g2.setColor(f ? focused : normal);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(x + 1, y + 1, w - 2, h - 2, arc, arc));
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(11, 14, 11, 14); }
    }

    private static class RoundLineBorder extends AbstractBorder {
        private final Color color;
        private final int thickness, arc;
        RoundLineBorder(Color color, int thickness, int arc) {
            this.color = color; this.thickness = thickness; this.arc = arc;
        }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Float(x + .5f, y + .5f, w - 1, h - 1, arc, arc));
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(thickness, thickness, thickness, thickness); }
    }
}
