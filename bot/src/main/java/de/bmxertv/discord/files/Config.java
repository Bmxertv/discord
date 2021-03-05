package de.bmxertv.discord.files;

import de.bmxertv.discord.Application;
import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
public class Config {

    private final String TOKEN;
    private final String VERSION;
    private final String WEBSITE;
    private final long ADMIN_ID;
    private final char PREFIX;

    private final Map<String, Object> map = new HashMap<>();

    @SneakyThrows
    public Config() {

        @Cleanup
        InputStream inputStream = Application.instance.getClass().getClassLoader().getResourceAsStream(".env");
        @Cleanup
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String[] lines = reader.lines().collect(Collectors.joining("\n")).split("\n");
        for (String line : lines) {
            String[] s = line.split("=");
            map.put(s[0], s[1]);
        }

        this.TOKEN = map.get("TOKEN").toString();
        this.PREFIX = map.get("PREFIX").toString().charAt(0);
        this.VERSION = map.get("VERSION").toString();
        this.WEBSITE = map.get("WEBSITE").toString();
        this.ADMIN_ID = Long.parseLong(map.get("ADMIN_ID").toString());
    }
}
