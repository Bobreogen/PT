package config;

import entities.VehicleType;
import gui.WindowMain;
import logic.Habitat;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigManager {
    public static void SaveHabitatConfig(String name) {
        String path = name + ".config";
        // дефолтные параметры для каждого из пользователей берем банально из дефолтного конфига
        try {
            if (!(new File(path).exists())) {
                Files.copy(Paths.get("Habitat.config"), Paths.get(path));
            }
        } catch (Exception e) { e.printStackTrace(); }

        try(FileOutputStream fos = new FileOutputStream(path)) {
            StringBuilder config = new StringBuilder();
            for(var vt : new VehicleType[]{VehicleType.CAR, VehicleType.TRUCK}) {
                config.append(Habitat.instance().getTimeGenerationByType(vt)).append("\n");
                config.append(Habitat.instance().getLifeTimeByType(vt)).append("\n");
                config.append(Habitat.instance().getGenerateProbabilityByType(vt)).append("\n");
            }
            config.append(WindowMain.Instance().getShowInfo()).append("\n");
            config.append(WindowMain.Instance().getShowSimulationTime()).append("\n");
            fos.write(config.toString().getBytes());

        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void SaveHabitatConfig() {
        SaveHabitatConfig("Habitat");
    }

    public static void LoadHabitatConfig(String name) {
        String path = name + ".config";
        // дефолтные параметры для каждого из пользователей берем банально из дефолтного конфига
        try {
            if (!(new File(path).exists())) {
                Files.copy(Paths.get("Habitat.config"), Paths.get(path));
            }
        } catch (Exception e) { e.printStackTrace(); }
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(name + ".config")))) {
            for(var vt : new VehicleType[]{VehicleType.CAR, VehicleType.TRUCK}) {
                Habitat.instance().setTimeGenerationByType(vt, Integer.parseInt(reader.readLine()));
                Habitat.instance().setLifeTimeByType(vt, Integer.parseInt(reader.readLine()));
                Habitat.instance().setGenerateProbabilityByType(vt, Integer.parseInt(reader.readLine()));
            }
            WindowMain.Instance().setShowInfo(Boolean.parseBoolean(reader.readLine()));
            WindowMain.Instance().setShowSimulationTime(Boolean.parseBoolean(reader.readLine()));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void LoadHabitatConfig() {
        LoadHabitatConfig("Habitat");
    }
}
