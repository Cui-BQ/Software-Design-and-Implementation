package marvel;

import com.opencsv.bean.CsvBindByName;

/*
    A helper class of OpenCSV Parser
 */
public class Model {
    @CsvBindByName
    private String hero;
    @CsvBindByName
    private String book;

    public String getHero() {
        return hero;
    }
    public void setHero(String hero) {
        this.hero = hero;
    }
    public String getBook() {
        return book;
    }
    public void setBook(String book) {
        this.book = book;
    }
}
