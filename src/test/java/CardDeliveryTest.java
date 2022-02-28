import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {
    private WebDriver driver;

    @BeforeAll
    public static void SetUpCLass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999/");
    }

    public String delivery(boolean date) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 7);
        if (date) {
            return new SimpleDateFormat("d").format((calendar.getTime()));
        } else {
            return new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
        }
    }

    @Test
    void shouldSendForm() {
        $("[data-test-id=city] .input__control").setValue("Нижний Новгород");
        $("[data-test-id=date] [placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.CONTROL + "A"), Keys.BACK_SPACE, delivery(false));
        $("[data-test-id=name] [name=name]").setValue("Войтенко Константин");
        $("[data-test-id=phone] [name=phone]").setValue("+79991236558");
        $("[data-test-id=agreement]>.checkbox__box").click();
        $("button>.button__content").click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Успешно! Встреча успешно забронирована на " + delivery(false)));
        $(".icon_name_close").click();
    }

    @Test
    void shouldSendFormAnotherVar() {
        $("[data-test-id=city] .input__control").setValue("Ниж");
        $$(".menu-item__control").findBy(text("Нижний Новгород")).click();
        $("[data-test-id=date] [placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.CONTROL + "A"), Keys.DELETE);
        $$(".calendar__day").findBy(text(delivery(true))).click();
        $("[data-test-id=name] [name=name]").setValue("Войтенко Константин");
        $("[data-test-id=phone] [name=phone]").setValue("+79865412233");
        $("[data-test-id=agreement]>.checkbox__box").click();
        $("button>.button__content").click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Успешно! Встреча успешно забронирована на " + delivery(false)));
        $(".icon_name_close").click();
    }
}
