package Components.DTO;

import java.util.List;

public class ContextUpdateDTO {
  private Boolean combate;
  private String description;
  private List<String> options;
  private Boolean item;

  public Boolean getCombate() {
    return combate;
  }

  public String getDescription() {
    return description;
  }

  public List<String> getOptions() {
    return options;
  }

  public Boolean getItem() {
    return item;
  }

  public void setCombate(Boolean combate) {
    this.combate = combate;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setOptions(List<String> options) {
    this.options = options;
  }

  public void setItem(Boolean item) {
    this.item = item;
  }
}
