package com.taskmaster.Utils;

import java.sql.Connection;

public class LeaderDataFetcher {

    // Constructor
    public LeaderDataFetcher() {
        // Get a connection instance from DBConnection class
        Connection connection = DBConnection.getCon();
    }


}
