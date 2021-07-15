package anb.codes.mchat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;

public class PluginDataFile {
  private File file;
  private MChatPlugin plugin;

  public PluginDataFile(MChatPlugin plugin, String name) {
    this.plugin = plugin;
    createFile(name);
  }

  private void createFile(String name) {
    Path path = Path.of(plugin.getDataFolder().getAbsolutePath(), name);
    file = path.toFile();
    try {
      file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void append(String str) {
    write(str, true);
  }

  public void overwrite(String str) {
    write(str, false);
  }

  private void write(String str, Boolean append) {
    try {
      tryWrite(str, append);
    } catch (IOException e) {
      plugin.getLogger().warning("Error writing to file: " + file.getPath());
      plugin.getLogger().warning(e.getMessage());
    }
  }

  private void tryWrite(String str, Boolean append) throws IOException {
    FileWriter fw = new FileWriter(file, append);
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write(str);
    bw.close();
  }

  public String read() {
    try {
      return tryRead();
    } catch (IOException e) {
      plugin.getLogger().warning("Error reading file: " + file.getPath());
      plugin.getLogger().warning(e.getMessage());
      return null;
    }
  }

  private String tryRead() throws IOException {
    FileInputStream fis = new FileInputStream(file);
    byte[] data = new byte[(int) file.length()];
    fis.read(data);
    fis.close();

    String str = new String(data, "UTF-8");
    return str;
  }

  public ArrayList<String> readNLinesFromFileFromNLinesFromEnd(int lineCount, int fromEnd) {
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = new RandomAccessFile(file, "r");
      return tryreadNLinesFromFileFromNLinesFromEnd(lineCount, fromEnd, randomAccessFile);
    } catch (IOException e) {
      plugin.getLogger().warning("Error reading lines: " + file.getPath());
      plugin.getLogger().warning(e.getMessage());
      return null;
    } finally {
      if (randomAccessFile != null) {
        try {
          randomAccessFile.close();
        } catch (IOException e) {
          plugin.getLogger().warning("Error closing file after reading lines: " + file.getPath());
          plugin.getLogger().warning(e.getMessage());
          return null;
        }
      }
    }
  }

  private ArrayList<String> tryreadNLinesFromFileFromNLinesFromEnd(int lineCount, int fromEnd, RandomAccessFile file)
      throws IOException {
    long fileLength = file.length() - 1;
    StringBuilder builder = new StringBuilder();
    ArrayList<String> readLines = new ArrayList<>();
    int linesLeft = lineCount;
    int linesFromEndLeft = fromEnd;

    file.seek(fileLength);
    for (long pointer = fileLength; pointer >= 0; pointer--) {
      file.seek(pointer);
      char c = (char) file.read();

      if (linesFromEndLeft > 0) {
        if (c == '\n') {
          linesFromEndLeft--;
        }
      } else {
        if (c == '\n') {
          builder.reverse();
          readLines.add(builder.toString());
          builder = new StringBuilder();

          linesLeft--;
          if (linesLeft == 0) {
            break;
          } else {
            continue;
          }
        }
        builder.append(c);
      }
    }
    return readLines;
  }

}
