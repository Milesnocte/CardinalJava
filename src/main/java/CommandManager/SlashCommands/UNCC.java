package CommandManager.SlashCommands;

import CommandManager.ISlashCommand;
import Main.FetchUNCC;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.utils.AttachedFile;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class UNCC implements ISlashCommand {
    @Override
    public void run(SlashCommandInteractionEvent event) throws IOException, InterruptedException {
        event.deferReply().queue();
        switch (event.getSubcommandName()) {
            case "sovi" -> {
                
                String openMessage = FetchUNCC.openInfo("SoVi")[1];
                String openLabel = FetchUNCC.openInfo("SoVi")[0].toLowerCase();
                if(openLabel.equals("open")){
                    event.getHook().editOriginalAttachments(AttachedFile.fromData(new File("./img/sovi.png"))).setContent("Sovi Occupancy: " + openMessage).queue();
                } else {
                    event.getHook().editOriginalAttachments(AttachedFile.fromData(new File("./img/closed.png"))).setContent(openMessage).queue();
                }

            }

            case "crown" -> {
                String openMessage = FetchUNCC.openInfo("Crown Commons")[1];
                String openLabel = FetchUNCC.openInfo("Crown Commons")[0].toLowerCase();
                if(openLabel.equals("open")) {
                    event.getHook().editOriginalAttachments(AttachedFile.fromData(new File("./img/crown.png"))).setContent("Crown Occupancy: " + openMessage).queue();
                } else {
                    event.getHook().editOriginalAttachments(AttachedFile.fromData(new File("./img/closed.png"))).setContent(openMessage).queue();
                }
            }

            case "parking" ->
                event.getHook().editOriginalAttachments(AttachedFile.fromData(new File("./img/parking.png"))).setContent("Parking Availability").queue();

            case "canvas" -> {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://canvas.charlotte.edu/"))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                int responseCode = response.statusCode();
                if(responseCode == 200){
                    event.getHook().editOriginal(":white_check_mark: Canvas is up on my end!").queue();
                } else {
                    event.getHook().editOriginal(":x: Error loading canvas. Response: " + responseCode).queue();
                }

            }

            case "atkins" -> {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://library.charlotte.edu/resources/building_occupancy/atkins.json"))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                int responseCode = response.statusCode();
                JSONObject jsonObject = new JSONObject(response.body());
                try {
                    if(responseCode == 200) {
                        int currentOccupancy = Integer.parseInt(jsonObject.getString("atkins_current_occupancy"));
                        event.getHook().editOriginal(currentOccupancy + " people in Atkins.").queue();
                    } else {
                        event.getHook().editOriginal(":x: Error fetching number: " + responseCode).queue();
                    }
                } catch (NumberFormatException ex) {
                    event.getHook().editOriginal("Atkins is closed.").queue();
                }
            }

        }
    }

    @Override
    public void run(ButtonInteractionEvent event) throws Exception {

    }

    @Override
    public void run(StringSelectInteractionEvent event) throws Exception {

    }

    @Override
    public List<String> buttons() {
        return Collections.emptyList();
    }

    @Override
    public String commandName() {
        return "uncc";
    }

    @Override
    public Boolean enabled() {
        return true;
    }

    @Override
    public String description() {
        return null;
    }
}
