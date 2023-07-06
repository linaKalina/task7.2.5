package exercise.worker;

import exercise.article.Article;
import exercise.article.Library;
import exercise.article.LibraryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkerImplTest {

    private Worker worker;
    @Mock
    private Library library;
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        worker = new WorkerImpl(library);
    }
    @Test
    void addNewCorrectArticle() {
        List<String> list = new ArrayList<>();
        list.add("title1");
        list.add("title2");
        Mockito.when(library.getAllTitles()).thenReturn(list);
        assertEquals("Список доступных статей:\ntitle1\ntitle2\n", worker.getCatalog());

    }
    @Test
    void udateCatalog() {
        List<Article> list = new ArrayList<>();
        list.add(new Article("title", "content", "name", LocalDate.of(2023, 1, 6)));
        //list.add(new Article("title2", "content", "name", LocalDate.of(2022, 1, 6)));
        worker.addNewArticles(list);
        Mockito.verify(library).updateCatalog();
    }



    @Test
    void noFieldTitleNoArticle() {
        List<Article> list = new ArrayList<>();
        list.add(new Article(null, "content", "name", LocalDate.of(2023, 1, 6)));
        assertEquals(new ArrayList<>(), worker.prepareArticles(list));
    }
    @Test
    void noFieldContentNoArticle() {
        List<Article> list = new ArrayList<>();
        list.add(new Article("title", null, "name", LocalDate.of(2023, 1, 6)));
        assertEquals(new ArrayList<>(), worker.prepareArticles(list));
    }
    @Test
    void noFieldAuthorNoArticle() {
        List<Article> list = new ArrayList<>();
        list.add(new Article("title", "content", null, LocalDate.of(2023, 1, 6)));
        assertEquals(new ArrayList<>(), worker.prepareArticles(list));
    }
    @Test
    void setTodayDate() {
        List<Article> list = new ArrayList<>();
        list.add(new Article("title", "content", "name", null));
        assertEquals(LocalDate.now(), worker.prepareArticles(list).get(0).getCreationDate());
    }


    @Test
    void uniquenessOfTitle() {
        List<Article> list = new ArrayList<>();
        list.add(new Article("название", "content", "name", LocalDate.of(2023, 1, 6)));
        list.add(new Article("название", "content", "name", LocalDate.of(2023, 1, 6)));
        list.add(new Article("название2", "content2", "name2", LocalDate.of(2023, 1, 6)));
        assertEquals(2, worker.prepareArticles(list).size());

    }
}