#! /usr/bin/python

from sys import *

ansFile = open(argv[1])
outFile = open(argv[2])

p = int(ansFile.readline())
p_ = int(outFile.readline())

if p != p_:
	print 'Wrong field characteristic'
	exit()

n = int(ansFile.readline())
n_ = int(outFile.readline())

if n != n_:
	print 'Wrong polynomial degree'
	exit()

ansSeq = [int(inp) for inp in ansFile.readline().split()]
outSeq = [int(inp) for inp in outFile.readline().split()]

for ansLeading in reversed(ansSeq):
	if ansLeading != 0:
		break

for outLeading in reversed(outSeq):
	if outLeading != 0:
		break

for i in range(n):
	if (ansSeq[i] * outLeading) % p != (outSeq[i] * ansLeading) % p:
		print 'Wrong polynomial coefficient at degree', i
		exit()

print 'Correct'