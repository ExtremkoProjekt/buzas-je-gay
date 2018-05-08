# Strategická hra

Jedná sa o ťahovú strategickú hru. Každá mapa obsahuje N miest, kde každé z týchto miest je pridelené jednému hráčovi (1 človek VS N-1 virtuálnych hráčov). Cieľom hry je vybudovať vojsko, s ktorým bude hráč postupne dobýjať okolité mestá. Každé mesto má 3 budovy:

```
Kasárne, Zlatú baňa, Hlavnú budova
```

## Budovy

* **Kasárne:** slúžia na výcvik (výrobu) vojakov
* **Zlatá baňa:** slúži na ťažbu zlata
* **Hlavná budova:** slúži na umožnenie dobytia mesta

Hráč môže každú budovu vylepšovať, tie majú 5 úrovní vylepšenia. Pri spustení hry má každá budova úroveň 1 a ďalšie úrovne hráč odomyká zaplatením určitého obnosu zlata.

###Kasárne
* **úroveň 1** – v jednom kroku dokáže kasáreň vyrobiť **max. 5 vojakov**
* **úroveň 2** – v jednom kroku dokáže kasáreň vyrobiť **max. 20 vojakov**
* **úroveň 3** – v jednom kroku dokáže kasáreň vyrobiť **max. 45 vojakov**
* **úroveň 4** – v jednom kroku dokáže kasáreň vyrobiť **max. 80 vojakov**
* **úroveň 5** – v jednom kroku dokáže kasáreň vyrobiť **max. 125 vojakov**

###Zlatá baňa
* **úroveň 1** – v jednom kroku dodá každému hráčovi **5 zlatiek**
* **úroveň 2** – v jednom kroku dodá každému hráčovi **10 zlatiek**
* **úroveň 3** – v jednom kroku dodá každému hráčovi **15 zlatiek**
* **úroveň 4** – v jednom kroku dodá každému hráčovi **20 zlatiek**
* **úroveň 5** – v jednom kroku dodá každému hráčovi **25 zlatiek**

###Hlavná budova

má rovnako možnosť vylepšenia na 5. úroveň. Po dosiahnutí 5. úrovne sa hráčovi povolí možnosť obsadiť protivníkove mestá.

## Financie

Každý hráč dostáva na začiatku 50 zlatiek (50 G). Vylepšovanie budov a vyrábanie vojakov stojí určitý obnos G.

###Ceny

* Jeden vojak = 2 G
* Kasárne 2. úroveň = 30 G
* Kasárne 3. úroveň = 45 G
* Kasárne 4. úroveň = 60 G
* Kasárne 5. úroveň = 75 G
* Zlatá baňa 2. úroveň = 50 G
* Zlatá baňa 3. úroveň = 75 G
* Zlatá baňa 4. úroveň = 100 G
* Zlatá baňa 5. úroveň = 125 G
* 2. úroveň = 40 G
* 3. úroveň = 60 G
* 4. úroveň = 80 G
* 5. úroveň = 100 G

##Ťahanie

V každom ťahu môže hráč vykonať len jednu činnosť: 
* vyrobiť vojakov
* vylepšiť budovu
* zaútočiť
* nespraviť nič

Každým z týchto ťahov dostáva hráč zo Zlatej bane určený obnos G (podľa úrovne vylepšenia Zlatej bane).

Výroba vojakov a vylepšovanie budov trvá istý počet ťahov. Počas týchto ťahov hráč nemôže nič robiť. Rovnako, presun vojska medzi mestami trvá istý počet krokov (podľa mapy – neorientovaný graf).

Ak hráč dosiahne možnosť útočiť na protihráčove mesto (vylepší hlavnú budovu na úroveň 5), môže zobrať ľubovoľnú časť svojej armády a zaútočiť na mesto. Ak hráčovi zostane po útoku na mesto aspoň jeden vojak, môže toto mesto obsadiť. Tým získa nové mesto a porazí vyradí protihráča. Cieľom hry je obsadiť všetky mestá a tým vyhrať hru. 


#Autori
* Buzáš Matej
* Hrtánek Viktor
* Ortutay Štefan
* Reguly Martin
* Sadloň Martin

```
Extémne programovanie | 2018
```