package com.hotel.gui.panels;

import com.hotel.dao.RoomDAO;
import com.hotel.dao.GuestDAO;
import com.hotel.gui.ModernColors;
import com.hotel.gui.components.StatCard;
import com.hotel.model.Room;
import com.hotel.model.Guest;
import javax.swing.*;
import java.awt.*;
import java.util.List;

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
        
        // Create stat cards with modern design
        totalRoomsCard = new StatCard("Total Rooms", "0", "All rooms in hotel", "🏠", 
            ModernColors.CARD_PURPLE_START, ModernColors.CARD_PURPLE_END);
        
        availableRoomsCard = new StatCard("Available Rooms", "0", "Ready for booking", "✅", 
            ModernColors.CARD_GREEN_START, ModernColors.CARD_GREEN_END);
        
        occupiedRoomsCard = new StatCard("Occupied Rooms", "0", "Currently in use", "🔑", 
            ModernColors.CARD_PINK_START, ModernColors.CARD_PINK_END);
        
        totalGuestsCard = new StatCard("Total Guests", "0", "Registered guests", "👥", 
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
        
        // Quick action buttons
        JButton addRoomBtn = createQuickActionButton("Add Room", "🏠", ModernColors.STATUS_INFO);
        JButton addGuestBtn = createQuickActionButton("Add Guest", "👤", ModernColors.STATUS_SUCCESS);
        JButton newReservationBtn = createQuickActionButton("New Reservation", "📅", ModernColors.STATUS_WARNING);
        JButton viewReportsBtn = createQuickActionButton("View Reports", "📊", ModernColors.CARD_PURPLE_START);
        
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
    
    private JButton createQuickActionButton(String text, String icon, Color color) {
        JButton button = new JButton("<html><center>" + icon + "<br>" + text + "</center></html>") {
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
        
        button.setPreferredSize(new Dimension(120, 80));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add functionality to quick action buttons
        button.addActionListener(e -> {
            Container parent = getParent();
            while (parent != null && !(parent instanceof JFrame)) {
                parent = parent.getParent();
            }
            
            if (parent instanceof JFrame) {
                JFrame frame = (JFrame) parent;
                Component[] components = frame.getContentPane().getComponents();
                
                for (Component comp : components) {
                    if (comp instanceof JPanel) {
                        JPanel panel = (JPanel) comp;
                        if (panel.getLayout() instanceof CardLayout) {
                            CardLayout cardLayout = (CardLayout) panel.getLayout();
                            
                            switch (text) {
                                case "Add Room":
                                    cardLayout.show(panel, "Rooms");
                                    break;
                                case "Add Guest":
                                    cardLayout.show(panel, "Guests");
                                    break;
                                case "New Reservation":
                                    cardLayout.show(panel, "Reservations");
                                    break;
                                case "View Reports":
                                    cardLayout.show(panel, "Billing");
                                    break;
                            }
                            break;
                        }
                    }
                }
            }
        });
        
        return button;
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
