/**
 * Exempel p� in- och utdatahantering f�r maxfl�deslabben i kursen
 * ADK.
 *
 * Anv�nder scanf/printf i C.
 *
 * Author: Per Austrin
 */

#include <stdio.h>


void readBipartiteGraph() {
  int x, y, e, i;
  // L�s antal h�rn och kanter
  scanf("%d%d%d", &x, &y, &e);

  // L�s in kanterna
  for (int i = 0; i < e; ++i) {
    int a, b;
    scanf("%d %d", &a, &b);
  }
}


void writeFlowGraph() {
  int v = 23, e = 0, s = 1, t = 2;

  // Skriv ut antal h�rn och kanter samt k�lla och s�nka
  printf("%d\n%d %d\n%d\n", v, s, t, e);
  for (int i = 0; i < e; ++i) {
    int u, v, c;
    // Kant fr�n u till v med kapacitet c
    printf("%d %d %d\n", u, v, c);
  }
  // Var noggrann med att flusha utdata n�r fl�desgrafen skrivits ut!
  fflush(stdout);

  // Debugutskrift
  fprintf(stderr, "Skickade iv�g fl�desgrafen\n");
}


void readMaxFlowSolution() {
  int v, e, s, t, f;

  // L�s in antal h�rn, kanter, k�lla, s�nka, och totalt fl�de
  // (Antal h�rn, k�lla och s�nka borde vara samma som vi i grafen vi
  // skickade iv�g)
  scanf("%d%d%d%d%d", &v, &s, &t, &f, &e);

  for (int i = 0; i < e; ++i) {
    int u, v, f;
    // Fl�de f fr�n u till v
    scanf("%d%d%d", &u, &v, &f);
  }
}


void writeBipMatchSolution() {
  int x = 17, y = 4711, maxMatch = 0;

  // Skriv ut antal h�rn och storleken p� matchningen
  printf("%d %d\n%d\n", x, y, maxMatch);

  for (int i = 0; i < maxMatch; ++i) {
    int a, b;
    // Kant mellan a och b ing�r i v�r matchningsl�sning
    printf("%d %d\n", a, b);
  }

}


int main(void) {
  readBipartiteGraph();

  writeFlowGraph();

  readMaxFlowSolution();

  writeBipMatchSolution();

  // debugutskrift
  fprintf(stderr, "Bipred avslutar\n");
  return 0;
}
