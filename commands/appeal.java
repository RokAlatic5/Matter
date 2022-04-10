import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class appeal extends Command{

    public appeal() {
        this.name = "appeal";
        this.help = "Appeals for a punishement";
        this.botPermissions = new Permission[]{};
        this.guildOnly = false;
    }

    private final long channelId, authorId;

    static String Warn;
    static String Punisher;

    public appeal(MessageChannel channel, User author) {
        this.channelId = channel.getIdLong();
        this.authorId = author.getIdLong();
    }

    protected void execute(CommandEvent event) {
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
        try (Connection connection = DriverManager.getConnection(main.url, main.username, main.password)) {

            Statement statement = connection.createStatement();

            String checkForAppeals = "SELECT Warn1 , Warn2 , Warn3, Executor , Executor1 ,Executor2 ,Appeal1, Appeal2, Appeal3 FROM Warn WHERE UserID='"+UserID+"' AND Guild='"+GuildId+"'";


            ResultSet rs = statement.executeQuery(checkForAppeals);


            String Appeal1 = "";
            String Appeal2 = "";
            String Appeal3 = "";
            String Warn1 = "";
            String Warn2 = "";
            String Warn3 = "";
            String Executor = "";
            String Executor1 = "";
            String Executor2 = "";

            while (rs.next()) {
                Appeal1 = rs.getString("Appeal1");
                Appeal2 = rs.getString("Appeal2");
                Appeal3 = rs.getString("Appeal3");
                Warn1 = rs.getString("Warn1");
                Warn2 = rs.getString("Warn2");
                Warn3 = rs.getString("Warn2");
                Executor = rs.getString("Executor");
                Executor1 = rs.getString("Executor1");
                Executor2 = rs.getString("Executor2");
            }

            if (AppealID.equals(Appeal1)) {
                Warn = Warn1;
                Punisher = Executor;
            }
            if (AppealID.equals(Appeal2)) {
                Warn = Warn2;
                Punisher = Executor1;
            }
            if (AppealID.equals(Appeal3)) {
                Warn = Warn3;
                Punisher = Executor2;
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


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getChannel().getIdLong() != channelId) return;
        if (event.getAuthor().getIdLong() == authorId) {
            MessageChannel channel = event.getChannel();
            String content = event.getMessage().getContentRaw();
            Message.Attachment attachment;
            String AppealReason = "";
            String Attachment = "";
            if (Attachment.isEmpty() || AppealReason.isEmpty()) {
            if (content.equalsIgnoreCase("cancel") || content.equalsIgnoreCase("stop")) {
                channel.sendMessage("Appeal request canceled!").queue();
                event.getJDA().removeEventListener(this);
            }
                if (content.isEmpty() == false) {
                    AppealReason = event.getMessage().getContentDisplay();
                    channel.sendMessage("Please include photos or type no if you have none.").queue();
                }
                try {
                    if (content.isEmpty() == true || event.getMessage().getAttachments().get(0) != null) {
                        Attachment = event.getMessage().getAttachments().get(0).getUrl();
                        System.out.println(Attachment);
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setTitle(event.getMember().getUser().getName() + " is appealing for their punishment.");
                        embed.addField("Warning:", Warn, false);
                        embed.addField("Moderator: ", Punisher, false);
                        embed.addField("Reason: ", AppealReason, false);
                        channel.sendMessageEmbeds(embed.build()).setActionRow(
                                Button.primary("hello", "Click Me")) // Button with only an emoji
                                .queue();
                        for (int i = 0; i < event.getMessage().getAttachments().size(); i++)
                            channel.sendMessage(event.getMessage().getAttachments().get(i).getUrl()).queue();
                    }
                } catch (IndexOutOfBoundsException e) { }
            }
        }
    }
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent interaction) {
        System.out.println("test");
        if (interaction.getComponentId().equals("hello")) {
            interaction.reply("Hello :)").queue(); // send a message in the channel
        }
    }
}
