/**
 * Exempel p� in- och utdatahantering f�r maxfl�deslabben i kursen
 * ADK.
 *
 * Anv�nder iostreams i C++.
 * Anm�rkning: scanf/printf-rutinerna fr�n C-exemplet g�r att anv�nda
 * �ven i C++ och �r ofta m�rkbart snabbare �n cin/cout som det h�r
 * programmet anv�nder.
 *
 * Author: Per Austrin
 */

#include <iostream>

using std::cin;
using std::cout;
using std::cerr;

void readBipartiteGraph() {
  int x, y, e;
  // L�s antal h�rn och kanter
  cin >> x >> y >> e;

  // L�s in kanterna
  for (int i = 0; i < e; ++i) {
    int a, b;
    cin >> a >> b;
  }
}


void writeFlowGraph() {
  int v = 23, e = 0, s = 1, t = 2;

  // Skriv ut antal h�rn och kanter samt k�lla och s�nka
  cout << v << "\n" << s << " " << t << "\n" << e << "\n"; 
  for (int i = 0; i < e; ++i) {
    int u, v, c;
    // Kant fr�n u till v med kapacitet c
    cout << u << " " << v << " " << c << "\n";
  }
  // Var noggrann med att flusha utdata n�r fl�desgrafen skrivits ut!
  cout.flush();

  // Debugutskrift
  cerr << "Skickade iv�g fl�desgrafen\n";
}


void readMaxFlowSolution() {
  int v, e, s, t, f;

  // L�s in antal h�rn, kanter, k�lla, s�nka, och totalt fl�de
  // (Antal h�rn, k�lla och s�nka borde vara samma som vi i grafen vi
  // skickade iv�g)
  cin >> v >> s >> t >> f >> e;

  for (int i = 0; i < e; ++i) {
    int u, v, f;
    // Fl�de f fr�n u till v
    cin >> u >> v >> f;
  }
}


void writeBipMatchSolution() {
  int x = 17, y = 4711, maxMatch = 0;

  // Skriv ut antal h�rn och storleken p� matchningen
  cout << x << " " << y << "\n" << maxMatch << "\n";

  for (int i = 0; i < maxMatch; ++i) {
    int a, b;
    // Kant mellan a och b ing�r i v�r matchningsl�sning
    cout << a << " " << b << "\n";
  }

}


int main(void) {
  // Tv� trick f�r att g�ra cin/cout lite snabbare.
  // Se http://kattis.csc.kth.se/doc/iostreamio
  std::ios::sync_with_stdio(false);
  cin.tie(0);

  readBipartiteGraph();

  writeFlowGraph();

  readMaxFlowSolution();

  writeBipMatchSolution();

  // debugutskrift
  cerr << "Bipred avslutar\n";
  return 0;
}
