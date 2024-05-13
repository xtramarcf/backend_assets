package de.fortmeier.asset_management.assets.requests;

import de.fortmeier.asset_management.assets.Document;

import java.util.Arrays;
import java.util.Objects;


/**
 * Record for transmitting Documents.
 * @param id
 * @param docName
 * @param content
 */
public record DocumentDto(
        Integer id,
        String docName,
        byte[] content
) {

    public DocumentDto(Document document) {
        this(document.getId(), document.getDocName(), document.getContent());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentDto that = (DocumentDto) o;
        return Objects.equals(id, that.id) && Objects.equals(docName, that.docName) && Objects.deepEquals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, docName, Arrays.hashCode(content));
    }

    @Override
    public String toString() {
        return "DocumentDto{" +
                "id=" + id +
                ", docName='" + docName + '\'' +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
