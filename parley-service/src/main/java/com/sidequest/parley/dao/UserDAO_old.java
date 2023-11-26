package com.sidequest.parley.dao;

public class UserDAO_old {
//
//  //  private final SQLiteConnection dbConnection;
//
//// --Commented out by Inspection START (11/26/2023 8:06 AM):
////    public UserDAO_old() {
////        dbConnection = new SQLiteConnection();
////    }
//// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
//
//// --Commented out by Inspection START (11/26/2023 8:06 AM):
////    public List<User> getAllUsers() {
////        List<User> users = new ArrayList<>();
////
////        try (Connection connection = dbConnection.getConnection();
////             Statement statement = connection.createStatement();
////             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {
////
////            while (resultSet.next()) {
////                int id = resultSet.getInt("id");
////                String name = resultSet.getString("name");
////
////                User user = new User(id, name);
////                users.add(user);
////            }
////
////        } catch (SQLException e) {
//// --Commented out by Inspection START (11/26/2023 8:06 AM):
//////            e.printStackTrace();
//////            // Handle database access error
//////        }
//////
//////        return users;
//////    }
////// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
////
////    public User getUserById(int id) {
////        User user = null;
////
////        try (Connection connection = dbConnection.getConnection();
////             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
////            statement.setInt(1, id);
////            ResultSet resultSet = statement.executeQuery();
////
////            if (resultSet.next()) {
////                String name = resultSet.getString("name");
////
////                user = new User(id, name);
//// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // Handle database access error
//        }
//
//        return user;
//    }
//
//// --Commented out by Inspection START (11/26/2023 8:06 AM):
////    public void createUser(User user) {
////        try (Connection connection = dbConnection.getConnection();
//// --Commented out by Inspection START (11/26/2023 8:06 AM):
//////             PreparedStatement statement = connection
//////                     .prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)")) {
//////
//////            statement.setInt(1, user.getId());
//////            statement.setString(2, user.getName());
//////
//////            statement.executeUpdate();
//////
//////        } catch (SQLException e) {
//////            e.printStackTrace();
//////            // Handle database access error
//////        }
//////    }
////// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
////
////    public void updateUser(User user) {
//// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
//        try (Connection connection = dbConnection.getConnection();
//             PreparedStatement statement = connection.prepareStatement("UPDATE users SET name = ? WHERE id = ?")) {
//
//            statement.setString(1, user.getName());
//            statement.setInt(2, user.getId());
//
//// --Commented out by Inspection START (11/26/2023 8:06 AM):
////            statement.executeUpdate();
////
////        } catch (SQLException e) {
////            e.printStackTrace();
////            // Handle database access error
////        }
////    }
////
////    public void deleteUser(int id) {
////        try (Connection connection = dbConnection.getConnection();
////             PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
////
////            statement.setInt(1, id);
////            statement.executeUpdate();
//// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // Handle database access error
//        }
//    }
}
