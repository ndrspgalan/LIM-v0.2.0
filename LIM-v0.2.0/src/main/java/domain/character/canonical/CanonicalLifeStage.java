package domain.character.canonical;
public enum CanonicalLifeStage { CHILD(6), ADOLESCENT(15), ADULT(18); private final int age; CanonicalLifeStage(int age){this.age=age;} public int ageYears(){return age;} }
