package com.severalcircles.flames.buttonaction.data;

import com.severalcircles.flames.buttonaction.ButtonAction;
import com.severalcircles.flames.data.base.ConsentException;
import com.severalcircles.flames.data.base.FlamesDataManager;
import com.severalcircles.flames.data.user.FlamesUser;
import com.severalcircles.flames.data.user.UserFunFacts;
import com.severalcircles.flames.features.FlamesPrettyDate;
import com.severalcircles.flames.features.rank.Ranking;
import com.severalcircles.flames.system.Flames;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.util.Locale;
import java.util.ResourceBundle;

public class
FunFactsButtonAction implements ButtonAction {

    @Override
    public void execute(ButtonClickEvent event, FlamesUser user) throws IOException {
        UserFunFacts funFacts = user.getFunFacts();
        if (funFacts == null) {
            try {
                user = FlamesDataManager.readUser(event.getUser());
            } catch (ConsentException ignored) {

            }
            funFacts = user.getFunFacts();
            if (funFacts == null) {
                //oh my god
                funFacts = new UserFunFacts(Instant.now(), user.getEmotion(), Instant.now(), user.getEmotion(), user.getScore(), user.getScore(), Ranking.getRank(user.getScore()), 0);
            }
        }
        ResourceBundle resources = ResourceBundle.getBundle("commands/MyDataCommand", Locale.ENGLISH);
//        System.out.println(Date.from(Instant.now);
        String rank;
        try {
            switch (Ranking.getRank(funFacts.getHighestFlamesScore())) {
                case APPROACHING_BRONZE:
                    rank = resources.getString("rank.1");
                    break;
                case BRONZE:
                    rank = resources.getString("rank.2");
                    break;
                case SILVER:
                    rank = resources.getString("rank.3");
                    break;
                case SHINING_SILVER:
                    rank = resources.getString("rank.4");
                    break;
                case GOLD:
                    rank = resources.getString("rank.5");
                    break;
                case BEYOND_GOLD:
                    rank = resources.getString("rank.6");
                    break;
                case PLATINUM:
                    rank = resources.getString("rank.7");
                    break;
                case SPARKLING_PLATINUM:
                    rank = resources.getString("rank.8");
                    break;
                case PLATINUM_SUMMIT:
                    rank = resources.getString("rank.9");
                    break;
                default:
                case UNRANKED:
                    rank = resources.getString("rank.none");
            }
        } catch (NullPointerException e) {
            rank = resources.getString("rank.none");
        }
        MessageEmbed embed = new EmbedBuilder()
                .setColor(new Color(153, 85,187))
                .setAuthor("User Data: Fun Facts")
                .setTitle(event.getUser().getName())
                .setDescription("French Toast Score: " + funFacts.getFrenchToastMentioned() + "")
                .addField("Best Day Ever", FlamesPrettyDate.prettifyDate(funFacts.getHappyDay()),true)
                .addField("Worst Day Ever", (FlamesPrettyDate.prettifyDate(funFacts.getSadDay())), true)
                .addField("Highest Ever Flames Score", funFacts.getHighestFlamesScore() + " (" + rank + ")", true)
                .addField("Lowest Ever Flames Score", funFacts.getLowestFlamesScore() + "", true)
                .setTimestamp(Instant.now())
                .setFooter("Flames", Flames.api.getSelfUser().getAvatarUrl()).build();
        event.editMessageEmbeds(embed).queue();
    }
}
