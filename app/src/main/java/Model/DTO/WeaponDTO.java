package Model.DTO;

public class WeaponDTO {
  private int atk;
  private String name;
  private Boolean equipped;
  private int id;
  private String type;

  // "id": 10,
  // "name": "Espada de Ferro",
  // "atk": 10,
  // "equipped": false,
  // "type": "WEAPON"

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setAtk(int atk) {
    this.atk = atk;
  }

  public int getAtk() {
    return atk;
  }

  public void setEquipped(Boolean equipped) {
    this.equipped = equipped;
  }

  public Boolean getEquipped() {
    return equipped;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
