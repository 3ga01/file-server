package company.trial.repositories;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "files")
public class Files {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "description", nullable = false)
  private String description;

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  @Lob
  @Column(name = "files", nullable = false)
  private byte[] files;

  /**
   * @return the files
   */
  public byte[] getFiles() {
    return files;
  }

  /**
   * @param files the files to set
   */
  public void setFiles(byte[] files) {
    this.files = files;
  }

}
