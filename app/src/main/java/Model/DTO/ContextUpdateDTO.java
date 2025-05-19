package Model.DTO;

import java.util.List;

public class ContextUpdateDTO {
  private String description;
  private  String options;
  private Boolean weapon, armor, combat;

  public Boolean getCombat() {
    return combat;
  }

  public String getDescription() {
    return description;
  }

  public String getOptions() {
    return options;
  }

  public Boolean getArmor() {
    return armor;
  }

  public Boolean getWeapon() {
    return weapon;
  }

  public void setCombat(Boolean combat) {
    this.combat = combat;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setOptions(String options) {
    this.options = options;
  }

  public void setArmor(Boolean armor) {
    this.armor = armor;
  }

  public void setWeapon(Boolean weapon) {
    this.weapon = weapon;
  }
}
