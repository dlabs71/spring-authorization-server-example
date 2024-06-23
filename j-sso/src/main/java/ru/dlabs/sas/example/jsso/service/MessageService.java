package ru.dlabs.sas.example.jsso.service;

import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    public static Locale russian = new Locale("ru", "RU");
    private final ResourceBundleMessageSource messageSource;

    /**
     * Получить сообщение по его коду.
     *
     * @return сообщение соответствующее коду или при отсутствии значения сам код.
     */
    public String getMessage(String code) {
        return getMessage(code, russian);
    }

    /**
     * Получить сообщение по его коду и локали
     *
     * @return сообщение соответствующее коду или при отсутствии значения сам код.
     */
    public String getMessage(String code, Locale locale) {
        try {
            return messageSource.getMessage(code, null, Optional.ofNullable(locale).orElse(russian));
        } catch (Exception e) {
            log.warn("Get message key='" + code + "' error:" + e.getMessage());
        }
        return code;
    }

    /**
     * Получить сообщение по его коду и применить форматирование строки используя args.
     *
     * @return сообщение соответствующее коду или при отсутствии значения сам код.
     */
    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, russian);
    }

    /**
     * Получить сообщение используя Enum.
     * Код сообщения в таком случае, это имя enum класса (вместе с пакетом) и имя самого enum разделённые точкой.
     */
    public String getMessage(Enum e) {
        Assert.notNull(e, "Error convert enum value to message. Enum value is null.");
        String className = e.getClass().getName();
        return getMessage(className + "." + e);
    }
}