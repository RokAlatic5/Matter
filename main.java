import com.jagrosh.jdautilities.command.CommandBuilder;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.ini4j.Ini;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.lang.String;

public class main {

    static String token;
    static String owner;
    static String prefix;
    static String url;
    static String username;
    static String password;

    public static void main(String[] args)
            throws LoginException, IOException, RateLimitedException {
        Ini config = new Ini( new File("C:/Users/Home/Desktop/MatterBot/config.ini"));

        token = config.get("botStuff", "botToken");
        owner = config.get("botStuff", "botOwner");
        prefix = config.get("botStuff", "botPrefix");
        url = config.get("database", "url");
        username = config.get("database", "username");
        password = config.get("database", "password");

        EventWaiter waiter = new EventWaiter();

        CommandClientBuilder bot = new CommandClientBuilder();

        bot.useDefaultGame();

        bot.setOwnerId(owner);

        bot.setPrefix(prefix);

        bot.addCommands(
                new psn(),
                new setpsn(),
                new warn(),
                new appeal(),
                new userInfo()
        );


        JDABuilder.createDefault(token)
                .addEventListeners(waiter, bot.build())
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.playing("loading..."))

                .build();

        System.out.println("[Matter] Connecting database...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("[Matter] Database connected!");

            connection.close();


        } catch (SQLException e) {
            throw new IllegalStateException("[Matter] Cannot connect the database!", e);
        }
    }
}

