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

        String renderedHtml = templateEngine.process("hello", context);


        Document document = Jsoup.parse(renderedHtml, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            renderer.setDocumentFromString(document.html());
            renderer.layout();
            renderer.createPDF(outputStream);

            return new ByteArrayResource(outputStream.toByteArray());
        }

//        var resource = new ClassPathResource("static/claude.png");
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
////        byteArrayOutputStream.write(name.getBytes());
//        byteArrayOutputStream.write(Files.readAllBytes(resource.getFile().toPath()));
//
//        return byteArrayOutputStream.toByteArray();
    }
}
