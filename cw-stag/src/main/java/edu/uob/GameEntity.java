package edu.uob;

public abstract class GameEntity
{
    private String name;
    private String description;

    public GameEntity(String name, String description)
    {
        this.name = name;
        this.description = description;
    }
    public  GameEntity() {
    }

    public GameEntity(String name) {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
