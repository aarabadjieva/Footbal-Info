package softuni.exam.util;

import javax.xml.bind.JAXBException;

public interface XmlParser {

    <O> O importXml(Class<O> objectClass, String filePath) throws JAXBException;
}
