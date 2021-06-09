package config;

import entities.VehicleType;
import gui.WindowMain;
import logic.Habitat;

import java.io.*;

public class ConfigManager {
    private static final String HabitatConfigPath = "Habitat.config";
    public static void SaveHabitatConfig() {
        try(FileOutputStream fos = new FileOutputStream(HabitatConfigPath)) {
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

    public static void LoadHabitatConfig() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(HabitatConfigPath)))) {
            for(var vt : new VehicleType[]{VehicleType.CAR, VehicleType.TRUCK}) {
                Habitat.instance().setTimeGenerationByType(vt, Integer.parseInt(reader.readLine()));
                Habitat.instance().setLifeTimeByType(vt, Integer.parseInt(reader.readLine()));
                Habitat.instance().setGenerateProbabilityByType(vt, Integer.parseInt(reader.readLine()));
            }
            WindowMain.Instance().setShowInfo(Boolean.parseBoolean(reader.readLine()));
            WindowMain.Instance().setShowSimulationTime(Boolean.parseBoolean(reader.readLine()));

        } catch (Exception e) { e.printStackTrace(); }
    }
}
