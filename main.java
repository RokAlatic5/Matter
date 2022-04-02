import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.sql.*;
import java.lang.String;

public class main extends ListenerAdapter {


    public static void main(String[] args)
            throws LoginException {
        JDA jda = JDABuilder.createDefault(hidden.token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
                .enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS).build();
        jda.addEventListener(new main());

        System.out.println("[Matter] Connecting database...");

        try (Connection connection = DriverManager.getConnection(hidden.url, hidden.username, hidden.password)) {
            System.out.println("[Matter] Database connected!");

            connection.close();


        } catch (SQLException e) {
            throw new IllegalStateException("[Matter] Cannot connect the database!", e);
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel channel = event.getChannel();
        Member member = event.getMember();
        if (!event.getAuthor().isBot()) {

            if (msg.getContentRaw().startsWith("!setpsn")) {
                setpsn.setUserPsn(event);
            }
            if (msg.getContentRaw().startsWith("!psn")) {
                psn.userPsn(event);
            }
            if (msg.getContentRaw().startsWith("!userinfo")) {
                    userInfo.userInformation(event);
            }
            if (msg.getContentRaw().startsWith("!warn")) {
                warn.warnUser(event);
            }
            if (msg.getContentRaw().startsWith("!appeal")) {
                appeal.appeal(event);
            }
        }
    }
}
