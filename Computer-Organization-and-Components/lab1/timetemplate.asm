  # timetemplate.asm
  # Written 2015 by F Lundevall
  # Copyright abandonded - this file is in the public domain.

.macro	PUSH (%reg)
	addi	$sp,$sp,-4
	sw	%reg,0($sp)
.end_macro

.macro	POP (%reg)
	lw	%reg,0($sp)
	addi	$sp,$sp,4
.end_macro

	.data
	.align 2
mytime:	.word 0x5957
timstr:	.ascii "text more text lots of text\0"
	.text
main:
	# print timstr
	la	$a0,timstr
	li	$v0,4
	syscall
	nop
	# wait a little
	li	$a0,2
	jal	delay
	nop
	# call tick
	la	$a0,mytime
	jal	tick
	nop
	# call your function time2string
	la	$a0,timstr
	la	$t0,mytime
	lw	$a1,0($t0)
	jal	time2string
	nop
	# print a newline
	li	$a0,10
	li	$v0,11
	syscall
	nop
	# go back and do it all again
	j	main
	nop
# tick: update time pointed to by $a0
tick:	lw	$t0,0($a0)	# get time
	addiu	$t0,$t0,1	# increase
	andi	$t1,$t0,0xf	# check lowest digit
	sltiu	$t2,$t1,0xa	# if digit < a, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0x6	# adjust lowest digit
	andi	$t1,$t0,0xf0	# check next digit
	sltiu	$t2,$t1,0x60	# if digit < 6, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0xa0	# adjust digit
	andi	$t1,$t0,0xf00	# check minute digit
	sltiu	$t2,$t1,0xa00	# if digit < a, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0x600	# adjust digit
	andi	$t1,$t0,0xf000	# check last digit
	sltiu	$t2,$t1,0x6000	# if digit < 6, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0xa000	# adjust last digit
tiend:	sw	$t0,0($a0)	# save updated result
	jr	$ra		# return
	nop

  # you can write your code for subroutine "hexasc" below this line
  #
hexasc:
	andi 	$t0, $a0, 0xF # number to conver to ascii
	addi 	$t1, $0, 0x30 # asci code for digits 0-9
	blt 	$t0, 10, returnhex # we are done
	nop
	subi 	$t0, $t0, 10 # remove 0-9
	addi 	$t1, $0, 0x41 # add remaining parts to start of ascii char A	
returnhex:
	add 	$v0, $t1, $t0
	andi 	$v0, $v0, 0x7F # mask the 7 least significant bits
	jr 	$ra
	nop
	
time2string:
	PUSH 	$v0
	PUSH 	$ra
	
	PUSH 	$s0
	PUSH 	$s1
	PUSH 	$s2
	PUSH 	$s3
	
	PUSH	$a0
	
	andi 	$t0, $a1, 0xF000 # first minute digit
	srl  	$a0, $t0, 12
	jal  	hexasc
	nop
	move	$s0, $v0
	
	andi 	$t0, $a1, 0x0F00 # second minute digit
	srl  	$a0, $t0, 8
	jal  	hexasc
	nop
	move	$s1, $v0
	
	andi 	$t0, $a1, 0x00F0 # first second digit
	srl  	$a0, $t0, 4
	jal  	hexasc
	nop
	move	$s2, $v0
	
	andi 	$a0, $a1, 0x000F # second second digit
	jal  	hexasc
	nop
	move	$s3, $v0

	POP 	$a0
	
	sb   	$s0, 0($a0)
	sb   	$s1, 1($a0)
	
	addi 	$t0, $0, 0x3a # comma
	sb   	$t0, 2($a0)
	
	sb	$s2, 3($a0)
	sb	$s3, 4($a0)
	
	sb   	$0, 5($a0) # null byte
	
	POP	$s3
	POP	$s2
	POP	$s1
	POP	$s0
	
	POP 	$ra
	POP 	$v0
	
	jr 	$ra

delay:
	move $t0, $a0 # a0 = ms param
delay_ms:
	blez $t0, delay_ret
	nop
	
	subi $t0, $t0, 1
	
	# inner loop
	addi $t1, $0, 0
	addi $t2, $0, 2000 # constant
run_1_ms:
	beq  $t1, $t2, delay_ms
	nop
	
	addi $t1, $t1, 1
	j run_1_ms
	nop
	
delay_ret:
	jr $ra
	nop
