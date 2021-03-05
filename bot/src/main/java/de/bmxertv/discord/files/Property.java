package de.bmxertv.discord.files;

import de.bmxertv.discord.Application;
import lombok.Cleanup;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Property {

    private static Path path = Paths.get(Application.getJarLocation().toString(), "application.cfg");

    public static Map<String, Object> getContent() {
        Map<String, Object> map = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));

            String[] lines = reader.lines().collect(Collectors.joining("\n")).split("\n");
            for (String line : lines) {
                String[] s = line.split("=");
                map.put(s[0], s[1]);
            }
            reader.close();
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public static void write(Map<String, Object> content) {
        try {
            @Cleanup
            BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()));
            content.forEach((s, o) -> {
                try {
                    writer.write(s + "=" + o);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
