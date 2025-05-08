package Components;

import java.util.List;

public class Context {
  private Boolean combate;
  private String description;
  private  List<String> options;
  private Boolean item;

  public Context(String description, List<String> options, Boolean combate, Boolean item) {
    this.description = description;
    this.options = options;
    this.combate = combate;
    this.item = item;
  }

  public String getDescription() {
    return description;
  }

  public List<String> getOptions() {
    return options;
  }

  public Boolean getCombate() {
    return combate;
  }

  public Boolean getItem() {
    return item;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setoOtions(List<String> options) {
    this.options = options;
  }

  public void setCombate(Boolean combate) {
    this.combate = combate;
  }

  public void setItem(Boolean item) {
    this.item = item;
  }
  
  public void display() {
    System.out.println(description);

    for (int i = 0; i < options.size(); i++) {
      System.out.println((i + 1) + ". " + options.get(i));
    }
  }
}
