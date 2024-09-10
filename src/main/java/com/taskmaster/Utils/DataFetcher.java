package com.taskmaster.Utils;

import com.taskmaster.Models.Project;
import com.taskmaster.Models.Task;
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

    // LOGIN LOGIC
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
     * Fetch projects based on the logged user's ID.
     *
     * @param userId The ID of the logged-in user.
     * @return ObservableList of Project objects.
     */
    public ObservableList<Project> getProjectsByUserId(int userId) {
        ObservableList<Project> projectsList = FXCollections.observableArrayList();
        String query = "SELECT p.id, p.name, p.description, p.leader_id, p.start_date, p.end_date, u.name AS leader_name " +
                "FROM projects p " +
                "JOIN users u ON p.leader_id = u.id " +
                "WHERE p.leader_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

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
     * Get the user ID based on the username.
     *
     * @param username The username of the user.
     * @return The ID of the user, or -1 if no user is found.
     */
    public int getUserIdByUsername(String username) {
        String query = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no user ID is found
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
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM assigned_tasks WHERE task_id IN (SELECT id FROM tasks WHERE project_id = ?)")) {
            stmt.setInt(1, projectId);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM tasks WHERE project_id = ?")) {
            stmt.setInt(1, projectId);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM projects WHERE id = ?")) {
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

    /**
     * Update the leader of a project.
     *
     * @param projectId The ID of the project.
     * @param leaderId  The new leader's ID.
     */
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

    ////////////////////////////////////
    public Project getProjectDetails(int projectId) {
        Project project = null;

        // Fetch Project Details
        String projectQuery = "SELECT p.id, p.name, p.description, p.start_date, p.end_date " +
                "FROM projects p WHERE p.id = ?";

        try (PreparedStatement projectStmt = connection.prepareStatement(projectQuery)) {
            projectStmt.setInt(1, projectId);
            ResultSet projectResult = projectStmt.executeQuery();

            if (projectResult.next()) {
                int id = projectResult.getInt("id");
                String name = projectResult.getString("name");
                String description = projectResult.getString("description");
                LocalDate startDate = projectResult.getDate("start_date").toLocalDate();
                LocalDate endDate = projectResult.getDate("end_date").toLocalDate();

                project = new Project(id, name, description, startDate, endDate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Fetch Related Tasks if the project exists
        if (project != null) {
            String tasksQuery = "SELECT t.id, t.title, t.description, t.status, t.start_date, t.end_date, u.name AS leader_name " +
                    "FROM tasks t " +
                    "JOIN users u ON t.assigned_by = u.id " +
                    "WHERE t.project_id = ?";

            ObservableList<Task> tasksList = FXCollections.observableArrayList();

            try (PreparedStatement tasksStmt = connection.prepareStatement(tasksQuery)) {
                tasksStmt.setInt(1, projectId);
                ResultSet tasksResult = tasksStmt.executeQuery();

                while (tasksResult.next()) {
                    int id = tasksResult.getInt("id");
                    String title = tasksResult.getString("title");
                    String description = tasksResult.getString("description");
                    String status = tasksResult.getString("status");
                    LocalDate startDate = tasksResult.getDate("start_date").toLocalDate();
                    LocalDate endDate = tasksResult.getDate("end_date").toLocalDate();
                    String leader_name = tasksResult.getString("leader_name");

                    Task task = new Task(title, id, projectId, description, status, startDate, endDate, leader_name);
                    tasksList.add(task);
                }

                // Set tasks list to the project
                project.setTasks(tasksList);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return project;
    }



    public boolean addTask(Task task) {
        String query = "INSERT INTO tasks (project_id, title, description, status, start_date, end_date, assigned_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, task.getProject_id());
            preparedStatement.setString(2, task.getTitle());
            preparedStatement.setString(3, task.getDescription());
            preparedStatement.setString(4, task.getStatus());
            preparedStatement.setDate(5, Date.valueOf(task.getStart_date()));
            preparedStatement.setDate(6, Date.valueOf(task.getEnd_date()));
            preparedStatement.setInt(7, task.getAssigned_by());

            int affectedRows = preparedStatement.executeUpdate();

            // If insert is successful, we can get the generated task ID
            if (affectedRows > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    task.setId(generatedKeys.getInt(1)); // Set the generated ID in the task object
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTask(Task task) {
        String query = "UPDATE tasks SET title = ?, description = ?, status = ?, start_date = ?, end_date = ?, assigned_by = ? " +
                "WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getStatus());
            preparedStatement.setDate(4, Date.valueOf(task.getStart_date()));
            preparedStatement.setDate(5, Date.valueOf(task.getEnd_date()));
            preparedStatement.setInt(6, task.getAssigned_by());
            preparedStatement.setInt(7, task.getId());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0; // Return true if the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
}


