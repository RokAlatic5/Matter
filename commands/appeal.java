import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.*;
import java.util.concurrent.TimeUnit;

public class appeal extends ListenerAdapter{
    private final long channelId, authorId;

    public appeal(MessageChannel channel, User author) {
        this.channelId = channel.getIdLong();
        this.authorId = author.getIdLong();
    }

    static void appeal(MessageReceivedEvent event) {
        int msgLength = event.getMessage().getContentDisplay().split(" ").length;
        String UserID = event.getMember().getId();
        String channelId = event.getChannel().getId();
        String AppealID;
        Member member = event.getMember();
        String GuildId = event.getGuild().getId();
        final MessageChannel channel = event.getChannel();
        if (msgLength <= 1) {
            channel.sendMessage("You need to enter your appeal ID").queue();
            return;
        } else {
            AppealID = event.getMessage().getContentDisplay().split(" ")[1];
        }
        try (Connection connection = DriverManager.getConnection(hidden.url, hidden.username, hidden.password)) {

            Statement statement = connection.createStatement();

            String checkForAppeals = "SELECT Appeal1, Appeal2, Appeal3 FROM Warn WHERE UserID='"+UserID+"' AND Guild='"+GuildId+"'";


            ResultSet rs = statement.executeQuery(checkForAppeals);


            String Appeal1 = "";
            String Appeal2 = "";
            String Appeal3 = "";

            while (rs.next()) {
                Appeal1 = rs.getString("Appeal1");
                Appeal2 = rs.getString("Appeal2");
                Appeal3 = rs.getString("Appeal3");
            }

            if (AppealID.equals(Appeal1) || AppealID.equals(Appeal2) || AppealID.equals(Appeal3)) {
                channel.sendMessage("Please explain why you want to appeal this punishment.").queue();
                event.getJDA().addEventListener(new appeal(channel, member.getUser()));
            } else {
                channel.sendMessage("Unknown punishment ID").queue();
                return;
            }



            connection.close();


        } catch (SQLException e) {
            throw new IllegalStateException("[Matter] Cannot connect the database!", e);
        }

    }

    String AppealReason = "";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return; // don't respond to other bots
        if (event.getChannel().getIdLong() != channelId) return; // ignore other channels
        if (event.getAuthor().getIdLong() == authorId){
        MessageChannel channel = event.getChannel();
        String content = event.getMessage().getContentRaw();
            Message.Attachment attachment = null;
        if (content.equalsIgnoreCase("cancel") || content.equalsIgnoreCase("stop")) {
            channel.sendMessage("Appeal request canceled!").queue();
            event.getJDA().removeEventListener(this); // stop listening
        }
        if (content.isEmpty() == false) {
            AppealReason = event.getMessage().getContentDisplay();
            System.out.println(AppealReason);
            channel.sendMessage("Please include photos or type no if you have none.").queue();
            event.getJDA().removeEventListener(this); // stop listening
            }
        }
    }
}
