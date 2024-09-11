package com.taskmaster.Utils;

import com.taskmaster.Models.Project;
import com.taskmaster.Models.Task;
import com.taskmaster.Models.User;
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
                "WHERE u.role_id = (SELECT id FROM roles WHERE name = 'Leader')"
                ;

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
        String query = "INSERT INTO projects (name, description, leader_id, start_date, end_date, manager_id) VALUES (?, ?, ?, ?, ?, 1)";

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
                LocalDate startDate = projectResult.getDate("start_date") != null ? projectResult.getDate("start_date").toLocalDate() : null;
                LocalDate endDate = projectResult.getDate("end_date") != null ? projectResult.getDate("end_date").toLocalDate() : null;

                project = new Project(id, name, description, startDate, endDate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Fetch Related Tasks if the project exists
        if (project != null) {
            String tasksQuery = "SELECT t.id, t.title, t.description, t.status, t.start_date, t.end_date, u.name AS developer_name " +
                    "FROM tasks t " +
                    "JOIN assigned_tasks at ON t.id = at.task_id " +     // Join with pivot table to get assigned tasks
                    "JOIN users u ON at.user_id = u.id " +               // Join with users to get developer's name
                    "WHERE t.project_id = ?";

            ObservableList<Task> tasksList = FXCollections.observableArrayList();

            try (PreparedStatement tasksStmt = connection.prepareStatement(tasksQuery)) {
                tasksStmt.setInt(1, projectId);
                ResultSet tasksResult = tasksStmt.executeQuery();

                while (tasksResult.next()) {
                    Task task = new Task();
                    task.setId(tasksResult.getInt("id"));
                    task.setTitle(tasksResult.getString("title"));
                    task.setDescription(tasksResult.getString("description"));
                    task.setStatus(tasksResult.getString("status"));
                    task.setStartDate(tasksResult.getDate("start_date").toLocalDate());
                    task.setEndDate(tasksResult.getDate("end_date").toLocalDate());
                    task.setDeveloperName(tasksResult.getString("developer_name"));  // Custom property to hold developer's name
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

    public List<Task> getTasksForLeader(int leaderId) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT t.id, t.title,t.description, t.status, t.start_date, t.end_date, u.name AS developer_name " +
                "FROM tasks t " +
                "LEFT JOIN assigned_tasks at ON t.id = at.task_id " +
                "LEFT JOIN users u ON at.user_id = u.id " +
                "WHERE t.assigned_by = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, leaderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Task task = new Task();
                task.setId(resultSet.getInt("id"));
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));
                task.setStatus(resultSet.getString("status"));
                task.setStartDate(resultSet.getDate("start_date").toLocalDate());
                task.setEndDate(resultSet.getDate("end_date").toLocalDate());
                task.setDeveloperName(resultSet.getString("developer_name"));  // Custom property to hold developer's name
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }




    public boolean addTask(Task task, String developerName) {
        String taskQuery = "INSERT INTO tasks (project_id, title, description, status, start_date, end_date, assigned_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String assignedTasksQuery = "INSERT INTO assigned_tasks (task_id, user_id, assigned_date) VALUES (?, ?, ?)";
        int developerId = getUserIdByUsername(developerName);

        try {
            // Start a transaction
            connection.setAutoCommit(false);

            // Insert the new task
            int taskId = -1;
            try (PreparedStatement taskPreparedStatement = connection.prepareStatement(taskQuery, Statement.RETURN_GENERATED_KEYS)) {
                taskPreparedStatement.setInt(1, task.getProjectId());
                taskPreparedStatement.setString(2, task.getTitle());
                taskPreparedStatement.setString(3, task.getDescription());
                taskPreparedStatement.setString(4, task.getStatus());
                taskPreparedStatement.setDate(5, Date.valueOf(task.getStartDate()));
                taskPreparedStatement.setDate(6, Date.valueOf(task.getEndDate()));
                taskPreparedStatement.setInt(7, task.getAssignedBy());

                int affectedRows = taskPreparedStatement.executeUpdate();

                // If insert is successful, get the generated task ID
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = taskPreparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            taskId = generatedKeys.getInt(1); // Set the generated ID in the task object
                        }
                    }
                } else {
                    // Rollback and return false if no rows were affected
                    connection.rollback();
                    return false;
                }
            }

            if (taskId > 0) {
                // Insert into the assigned_tasks table
                try (PreparedStatement assignedTasksPreparedStatement = connection.prepareStatement(assignedTasksQuery)) {
                    assignedTasksPreparedStatement.setInt(1, taskId);
                    assignedTasksPreparedStatement.setInt(2, developerId);
                    assignedTasksPreparedStatement.setDate(3, Date.valueOf(LocalDate.now())); // Set the assigned date to now

                    int affectedRows = assignedTasksPreparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        // Commit the transaction
                        connection.commit();
                        return true;
                    } else {
                        // Rollback if no rows were affected
                        connection.rollback();
                    }
                }
            } else {
                // Rollback if task ID is invalid
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Rollback transaction in case of error
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                // Reset auto-commit to true
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateTask(Task task, Integer projectIdToUpdate) {
        String taskQuery;
        String assignedTasksQuery = "UPDATE assigned_tasks SET user_id = ? WHERE task_id = ?";

        // If projectIdToUpdate is provided, include project-specific conditions in the SQL query
        if (projectIdToUpdate != null) {
            taskQuery = "UPDATE tasks SET title = ?, description = ?, status = ?, start_date = ?, end_date = ? WHERE id = ? AND project_id = ?";
        } else {
            taskQuery = "UPDATE tasks SET title = ?, description = ?, status = ?, start_date = ?, end_date = ?, project_id = ?, assigned_by = ? WHERE id = ?";
        }

        try {
            // Update task details
            try (PreparedStatement taskPreparedStatement = connection.prepareStatement(taskQuery)) {
                // Set common parameters first
                taskPreparedStatement.setString(1, task.getTitle());
                taskPreparedStatement.setString(2, task.getDescription());
                taskPreparedStatement.setString(3, task.getStatus());
                taskPreparedStatement.setDate(4, Date.valueOf(task.getStartDate()));
                taskPreparedStatement.setDate(5, Date.valueOf(task.getEndDate()));

                if (projectIdToUpdate != null) {
                    // Set parameters for project-specific update
                    taskPreparedStatement.setInt(6, task.getId());
                    taskPreparedStatement.setInt(7, projectIdToUpdate);
                } else {
                    // Set parameters for general task update
                    taskPreparedStatement.setInt(6, task.getProjectId());
                    taskPreparedStatement.setInt(7, task.getAssignedBy());
                    taskPreparedStatement.setInt(8, task.getId());
                }

                int affectedRows = taskPreparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    return false; // Task update failed
                }
            }

            // Update the assigned developer
            try (PreparedStatement assignedTasksPreparedStatement = connection.prepareStatement(assignedTasksQuery)) {
                // Assuming you have a method to get the user ID from developer name
                int developerId = getUserIdFromUserName(task.getDeveloperName());
                assignedTasksPreparedStatement.setInt(1, developerId);
                assignedTasksPreparedStatement.setInt(2, task.getId());

                int affectedRows = assignedTasksPreparedStatement.executeUpdate();
                return affectedRows > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean setTaskStatus(Task task) {
        String query = "UPDATE tasks SET  status = ? WHERE id = ? " ;


        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, task.getStatus());
            preparedStatement.setInt(2, task.getId());

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

    private int getUserIdFromUserName(String developerName) {
        String query = "SELECT id FROM users WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, developerName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return a default or error value if not found
    }

    public List<String> getDevelopers() {

        ObservableList<String> developersList = FXCollections.observableArrayList();
        String query = "SELECT u.name FROM users u WHERE u.role_id = (SELECT id FROM roles WHERE name = 'Developer')";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                developersList.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (developersList.size() > 0) {
            System.out.println("got developers");
        }else{
            System.out.println("no developers");
        }
        return developersList;

    }

    public List<Task> getDeveloperTasks(int developerId) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT t.id, t.title, t.description, t.status, t.start_date, t.end_date, p.name AS project_name, u_leader.name AS leader_name " +
                "FROM tasks t " +
                "LEFT JOIN assigned_tasks at ON t.id = at.task_id " +
                "LEFT JOIN projects p ON t.project_id = p.id " +
                "LEFT JOIN users u_leader ON t.assigned_by = u_leader.id " +  // Leader who assigned the task
                "WHERE at.user_id = ?";  // Developer ID

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, developerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Task task = new Task();
                task.setId(resultSet.getInt("id"));
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));
                task.setStatus(resultSet.getString("status"));
                task.setStartDate(resultSet.getDate("start_date").toLocalDate());
                task.setEndDate(resultSet.getDate("end_date").toLocalDate());
                task.setAssignedByName(resultSet.getString("leader_name"));
                task.setProjectName(resultSet.getString("project_name"));// Custom property to hold developer's name
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public void createUser(String name, String username, String password, int roleId)  {
        String query = "INSERT INTO USERS (name, username, password, role_id) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setInt(4, roleId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<User> getUsersByRole(int roleId) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role_id = ?";

        try {
             PreparedStatement statement = connection.prepareStatement(query) ;

            statement.setInt(1, roleId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setUsername(resultSet.getString("username"));
                // Assume User has a constructor or setters for these fields
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly in production code
        }

        return users;
    }
}



