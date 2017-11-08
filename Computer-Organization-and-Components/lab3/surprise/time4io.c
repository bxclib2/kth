#include <stdint.h>
#include <pic32mx.h>
#include "mipslab.h"

int getsw() {
  int sw = PORTD >> 8;
  return (sw & 0xf);
}

int getbtns() {
  int btns = PORTD >> 5;
  return (btns & 0x7);
}
