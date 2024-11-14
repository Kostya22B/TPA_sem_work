# Minimal Requirements
Java 17

# About
Projekt zkompilujte a spustte pres IntelliJ. Provede se:
- Deployment modelu myprocessapplication.bpmn ze slozky resources (viz. anotace @Deployment na hlavni tride DemoApplication.java)
- Nastartovani procesni instance, kterou pak vidim v Camunda Operate a muzu sledovat jeji stav.
- Proces obsahuje user task, kde se provadeni zastavi, dokud si user task nekdo na sebe nepriradi a nedokonci jej (Camunda Tasklist). Zde se zadava cena zbozi, ktere si klient objednal.
- Posleze se vezme hodnota z tohoto user tasku a preda se do service tasku, kde by se provedla platba pres platebni branu ("Charge Credit Card"). 
- Tento service task vraci castku, ktera byla klientovi strzena - momentalne je to 1:1 cena zbozi, ale my samozrejme mame moznost v kodu cenu zmenit.
- Tato castka se posleze vyhodnocuje v gateway abychom zjistili, zda zakaznik dost utratil. 
- Pokud utratil mene nez 5, chceme klientovi po uplynuti kratke cekaci lhuty zaslat "email se skvelou nabidkou", abychom ho prinutili jeste neco koupit - toto provedeme s pomoci subprocesu, timer intermediary eventu a dalsiho service tasku.
  - Posleze se procesni instance ukonci.
- Pokud utratil 5 nebo vice, procesni instance se ukonci

# Further Reading
- Pro blizsi pochopeni zapisu podminky pro vyhodnoceni flow v gateway je nutno znat alespon zaklad jazyka Feel https://docs.camunda.io/docs/components/modeler/feel/language-guide/feel-unary-tests
- Pro predani promennych do vaseho service tasku pouzijte input mapping https://docs.camunda.io/docs/components/concepts/variables/#input-mappings. 
- Analogicky pro predani vysledku vaseho service tasku do vasi procesni instance pouzijte output mapping. Pozor, jak input mapping tak output mapping je potreba jen tehdy, pokud se promena v procesu ma namapovat na promennou jineho jmena v kodu (pripadne naopak pro output mapping). Pokud se pouziji stejna jmena, muzete si mapping odpustit. 

# Hint