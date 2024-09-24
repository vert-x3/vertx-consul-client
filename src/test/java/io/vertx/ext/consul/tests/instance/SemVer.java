package io.vertx.ext.consul.tests.instance;

import org.jetbrains.annotations.NotNull;

public class SemVer implements Comparable<SemVer> {
  int major;
  int minor;
  int patch;

  public SemVer(String version) {
    String[] split = version.split("\\.");
    if (split.length == 0) {
      throw new RuntimeException("Empty version");
    }
    major = Integer.parseInt(split[0]);
    minor = Integer.parseInt(split[1]);
    patch = Integer.parseInt(split[2]);
  }

  @Override
  public int compareTo(@NotNull SemVer o) {
    int majors = Integer.compare(major, o.major);
    if (majors == 0) {
      int minors = Integer.compare(minor, o.minor);
      if (minors == 0) {
        return Integer.compare(patch, o.patch);
      } else return minors;
    } else return majors;
  }
}
