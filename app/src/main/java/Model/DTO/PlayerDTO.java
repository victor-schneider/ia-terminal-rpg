package Model.DTO;

import java.util.List;

public class PlayerDTO {
    private String name;
    private Float hp;
    private Float atk;
    private Float def;
    private Float dex;
    private Float lck;
    private Integer level;
    private Float exp;
    private Double nextLevel;
    private List<WeaponDTO> equippedWeapon;
    private List<ArmorDTO> equippedArmor;

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

    public Float getLck() {
        return lck;
    }

    public Integer getLevel() {
        return level;
    }

    public Float getExp() {
        return exp;
    }

    public Double getNextLevel() {
        return nextLevel;
    }

    public List<WeaponDTO> getEquippedWeapon() {
      return equippedWeapon;
    }

    public List<ArmorDTO> getEquippedArmor() {
      return equippedArmor;
    }

    // Setters
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

    public void setLck(Float lck) {
        this.lck = lck;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setExp(Float exp) {
        this.exp = exp;
    }

    public void setNextLevel(Double nextLevel) {
        this.nextLevel = nextLevel;
    }

    public void setEquippedWeapon(List<WeaponDTO> equippedWeapon) {
      this.equippedWeapon = equippedWeapon;
    }

    public void setEquippedArmor(List<ArmorDTO> equippedArmor) {
      this.equippedArmor = equippedArmor;
    }
}
