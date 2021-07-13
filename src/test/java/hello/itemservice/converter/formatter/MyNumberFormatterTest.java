package hello.itemservice.converter.formatter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Locale;


class MyNumberFormatterTest {

    MyNumberFormatter formatter = new MyNumberFormatter();

    @Test
    void parse() throws ParseException {
        Number result = formatter.parse("12,444", Locale.KOREA);
        Assertions.assertThat(result).isEqualTo(12444L);
    }

    @Test
    void print() {
        String result = formatter.print(1000, Locale.KOREA);
        Assertions.assertThat(result).isEqualTo("1,000");
    }

}