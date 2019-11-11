/*
 * This program spends a couple of seconds inside a useless, time-wasting loop
 * and then spends the same time in the system's sleep() routine. When
 * profiling the application, we expect to see only a single hot spot. That of
 * the time-wasting loop. If the kernel did indeed ignore the time spent in
 * the sleep() call, it should not show up as a bar in the profiling
 * histogram. However, if the report shows two almost equally sized peaks, we
 * can conclude the kernel does not handle the blocking state correctly. Note
 * that since the we measure the time that was wasted with an accuracy of one
 * second, the actual time spend in the sleep method could differ from the
 * wasted time by a whole second. However, if enough time was wasted, this
 * shouldn't be an issue for our test.
 */

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include <time.h>

void waste_time(void);
void save_time(int time);

int main(void)
{
	int elapsed;
	time_t start;

	printf("Wasting time...\n");
	start = time(NULL);	
	waste_time();
	elapsed = time(NULL) - start;

	printf("Wasted time for %d seconds, now sleeping %d seconds...\n", elapsed, elapsed);
	save_time(elapsed);

	return 0;
}

void waste_time(void)
{
	long i;
	for(i = 0; i < 400000000; i++);
}

void save_time(int time)
{
	sleep(time);
}

