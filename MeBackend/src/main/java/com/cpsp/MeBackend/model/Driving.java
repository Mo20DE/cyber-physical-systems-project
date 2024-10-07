package com.cpsp.MeBackend.model;

public enum Driving {
  NOTHING(0),
  FRONT(1),
  BACK(2),
  LEFT(3),
  RIGHT(4);

  public final int value;

  Driving(int value) {
    this.value = value;
  }
}
