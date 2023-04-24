package ar.edu.itba.paw.models.userContext.implementations;

import ar.edu.itba.paw.models.userContext.interfaces.User;

final public class UserImpl implements User {
    private final String email;
    private final String name;
    private final String message;
    private final Behaviour behavior;
    private final String password;
    private final  int id;
    public UserImpl(int id,String email, String name, String message,String password,Behaviour behaviour) {
        this.email = email;
        this.name = name;
        this.message = message;
        this.id = id;
        this.behavior = behaviour;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserImpl{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Behaviour getBehavior() {
        return behavior;
    }

    @Override
    public String getTelephone() {
        return "Phone";
    }

    @Override
    public String getName() {
        return this.name;
    }


}

