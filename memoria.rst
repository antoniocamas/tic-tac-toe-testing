============
 Práctica 2
============

Implementacion de Pruebas Automáticas
=====================================

:Autor: Antonio Camas Maestre
:git: 
   
.. contents:: Contenidos
	      
.. raw:: pdf

   PageBreak
	      
Unit Tests con JUnit 4 de la clase Board
----------------------------------------

El testing de esta clase se ha hecho con unit test en sentido amplio. Es decir, no se han empleado
dobles para sus colabores necesarios. La clase Board usa objetos de la clase de datos Cell.

**BUG:** durante la realización de estos tests se ha encontrado un bug en el método *board.getCellsIfWinner(String)*. Las comparaciones cellValue.equals == this.cells.get(winPos[1]).value devuelven *false* en situaciones en los que los escenarios requerían un *true*. El bug se debe a un mal uso de operador *==* que en Java comprueba la igualdad de objetos y la igualdad en el contenido de los objetos. Este problema hacía imposible ejecutar correctamente los tests unitarios. Para solventarlo se ha cambiado el código del sistema bajo test por esta expresion que comprueba la equidad del contenido de dos objetos String *cellValue.equals(this.cells.get(winPos[1]).value)*.


BoardTest.java
^^^^^^^^^^^^^^

Prueba los métodos publicos de la clase Board con tests no parametrizados. Hace uso delos *matchers* de la librería AssertJ que facila la lectura de los tests y ofrece mensajes de error muy legibles.

La construcción de los escenearios bajos test "Given" es tan sencilla que no hay lugar para usar métodos de SetUp o TearDown.

Los tests en este fichero prueban los escenarios más comunes pero no ejercitan toda la combinatoria posible de movimentos. Por tanto no tienen código en común que refactorizar y parametrizar.

Mereze la pena sin embargo probar una mayor combinatoria de movimientos y posibilidades de jugadas ganadoras. Estos test irán en otro clase de test.


BoardTestParam.java
^^^^^^^^^^^^^^^^^^^

Prueba combinatoria de movimientos y posibilidades de jugadas ganadoras. Todas las jugadas y tanto el testing negativo como el positivo del método *board.getCellsIfWinner(String)* se ejercita en un solo test parametrizado.

Existen escerarios duplicados en los test de esta clase y los de *BoardTest.java* sin embargo se decide no elminar la duplicidad al ser los tests poco costosos y para poder beneficiarnos de la facilidad comprension y lectura de que otorga la simplidad de los tests no parametrizados, sobre todo en caso de que alguno detectase alguna regresión.


Tets GivenABoard_when_play_getsWinnerRight
""""""""""""""""""""""""""""""""""""""""""

Recibe por parametro los movimientos a realizar y el resultado esperado de *getCellsIfWinner* tras cada moviento.

Así pues una ejecución recibe una lista de movimientos y tras cada moviento el tests hace una aserción probando un escenario.

Para facilitar la legibilidad del test se ha creado una clase *Movement* que describe un escenario en concreto.

Es una cuestión pendiente el mejorar el nombrado de los tests parametrizados. El idioma *@Parameters(name = "{index}: {0})* no aporta legibilidad puesto que los elementos del parametro 0 son *List<Movement>* que no tiene una buena representación ascii cuando se imprimen. Quizas con un mayor conocimiento de Java se hubiera podido solventar. Por ejemplo en otros leguajes como Python hubiera bastado con reimplementar el métido *__repr__* de los objetos *List<Movement>*. Con el objetivo de aportar claridad cada elemento *List<Movement>* tiene un nombre de variable que intenta identificar el escenario que prueba.


