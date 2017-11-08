#include <stdio.h>
#include <stdlib.h>

#define COLUMNS 6

int curr_col = 0; // global variable

void print_number(int n) {
  if (curr_col == COLUMNS) {
      curr_col = 0;
      printf("\n");
  }
  printf("%10d ", n);
  curr_col++;
}

void print_all(_Bool *primes, int size) {
    for (int i = 0; i <= size; i++) {
        if (primes[i] == 1) {
            print_number(i+2); // primes start from 2
        }
    }
}

void print_largest(_Bool *primes, int size) {
    for (int i = size - 1; i != 0; i--) {
        if (primes[i] == 1) {
            print_number(i+2); // primes start from 2
            return;
        }
    }
}

void print_sieves(int n) {
    int size = n - 2;
    _Bool primes[size];
    int i, j;
    for (i = 0; i < size; i++) {
        primes[i] = 1;
    }
    for (i = 2; i*i < n; i++) {
        if (primes[i-2] == 1) {
            for (j = 2*i; j < n; j+=i) {
                primes[j-2] = 0;
            }
        }
    }

    // stackoverflow
    //print_largest(primes, size);
    print_all(primes, n);
}

// 'argc' contains the number of program arguments, and
// 'argv' is an array of char pointers, where each
// char pointer points to a null-terminated string.
int main(int argc, char *argv[]){
  if(argc == 2)
    print_sieves(atoi(argv[1]));
  else
    printf("Please state an interger number.\n");
  return 0;
}
