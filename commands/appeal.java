import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class appeal extends Command implements net.dv8tion.jda.api.hooks.EventListener {

     private final EventWaiter waiter;


    appeal(EventWaiter waiter) {
        this.name = "appeal";
        this.help = "Appeals for a punishment";
        this.botPermissions = new Permission[]{};
        this.guildOnly = false;
        this.waiter = waiter;
    }

    private String AppealReason;
    private String Warn;
    private String Punisher;
    private String AppealID;
    private String UserID;
    private String GuildId;

    private String buttonID;
    private Button accept;


    private void waitForReason(CommandEvent event) {
        waiter.waitForEvent(MessageReceivedEvent.class,
                e -> e.getAuthor().equals(event.getAuthor())
                        && e.getChannel().equals(event.getChannel())
                        && !e.getMessage().equals(event.getMessage()),
                e -> { event.reply("Please attach images if you have any or type none");
                    AppealReason = e.getMessage().getContentDisplay();
                    System.out.println(AppealReason);
                    accept = Button.success(AppealID+","+UserID+","+GuildId, "Accept");
                    waitForAttachment(event);
                }
                ,
                5, TimeUnit.MINUTES, () -> event.reply("Appeal submit timed out."));
    }



    private void waitForAttachment(CommandEvent event) {
        waiter.waitForEvent(MessageReceivedEvent.class,
                e -> e.getAuthor().equals(event.getAuthor())
                        && e.getChannel().equals(event.getChannel())
                        && !e.getMessage().equals(event.getMessage()),
                e -> {String response = e.getMessage().getContentDisplay();
                    if (response.equals("none")) {
                        List attachments = e.getMessage().getAttachments();
                        for (int i = 0; i < attachments.size(); i++) {
                            Message.Attachment attachment = e.getMessage().getAttachments().get(i);
                            event.reply(attachment.getUrl());
                        }
                    } else if (response.isEmpty()) {
                        List attachments = e.getMessage().getAttachments();
                        for (int i = 0; i < attachments.size(); i++) {
                            Message.Attachment attachment = e.getMessage().getAttachments().get(i);
                            event.reply(attachment.getUrl());
                        }
                    }
                    submitAppeal(event);
                },
                2, TimeUnit.MINUTES, () -> event.reply("Appeal submit timed out."));

    }

    private final Button reject = Button.danger("reject", "Reject");

    private void submitAppeal(CommandEvent e) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(e.getMember().getUser().getName() + " is appealing for their punishment.");
        embed.addField("Warning:", Warn, false);
        embed.addField("Moderator: ", Punisher, false);
        embed.addField("Reason: ", AppealReason , false);
        e.getChannel().sendMessageEmbeds(embed.build()).setActionRow(
                accept, reject)
                .queue();

    }



@Override
    protected void execute(CommandEvent event) {
    UserID = event.getMember().getId();
    GuildId = event.getGuild().getId();
        int msgLength = event.getMessage().getContentDisplay().split(" ").length;
        final MessageChannel channel = event.getChannel();
        if (msgLength <= 1) {
            channel.sendMessage("You need to enter your appeal ID").queue();
            return;
        } else {
            AppealID = event.getMessage().getContentDisplay().split(" ")[1];
        }
        try (Connection connection = DriverManager.getConnection(main.url, main.username, main.password)) {

            Statement statement = connection.createStatement();

            String checkForAppeals = "SELECT Warn1 , Warn2 , Warn3, Executor , Executor1 ,Executor2 ,Appeal1, Appeal2, Appeal3 FROM Warn WHERE UserID='" + UserID + "' AND Guild='" + GuildId + "'";


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
                channel.sendMessage("Please explain why this punishment should be appealed.").queue();
                waitForReason(event);
            } else {
                channel.sendMessage("Unknown punishment ID").queue();
                return;
            }


            connection.close();


        } catch (SQLException e) {
            throw new IllegalStateException("[Matter] Cannot connect the database!", e);
        }

    }

    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ButtonInteractionEvent) {
            if (((ButtonInteractionEvent) event).getComponentId().equals("reject")) {
                ((ButtonInteractionEvent) event).reply("Rejected").queue();
            }
            else {
                buttonID = ((ButtonInteractionEvent) event).getComponentId();
                ((ButtonInteractionEvent) event).reply("Appeal accepted.").queue();
                try {
                    Connection connection = DriverManager.getConnection(main.url, main.username, main.password);

                    Statement statement = connection.createStatement();

                    String args[] = buttonID.split(",");

                    String query = "SELECT Warns, Appeal1, Appeal2, Appeal3 FROM Warn WHERE UserID='"+args[1]+"' AND Guild='"+args[2]+"'";

                    ResultSet rs = statement.executeQuery(query);

                    String Appeal1 = "";
                    String Appeal2 = "";
                    String Appeal3 = "";
                    int Warns = 0;

                    while (rs.next()) {
                        Appeal1 = rs.getString("Appeal1");
                        Appeal2 = rs.getString("Appeal2");
                        Appeal3 = rs.getString("Appeal3");
                        Warns = rs.getInt("Warns");
                    }

                    Warns = Warns - 1;

                    if (Appeal1.equals(args[0])) {
                        System.out.println("This is Warning 1");
                        String removeWarn1 = "UPDATE Warn SET Warns='"+ Warns +"', Warn1='None', Appeal1='None', Executor='None' WHERE UserID="+ UserID +" AND Guild="+ GuildId +";";
                        statement.executeUpdate(removeWarn1);
                    } else if (Appeal2.equals(args[0])) {
                        System.out.println("This is Warning 2");
                        String removeWarn2 = "UPDATE Warn SET Warns='"+ Warns +"', Warn2='None', Appeal2='None', Executor1='None' WHERE UserID="+ UserID +" AND Guild="+ GuildId +";";
                        statement.executeUpdate(removeWarn2);
                    } else if (Appeal3.equals(args[0])) {
                        System.out.println("This is Warning 3");
                        String removeWarn3 = "UPDATE Warn SET Warns='"+ Warns +"', Warn3='None', Appeal3='None', Executor2='None' WHERE UserID="+ UserID +" AND Guild="+ GuildId +";";
                        statement.executeUpdate(removeWarn3);
                    }

                    connection.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ((ButtonInteractionEvent) event).getMessage().delete().queue();

            }
        }
    }
}
