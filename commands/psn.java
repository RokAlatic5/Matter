import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Member;
import java.awt.*;
import java.sql.*;

public class psn extends Command{

    public psn() {
        this.name = "psn";
        this.help = "shows users psn";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        String UserID = "";
        String mentionedUser = "";
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        EmbedBuilder embed = new EmbedBuilder();
        Member member = null;
        try {
            mentionedUser = event.getMessage().getMentionedMembers().get(0).getId();
        } catch (IndexOutOfBoundsException err) {}
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


        try (Connection connection = DriverManager.getConnection(main.url, main.username, main.password)) {

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

