.text

	addi $a0,$0,5 		# a0 contains n
	beq  $a0,$0,zero
	addi $v0,$a0,0 		# v0 is the result
	beq  $v0,1,stop
	addi $at,$0,1 		# stops when at == a0
	addi $a2,$a0,0 		# a2 keeps track of how many times to add n in each inner loop
loop:	addi $at,$at,1
	beq  $at,$a0,stop
	add  $0,$0,$0	  	# NOP
	
	addi $a2,$a2,-1 	# we add less and less n 
	addi $a1,$0, 1 
	addi $a3,$v0,0
inner:	beq  $a1,$a2,loop
	add  $0,$0,$0	  	# NOP
	
        add  $v0,$v0,$a3 	# add n to $v0
	addi $a1,$a1,1
	beq  $0,$0,inner
	add $0,$0,$0

zero:   addi $v0, $0, 1
stop:	beq  $0,$0,stop
	
