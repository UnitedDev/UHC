<img src="https://maven.apache.org/images/maven-logo-white-on-black.png" />

## Authentification
Vous pouvez vous authentifier avec GitHub pour Maven en modifiant votre fichier *\~/.m2/settings.xml* afin d'ajouter votre token personnel. Créez un nouveau fichier *\~/.m2/settings.xml* s'il n'en existe pas.

Dans la balise `servers`, ajoutez une balise `server` avec un `id`, en remplaçant *USERNAME* par votre nom d'utilisateur GitHub, et *TOKEN* par votre token d'accès personnel.

<a href="https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token">Comment créer un token d'accès personnel ?<a/>

Exemple de fichier `settings.xml`
```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
          http://maven.apache.org/xsd/settings-1.0.0.xsd">
  
  <servers>
    <server>
      <id>uniteduhc</id>
      <username>Rhodless</username>
      <password>mon_super_token</password>
    </server>
  </servers>
</settings>

```
  
## Ajouter le projet en dependency pom.xml
 
```xml
<repository>
  <id>uniteduhc</id>
  <url>https://maven.pkg.github.com/UnitedDev/UHC</url>
</repository>
```
   
```xml
<dependency>
  <groupId>fr.uniteduhc</groupId>
  <artifactId>uhc</artifactId>
  <version>VERSION</version>
</dependency>
```
