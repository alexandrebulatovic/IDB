------IDB------

Application pour utiliser quelques fonctionnalités d'un SGBD Oracle.
Le but du projet est de permettre à l'utilisateur de se passer du SQL et des commandes, et de profiter à la place d'une IHM.

Programmé en java.

ALCANTUD Gael;
BULATOVIC Alexandre;
MAURY Adrian;
UGOLINI Romain;

Tuteur : PALLEJA Xavier.

Nécessite le jre-8 et jdk associé : https://www.java.com/fr/download/
Nécessite le pilote oracle : http://www.oracle.com/technetwork/database/features/jdbc/jdbc-drivers-12c-download-1958347.html

Le fichier se nomme ojdbc[7-16].jar
Il doit être placé dans les bibliothèques externes de java
chemin d'accès sous ubuntu : /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext
chemin d'accès sous windows :

Compiler : javac launcher/Launch.java
Lancer l'application : java launcher.Launch
Lancer l'application avec le pilote dans le répertoire courant :
       java -cp .:ojdbc[7-16].jar launch.Launcher

