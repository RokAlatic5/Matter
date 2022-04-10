import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.sql.*;

public class setpsn extends Command {

    public setpsn() {
        this.name = "setpsn";
        this.help = "Sets a psn";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.guildOnly = true;
    }
    protected void execute(CommandEvent event) {
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


            try (Connection connection = DriverManager.getConnection(main.url, main.username, main.password)) {

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
