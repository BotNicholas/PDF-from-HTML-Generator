package org.botnicholas.projects.filesgenerator.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@Service
public class FilesService {
    public ByteArrayResource getFile(final String name) throws IOException {
//        Preparing Thymeleaf
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        templateResolver.setOrder(1);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("name", "Ben");


//        Generating html as a tring
        String renderedHtml = templateEngine.process("hello", context);

//        Here we're normalizing html string with help of thymeleaf
        Document document = Jsoup.parse(renderedHtml, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            Generating PDF from normalized html string and save it in Memory, not on disc as file
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();

            sharedContext.setPrint(true); //Prepared to be printed on paper
            sharedContext.setInteractive(false); //It's not interactive == no JS and ect...
            renderer.setDocumentFromString(document.html()); //Setting the html string itself
            renderer.layout(); //Preparing the layout before generation
            renderer.createPDF(outputStream); //Creating the pdf

            return new ByteArrayResource(outputStream.toByteArray());
        }
    }
}
