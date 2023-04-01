package com.quizcreator.app.services.projectLocation;

import java.util.HashMap;

public interface ProjectLocationService {
    HashMap<String,String> loadProjectLocationsFromDisk();
    void addAndSaveOnDisk(final String projectLocation, final String projectName);

    void clearProjectLocationsOnDisk();
}
