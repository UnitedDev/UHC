package fr.kohei.uhc.utils.frame;

import org.bukkit.entity.Player;

import java.util.List;

public interface FrameAdapter
{
    String getTitle(final Player p0);
    
    List<String> getLines(final Player p0);
}
