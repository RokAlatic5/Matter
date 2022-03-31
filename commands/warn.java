import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.awt.*;
import java.sql.*;
import java.time.Instant;
import java.util.*;


public class warn {
    static int warns;
    static String Appeal;
    static void warnUser(MessageReceivedEvent event) {
        String UserID = event.getMember().getId();
        Member member = null;
        String message[] = event.getMessage().getContentDisplay().split(" ");
        String mentionedUser = "";
        String executor = event.getAuthor().getName();
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        String warnReason = "";
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";

        String generateIdChars = uppercaseChars + numbers;

        Random generateId = new Random();

        int lenght = 4;

        char[] appeal = new char[lenght];

        for (int i = 0; i < lenght; i++)
        {
            appeal[i] = generateIdChars.charAt(generateId.nextInt(generateIdChars.length()));
        }

        Appeal = String.valueOf(appeal);

        try {
            mentionedUser = event.getMessage().getMentionedMembers().get(0).getId();
        } catch (IndexOutOfBoundsException err) {}
        int msgLength = event.getMessage().getContentDisplay().split(" ").length;

        if (msgLength == 1) {
            channel.sendMessage("You need to specify a user to warn.").queue();
            return;
        } else if (msgLength == 2) {
            warnReason = "None";
            if (mentionedUser.isEmpty() == false)  {
                UserID = event.getMessage().getMentionedMembers().get(0).getId();
                member = event.getMessage().getMentionedMembers().get(0);
            }else {
                UserID = event.getMessage().getContentDisplay().split(" ")[1];
                member = guild.retrieveMemberById(UserID).complete();
            }
        } else if (msgLength > 2) {
            StringJoiner joiner = new StringJoiner(" ");
            for (int i = 2; i < msgLength; i++) {
                joiner.add(message[i]);
            }

            String reason = joiner.toString();

            warnReason = reason.toString();


            if (mentionedUser.isEmpty() == false)  {
                UserID = event.getMessage().getMentionedMembers().get(0).getId();
                member = event.getMessage().getMentionedMembers().get(0);
            }else {
                UserID = event.getMessage().getContentDisplay().split(" ")[1];
                member = guild.retrieveMemberById(UserID).complete();
            }
        }
            try (Connection connection = DriverManager.getConnection(hidden.url, hidden.username, hidden.password)) {

                Statement statement = connection.createStatement();

                String checkForUser = "SELECT Warns FROM Warn " +
                        "WHERE UserID="+ UserID+" AND Guild='"+guild.getId()+"';";
                ResultSet rs = statement.executeQuery(checkForUser);
                while (rs.next()) {
                    warns = rs.getInt("Warns");
                }
                if (warns == 0) {
                    String addWarn1 = "INSERT INTO Warn (Warns, UserID, Warn1, Appeal1, Guild, Executor)" +
                            "VALUES ('1', '"+ UserID +"', '"+warnReason+"', '"+ Appeal +"', '"+ guild.getId() +"', '"+ executor +"');";
                    statement.executeUpdate(addWarn1);
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("You've been warned");
                    embed.addField("Reason", warnReason, false);
                    embed.addField("Moderator", executor, false);
                    embed.addField("Punishment ID", Appeal+ " | You can appeal this warning with /appeal "+ Appeal, false);
                    embed.setFooter(guild.getName()+ " • Warning 1/3");
                    embed.setTimestamp(Instant.now()).build();
                    embed.setColor(Color.decode("#FFD700"));
                    member.getUser().openPrivateChannel().complete().sendMessageEmbeds(embed.build()).queue();
                }
                if (warns == 1) {
                    String addWarn2 = "UPDATE Warn "+
                            "SET Warns = '2', Warn2= '"+warnReason+"', Appeal2= '"+Appeal+"', Executor1= '"+executor+"' "+
                            "WHERE UserID="+ UserID+" AND Guild='"+guild.getId()+"';";
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("You've been warned");
                    embed.addField("Reason", warnReason, false);
                    embed.addField("Moderator", executor, false);
                    embed.addField("Punishment ID", Appeal+ " | You can appeal this warning with /appeal "+ Appeal, false);
                    embed.setFooter(guild.getName()+ " • Warning 2/3");
                    embed.setTimestamp(Instant.now()).build();
                    embed.setColor(Color.decode("#FFD700"));
                    member.getUser().openPrivateChannel().complete().sendMessageEmbeds(embed.build()).queue();

                    statement.executeUpdate(addWarn2);
                }
                if (warns == 2) {
                    String addWarn3 = "UPDATE Warn "+
                            "SET Warns = '3', Warn3= '"+warnReason+"', Appeal3= '"+Appeal+"', Executor2= '"+executor+"' "+
                            "WHERE UserID="+ UserID+" AND Guild='"+guild.getId()+"';";

                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("You've been warned");
                    embed.addField("Reason", warnReason, false);
                    embed.addField("Moderator", executor, false);
                    embed.addField("Punishment ID", Appeal+ " | You can appeal this warning with /appeal "+ Appeal, false);
                    embed.setFooter(guild.getName()+ " • Warning 3/3");
                    embed.setTimestamp(Instant.now()).build();
                    embed.setColor(Color.decode("#FFD700"));
                    member.getUser().openPrivateChannel().complete().sendMessageEmbeds(embed.build()).queue();

                    statement.executeUpdate(addWarn3);
                }

                connection.close();

            } catch (Exception err) {
                System.out.println(err);
                channel.sendMessage(err.toString()).queue();
            }
    }
}
