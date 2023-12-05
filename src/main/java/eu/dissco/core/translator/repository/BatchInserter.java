package eu.dissco.core.translator.repository;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.exception.DisscoRepositoryException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.postgresql.copy.CopyManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchInserter {

  private final CopyManager copyManager;

  public void batchCopy(String tableName, List<Pair<String, JsonNode>> dbRecords)
      throws DisscoRepositoryException {
    try (var outputStream = new ByteArrayOutputStream()) {
      for (var dbRecord : dbRecords) {
        outputStream.write(getCsvRow(dbRecord));
      }
      var inputStream = new ByteArrayInputStream(outputStream.toByteArray());
      copyManager.copyIn("COPY " + tableName
          + " FROM stdin DELIMITER ','", inputStream);
    } catch (IOException | SQLException e) {
      throw new DisscoRepositoryException(
          String.format("An error has occurred inserting %d records into temp table %s",
              dbRecords.size(), tableName), e);
    }
  }

  private static byte[] getCsvRow(Pair<String, JsonNode> dbRecord) {
    return (dbRecord.getLeft() + "," +
        cleanString(dbRecord.getRight())
        + "\n").getBytes(StandardCharsets.UTF_8);
  }

  private static String cleanString(JsonNode jsonNode) {
    if (jsonNode.isEmpty()) {
      return "{}";
    }
    var node = jsonNode.toString();
    node = node.replace("\\u0000", "");
    node = node.replace("\\", "\\\\");
    node = node.replace(",", "\\,");
    return node;
  }

}
