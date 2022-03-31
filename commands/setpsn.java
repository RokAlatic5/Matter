import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.*;

public class setpsn {
    static void setUserPsn(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        int msgLenght = event.getMessage().getContentDisplay().split(" ").length;

        if (msgLenght <= 1) {
            channel.sendMessage("You need to enter your psn.").queue();
        } else {
            String psnMsg = event.getMessage().getContentDisplay().split(" ")[1];
            String UserID = event.getMember().getId();

            String query = "SELECT UserPSN FROM test1 " +
                    "WHERE UserID=" + UserID + ";";

            String insertPSN = "INSERT INTO test1 (UserID, UserPsn) " +
                    "VALUES ('" + UserID + "', '" + psnMsg + "');";

            String updatePSN = "UPDATE test1 " +
                    "SET UserPsn = '" + psnMsg + "' " +
                    "WHERE UserID = " + UserID + ";";


            try (Connection connection = DriverManager.getConnection(hidden.url, hidden.username, hidden.password)) {

                Statement statement = connection.createStatement();

                ResultSet rs = statement.executeQuery(query);

                String psn = "";

                while (rs.next()) {
                    psn = rs.getString("UserPsn");
                }

                if (psn.equals(psnMsg)) {
                    channel.sendMessage("Your psn is already set to " + psn).queue();
                } else if (psn.equals("")) {
                    channel.sendMessage("psn set to: " + psnMsg).queue();
                    statement.executeUpdate(insertPSN);
                    System.out.println("[Matter] New psn inserted.");
                    connection.close();
                } else {
                    channel.sendMessage("psn updated to: " + psnMsg).queue();
                    statement.executeUpdate(updatePSN);
                    System.out.println("[Matter] New psn updated.");
                    connection.close();
                }


            } catch (SQLException e) {
                throw new IllegalStateException("[Matter] Cannot connect the database!", e);
            }

        }
    }
}
