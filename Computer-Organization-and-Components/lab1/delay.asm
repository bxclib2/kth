.data

out_string: .asciiz "tic\n"

.text
	addi $t7, $0, 0
	addi $t8, $0, 60
tictoc:
	beq $t7, $t8, finish 
	nop
	addi $t7, $t7, 1
	
	addi $a0, $0, 1000
	jal delay
	nop
	
	li $v0, 1 # system call code for printing string = 4
	move $a0, $t7 # load address of string to be printed into $a0
	syscall # call operating system to perform operation
	j tictoc
	nop

delay:
	move $t0, $a0 # a0 = ms param
delay_ms:
	blez $t0, delay_ret
	nop
	
	subi $t0, $t0, 1
	
	# inner loop
	addi $t1, $0, 0
	addi $t2, $0, 60 # constant
run_1_ms:
	beq  $t1, $t2, delay_ms
	nop
	
	addi $t1, $t1, 1
	j run_1_ms
	nop
delay_ret:
	jr $ra
	nop
	
finish:
	nop
