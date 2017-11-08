/* mipslabwork.c

   This file written 2015 by F Lundevall
   Updated 2017-04-21 by F Lundevall

   This file should be changed by YOU! So you must
   add comment(s) here with your name(s) and date(s):

   This file modified 2017-04-31 by Ture Teknolog 

   For copyright and licensing, see file COPYING */

#include <stdint.h>   /* Declarations of uint_32 and the like */
#include <pic32mx.h>  /* Declarations of system-specific addresses etc */
#include "mipslab.h"  /* Declatations for these labs */

int mytime = 0x5957;
int timeouts = 0;
int hz = 10;
int prime = 1234567;

volatile unsigned int *trise = (volatile unsigned int *) 0xbf886100;
volatile unsigned int *porte = (volatile unsigned int *) 0xbf886110;

char textstring[] = "text, more text, and even more text!";

/* Interrupt Service Routine */
void user_isr( void ) {

  if (IFS(0) & 0x800) {
      int i = 0;
      while (i < 3) {
          tick(&mytime);
          i++;
      }
  }
  IFSCLR(0) = 0x800;

  if (IFS(0) & 0x100) {
     timeouts++;
     if (timeouts == 10) {
        time2string(textstring, mytime);
        display_string(3, textstring);
        display_update();
        tick(&mytime);
        timeouts = 0;
     }
     IFSCLR(0) = 0x100;
  }
}

/* Lab-specific initialization goes here */
void labinit( void )
{
  *porte = 0;
  *trise &= ~0xff;
  PORTD |= 0xfe0;

  // timer
  T2CON = 0; // set clock to 0
  T2CONSET = 0x70; // 1:256 prescaling
  TMR2 = 0; // reset timer
  PR2 = (80e6 / 256) / hz;
  T2CONSET = 0x8000; // trn the timer on by setting bit 15 to 1

  IPC(2) |= 0x10;
  // set bit no 8 to enable interupt
  IEC(0) = 0x100;

  // SURPRISE - SW2
  IEC(2) |= 0x800;
  IPC(2) |= 0x7000000;

  enable_interrupt();

  return;
}

void bump(volatile unsigned int *p) {
  *p += 1;
}

void update_screen() {
  time2string(textstring, mytime);
  display_string(3, textstring);
  display_update();
  display_image(96, icon);
}

void handle_btns() {
  int btns = getbtns();
  if (0 < btns) {
    // some button has been pressed
    int sw = getsw();
    if (0 != (btns & 1<<2)) {
      // 4th button
      mytime |= (sw << 12);
    }
    if (0 != (btns & 1<<1)) {
      // 3rd button
      mytime |= (sw << 8);
    }
    if (0 != (btns & 1)) {
      // 2nd button
      mytime |= (sw << 4);
    }
    update_screen();
  }
}

/* This function is called repetitively from the main program */
void labwork( void ) {
  prime = nextprime(prime);
  display_string(0, itoaconv(prime));
  display_update();
}

