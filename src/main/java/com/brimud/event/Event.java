package com.brimud.event;

import java.io.Serializable;

public interface Event<L> extends Serializable {
  void notify(L listener);
}
