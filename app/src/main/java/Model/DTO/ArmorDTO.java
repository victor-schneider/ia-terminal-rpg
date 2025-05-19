package Model.DTO;

public class ArmorDTO {
  private String name;
  private String type;
  private String slot;
  private int def;
  private Boolean equipped;
  private int id;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStlot() {
    return slot;
  }

  public void setSlot(String slot) {
    this.slot = slot;
  }

  public int getDef() {
    return def;
  }

  public void setDef(int def) {
    this.def = def;
  }

  public Boolean getEquipped () {
    return equipped;
  }

  public void setEquipped(Boolean equipped) {
    this.equipped = equipped;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}


