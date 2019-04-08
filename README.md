# Epidemia
Jedną z metod epidemiologii jest modelowanie badanego zjawiska przez symulacje komputerowe. W trakcie symulacji obserwujemy rozprzestrzenianie się chorób wśród populacji, spośród której każdy osobnik jest reprezentowany w systemie jako agent. Możliwe interakcje między agentami wyznaczone są przez sieć społecznościową (reprezentowaną jako graf nieskierowany) oraz rodzaj agenta. Wynikiem symulacji jest poziom rozprzestrzenienia się zagrożenia oraz śmiertelność wśród agentów.

## Zadanie
Należy napisać program Symulator Epidemii, który dla zadanych parametrów wejściowych przeprowadzi symulację na podstawie opisanego niżej modelu, a następnie stworzy raport z wynikiem.

##Parametry dla programu
Parametry dla programu należy odczytać z plików properties. Jak to robić można znaleźć w wielu tutorialach w sieci, np. [http://www.baeldung.com/java-properties](http://www.baeldung.com/java-properties). Do prawidłowego działania program powinien oczekiwać dwóch plików properties. Jednego z wartościami domyślnymi default.properties oraz z wartościami dla symulacji simulation-conf.xml, z tym że pierwszy jest tekstowy a drugi XML:

default.properties
```
#seed dla generatora liczb losowych
seed=0
 
#początkowa liczba agentów w populacji
#liczba całkowita z przedziału 1..1000000
liczbaAgentów=100

#prawdopodobieństwo wylosowania agenta towarzyskiego
prawdTowarzyski=0.2
 
#prawdopodobieństwo spotkaniach
prawdSpotkania=0.1
 
#prawdopodobieństwo zarażenia
prawdZarażenia=0.5
 
#prawdopodobieństwo wyzdrowienia
prawdWyzdrowienia=0.1
 
#Smiertelność
śmiertelność=0.001
 
#liczba dni jaką trwa symulacja
#liczba całkowita z przedziału 1..1000
liczbaDni=100
 
#średnia liczba znajomych każdego agenta
#liczba całkowita z przedziału 0..liczbaAgentów-1
śrZnajomych=15
 
#ścieżka do pliku z raportem
#jeżeli taki plik istnieje to zostanie nadpisany
plikZRaportem=./raport.txt
```

simulation-conf.xml
```
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
    <comment>wartości dla symulacji</comment>
    <entry key="seed">5</entry>
    <entry key="śrZnajomych">25</entry>
    <entry key="plikZRaportem">./raport_5_25.txt</entry>
</properties>
```
Brak któregoś z plików lub to że pierwszy nie jest tekstowy a drugi nie jest XML powinno skutkować zakończeniem programu i wypisaniem komunikatu o błędzie. Program powinien się też kończyć komunikatem o błędzie jeżeli wszystkie wymienione w przykładzie klucze nie posiadają wartości typu zgodnego z dokumentacją z komentarzy.

Oczekiwane komunikaty błędu:
- Brak pliku default.properties
- Brak pliku simulation-conf.xml
- default.properties nie jest tekstowy
- simulation-conf.xml nie jest XML
- Brak wartości dla klucza plikZRaportem
- Niedozwolona wartość "ala ma kota" dla klucza śrZnajomych

## Model
Zakładamy, że epidemia rozwija się w populacji agentów, którzy zorganizowani są w sieć społecznościową zdefiniowaną jako graf nieskierowany. Symulacja zaczyna się od wylosowania tego grafu tak żeby średnia liczba znajomych była maksymalnie zbliżona do wartości `śrZnajomych` (tzn. żadna inna liczba krawędzi nie dawała innego lepszej średniej). W chwili obecnej rozważamy dwa rodzaje agentów: zwykłych i towarzyskich. Rodzaj agenta określany jest przez losowanie przy użyciu parametru `prawdTowarzyski`. Agenci w populacji mogą być zdrowi, zarażeni lub mieć odporność (są to grupy rozłączne). Wszyscy poza jednym wylosowanym agentem zaczynają jako zdrowi (bez odporności), a jeden zaczyna jako zarażony.

Na początku każdego dnia każdy zarażony agent może umrzeć (z prawd. `śmiertelność`) lub wyzdrowieć (z prawd. `prawdWyzdrowienia`). Agent, który umarł przestaje uczestniczyć w symulacji, a agent który wyzdrowiał nabiera odporność i już nigdy nie zachoruje.

Każdego dnia symulacji każdy agent może umawiać się na spotkanie z innymi agentami. Agent z prawd. `prawdSpotkania` decyduje czy chce się spotkać i jeżeli tak to losuje jednego ze swoich znajomych w przypadku agenta zwykłego lub ze swoich znajomych i znajomych swoich znajomych w przypadku agenta towarzyskiego (można planować spotkania i spotykać się z tym samym agentem wiele razy danego dnia). Następnie agent losuje jeden z pozostałych dni symulacji kiedy do takiego spotkania dojdzie. Agent powtarza planowanie spotkań dopóki nie wylosuje, że nie chce się spotykać.

Gdy już wszystkie nowe spotkania zostały zaplanowane, to dochodzi spotkań przypadających na dany dzień. Jeżeli któryś ze spotykających się agentów jest zarażony a drugi nie ma odporności, to z prawd. `prawdZarażenia` może dojść do zarażenia, wpp. takie spotkanie nie ma żadnego efektu. Zarażenie agenta ma następujące konsekwencje. Jeżeli to agent zwykły to dopóki nie wyzdrowieje będzie planował nowe spotkania z dwa razy mniejszym prawdopodobieństwem. Jeżeli to agent towarzyski to dopóki nie wyzdrowieje będzie planował się spotykać tylko ze swoimi bezpośrednimi znajomymi (nie ma to wpływu na to czy inni będą się decydować spotykać z nim i na spotkania, które już zaplanowano).

Wynik symulacji
Wynikiem symulacji jest tekstowa informacja o początkowej sieci społecznościowej, np.:
```
# twoje wyniki powinny zawierać te komentarze
seed=...
liczbaAgentów=...
prawdTowarzyski=...
prawdSpotkania=...
prawdZarażenia=...
prawdWyzdrowienia=...
śmiertelność=...
liczbaDni=...
śrZnajomych=...
 
# agenci jako: id typ lub id* typ dla chorego
1 zwykły
2 zwykły
3 towarzyski
4* zwykły
5 towarzyski
6 zwykły
 
# graf
1 5 2
2 3 4 1
3 2 4
4 2
5 1
 
# liczność w kolejnych dniach
zdrowi1 chorzy1 uodp1
zdrowi2 chorzy2 uodp2
zdrowi3 chorzy3 uodp3
...
```
Następnie po jednej wolnej linii, w kolejnych linijkach należy wypisać liczbę osób kolejno zdrowych, chorych, uodpornionych oddzielając te liczby spacją.

## Wymagania techniczne
Poniższe wymagania techniczne mają na celu ułatwienie przetestowania rozwiązań przez sprawdzających. Niezastosowanie się do nich zmarnuje nasz czas i zostanie ukarane obcięciem części punktów.

Jako rozwiązanie przyślij archiwum zip. Nazwa archiwum powinna odpowiadać twojemu loginowi na students z rozszerzeniem `.zip`. Po rozpakowaniu powinien powstać katalog odpowiadający twojemu loginowi na students. Wszystkie pliki powinny znajdować się w tym katalogu. Powinieneś przysłać jedynie źródła (w podkatalogach odpowiadających pakietom). Na najwyższym poziomie oczekujemy pliku Symulacja.java, z klasą zawierającą metodę `main` uruchamiającą symulację. W nazwach plików nie wolno używać polskich liter.
