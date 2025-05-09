package Components.DTO;

public class PlayerUpdateDTO {
    private String name;
    private Float hp;
    private Float atk;
    private Float def;
    private Float dex;
    private Float lck;
    private Integer level;
    private Float exp;
    private Double nextLevel;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getHp() {
        return hp;
    }

    public void setHp(Float hp) {
        this.hp = hp;
    }

    public Float getAtk() {
        return atk;
    }

    public void setAtk(Float atk) {
        this.atk = atk;
    }

    public Float getDef() {
        return def;
    }

    public void setDef(Float def) {
        this.def = def;
    }

    public Float getDex() {
        return dex;
    }

    public void setDex(Float dex) {
        this.dex = dex;
    }

    public Float getLck() {
        return lck;
    }

    public void setLck(Float lck) {
        this.lck = lck;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Float getExp() {
        return exp;
    }

    public void setExp(Float exp) {
        this.exp = exp;
    }

    public Double getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(Double nextLevel) {
        this.nextLevel = nextLevel;
    }
}
