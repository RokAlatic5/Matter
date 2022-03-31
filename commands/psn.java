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
        Member member = new Member() {
            @Override
            public long getIdLong() {
                return 0;
            }

            @Nonnull
            @Override
            public String getAsMention() {
                return null;
            }

            @Nonnull
            @Override
            public User getUser() {
                return null;
            }

            @Nonnull
            @Override
            public Guild getGuild() {
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
        };
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

