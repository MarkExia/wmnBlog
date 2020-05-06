import org.junit.Test;
import org.springframework.web.util.HtmlUtils;

public class TestEditor {
    @Test
    public static void main(String[] args) {
        String html = "<p>你好helloword</p><p><span style=\"font-weight: bold;\">你好springmvc。</span></p>";
        String temp = HtmlUtils.htmlEscape(html);
        System.out.println(temp);

        temp = HtmlUtils.htmlUnescape(temp);
        System.out.println(temp);
    }
}
