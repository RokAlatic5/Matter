
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.lang.String;
import java.util.stream.Collectors;

public class userInfo {
    static String acticityName;
    static String activityPresenceImage;
    static String activityPresenceDetails;
    static void userInformation(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        Guild guild = event.getGuild();
        Member author = new Member() {
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
        int msgLenght = event.getMessage().getContentDisplay().split(" ").length;
        if (msgLenght <= 1) {
            author = event.getMember();
        }
        else if (event.getMessage().getMentionedMembers().size() == 1) {
            author = event.getMessage().getMentionedMembers().get(0);
        }
        else if (msgLenght >= 1) {
            String id = event.getMessage().getContentDisplay().split(" ")[1];
            try {
                author = guild.retrieveMemberById(id).complete();
            }catch (NumberFormatException err) {
                channel.sendMessage("Member not found!").queue();
            }
        }
        for (Activity activity : author.getActivities()) {
            acticityName = activity.getName();
            try {
                if (activity.asRichPresence().getLargeImage().getUrl() != null) {
                    activityPresenceImage = activity.asRichPresence().getLargeImage().getUrl();
                } if (activity.asRichPresence().getDetails() != null) {
                    activityPresenceDetails = activity.asRichPresence().getDetails();
                }
            } catch (NullPointerException nullerr) {
                System.out.println(nullerr);
            }
        }
        embed.setTitle(author.getUser().getName() + "#" + author.getUser().getDiscriminator());
        embed.addField("ID", author.getId(), false);
        embed.addField("Creation Date:", author.getTimeCreated().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")), true);
        embed.addField("Joined Server:",  author.getTimeJoined().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")), true);
        embed.addField("Is Boosting:",  String.valueOf(author.isBoosting()), false);
        embed.addField("Is Bot:", String.valueOf(author.getUser().isBot()), true);
        embed.addField("Is System:", String.valueOf(author.getUser().isSystem()), true);
        embed.addField("Roles: " + "(" + author.getRoles().size() + ")", author.getRoles().stream().map(Role::getName).collect(Collectors.toList()).toString(), false);
        embed.addField("Mutual Servers:", String.valueOf(author.getUser().getMutualGuilds().stream().map(Guild::getName).collect(Collectors.toList())), false);
        embed.addField("Status:", String.valueOf(author.getOnlineStatus()), false);
        embed.addField("Permissions:", String.valueOf(author.getPermissions()), false);
        embed.setThumbnail(author.getEffectiveAvatarUrl());
        System.out.println(acticityName + activityPresenceDetails + activityPresenceImage);
        if (acticityName == null ) {
        } if (activityPresenceDetails != null && activityPresenceImage != null){
            embed.setFooter(acticityName + ", " + activityPresenceDetails, activityPresenceImage);
        } else{
            embed.setFooter(acticityName);
        }
        embed.setColor(Color.decode("#FFD700"));
        channel.sendMessageEmbeds(embed.build()).queue();
    }
}
