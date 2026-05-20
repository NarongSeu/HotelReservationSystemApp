package com.hotel.dto.panels;

import com.hotel.dao.RoomDAO;
import com.hotel.dao.GuestDAO;
import com.hotel.dto.ModernColors;
import com.hotel.dto.components.StatCard;
import com.hotel.model.Room;
import com.hotel.model.Guest;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class DashboardPanel extends JPanel {
    private RoomDAO roomDAO;
    private GuestDAO guestDAO;
    private StatCard totalRoomsCard, availableRoomsCard, occupiedRoomsCard, totalGuestsCard;
    
    public DashboardPanel() {
        roomDAO = new RoomDAO();
        guestDAO = new GuestDAO();
        initializeComponents();
        setupLayout();
        refreshData();
    }
    
    private void initializeComponents() {
        setBackground(ModernColors.MAIN_BACKGROUND);
        
        // Create stat cards with proper icons
        totalRoomsCard = new StatCard("Total Rooms", "0", "Total capacity", 
            createIconLabel("https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-2WEhHYngxG8FBtU2JjGYEpNijkVGED.png", 24, 24), 
            ModernColors.CARD_PURPLE_START, ModernColors.CARD_PURPLE_END);

        availableRoomsCard = new StatCard("Available Rooms", "0", "Ready to book", 
            createIconLabel("https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-MvXvMqzcddpVe1rLXlXEe71hCqMInS.png", 24, 24), 
            ModernColors.CARD_GREEN_START, ModernColors.CARD_GREEN_END);

        occupiedRoomsCard = new StatCard("Occupied Rooms", "0", "Currently occupied", 
            createIconLabel("https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-3cjnDOji2uQ0U4UUpauQOKw0bAiNL7.png", 24, 24), 
            ModernColors.CARD_PINK_START, ModernColors.CARD_PINK_END);

        totalGuestsCard = new StatCard("Total Guests", "0", "Registered guests", 
            createIconLabel("https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-E15rmZgH5dmpt4jpn3GxoiWUCVVgR4.png", 24, 24), 
            ModernColors.CARD_YELLOW_START, ModernColors.CARD_YELLOW_END);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Title
        JLabel titleLabel = new JLabel("Dashboard Overview");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(ModernColors.TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Stats cards panel
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.CENTER);
        
        // Quick actions panel
        JPanel actionsPanel = createQuickActionsPanel();
        add(actionsPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        
        statsPanel.add(totalRoomsCard);
        statsPanel.add(availableRoomsCard);
        statsPanel.add(occupiedRoomsCard);
        statsPanel.add(totalGuestsCard);
        
        return statsPanel;
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel actionsTitle = new JLabel("Quick Actions");
        actionsTitle.setFont(new Font("Arial", Font.BOLD, 18));
        actionsTitle.setForeground(ModernColors.TEXT_PRIMARY);
        actionsTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(actionsTitle, BorderLayout.WEST);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonsPanel.setOpaque(false);
        
        // Quick action buttons with proper icons
        JButton addRoomBtn = createQuickActionButton("Add Room", 
            createIconLabel("https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-2WEhHYngxG8FBtU2JjGYEpNijkVGED.png", 20, 20), ModernColors.STATUS_INFO);
        JButton addGuestBtn = createQuickActionButton("Add Guest", 
            createIconLabel("https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-E15rmZgH5dmpt4jpn3GxoiWUCVVgR4.png", 20, 20), ModernColors.STATUS_SUCCESS);
        JButton newReservationBtn = createQuickActionButton("New Reservation", 
            createIconLabel("https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-MvXvMqzcddpVe1rLXlXEe71hCqMInS.png", 20, 20), ModernColors.STATUS_WARNING);
        JButton viewReportsBtn = createQuickActionButton("View Reports", 
            createIconLabel("https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-FBEBG7E1OhWPTL9TseV4zfPWNsnaX9.png", 20, 20), ModernColors.CARD_PURPLE_START);
        
        buttonsPanel.add(addRoomBtn);
        buttonsPanel.add(addGuestBtn);
        buttonsPanel.add(newReservationBtn);
        buttonsPanel.add(viewReportsBtn);
        
        JPanel mainActionsPanel = new JPanel(new BorderLayout());
        mainActionsPanel.setOpaque(false);
        mainActionsPanel.add(titlePanel, BorderLayout.NORTH);
        mainActionsPanel.add(buttonsPanel, BorderLayout.CENTER);
        
        return mainActionsPanel;
    }
    
    private JLabel createIconLabel(String iconUrl, int width, int height) {
        try {
            URL url = new URL(iconUrl);
            BufferedImage originalImage = ImageIO.read(url);
            
            // Create white version of the icon
            BufferedImage whiteImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = whiteImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Scale and draw the original image
            g2d.drawImage(originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            
            // Apply white color filter
            g2d.setComposite(AlphaComposite.SrcAtop);
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);
            g2d.dispose();
            
            return new JLabel(new ImageIcon(whiteImage));
        } catch (Exception e) {
            System.err.println("Could not load icon from: " + iconUrl);
            // Fallback to text
            return new JLabel("●");
        }
    }

    private JButton createQuickActionButton(String text, JLabel iconLabel, Color color) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color currentColor = getModel().isPressed() ? color.darker() : 
                                   getModel().isRollover() ? color.brighter() : color;
                
                g2d.setColor(currentColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(120, 80));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add icon and text
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        content.add(iconLabel, BorderLayout.CENTER);
        
        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Arial", Font.BOLD, 11));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        content.add(textLabel, BorderLayout.SOUTH);
        
        button.add(content);
        
        // Add functionality to quick action buttons
        button.addActionListener(e -> {
            Container cardContainer = DashboardPanel.this.getParent();
            while (cardContainer != null && !(cardContainer.getLayout() instanceof CardLayout)) {
                cardContainer = cardContainer.getParent();
            }

            if (cardContainer instanceof JPanel) {
                JPanel panel = (JPanel) cardContainer;
                CardLayout cardLayout = (CardLayout) panel.getLayout();
                cardLayout.show(panel, resolveTargetCard(text));
            }
        });
        
        return button;
    }

    private String resolveTargetCard(String actionText) {
        switch (actionText) {
            case "Add Room":
                return "Rooms";
            case "Add Guest":
                return "Guests";
            case "New Reservation":
                return "Reservations";
            case "View Reports":
                return "Billing";
            default:
                return "Dashboard";
        }
    }
    
    public void refreshData() {
        List<Room> allRooms = roomDAO.getAllRooms();
        List<Room> availableRooms = roomDAO.getAvailableRooms();
        List<Guest> allGuests = guestDAO.getAllGuests();
        
        long occupiedCount = allRooms.stream()
            .filter(room -> "Occupied".equals(room.getStatus()))
            .count();
        
        // Update stat cards
        totalRoomsCard.updateValues(String.valueOf(allRooms.size()), "Total capacity");
        availableRoomsCard.updateValues(String.valueOf(availableRooms.size()), "Ready to book");
        occupiedRoomsCard.updateValues(String.valueOf(occupiedCount), "Currently occupied");
        totalGuestsCard.updateValues(String.valueOf(allGuests.size()), "Registered guests");
    }
}
