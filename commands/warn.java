import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.sql.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.List;


public class warn {
    static int warns;
    static String Appeal;
    static void warnUser(MessageReceivedEvent event) {
        String UserID = event.getMember().getId();
        Member member = new Member() {
            @Nonnull
            @Override
            public User getUser() {
                return null;
            }

            @Nonnull
            @Override
            public net.dv8tion.jda.api.entities.Guild getGuild() {
                return null;
            }

            @Nonnull
            @Override
            public JDA getJDA() {
                return null;
            }

            @Nonnull
            @Override
            public OffsetDateTime getTimeJoined() {
                return null;
            }

            @Override
            public boolean hasTimeJoined() {
                return false;
            }

            @Nullable
            @Override
            public OffsetDateTime getTimeBoosted() {
                return null;
            }

            @Override
            public boolean isBoosting() {
                return false;
            }

            @Nullable
            @Override
            public OffsetDateTime getTimeOutEnd() {
                return null;
            }

            @Nullable
            @Override
            public GuildVoiceState getVoiceState() {
                return null;
            }

            @Nonnull
            @Override
            public List<Activity> getActivities() {
                return null;
            }

            @Nonnull
            @Override
            public OnlineStatus getOnlineStatus() {
                return null;
            }

            @Nonnull
            @Override
            public OnlineStatus getOnlineStatus(@Nonnull ClientType clientType) {
                return null;
            }

            @Nonnull
            @Override
            public EnumSet<ClientType> getActiveClients() {
                return null;
            }

            @Nullable
            @Override
            public String getNickname() {
                return null;
            }

            @Nonnull
            @Override
            public String getEffectiveName() {
                return null;
            }

            @Nullable
            @Override
            public String getAvatarId() {
                return null;
            }

            @Nonnull
            @Override
            public List<Role> getRoles() {
                return null;
            }

            @Nullable
            @Override
            public Color getColor() {
                return null;
            }

            @Override
            public int getColorRaw() {
                return 0;
            }

            @Override
            public boolean canInteract(@Nonnull Member member) {
                return false;
            }

            @Override
            public boolean canInteract(@Nonnull Role role) {
                return false;
            }

            @Override
            public boolean canInteract(@Nonnull Emote emote) {
                return false;
            }

            @Override
            public boolean isOwner() {
                return false;
            }

            @Override
            public boolean isPending() {
                return false;
            }

            @Nullable
            @Override
            public BaseGuildMessageChannel getDefaultChannel() {
                return null;
            }

            @Nonnull
            @Override
            public String getAsMention() {
                return null;
            }

            @Nonnull
            @Override
            public EnumSet<Permission> getPermissions() {
                return null;
            }

            @Nonnull
            @Override
            public EnumSet<Permission> getPermissions(@Nonnull GuildChannel guildChannel) {
                return null;
            }

            @Nonnull
            @Override
            public EnumSet<Permission> getPermissionsExplicit() {
                return null;
            }

            @Nonnull
            @Override
            public EnumSet<Permission> getPermissionsExplicit(@Nonnull GuildChannel guildChannel) {
                return null;
            }

            @Override
            public boolean hasPermission(@Nonnull Permission... permissions) {
                return false;
            }

            @Override
            public boolean hasPermission(@Nonnull Collection<Permission> collection) {
                return false;
            }

            @Override
            public boolean hasPermission(@Nonnull GuildChannel guildChannel, @Nonnull Permission... permissions) {
                return false;
            }

            @Override
            public boolean hasPermission(@Nonnull GuildChannel guildChannel, @Nonnull Collection<Permission> collection) {
                return false;
            }

            @Override
            public boolean canSync(@Nonnull IPermissionContainer iPermissionContainer, @Nonnull IPermissionContainer iPermissionContainer1) {
                return false;
            }

            @Override
            public boolean canSync(@Nonnull IPermissionContainer iPermissionContainer) {
                return false;
            }

            @Override
            public long getIdLong() {
                return 0;
            }
        };
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
        System.out.println(member.getUser().getName() + " Warned, Reason:" + warnReason);
            try (Connection connection = DriverManager.getConnection(hidden.url, hidden.username, hidden.password)) {

                Statement statement = connection.createStatement();

                String checkForUser = "SELECT Warns FROM Warn " +
                        "WHERE UserID="+ UserID+" AND Guild='"+guild.getId()+"';";
                ResultSet rs = statement.executeQuery(checkForUser);
                while (rs.next()) {
                    warns = rs.getInt("Warns");
                }
                System.out.println("Warns" + warns);
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
