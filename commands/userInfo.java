
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.lang.String;
import java.util.Objects;
import java.util.stream.Collectors;

public class userInfo extends Command {

    public userInfo() {
        this.name = "userinfo";
        this.help = "get user info from provided user";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.guildOnly = true;
    }

    static String acticityName;
    static String activityPresenceImage;
    static String activityPresenceDetails;
    protected void execute(CommandEvent event) {
        MessageChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        Member author = null;
        Guild guild = event.getGuild();
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
        try {
            for (Activity activity : Objects.requireNonNull(author).getActivities()) {
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
        } catch (NullPointerException nerr) {
            System.out.println(nerr);
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
