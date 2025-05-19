package Model.DTO;

public class EnemyDTO {
    private String name;
    private Float hp;
    private Float atk;
    private Float def;
    private Float dex;
    private Integer level;
    private Float exp;

    // Getters
    public String getName() {
        return name;
    }

    public Float getHp() {
        return hp;
    }

    public Float getAtk() {
        return atk;
    }

    public Float getDef() {
        return def;
    }

    public Float getDex() {
        return dex;
    }

    public Integer getLevel() {
        return level;
    }

    public Float getExp() {
        return exp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHp(Float hp) {
        this.hp = hp;
    }

    public void setAtk(Float atk) {
        this.atk = atk;
    }

    public void setDef(Float def) {
        this.def = def;
    }

    public void setDex(Float dex) {
        this.dex = dex;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setExp(Float exp) {
        this.exp = exp;
    }
}
