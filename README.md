# Hangman-android-app
Hangman applikasjonen er laget for API 27+ så applikasjonen kjører enkelt på en emulator eller en telefon med API 27+.
Det er ingenting ekstra som må til for å kjøre applikasjonen.

Applikasjonen består av en startskjerm der en kan velge mellom norsk og engelsk, hvilken type "Hangman" en vil spille,
og om man vil spille 2-player eller en player. 

Hvis en spiller en player vil du brukeren gjette ord på gitt språk, ordene som er mulig å gjette er hentet ut fra 
english_words.txt og norwegian_words.txt, som ligger under res/raw/.. 
ved å lese ordene fra fil er det lett å legge til nye ord når en vil.

Når en spiller 2 player blir en mellom hvert ord sendt til en aktivitet der spiller 1 kan skrive inn ett ord som spiller 2
skal gjette.

På game_activity.xml er det en bakgrunn der en bytter bilde for hver gjettet bokstav som er feil. 
En ser alltid hvor mange bokstaver som er i ordet og hvilke bokstaver som en har gjettet, de som er rette vises
hvor de skal vere i ordet, og de som er feil vises på siden.
Spilleren velger hvilke bokstaver den skal gjette ved å trykke på bokstavknappene. Disse knappene forsvinner etterhvert som
de blir trykket på. Det er oversikt over hvor mange ganger en har vunnet og tapt så lenge en ikke går helt tilbake til 
startskjermen eller avslutter appen. 
Å gå tilbake kan du gjøre ved å trykke på døren nede i hjørne.

På startskjermen kan du og trykke på spørsmålstegne for å få opp en instruks på hvordan spillet fungerer, og du kan trykke
på X-en som da lukker appen og gir slipp på alle maskinvareressurser.

Når applikasjonen leser inn ordene fra tekstfilene vil de bli lagt inn i en liste. Ordet som blir spillt blir valgt tilfeldig
og etter ordet er spillt blir det fjernet fra listen, da kan ikke det ordet bli spillt to ganger med mindre du starter på nytt.
Bakgrunnene som endrer seg er lagt til i en liste i strings, slik at det er lett å bytte mellom to ulike temaer.
