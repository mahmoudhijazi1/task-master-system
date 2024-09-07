package com.taskmaster.Utils;

import com.taskmaster.Models.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataFetcher {

    private Connection connection;

    // Constructor
    public DataFetcher() {
        // Get a connection instance from DBConnection class
        this.connection = DBConnection.getCon();
    }

    //LOGIN LOGIC
    /**
     * Validate user credentials.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @param role     The user's role.
     * @return true if valid, false otherwise.
     */
    public boolean validateUser(String username, String password, String role) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role_id = (SELECT id FROM roles WHERE name = ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Return true if a record is found
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Fetch roles from the database.
     *
     * @return List of role names.
     */
    public List<String> getRoles() {
        List<String> rolesList = new ArrayList<>();
        String query = "SELECT name FROM roles";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                rolesList.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rolesList;
    }

    /**
     * Fetch all projects from the database, including the leader's name.
     *
     * @return ObservableList of Project objects.
     */
    public ObservableList<Project> getProjects() {
        ObservableList<Project> projectsList = FXCollections.observableArrayList();
        String query = "SELECT p.id, p.name, p.description, p.leader_id, p.start_date, p.end_date, u.name AS leader_name " +
                "FROM projects p " +
                "JOIN users u ON p.leader_id = u.id " +
                "WHERE u.role_id = (SELECT id FROM roles WHERE name = 'Leader')";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                int leaderId = resultSet.getInt("leader_id");
                String leaderName = resultSet.getString("leader_name"); // Get leader's name
                LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
                LocalDate endDate = resultSet.getDate("end_date").toLocalDate();

                // Create a new Project object
                Project project = new Project(id, name, description, leaderId, leaderName, startDate, endDate);
                projectsList.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectsList;
    }



    /**
     * Update an existing project in the database.
     *
     * @param project The Project object to be updated.
     */
    public void updateProject(Project project) {
        String query = "UPDATE projects SET name = ?, description = ?, leader_id = ?, start_date = ?, end_date = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, project.getName());
            preparedStatement.setString(2, project.getDescription());
            preparedStatement.setInt(3, project.getLeader_id());
            preparedStatement.setDate(4, Date.valueOf(project.getStart_date()));
            preparedStatement.setDate(5, Date.valueOf(project.getEnd_date()));
            preparedStatement.setInt(6, project.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a project from the database.
     *
     * @param projectId The ID of the project to be deleted.
     */
    public void deleteProject(int projectId) throws SQLException {
        try (Connection conn = DBConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM assigned_tasks WHERE task_id IN (SELECT id FROM tasks WHERE project_id = ?)")) {
            stmt.setInt(1, projectId);
            stmt.executeUpdate();
        }

        try (Connection conn = DBConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM tasks WHERE project_id = ?")) {
            stmt.setInt(1, projectId);
            stmt.executeUpdate();
        }

        try (Connection conn = DBConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM projects WHERE id = ?")) {
            stmt.setInt(1, projectId);
            stmt.executeUpdate();
        }
    }

    /**
     * Add a new project to the database.
     *
     * @param project The Project object to be added.
     */
    public void addProject(Project project) {
        String query = "INSERT INTO projects (name, description, leader_id, start_date, end_date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, project.getName());
            preparedStatement.setString(2, project.getDescription());
            preparedStatement.setInt(3, project.getLeader_id());
            preparedStatement.setDate(4, Date.valueOf(project.getStart_date()));
            preparedStatement.setDate(5, Date.valueOf(project.getEnd_date()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Get the list of leaders from the database.
     *
     * @return ObservableList of leader names.
     */
    public ObservableList<String> getLeaders() {
        ObservableList<String> leadersList = FXCollections.observableArrayList();
        String query = "SELECT u.name FROM users u WHERE u.role_id = (SELECT id FROM roles WHERE name = 'Leader')";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                leadersList.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leadersList;
    }

    /**
     * Get the leader's ID by name.
     *
     * @param leaderName The name of the leader.
     * @return The ID of the leader.
     */
    public int getLeaderIdByName(String leaderName) {
        String query = "SELECT id FROM users WHERE name = ? AND role_id = (SELECT id FROM roles WHERE name = 'Leader')";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, leaderName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no leader ID is found
    }

    // Close the connection when done (optional)
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateProjectLeader(int projectId, int leaderId) {
        String query = "UPDATE projects SET leader_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, leaderId);
            preparedStatement.setInt(2, projectId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
