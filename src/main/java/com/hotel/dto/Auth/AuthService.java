package com.hotel.dto.Auth;

import com.hotel.model.User;
import com.hotel.util.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AuthService {
    private static final String DEMO_ADMIN_EMAIL = "admin@hotel.com";
    private static final String DEMO_ADMIN_PASSWORD = "password123";

    private User currentUser;

    public AuthService() {
    }

    public User login(String email, String password) throws Exception {
        if (email == null || password == null) {
            throw new Exception("Email and password are required");
        }

        String normalizedEmail = email.trim().toLowerCase();
        Connection connection = DatabaseConnection.getConnection();

        if (connection == null) {
            return loginDemoAdmin(normalizedEmail, password);
        }

        String sql = "SELECT id, email, password_hash, full_name, phone, role, is_active, created_at, updated_at " +
                "FROM hotel_users WHERE LOWER(email) = ? LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, normalizedEmail);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new Exception("Invalid email or password");
                }

                String storedHash = rs.getString("password_hash");
                if (storedHash == null || !BCrypt.checkpw(password, storedHash)) {
                    throw new Exception("Invalid email or password");
                }

                boolean isActive = rs.getBoolean("is_active");
                if (!isActive) {
                    throw new Exception("Account is inactive. Please contact administrator.");
                }

                currentUser = mapUser(rs);
                return currentUser;
            }
        } catch (SQLException e) {
            throw new Exception("Could not complete login: " + e.getMessage(), e);
        }
    }

    public User register(String email, String password, String fullName, String phone) throws Exception {
        if (email == null || password == null || fullName == null) {
            throw new Exception("Email, password and full name are required");
        }

        Connection connection = DatabaseConnection.getConnection();
        if (connection == null) {
            throw new Exception("Registration requires a working MySQL connection.");
        }

        String normalizedEmail = email.trim().toLowerCase();
        String sql = "INSERT INTO hotel_users (email, password_hash, full_name, phone, role, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, normalizedEmail);
            stmt.setString(2, BCrypt.hashpw(password, BCrypt.gensalt(12)));
            stmt.setString(3, fullName.trim());
            stmt.setString(4, phone == null || phone.trim().isEmpty() ? null : phone.trim());
            stmt.setString(5, "customer");
            stmt.setBoolean(6, true);
            stmt.executeUpdate();

            return findUserByEmail(connection, normalizedEmail);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new Exception("Email already exists");
            }
            throw new Exception("Registration failed: " + e.getMessage(), e);
        }
    }

    private User findUserByEmail(Connection connection, String email) throws Exception {
        String sql = "SELECT id, email, password_hash, full_name, phone, role, is_active, created_at, updated_at " +
                "FROM hotel_users WHERE LOWER(email) = ? LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new Exception("User record was not found after registration.");
                }

                return mapUser(rs);
            }
        } catch (SQLException e) {
            throw new Exception("Could not load registered user: " + e.getMessage(), e);
        }
    }

    private User loginDemoAdmin(String email, String password) throws Exception {
        if (!DEMO_ADMIN_EMAIL.equalsIgnoreCase(email) || !DEMO_ADMIN_PASSWORD.equals(password)) {
            throw new Exception("Database is unavailable. Demo login only supports admin@hotel.com / password123.");
        }

        User user = new User();
        user.setId("demo-admin");
        user.setEmail(DEMO_ADMIN_EMAIL);
        user.setFullName("Demo Administrator");
        user.setRole("admin");
        user.setActive(true);
        currentUser = user;
        return user;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(String.valueOf(rs.getInt("id")));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("is_active"));
        user.setPhone(rs.getString("phone"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
