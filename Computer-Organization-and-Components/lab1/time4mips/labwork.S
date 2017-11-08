  # labwork.S
  # Written 2015-2017 by F Lundevall
  # Skeleton file for IS1200/IS1500 lab 1.
  # The macros PUSH and POP are in the public domain.
  # Please add your own code at the end of the file.

  #
  # Please keep the two macros PUSH and POP unchanged
  #

.macro	PUSH reg
	addi	$sp,$sp,-4
	sw	\reg,0($sp)
.endm

.macro	POP reg
	lw	\reg,0($sp)
	addi	$sp,$sp,4
.endm
  #
  # Please add your own code below this line
  #

# ---

.global hexasc, delay, time2string

hexasc:
	andi 	$t0, $a0, 0xF # number to conver to ascii
	addi 	$t1, $0, 0x30 # asci code for digits 0-9
	blt 	$t0, 10, returnhex # we are done
	nop
	addi 	$t0, $t0, -10 # remove 0-9
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

        andi    $t0, $a1, 0x1
        beq     $t0, 0, even
        li      $t0, 0x44
        sb      $t0, 5($a0)
    cont:
	
	sb   	$0, 6($a0) # null byte
	
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
	
	addi $t0, $t0, -1
	
	# inner loop
	addi $t1, $0, 0
	addi $t2, $0, 4450 # constant
run_1_ms:
	beq  $t1, $t2, delay_ms
	nop
	
	addi $t1, $t1, 1
	j run_1_ms
	nop
	
delay_ret:
	jr $ra
	nop

even:
        li $t0, 0x45
        sb $t0, 5($a0)
        j cont
        nop

