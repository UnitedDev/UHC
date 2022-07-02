package fr.kohei.uhc.utils.world;

import fr.kohei.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;
import org.imanity.imanityspigot.chunk.AsyncPriority;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PreGeneration {

    private World w;
    private int x, z, radiusX, radiusZ, current, todo, calcTime;
    private long startTime;
    private BukkitTask task;
    private boolean running;
    private BlockingQueue<Runnable> queue;

    public PreGeneration(Cuboid.SubCuboid subCuboid) {
        this.w = subCuboid.getW();
        this.x = subCuboid.getX();
        this.z = subCuboid.getZ();
        this.radiusX = subCuboid.getRadiusX();
        this.radiusZ = subCuboid.getRadiusZ();
        this.current = 0;
        this.todo = radiusX * radiusZ / 256;
        this.queue = new LinkedBlockingQueue<>();
        load();
    }

    private void load() {
        this.running = true;
        this.startTime = System.currentTimeMillis();
        new Thread(() -> {
            this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(UHC.getPlugin(), new Runnable() {
                private int xI = x;
                private int zI = z;

                @Override
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        w.imanity().getChunkAtAsynchronously(xI, zI, AsyncPriority.NORMAL).whenCompleteAsync(
                                (chunk, throwable) -> {
                                    if (throwable == null) {
                                        w.loadChunk(chunk);
                                        this.zI += 16;
                                        if (this.zI > (z + radiusZ)) {
                                            this.zI = z;
                                            this.xI += 16;
                                        }
                                        if (this.xI > (x + radiusX)) {
                                            task.cancel();
                                            calcTime = Math.round((float) ((System.currentTimeMillis() - startTime) / 1000L));
                                            // TODO Pregen game option;
                                            running = false;
                                            return;
                                        }
                                        current++;
                                    }
                                }
                        );
                    }
                }
            }, 1L, 1L);
        }).start();
    }

    public int getCurrent() {
        return this.current;
    }

    public int getTodo() {
        return this.todo;
    }

    public boolean isRunning() {
        return this.running;
    }

    public int getCalcTime() {
        return this.calcTime;
    }
}
