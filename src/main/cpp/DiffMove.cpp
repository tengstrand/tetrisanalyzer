
#include "DiffMove.h"
#include "DiffMoveThList.h"

DiffMove::DiffMove(double step, double maxDiff)
{
	cnt = 0;
	cntDiff = 0;
	errorSum = 0;
	index = 0;
	this->step = step;
	this->maxDiff = maxDiff;
	maxSize = (int)(maxDiff/step);
	diff = new long[maxSize+1];
	error = new double[maxSize+1];

	for (int i=0; i<=maxSize; i++) {
		diff[i] = 0;
		error[i] = 0;
	}
}

DiffMove::~DiffMove()
{
	delete [] diff;
	delete [] error;
}

void DiffMove::copy(DiffMove *diffMove)
{
	index = diffMove->index;
	cnt = diffMove->cnt;
	cntDiff = diffMove->cntDiff;
	maxSize = diffMove->maxSize;
	errorSum = diffMove->errorSum;
	step = diffMove->step;
	maxDiff = diffMove->maxDiff;

	for (int i=0; i<=maxSize; i++) {
		diff[i] = diffMove->diff[i];
		error[i] = diffMove->error[i];
	}
}

void DiffMove::add(double d, double e)
{
	if (d > 0)
	{
		cntDiff++;
		errorSum += e;

		int idx = (int)(d/step);

		if (idx > maxSize)
			idx = maxSize;

		diff[idx]++;
		error[idx] += e;
	}
		
	cnt++;
}

int DiffMove::hasMoreElements()
{
	return (index <= maxSize);
}

void DiffMove::reset()
{
	index = 0;
}

double DiffMove::getNext(double &equity)
{
	equity = errorSum;

	if (cntDiff == 0) {
		equity = 0;
		index++;
		return 0;
	}

	long sum = cnt-cntDiff;

	for (int i=0; i<=index; i++) {
		sum += diff[i];
		equity -= error[i];
	}

	index++;

	equity /= cnt;

	return sum / (double)cnt;
}

void DiffMove::getThreshold(DiffMoveThList *diffList, int level1, int level2)
{
	DiffMoveTh *d = diffList->getFirst();

	while (d) {
		d->equity = 0;
		d->threshold = 0;
		d->percent = 0;
		d = diffList->getNext();
	}

	if (cntDiff == 0) {
		return;
	}

	double equity = errorSum;
	double sum = cnt-cntDiff;

	d = diffList->getFirst();

	for (int i=0; d && i<=maxSize; i++)
	{
		while (d && equity/cnt <= d->thEquity) {
			d->equity = equity / cnt;
			d->threshold = i * step;
			d->percent = (double)sum / cnt * 100.0;
			d = diffList->getNext();
		}

		sum += diff[i];
		equity -= error[i];
	}
}
