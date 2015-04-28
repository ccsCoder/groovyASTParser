class Person {
    // Class variables (also called static attributes) are prefixed by the keyword static
    static personCounter=0
    def age, name               // this creates setter and getter methods
    private alive

    // object constructor
    Person(age, name, alive = true) {            // Default arg like in C++
        this.age = age
        this.name = name
        this.alive = alive
        personCounter += 1
        // There is a '++' operator in Groovy but using += is often clearer.
    }

    public die() {
        alive = false
        println "$name has died at the age of $age."
        alive
    }

    private kill(String anotherPerson) {
        println "$name is killing $anotherPerson.name."
        anotherPerson.die()
    }

    // methods used as queries generally start with is, are, will or can
    // usually have the '?' suffix
    protected isStillAlive() {
        alive
    }

    def getYearOfBirth() {
        new Date().year - age
    }

    // Class method (also called static method)
    static getNumberOfPeople() { // accessors often start with get
                                 // in which case you can call it like
                                 // it was a field (without the get)
        personCounter
    }
}   


    