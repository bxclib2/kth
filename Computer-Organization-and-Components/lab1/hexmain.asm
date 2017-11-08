  # hexmain.asm
  # Written 2015-09-04 by F Lundevall
  # Copyright abandonded - this file is in the public domain.

	.text
main:
	li	$a0,1		# change this to test different values

	jal	hexasc		# call hexasc
	nop			# delay slot filler (just in case)	

	move	$a0,$v0		# copy return value to argument register

	li	$v0,11		# syscall with v0 = 11 will print out
	syscall			# one byte from a0 to the Run I/O window
	
stop:	j	stop		# stop after one run
	nop			# delay slot filler (just in case)

  # You can write your own code for hexasc here
  #

hexasc:
	andi $t0, $a0, 0xF # number to conver to ascii
	addi $t1, $0, 0x30 # asci code for digits 0-9
	blt $t0, 10, returnhex # we are done
	subi $t0, $t0, 10 # remove 0-9
	addi $t1, $0, 0x41 # add remaining parts to start of ascii char A
	
returnhex:
	add $v0, $t1, $t0
	andi $v0, $v0, 0x7F # mask the 7 least significant bits
	jr $ra
	
