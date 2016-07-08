package org.openlmis.requisition.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.referencedata.domain.BaseEntity;
import org.openlmis.referencedata.domain.Program;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "requisition_templates")
@NoArgsConstructor
public class RequisitionTemplate extends BaseEntity {

  @OneToOne
  @JoinColumn(name = "programId", nullable = false, unique = true)
  @Getter
  @Setter
  private Program program;

  @ElementCollection
  @MapKeyColumn(name = "key")
  @Column(name = "value")
  @Getter
  @Setter
  private Map<String,RequisitionTemplateColumn> columnsMap = new HashMap<>();

  /**
   * Allows creating requisition template with predefined columns.
   *
   * @param columns Columns to appear in requisition template
   */
  public RequisitionTemplate(Map<String, RequisitionTemplateColumn> columns) {
    for (Map.Entry<String, RequisitionTemplateColumn> entry : columns.entrySet()) {
      columnsMap.put(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Allows changing the display order of columns.
   *
   * @param key Key to column which needs a new display order
   * @param newDisplayOrder Number specifying new display order of extracted column
   */
  public void changeColumnDisplayOrder(String key, int newDisplayOrder) {
    RequisitionTemplateColumn column = columnsMap.get(key);
    if (column.getCanChangeOrder()) {
      column.setDisplayOrder(newDisplayOrder);
      columnsMap.put(key, column);
    }
  }

  /**
   *
   * @param key Column key.
   * @param display Should column be displayed.
   */
  public void changeColumnDisplay(String key, boolean display) {
    RequisitionTemplateColumn column = columnsMap.get(key);
    if (!column.getIsDisplayRequired()) {
      column.setIsDisplayed(display);
      columnsMap.put(key, column);
    }
  }

}
