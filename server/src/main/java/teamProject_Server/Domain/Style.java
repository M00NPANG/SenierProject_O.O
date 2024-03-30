package teamProject_Server.Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "style")
public class Style {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sty_id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer sty_street;
    private Integer sty_modern;
    private Integer sty_minimal;
    private Integer sty_feminine;
    private Integer sty_simpleBasic;
    private Integer sty_americanCasual;
    private Integer sty_businessCasual;
    private Integer sty_casual;
    private Integer sty_retro;
    private Integer sty_sports;
    private Integer sty_classic;
    private Integer sty_elegant;
    private Integer sty_girlish;
    private Integer sty_tomboy;
    private Integer sty_vintage;


    // 생성자
    // public Style() {}

    public Style(User user, Integer sty_street, Integer sty_modern, Integer sty_minimal, Integer sty_feminine, Integer sty_simpleBasic, Integer sty_americanCasual, Integer sty_businessCasual, Integer sty_casual, Integer sty_retro, Integer sty_sports, Integer sty_classic, Integer sty_elegant, Integer sty_girlish, Integer sty_tomboy, Integer sty_vintage ) {
        this.user = user;
        this.sty_street = sty_street;
        this.sty_modern = sty_modern;
        this.sty_minimal = sty_minimal;
        this.sty_feminine = sty_feminine;
        this.sty_simpleBasic = sty_simpleBasic;
        this.sty_americanCasual = sty_americanCasual;
        this.sty_businessCasual = sty_businessCasual;
        this.sty_casual = sty_casual;
        this.sty_retro = sty_retro;
        this.sty_sports = sty_sports;
        this.sty_classic = sty_classic;
        this.sty_elegant = sty_elegant;
        this.sty_girlish = sty_girlish;
        this.sty_tomboy = sty_tomboy;
        this.sty_vintage = sty_vintage;
    }

    public Style() {

    }
    // getter and setter


    public Long getSty_id() {
        return sty_id;
    }

    public void setSty_id(Long sty_id) {
        this.sty_id = sty_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getSty_street() {
        return sty_street;
    }

    public void setSty_street(Integer sty_street) {
        this.sty_street = sty_street;
    }

    public Integer getSty_modern() {
        return sty_modern;
    }

    public void setSty_modern(Integer sty_modern) {
        this.sty_modern = sty_modern;
    }

    public Integer getSty_minimal() {
        return sty_minimal;
    }

    public void setSty_minimal(Integer sty_minimal) {
        this.sty_minimal = sty_minimal;
    }

    public Integer getSty_feminine() {
        return sty_feminine;
    }

    public void setSty_feminine(Integer sty_feminine) {
        this.sty_feminine = sty_feminine;
    }

    public Integer getSty_simpleBasic() {
        return sty_simpleBasic;
    }

    public void setSty_simpleBasic(Integer sty_simpleBasic) {
        this.sty_simpleBasic = sty_simpleBasic;
    }

    public Integer getSty_americanCasual() {
        return sty_americanCasual;
    }

    public void setSty_americanCasual(Integer sty_americanCasual) {
        this.sty_americanCasual = sty_americanCasual;
    }

    public Integer getSty_businessCasual() {
        return sty_businessCasual;
    }

    public void setSty_businessCasual(Integer sty_businessCasual) {
        this.sty_businessCasual = sty_businessCasual;
    }

    public Integer getSty_casual() {
        return sty_casual;
    }

    public void setSty_casual(Integer sty_casual) {
        this.sty_casual = sty_casual;
    }

    public Integer getSty_retro() {
        return sty_retro;
    }

    public void setSty_retro(Integer sty_retro) {
        this.sty_retro = sty_retro;
    }

    public Integer getSty_sports() {
        return sty_sports;
    }

    public void setSty_sports(Integer sty_sports) {
        this.sty_sports = sty_sports;
    }

    public Integer getSty_classic() {
        return sty_classic;
    }

    public void setSty_classic(Integer sty_classic) {
        this.sty_classic = sty_classic;
    }

    public Integer getSty_elegant() {
        return sty_elegant;
    }

    public void setSty_elegant(Integer sty_elegant) {
        this.sty_elegant = sty_elegant;
    }

    public Integer getSty_girlish() {
        return sty_girlish;
    }

    public void setSty_girlish(Integer sty_girlish) {
        this.sty_girlish = sty_girlish;
    }

    public Integer getSty_tomboy() {
        return sty_tomboy;
    }

    public void setSty_tomboy(Integer sty_tomboy) {
        this.sty_tomboy = sty_tomboy;
    }

    public Integer getSty_vintage() {
        return sty_vintage;
    }

    public void setSty_vintage(Integer sty_vintage) {
        this.sty_vintage = sty_vintage;
    }
}
