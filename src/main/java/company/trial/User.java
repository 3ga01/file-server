package company.trial;

//import jakarta.persistence.Column;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.Id;
import jakarta.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "member")
public class User {

  @Id
  @Column(name = "id")
  @GeneratedValue
  private int ID;

  /**
   * @return the iD
   */
  public int getID() {
    return ID;
  }

  /**
   * @param iD the iD to set
   */
  public void setID(int iD) {
    ID = iD;
  }

  @Column(name = "NAME")
  private String NAME;

  /**
   * @return the nAME
   */
  public String getNAME() {
    return NAME;
  }

  /**
   * @param nAME the nAME to set
   */
  public void setNAME(String nAME) {
    NAME = nAME;
  }

  @Column(name = "EMAIL")
  private String EMAIL;

  /**
   * @return the eMAIL
   */
  public String getEMAIL() {
    return EMAIL;
  }

  /**
   * @param eMAIL the eMAIL to set
   */
  public void setEMAIL(String eMAIL) {
    EMAIL = eMAIL;
  }

  @Column(name = "password")
  private String password;

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }
}
