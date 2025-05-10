package Components.DTO;

public class EnemyUpdateDTO {
    private String name;
    private Integer level;
    private Float hp;
    private Float atk;
    private Float def;
    private Float dex;
    private Integer exp;
    private String weapon;
    private Integer id;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }
}
