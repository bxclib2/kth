/*
 prime.c
 By David Broman.
 Last modified: 2015-09-15
 This file is in the public domain.
*/


#include <stdio.h>

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

int main(void){
  printf("%d\n", is_prime(1));  // 1 is not a prime.      Should print 0.
  printf("%d\n", is_prime(2));  // 2 is a prime.      Should print 1.
  printf("%d\n", is_prime(3));  // 3 is a prime.      Should print 1.
  printf("%d\n", is_prime(5));  // 5 is a prime.      Should print 1.
//  printf("%d\n", is_prime(6));  // 6 is not a prime.      Should print 0.
  //printf("%d\n", is_prime(11));  // 11 is a prime.      Should print 1.
  //printf("%d\n", is_prime(383)); // 383 is a prime.     Should print 1.
  //printf("%d\n", is_prime(987)); // 987 is not a prime. Should print 0.
  //printf("%d\n", is_prime(1867)); // Should print 1.
  //printf("%d\n", is_prime(6367)); // Should print 1.
  //printf("%d\n", is_prime(1000)); // Should print 0.
}
