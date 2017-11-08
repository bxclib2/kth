.text

	addi $a0,$0,5 		# a0 contains n
	beq  $a0,$0,zero
	addi $v0,$a0,0 		# v0 is the result
	beq  $v0,1,stop
	addi $at,$0,1 		# stops when at == a0

loop:	addi $at,$at,1
	beq  $at,$a0,stop
	add  $0,$0,$0	  	# NOP
	
	mul  $v0,$v0,$at
	beq  $0,$0,loop
	add  $0,$0,$0		# NOP

zero:   addi $v0, $0, 1
stop:	beq  $0,$0,stop
	
