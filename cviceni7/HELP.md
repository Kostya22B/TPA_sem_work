# Minimal Requirements
Java 17

# About
Projekt zkompilujte a spustte pres IntelliJ. Provede se:
- deployment modelu Test_process_with_user_task.bpmn ze slozky resources (viz. anotace @Deployment na hlavni tride DemoApplication.java)
- nastartovani procesni instance, kterou pak vidim v Camunda Operate a muzu sledovat jeji stav
- proces obsahuje user task, kde se provadeni zastavi, dokud si user task nekdo na sebe nepriradi a nedokonci jej (Camunda Tasklist)
- posleze se proces uspesne dokonci
