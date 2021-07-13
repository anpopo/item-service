package hello.itemservice.converter.formatter;

import hello.itemservice.converter.StringToIpPortConverter;
import hello.itemservice.converter.type.IpPort;
import hello.itemservice.converter.type.IpPortToStringConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

public class FormattingConversionServiceTest {

    @Test
    void formattingConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

        // 컨버터 등록
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        // 포맷터 등록
        conversionService.addFormatter(new MyNumberFormatter());


        // 컨버터 사용
        IpPort convert = conversionService.convert("127.0.0.1:8080", IpPort.class);
        Assertions.assertThat(convert).isEqualTo(new IpPort("127.0.0.1", 8080));

        // 포맷터 사용
        String result = conversionService.convert(1000, String.class);
        Assertions.assertThat(result).isEqualTo("1,000");

        Number result2 = conversionService.convert("12,333", Number.class);
        Assertions.assertThat(result2).isEqualTo(12333L);
    }
}
