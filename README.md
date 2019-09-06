# Arquitectura empresarial 

## Proyecto primer tercio 

## Objetivo 

El servidor debe estar desplegado en heroku y  ser capaz de entregar paginas html e imagenes PNG, Igualmente el servidor provee un 
framework IoC para la construccion de servicios a partir de pojos. 

## Descargar

```linux 
$ git https://github.com/diego2097/arep-proyecto1
$ cd arep-proyecto1
$ mvn package
```

## Contenido
El servidor contiene una pagina /index donde se encuentran todos los recursos
- /img1, /img2: Estos recursos perimiten al usuario acceder a una imagen.  
- /facebook, /github: Estos recursos permiten al usuario acceder a la pagina html de facebook y github \
- /presentacion: Este recurso por medio de un pojo informa el uso y acceso correcto del mismo 
- /cuadrado: Este recurso por medio de un pojo permite calcular el cuadrado de un numero 
- /cuboL: Este recurso por medio de un pojo permite calcular el cubo de un numero 



## Autor
- Diego Alejandro Corredor Tolosa 

## Licencia 
- GNU General Public License v3.0

