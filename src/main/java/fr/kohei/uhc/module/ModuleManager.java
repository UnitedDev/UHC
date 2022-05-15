package fr.kohei.uhc.module;

import fr.kohei.uhc.module.manager.Camp;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ModuleManager {

    private Module module;
    private List<Camp> camps;

    public ModuleManager() {
        this.camps = new ArrayList<>();

    }
}
