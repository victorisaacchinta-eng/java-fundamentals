
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

    public class CoolDashboard extends JFrame {

        // Palette
        private static final Color BG_DARK      = new Color(24, 26, 38);
        private static final Color SIDEBAR_TOP  = new Color(35, 39, 61);
        private static final Color SIDEBAR_BOT  = new Color(20, 22, 34);
        private static final Color ACCENT       = new Color(94, 129, 244);
        private static final Color ACCENT_2     = new Color(107, 227, 194);
        private static final Color CARD_BG      = new Color(32, 35, 52);
        private static final Color TEXT_LIGHT   = new Color(230, 232, 240);
        private static final Color TEXT_MUTED   = new Color(148, 152, 175);

        private final JLabel clockLabel = new JLabel();
        private final ChartPanel chartPanel = new ChartPanel();

        public CoolDashboard() {
            setTitle("CoolDashboard \u2014 Java Swing Demo");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1000, 640);
            setMinimumSize(new Dimension(780, 520));
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());
            getContentPane().setBackground(BG_DARK);

            add(buildSidebar(), BorderLayout.WEST);

            JPanel main = new JPanel(new BorderLayout());
            main.setOpaque(false);
            main.add(buildHeader(), BorderLayout.NORTH);
            main.add(buildBody(), BorderLayout.CENTER);
            add(main, BorderLayout.CENTER);

            // Live clock ticking every second
            Timer clock = new Timer(1000, e -> updateClock());
            clock.start();
            updateClock();

            // Chart data refresh every 900ms for a "live" feel
            Timer chartTick = new Timer(900, e -> chartPanel.pushRandomValue());
            chartTick.start();
        }

        private void updateClock() {
            clockLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }

        // ---------------- Sidebar ----------------
        private JComponent buildSidebar() {
            JPanel sidebar = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    GradientPaint gp = new GradientPaint(0, 0, SIDEBAR_TOP, 0, getHeight(), SIDEBAR_BOT);
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
            };
            sidebar.setPreferredSize(new Dimension(220, 0));
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));

            JLabel logo = new JLabel("\u2728 CoolDash");
            logo.setFont(new Font("SansSerif", Font.BOLD, 22));
            logo.setForeground(TEXT_LIGHT);
            logo.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebar.add(logo);
            sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

            String[] items = { "\u2302  Dashboard", "\uD83D\uDCC8  Analytics", "\uD83D\uDC65  Team",
                    "\u2699  Settings", "\u2708  Messages" };
            for (int i = 0; i < items.length; i++) {
                NavButton b = new NavButton(items[i], i == 0);
                b.setAlignmentX(Component.LEFT_ALIGNMENT);
                sidebar.add(b);
                sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            sidebar.add(Box.createVerticalGlue());

            RoundedButton logout = new RoundedButton("Log Out", ACCENT_2, BG_DARK);
            logout.setAlignmentX(Component.LEFT_ALIGNMENT);
            logout.setMaximumSize(new Dimension(188, 42));
            sidebar.add(logout);

            return sidebar;
        }

        // ---------------- Header ----------------
        private JComponent buildHeader() {
            JPanel header = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    GradientPaint gp = new GradientPaint(0, 0, new Color(45, 49, 74), getWidth(), 0, BG_DARK);
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
            };
            header.setOpaque(false);
            header.setPreferredSize(new Dimension(0, 70));
            header.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));

            JLabel title = new JLabel("Welcome back, Victor \uD83D\uDC4B");
            title.setFont(new Font("SansSerif", Font.BOLD, 20));
            title.setForeground(TEXT_LIGHT);
            header.add(title, BorderLayout.WEST);

            clockLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
            clockLabel.setForeground(ACCENT_2);
            header.add(clockLabel, BorderLayout.EAST);

            return header;
        }

        // ---------------- Body ----------------
        private JComponent buildBody() {
            JPanel body = new JPanel(new BorderLayout());
            body.setOpaque(false);
            body.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

            // Stat cards row
            JPanel cards = new JPanel(new GridLayout(1, 3, 18, 0));
            cards.setOpaque(false);
            cards.add(new StatCard("Active Users", "1,284", "+12.4%", ACCENT));
            cards.add(new StatCard("Revenue", "$8,921", "+4.1%", ACCENT_2));
            cards.add(new StatCard("Server Load", "42%", "-2.3%", new Color(240, 130, 130)));
            cards.setPreferredSize(new Dimension(0, 130));

            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);
            top.add(cards, BorderLayout.CENTER);
            top.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

            body.add(top, BorderLayout.NORTH);
            body.add(chartPanel, BorderLayout.CENTER);

            return body;
        }

        // ---------------- Custom Components ----------------

        /** Sidebar navigation button with hover + selected highlighting. */
        static class NavButton extends JButton {
            private boolean selected;
            private boolean hover;

            NavButton(String text, boolean selected) {
                super(text);
                this.selected = selected;
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setForeground(selected ? Color.WHITE : TEXT_MUTED);
                setFont(new Font("SansSerif", Font.PLAIN, 15));
                setHorizontalAlignment(SwingConstants.LEFT);
                setMaximumSize(new Dimension(188, 42));
                setPreferredSize(new Dimension(188, 42));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
                    @Override public void mouseClicked(MouseEvent e) {
                        Container parent = getParent();
                        for (Component c : parent.getComponents()) {
                            if (c instanceof NavButton) ((NavButton) c).setSelectedState(false);
                        }
                        setSelectedState(true);
                    }
                });
            }

            void setSelectedState(boolean s) {
                selected = s;
                setForeground(s ? Color.WHITE : TEXT_MUTED);
                repaint();
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (selected) {
                    g2.setColor(ACCENT);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                } else if (hover) {
                    g2.setColor(new Color(255, 255, 255, 20));
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        }

        /** A generic rounded, filled button. */
        static class RoundedButton extends JButton {
            private final Color fill;

            RoundedButton(String text, Color fill, Color textColor) {
                super(text);
                this.fill = fill;
                setForeground(textColor);
                setFont(new Font("SansSerif", Font.BOLD, 14));
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fill);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
                super.paintComponent(g);
            }
        }

        /** A soft "card" showing a stat, with a subtle shadow and accent bar. */
        static class StatCard extends JPanel {
            StatCard(String label, String value, String delta, Color accent) {
                setOpaque(false);
                setLayout(new GridBagLayout());
                JPanel inner = new JPanel();
                inner.setOpaque(false);
                inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
                inner.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

                JLabel lbl = new JLabel(label);
                lbl.setForeground(TEXT_MUTED);
                lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));

                JLabel val = new JLabel(value);
                val.setForeground(TEXT_LIGHT);
                val.setFont(new Font("SansSerif", Font.BOLD, 26));

                JLabel dl = new JLabel(delta + " this week");
                dl.setForeground(accent);
                dl.setFont(new Font("SansSerif", Font.PLAIN, 12));

                inner.add(lbl);
                inner.add(Box.createRigidArea(new Dimension(0, 6)));
                inner.add(val);
                inner.add(Box.createRigidArea(new Dimension(0, 4)));
                inner.add(dl);

                add(inner);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // soft shadow
                g2.setColor(new Color(0, 0, 0, 60));
                g2.fill(new RoundRectangle2D.Float(4, 6, getWidth() - 8, getHeight() - 8, 18, 18));
                // card body
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 8, getHeight() - 10, 18, 18));
                g2.dispose();
                super.paintComponent(g);
            }
        }

        /** Simple animated bar chart panel fed by random data on a timer. */
        static class ChartPanel extends JPanel {
            private final int[] data = new int[14];
            private final Random rnd = new Random();

            ChartPanel() {
                setOpaque(false);
                for (int i = 0; i < data.length; i++) data[i] = 20 + rnd.nextInt(60);
            }

            void pushRandomValue() {
                System.arraycopy(data, 1, data, 0, data.length - 1);
                data[data.length - 1] = 15 + rnd.nextInt(75);
                repaint();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // card background with shadow
                int w = getWidth(), h = getHeight();
                g2.setColor(new Color(0, 0, 0, 60));
                g2.fill(new RoundRectangle2D.Float(4, 6, w - 8, h - 8, 20, 20));
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, w - 8, h - 10, 20, 20));

                g2.setColor(TEXT_LIGHT);
                g2.setFont(new Font("SansSerif", Font.BOLD, 15));
                g2.drawString("Live Activity", 22, 30);

                int padding = 30;
                int chartTop = 50;
                int chartBottom = h - 40;
                int chartLeft = 22;
                int chartRight = w - 40;
                int barGap = 10;
                int barWidth = Math.max(6, (chartRight - chartLeft - barGap * (data.length - 1)) / data.length);

                int max = 100;
                for (int i = 0; i < data.length; i++) {
                    int barHeight = (int) ((chartBottom - chartTop) * (data[i] / (double) max));
                    int x = chartLeft + i * (barWidth + barGap);
                    int y = chartBottom - barHeight;
                    GradientPaint gp = new GradientPaint(x, y, ACCENT_2, x, chartBottom, ACCENT);
                    g2.setPaint(gp);
                    g2.fill(new RoundRectangle2D.Float(x, y, barWidth, barHeight, 8, 8));
                }

                g2.dispose();
            }
        }

        public static void main(String[] args) {
            // Nimbus gives us a cleaner baseline before we override with custom painting
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ignored) { }

            SwingUtilities.invokeLater(() -> new CoolDashboard().setVisible(true));
        }
    }

