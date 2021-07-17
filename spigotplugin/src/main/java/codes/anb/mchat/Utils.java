package codes.anb.mchat;

import org.bukkit.World.Environment;

public class Utils {
  public static int envToInt(Environment env) {
    switch (env) {
      case NORMAL:
        return 0;
      case NETHER:
        return 1;
      case THE_END:
        return 2;
      default:
        return 0;
    }
  }
}
