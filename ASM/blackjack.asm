#===========================================================================================================#
#
#				        Jesus Cruz Olivera
#				      Pablo González Álvarez
#
#===========================================================================================================#

# Nota aclaratoria: no hemos puesto tildes en los comentarios para evitar problemas con las codificaciones de texto

.data

cadGenCartas:		.asciiz " \n Generando las cartas de el jugador... "
cadBienvenida:		.asciiz " \n Bienvenidos a el juego Blackjack, tu suerte depende de li $v0, 42\n"
cadTurnoJ1:		.asciiz " \n Turno de el jugador 1\n "
cadTurnoJ2:		.asciiz " \n Turno de el jugador 2\n " 
cadComa:		.asciiz ", "
cadPreguntarJugador: 	.asciiz " \n Desea una nueva carta (1/2): "
cadEmpate:		.asciiz " \n Jugador 1 y Jugador 2 han empatado la partida. "
cadPerdedor1:		.asciiz " \n\n Jugador 1 ha perdido, ha superado los 21 puntos. "
cadPerdedor2:		.asciiz " \n\n Jugador 2 ha perdido, ha superado los 21 puntos. "
cadGanador1:		.asciiz " \n ¡Ha ganado la partida el Jugador 1!\n "
cadGanador2: 		.asciiz " \n ¡Ha ganado la partida el Jugador 2!\n "
cadValorSuma:		.asciiz " La suma es: "
cadSeparador:		.asciiz " __________________________________________________________\n" # es un separador para separar los turnos de los jugadores
cadSeparadorGrande:	.asciiz "\n ==================================================================\n"
.align 2
cartas_jugador1:	.space 40
cartas_jugador2:	.space 40


.globl main
.text

main:
	li $v0, 4
	la $a0, cadSeparadorGrande
	syscall 

	li $v0, 4
	la $a0, cadBienvenida
	syscall
	
	
	li $v1, 0
	li $t5, 0 # el registro $t5 guardará el numero de cartas que tiene el jugador activo en ese momento, pero para las funciones se lo pasaremos por 
	# los registros $aX y nos lo devolvera por el registro $v1, de ahí que haya varios move con algun registro que sea $v1

turnoJugador1:	  # Comienza el turno de el jugador 1
	li $t7, 0 # si es 0 es el jugador 1 el que juega, si es 1 es el jugador 2 el que juega, lo usaremos mas adelante para varias instrucciones branch

	li $v0, 4
	la $a0, cadTurnoJ1
	syscall
	
	# a la funcion generaCartas le pasamos por $a1 la direccion de memoria donde comienzan las cartas de el jugador 1 y por $a0 el numero de cartas
	move $a0, $v1
	la $a1, cartas_jugador1		
	jal generaCartas
	# por $v1 nos devuelve el numero de cartas
	
	
	# a la funcion mostrarCartas le pasamos por $a0 la direccion a un asciiz que contiene una coma, por $a1 el numero de cartas y por $a2 la direccion donde estan las cartas
	la $a0, cadComa
	move $a1, $v1 
	li $t0, 0 # limpiamos el registro ya que lo hemos usado en una funcion anterior
	la $a2,cartas_jugador1
	jal mostrarCartas
	# nos devuelve por $v1 el numero de cartas, el cual no ha sido modificado si no usado
	
	
	
	# a la funcion calcularValor le pasamos por $a0 la direccion donde estan las cartas, por $a1 el numero de cartas, y limpiamos unos registros que hemos usado antes
	la $a0, cartas_jugador1	
	move $a1, $v1 
	li $t1,0
	li $t2,0
	li $t3,0 
	jal calcularValor
	# nos devuelve en $v0 la suma de las cartas de el jugador actual en el momento de llamar a la funcion, y en $v1 el numero de cartas sin modificar
	
	
	# imprimimos el valor de la suma
	move $t1, $v0
	li $v0, 4
	la $a0, cadValorSuma
	syscall
	move $a0, $t1
	li $v0, 1
	syscall
	move $v0, $a0
	
	
	bge $v0, 22, perdedor1 #si el valor de la suma ($v0) es mayor o igual que 22 entonces saltamos a la etiqueta perdedor

	# a la funcion preguntarJugador le cargamos la direccion de la cadena cadPreguntarJugador en $a0
	la $a0,cadPreguntarJugador  
	jal preguntarJugador 
	# nos devuelve en $v0 un 1 o un 2
	
	# dependiendo de si el jugador quiere continuar ( $v0 == 1 ) o si quiere acabar su turno ( $v0 == 2 ) volveremos a a iniciar el turno saltando a la 
	# etiqueta turnoJugador1 o pasaremos a el turno de el siguiente jugador saltando a la etiqueta InicioTurnoJugador2
	beq $v0, 1, turnoJugador1 
	beq $v0, 2, InicioTurnoJugador2
	
	

# Si saltamos a esta etiqueta (que indica una direccion de nuestro programa que contiene una instruccion) es porque hemos acabado el turno de el jugador 1
# guardamos el numero de cartas de el jugador1 (estaban en el registro $t5) en el registro $t6, lo usaremos para comparar resultados mas adelante
# ademas en $t5 ponemos un 0 ya que el jugador 2 todavia no tiene cartas, luego usamos una instruccion de salto incodincional que nos llevará a la primera instruccion
# de el turno de el jugador 2
InicioTurnoJugador2:


	move $t6, $v1 
	li $t5, 0
	li $v1, 0	
	
	li $v0, 4
	la $a0, cadSeparador
	syscall 
	j turnoJugador2

# Nos evitaremos el repetir comentarios el turno de el jugador2 ya que las instrucciones son las mismas que en el turno de el jugador1 salvo que imprimiremos
# las cadenas que corresponden a el jugador 2 y guardaremos las cartas en el segundo espacio que hemos reservado en memoria
turnoJugador2:
	li $t7, 1 # este registro nos indica que jugador esta jugando, como es el 2, valdra 1


	li $v0, 4
	la $a0, cadTurnoJ2
	syscall

	move $a0, $v1
	la $a1, cartas_jugador2
	jal generaCartas
	
	la $a0, cadComa
	move $a1, $v1 
	li $t0, 0
	la $a2,cartas_jugador2
	jal mostrarCartas
	
	
	la $a0, cartas_jugador2	
	move $a1, $v1 
	add $t9,$a1,0
	li $t1,0
	li $t2,0 
	li $t3,0 
	jal calcularValor
	
	# imprimimos el valor de la suma
	move $t1, $v0
	li $v0, 4
	la $a0, cadValorSuma
	syscall
	move $a0, $t1
	li $v0, 1
	syscall
	move $v0, $a0
	
	bge $v0, 22, perdedor2
	
	la $a0,cadPreguntarJugador 
	jal preguntarJugador 
	

	beq $v0, 1, turnoJugador2
	# si se acaba el turno de el jguador 2 saltamos a la etiqueta control, habiendo movido antes el numero de cartas a $t5
	move $t5, $v1
	beq $v0, 2, control
	
	
# etiqueta perdedor, si llegamos a ella es porque el jugador actual ha perdido, imprimimos el mensaje que lo indice y saltamos a la etiqueta de fin de programa
perdedor1:

	li $v0, 4 #llamada para imprimir una cadena
	la $a0, cadPerdedor1 
	syscall
	
	j finPrograma
	
perdedor2:

	li $v0, 4 #llamada para imprimir una cadena
	la $a0, cadPerdedor2
	syscall
	
	j finPrograma
	
# si saltamos a esta etiqueta es porque se ha terminado el turno de ambos jugadores sin que ninguno haya perdido, por lo que pasamos a comparar sus sumas
# el numero de cartas de el jugador 1 estaría en $t6, el numero de cartas de el jugador 2 esta en $t5
# llamamos a la funcion calcularValor dos veces, una por jugador, y luego comparamos los resultados
control: 
	la $a0, cartas_jugador1	

	move $a1, $t6	
	li $t1,0
	li $t2,0 #limpiamos el registro t2
	li $t3,0 #limpiamos el registro t3
	jal calcularValor

	move $t7, $v0
	li $v0,0 #limpiamos el registro
	
	la $a0, cartas_jugador2	

	move $a1, $t5
	li $t1,0
	li $t2,0 #limpiamos el registro t2
	li $t3,0 #limpiamos el registro t3
	jal calcularValor

	move $t8, $v0
	li $v0,0
	

	
	beq $t7,$t8,empate #si la suma del valor de las cartas de jugador1 guardadas
	# en el registro $t7 y las cartas de el jugador2 guardadas en el registro $t8 sin  iguales saltamos a empate
	
	bgt $t7,$t8,ganador1 #si la suma del valor de las cartas de jugador1 es mayor que la de jugador2 saltamos a ganador1
	
	blt $t7,$t8,ganador2  #si la suma del valor de las cartas de jugador1 es menor que la de jugador2 saltamos a ganador2

# etiqueta empate, si saltamos a ella es porque ha habido un empate, lo notificamos imprimiendo una cadena y finalizamos el programa con j finPrograma	
empate:
	li $v0, 4 
	la $a0, cadEmpate 
	syscall
	j finPrograma

# si saltamos a esta etiqueta es porque el jugador1 ha ganado, lo nofiticamos imprimiendo y acabamos el programa con finPrograma
ganador1:
	li $v0, 4 
	la $a0, cadGanador1
	syscall
	j finPrograma
	
# si saltamos a esta etiqueta es porque el jugador2 ha ganado, lo nofiticamos imprimiendo y acabamos el programa con finPrograma
ganador2:
	li $v0, 4 
	la $a0, cadGanador2 
	syscall
	j finPrograma
	
	

# fin de el programa
finPrograma:
	
	li $v0, 10
	syscall
	
	
	
	
	
#==========================================================================================================================================================#
# Aqui comienzan las funciones de nuestro programa: generaCartas, mostrarCartas,  calcularValor	y prreguntarJugador, asi como algunas otras dentro de las
# funciones que nos indican en el enunciado.
	
#===========================================================================================================#
# 	El objetivo de esta funcion es generar al menos 2 cartas ( luego si el jugador lo pide iria generando
# mas cartas). Primero generará una carta, ya que si hemos llamado a la funcion es porque como minimo
# queremos generar una, luego si el numero de cartas es == 0 (que le hemos pasado al principio)
# volvemos a llamar a la funcion, ojo, no hemos actualizado el numero de cartas al llamar por primera vez 
# a la funcion, el numero de cartas solamente es actualizado (calculado) antes de llamar a la funcion
# generaCartas, de tal forma que la primera vez que la llamemos su tamaño será 0, las siguientes veces 
# valdra 3,4,5...

generaCartas:
	# siempre generamos una carta
	addi $sp, $sp, -4 # desplazamos la pila para guardar el $ra antiguo
	sw $ra, ($sp) 
	move $t8, $a0	# $a0 contiene el numero de cartas de el jugador1, lo movemos a $t8 para que genAleatorio no lo cambie
	jal genAleatorio
	lw $ra, ($sp)
	addi $sp, $sp, 4 # desapilamos para obtener el anterior $ra	
	move $a0, $t8 

	# si el numero de cartas es uno, generamos otra carta mas
	beq $t5, 1, genAleatorio
	
	# volvemos a nuestra funcion principal
	jr $ra
	
# esta funcion genera un numero aleatorio y usa a la funcion guardarCarta para guardarla en el espacio reservado												
genAleatorio:
	move $t1, $a1 # hemos guardado temporalmente la direccion de nuestro espacio reservado en $t1
	li $a0, 1  
	li $a1, 13 # limite superior de nuestra funcion de generacion de un numero pseudoaleatorio
	li $v0, 42 # calculamos un numero aleatorio 
	syscall
	
	
	move $a1, $t1 # en $a1 metemos el valor de la direccion de nuestro espacio reservado para cartas
	addi $a0, $a0, 1 # este es nuestro numero aleatorio entre 1 y 13
	move $a2, $t5  # por $a1 le pasamos el numero de cartas a guardarCarta
	
	
	addi $sp, $sp, -4
	sw $ra, ($sp)
	jal guardarCarta
	lw $ra, ($sp)
	addi $sp, $sp, 4
	
	add $t5, $t5, 1 # este es nuestro contador de cartas, le sumaremos uno cada vez que generemos una
	move $v1, $t5   # movemos a $v1 nuestro numero de cartas
	
	li $v0, 0 # limpiamos el registro $v0 
	jr $ra	# volvemos a generaCartas
	
guardarCarta:
	move $t1, $a0 # guardamos temporalmente en $t1 nuestra carta
	add $a1, $a1, $t5 # a nuestra direccion a el espacio reservado le sumamos el numero de cartas que tenemos

	blez $t7, guardarCartaJ1 # si $t7 vale 0 entonces esta jugando el jugador1
	bgtz $t7, guardarCartaJ2 # si $t7 es mayor que 0 entonces esta jugando el jugador 2 
	
# guardamos la carta que esta en $t1 en el espacio reservado cuya direccion tenemos en $a1 habiendole sumado antes nuestro numero de cartas,
# de tal forma que la primera carta iria exactamente en la posicion 0 de dicho array (primera direccion), pero la segunda carta iria en esa
# direccion anterior + 1, ya que es la siguiente carta.
guardarCartaJ1:
	sb $t1, 0($a1)
	jr $ra # volvemos a genAleatorio

guardarCartaJ2:
	sb $t1, 0($a1)
	jr $ra # volvemos a genAleatorio

	
#===========================================================================================================#
# Esta funcion va a imprimir las cartas del jugador, para ello ira recorriendo el espacio reservado, cargando
# los numeros (byte) e imprimiendolos junto a las comas
mostrarCartas:
	beq $t0, $a1, finMostrarCartas # comprobamos si nuestro contador es igual a el numero de cartas
	lb $t2,0($a2)	# cargamos la primera carta (la cual es un byte)
	move $t3,$a0	# cargamos la coma	
	move $a0,$t2	# el byte cargado lo movemos a $a0 para imprimirlo
	li $v0,1
	syscall	
			
	move $a0,$t3		
	li $v0,4
	syscall
	
	addi $t0,$t0,1	# nuestro contador, cuando sea igual a el numero de cartas habremos acabado

	addi $a2,$a2,1	# en $a2 tenemos el puntero, le sumamos 1 (1 byte)

	j mostrarCartas
	
	
 finMostrarCartas:
 	# volvemos a la funcion principal de el programa
 	move $v1, $a1
 	jr $ra
#===========================================================================================================#	
# Esta funcion preguntara a el jugador acutal si quiere seguir jugando o no, para ello introducira un 1
# o un 2, si introduce otro numero volvera a preguntar
	
preguntarJugador:
	li $v0,4  # imprimimos la cadena que pregunta al jugador, en $a0 teniamos la direccion de la cadena
	# a imprimir
	syscall
	
	li $v0,5  # leemos el entero que nos pasa el jugador siendo 1 que quiere cartas 2 que no
	syscall
	
	beq $v0,1,finPreguntarJugador #si el valor introducido por teclado es uno saltamos a finPreguntarJugador
	beq $v0,2,finPreguntarJugador #si el valor introducido por teclado es 2 saltamos a finPreguntarJugador
	j preguntarJugador
finPreguntarJugador:
	# volvemos a la funcion principal de el programa
	jr $ra
	
#===========================================================================================================#	
# Esta funcion nos devovlera el valor de la suma de las cartas de un jugador, no nos importa si es el 
# jugador 1 o el 2, ya que eso dependerá de si le pasamos por $a0 el array de cartas de un jugador a otro. 
# Lo que si es importante es pasarle por $a1 el numero de cartas que se corresponde a el jugador del cual
# pasamos las cartas

calcularValor:
	lb $t1,0($a0)
	addi $t2,$t2,1 # nuestro indice 
	beq $t1,1,casoAS #si tenemos un AS saltamos a casoAS
	bge $t1,11,casosEspeciales #si tenemos un 11,12 o un 13 saltamos a casos especiales 
	add $t3,$t3,$t1 #este es el valor de mi suma

	beq $t2,$a1,finCalcularValor # si el valor de el registro $t2 en este caso del contador del bucle es igual a la longitud del numero de cartas $a1 salta a finCalcularValor
	addi $a0,$a0,1 #contador que al incrementarlo apunta a la siguiente carta
	j calcularValor
	
	
finCalcularValor:
	move $v1, $a1
	move $v0, $t3 
	jr $ra

casosEspeciales:
	li $t1,10 #cargamos en el registro $t1 el valor 10
	add $t3,$t3,$t1 #este es el valor de mi suma

	beq $t2,$a1,finCalcularValor # si el valor de el registro $t2 en este caso del contador del bucle es igual a la longitud del numero de cartas $a1 salta a finCalcularValor
	addi $a0,$a0,1 #contador que al incremetarlo apunta a la siguiente carta 
	j calcularValor

casoAS:
	#la implementacion en caso de as seria de la siguiente manera
	# si la suma de las cartas que tenemos con el as tomando este el valor 11 es mayor que 21 entonces el as tomara el valor 1
	#aplicando que 11+X>21 x toma todos los valores desde 11 en adelante
	#la condicion logica es que si la suma de las cartas que tenemos es mayor o igual que 11 entonces el as toma el valor 1.
	
	bge $t3,11,mayor11  #si la suma de las cartas que tenemos es 11 o mayor entonces saltamos a mayor11 es la implementacion del AS
	li $t1,11	#en caso contrario seguimos y cargamos en el registro $t1 el valor 11
	
	add $t3,$t3,$t1 #este es el valor de mi suma

	beq $t2,$a1,finCalcularValor # si el valor de el registro $t2 en este caso del contador del bucle es igual a la longitud del numero de cartas $a1 salta a finCalcularValor
	addi $a0,$a0,1 #contador que al incrementarlo apunta a la siguiente carta
	j calcularValor
	
mayor11:
	li $t1,1 #cargamos el valor 1 en el registro $t1

	add $t3,$t3,$t1 #este es el valor de mi suma

	beq $t2,$a1,finCalcularValor # si el valor de el registro $t2 en este caso del contador del bucle es igual a la longitud del numero de cartas $a1 salta a finCalcularValor
	addi $a0,$a0,1 # contador que al incrementarlo apunta a la siguiente carta
	j calcularValor
	
	









