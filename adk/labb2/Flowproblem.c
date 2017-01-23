#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int d, n, e;


//char *str;

int readNodes() {
	char *str;
	scanf("%s", str);
	//printf("%s", str);
	n = atoi(str);
	return n;
}

void sourceAndSink() {
	char *s, *t;
	int len = strlen(s);
	int i = 0;
	char c;
	int sink, source;
	while(i < len) {
		s[i] = getchar();
		i++;
	}
	i = 0;
	while(i < strlen(s)) {
		if(s[i] == 32) {
			strcat()
		}
	}
	printf("%s", s);
	scanf("%s %s", s, t);
	int source = atoi(s);
	int sink = atoi(t);
	printf("Källa, sänka: %d %d\n", source, sink);
}
int[] split(char *str, char *expr) {
	int i = 0;
	char *split[2];
	while(i < strlen(str)) {
		if(str[i] == expr) {
			i++;
		}
		split[i][j] = 
	}
}

int readE() {
	char *str;
	scanf("%s", str);
	//printf("%s", str);
	e = atoi(str);
	return e;
}

// int lol[x][y] {1,3}, {3,2}{3,9}
void readEdges(int e) {
	int i = 0;
	int u, v, c;
	while(i < e) {
		scanf("%d %d %d", &u, &v, &c);
		printf("Kanter: %d %d %d\n", &u, &v, &c);
		i++;
	}
}
int main(void)
{
	int nodes = readNodes();
	printf("Antal noder: %d\n", nodes);
	sourceAndSink();
	int e = readE();
	printf("Antal kanter: %d \n", &e);
	readEdges(e);
	
}

