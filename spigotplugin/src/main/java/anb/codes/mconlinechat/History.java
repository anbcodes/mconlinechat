package anb.codes.mconlinechat;

import java.util.ArrayList;

public class History {
  private PluginDataFile file;

  public History(String filename, MCOnlineChatPlugin plugin) {
    this.file = new PluginDataFile(plugin, filename);
  }

  public ArrayList<String> getLast(int lines, int fromEnd) {
    return file.readNLinesFromFileFromNLinesFromEnd(lines, fromEnd);
  }

  public void add(String line) {
    file.append(line + '\n');
  }
}
