package com.example.demo.service.impl;

import com.example.demo.repository.TestRepository;
import com.example.demo.service.TestService;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    public TestServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public Long getField() {
        String workDir = "src" + File.separator + "main" + File.separator + "resources" + File.separator;

        List<Integer> fields = testRepository.getField();
        createFirstFile(workDir, fields);
        createSecondFile(workDir);
        Long sumAllElem = null;
        try {
            sumAllElem = getSumAllElem(workDir);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return sumAllElem;
    }

    private void createFirstFile(String workDir, List<Integer> fields) {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("entries");
        document.appendChild(root);

        for (Integer field : fields) {
            Element employee = document.createElement("entry");
            root.appendChild(employee);
            Element firstName = document.createElement("field");
            firstName.appendChild(document.createTextNode(Integer.toString(field)));
            employee.appendChild(firstName);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(workDir + "1.xml"));
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private void createSecondFile(String workDir) {
        String fileXslt = Objects.requireNonNull(getClass().getClassLoader().getResource("test.xslt")).getFile();
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File(fileXslt));
        try {
            Transformer transformer = factory.newTransformer(xslt);
            Source text = new StreamSource(new File(Paths.get(workDir + "1.xml").toString()));
            StreamResult streamResult = new StreamResult(new File(workDir + "2.xml"));
            transformer.transform(text, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private Long getSumAllElem(String workDir) throws IOException, ParserConfigurationException, SAXException {
        InputStream inputStream = Files.newInputStream(Paths.get(workDir + "2.xml"));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document ret = builder.parse(new InputSource(inputStream));
        NodeList entry = ret.getDocumentElement().getElementsByTagName("entry");
        long result = 0L;
        for (int i = 0; i < entry.getLength(); i++) {
            String field = entry.item(i).getAttributes().getNamedItem("field").getNodeValue();
            result += Long.parseLong(field);
        }
        System.out.println("Сумма все элементов равна: " + result);
        return result;
    }

}
