/*
 print-prime.c
 By David Broman.
 Last modified: 2015-09-15
 This file is in the public domain.
*/


#include <stdio.h>
#include <stdlib.h>

#define COLUMNS 6

int is_prime(int n){
  if (n < 2) return 0;  
  if (n == 2) return 1;
  if (n % 2 == 0) return 0;

  for (int i = 3; i < n/2; i+=2) {
      if (n % i == 0) {
          return 0;
      }
  }
  return 1;
}

int curr_col = 0; // global variable

void print_number(int n) {
  if (curr_col == COLUMNS) {
      curr_col = 0;
      printf("\n");
  }
  printf("%10d ", n);
  curr_col++;
}

void print_primes(int n){
  // Should print out all prime numbers less than 'n'
  // with the following formatting. Note that
  // the number of columns is stated in the define
  // COLUMNS
  int curr_col = 0;
  for (int i = 0; i <= n; i++) {
      int maybe_prime = is_prime(i);
      if (maybe_prime) {
          print_number(i);
      }
  }
}

// 2s: 1,299,999,983, 10s: 2,147,483,647
void print_largest_prime(unsigned int n) {
  int largest = 0;
  for (int i = n; i != 0; i--) {
      if (is_prime(i)) {
          print_number(i);
          return;
      }
  }
}

// 'argc' contains the number of program arguments, and
// 'argv' is an array of char pointers, where each
// char pointer points to a null-terminated string.
int main(int argc, char *argv[]){
  if(argc == 2)
    print_primes(atoi(argv[1]));
    //print_largest_prime(atoi(argv[1]));
  else
    printf("Please state an interger number.\n");
  return 0;
}

 
