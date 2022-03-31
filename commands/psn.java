import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.entities.MemberImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

public class psn {
    static void userPsn(MessageReceivedEvent event){
        String UserID = "";
        String mentionedUser = "";
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        EmbedBuilder embed = new EmbedBuilder();
        Member member = null;
        try {
            mentionedUser = event.getMessage().getMentionedMembers().get(0).getId();
        } catch (IndexOutOfBoundsException err) {System.out.println(err);}
        int msgLenght = event.getMessage().getContentDisplay().split(" ").length;

        if (msgLenght <= 1) {
            UserID = event.getMember().getId();
            member = event.getMember();
        } else if (mentionedUser.isEmpty() == false)  {
            UserID = event.getMessage().getMentionedMembers().get(0).getId();
            member = event.getMessage().getMentionedMembers().get(0);
        }else if (msgLenght >= 1) {
            UserID = event.getMessage().getContentDisplay().split(" ")[1];
            member = guild.retrieveMemberById(UserID).complete();
        }


        try (Connection connection = DriverManager.getConnection(hidden.url, hidden.username, hidden.password)) {

            Statement statement = connection.createStatement();

            String query = "SELECT UserPSN FROM test1 " +
                    "WHERE UserID="+ UserID+ ";";

            ResultSet rs = statement.executeQuery(query);

            String psn = "";

            while (rs.next()) {
                psn = rs.getString("UserPsn");
            }

            if (psn.equals("")) {
                channel.sendMessage("None set use !setpsn to set your psn.").queue();
                connection.close();
                return;
            }


            embed.setTitle(member.getUser().getName() + "'s PSN:");
            embed.setDescription(psn);
            embed.setColor(Color.decode("#FFD700"));

            channel.sendMessageEmbeds(embed.build()).queue();

            connection.close();

        } catch (SQLException e) {
            throw new IllegalStateException("[Matter] Cannot connect the database!", e);
        }
    }
}

