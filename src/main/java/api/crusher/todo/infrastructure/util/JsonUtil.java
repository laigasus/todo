package api.crusher.todo.infrastructure.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class JsonUtil {

    public static String extractJson(String input) {
        String regex = "\\{.*\\}";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group().trim();
        } else {
            throw new IllegalArgumentException("JSON 값을 찾지 못했습니다");
        }
    }

    public static String exceptTag(String input) {
        return input.replaceAll("<[^>]*>", "").trim();
    }
}