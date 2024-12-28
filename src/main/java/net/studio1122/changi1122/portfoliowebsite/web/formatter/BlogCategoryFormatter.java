package net.studio1122.changi1122.portfoliowebsite.web.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogCategory;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.Category;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class BlogCategoryFormatter implements Formatter<BlogCategory> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public BlogCategory parse(String text, Locale locale) throws ParseException {
        List<Category> categories = null;
        try {
            categories = objectMapper.readValue(text, new TypeReference<List<Category>>() {});
            return new BlogCategory(categories);
        } catch (Exception e) {
            throw new ParseException(e.getMessage(), 0);
        }
    }

    @Override
    public String print(BlogCategory category, Locale locale) {
        String text = null;
        try {
            text = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(category.getCategory());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return text;
    }
}
