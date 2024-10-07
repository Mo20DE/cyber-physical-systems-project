package com.cpsp.MeBackend.model;

public enum Mood {
  HAPPY(0),
  FREEZE(1),
  SWEATING(2),
  SLEEPING(3),
  ANGRY(4),
  TIRED(5);

  public final int value;

  Mood(int value) {
    this.value = value;
  }
}
