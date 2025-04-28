package Components.PlayerComponents;

public class Item {
  private String name;
  private int id;
  private Boolean equipped;
  private String type;

  public Item(String name, Boolean equipped, int id, String type){
    this.name = name;
    this.id = id;
    this.equipped = equipped;
    this.type = type;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setName(String name){
    this.name = name;
  }
  public String getName(){
    return name;
  }

  public void setEquipped(Boolean equipped) {
    this.equipped = equipped;
  }

  public Boolean getEquipped() {
    return equipped;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
